package com.gmail.chiuchohin.item.custom;

import com.gmail.chiuchohin.BiomeSpecificGrowth;
import com.gmail.chiuchohin.GrowEvents;
import com.gmail.chiuchohin.Utils;

import java.util.List;

import javax.annotation.Nullable;

import org.jetbrains.annotations.Debug;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo.Map;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;


public class GrowthInspectorItem extends Item
{
    public GrowthInspectorItem(Properties pProperties){
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext){
        if(!pContext.getLevel().isClientSide()){
            return InteractionResult.SUCCESS;
        }
        BlockPos pos = pContext.getClickedPos();
        LevelAccessor world = pContext.getLevel();
        Block block = world.getBlockState(pos).getBlock();
        Biome biome = GrowEvents.getBiome(world, pos);

        Player player = pContext.getPlayer();

        OnUse(world, player,pContext.getHand(), biome);
        UseOnBlock(world, player, block, biome);
        Utils.sendMessageToPlayer(player, "");

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide()){
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        Biome biome = GrowEvents.getBiome(level, player.getOnPos());
        OnUse(level, player, hand, biome);
        Utils.sendMessageToPlayer(player, "");
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override 
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced){
        pTooltipComponents.add(Component.translatable("tooltip.biomedependentgrowth.growth_inspector.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void OnUse(LevelAccessor world, Player player, InteractionHand hand, Biome biome){
        Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registries.BIOME);

        ItemStack secondItemStack = null;

        if( hand.equals(InteractionHand.MAIN_HAND) ){
            //Utils.sendMessageToPlayer(player, "main");
            secondItemStack = player.getOffhandItem();
        }else{
            //Utils.sendMessageToPlayer(player, "off");
            secondItemStack = player.getMainHandItem();
        }

        MutableComponent displayMsg = Component.literal("");
        if(!player.isCrouching()){
            displayMsg.append(Component.literal("You are in ").withStyle(ChatFormatting.WHITE));
            displayMsg.append(Component.literal(biomeReg.getKey(biome).getPath()).withStyle(ChatFormatting.LIGHT_PURPLE));
            displayMsg.append(Component.literal(" Biome " + "(Shift click for more details)").withStyle(ChatFormatting.WHITE));
            Utils.sendMessageToPlayer(player, displayMsg);
        }else{
            displayMsg.append(Component.literal("The Temperature in this biome is ").withStyle(ChatFormatting.WHITE));
            displayMsg.append(Component.literal(String.format("%.02f", biome.getModifiedClimateSettings().temperature()) + "\n").withStyle(ChatFormatting.WHITE));
            displayMsg.append(Component.literal("The Downfall in this biome is ").withStyle(ChatFormatting.WHITE));
            displayMsg.append(Component.literal(String.format("%.02f", biome.getModifiedClimateSettings().downfall()) + "").withStyle(ChatFormatting.WHITE));
            Utils.sendMessageToPlayer(player, displayMsg);
        }

        if(!secondItemStack.isEmpty()){
            HeldItemInfo(player, secondItemStack.getItem(), biome, world);
        }
        
    }
    
    private void HeldItemInfo(Player player, Item item, Biome biome, LevelAccessor world){
        boolean debug = BiomeSpecificGrowth.CROPCONFIG.GetIsDebug() || BiomeSpecificGrowth.TREECONFIG.GetIsDebug() ;
        if(debug){
            MutableComponent component = Component.literal("This item has ID : " + item.getDescriptionId());
            Utils.sendMessageToPlayer(player, component);
        }
        List<String> isCrop = BiomeSpecificGrowth.CROPCONFIG.getItemMap(item);
        List<String> isTree = BiomeSpecificGrowth.TREECONFIG.getItemMap(item);

        if (isCrop != null) {
            DisplayCrop(player, isCrop.toArray()[0].toString(), biome);
        }
        if (isTree != null) {
            DisplayTree(player, isTree.toArray()[0].toString(), biome, world);
        }
    }

    private void UseOnBlock (LevelAccessor world, Player player, Block block, Biome biome){
        CheckCrop(player, block, biome);
        CheckSap(player, block, biome, world);
    }

    private void CheckCrop(Player player, Block block, Biome biome){
        List<Double> allowed = BiomeSpecificGrowth.CROPCONFIG.getCharacteristicsForCrop(block);
        if (allowed == null) {
            //nothing listed for this plant, everythings fine stop blocking the event
            return;
        }
        Double baseTemperature = allowed.get(0);
        Double baseDownfall = allowed.get(1);

        List<Double> variance = BiomeSpecificGrowth.CROPCONFIG.getVarianceForCrop(block);
        boolean blockBreak = BiomeSpecificGrowth.CROPCONFIG.GetCropBreak();

        Double tempVariance = variance.get(0);
        Double downfallVariance = variance.get(1);

        boolean tempInRange = Utils.isInRange( (double) biome.getModifiedClimateSettings().temperature(), baseTemperature, tempVariance);
        boolean downfallInRange = Utils.isInRange( (double) biome.getModifiedClimateSettings().downfall(), baseDownfall, downfallVariance);
        MutableComponent component = Component.literal("This crop ");
        if(tempInRange && downfallInRange){
            component
                .append(Component.literal("grow normally").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(" in this biome").withStyle(ChatFormatting.WHITE))
                ;
            Utils.sendMessageToPlayer(player, component);
        }else{
            String text = "grow slowly";
            if(blockBreak){
                text = "will not Grow";
            }

            component
                .append(Component.literal( text ).withStyle(ChatFormatting.RED))
                .append(Component.literal(" in this biome").withStyle(ChatFormatting.WHITE))
                ;
            Utils.sendMessageToPlayer(player, component);
        }
    }
    private void CheckSap(Player player, Block block, Biome biome, LevelAccessor world){
        List<String> allowed = BiomeSpecificGrowth.TREECONFIG.getBiomesForSapling(block);

        if (allowed == null) {
            //nothing listede for this plant, everythings fine stop blocking the event
            return;
        }

        DisplayTree(player, block.getDescriptionId(), biome, world);
    }

    private void DisplayCrop(Player player, String blockID, Biome biome){
        Double res[] = DisplayCrop(player, blockID);
        if(res == null){
            return;
        }

        MutableComponent textComponent = (Component.literal("This biome is ").withStyle(ChatFormatting.WHITE));

        String tempBiomeText = "";
        var tempColour = ChatFormatting.GREEN;
        Double tempBiome = (double) biome.getModifiedClimateSettings().temperature();
        boolean suitableTemp = true;
        if( tempBiome < (res[0] - res[1])){
            tempBiomeText += "too cold";
            tempColour = ChatFormatting.RED;
            suitableTemp = false;
        }else if( tempBiome > (res[0] + res[1])){
            tempBiomeText += "too hot";
            tempColour = ChatFormatting.RED;
            suitableTemp = false;
        }else{
            tempBiomeText += "suitable";
        }

        if(!suitableTemp){
            textComponent.append(Component.literal(tempBiomeText).withStyle(tempColour));
        }
        

        String downfallBiomeText = "";
        var downfallColour = ChatFormatting.GREEN;
        Double downfallBiome = (double) biome.getModifiedClimateSettings().downfall();
        boolean suitableDownfall = true;
        if( downfallBiome < (res[2] - res[3])){
            downfallBiomeText += "too dry";
            downfallColour = ChatFormatting.RED;
            suitableDownfall = false;
        }else if( downfallBiome > (res[2] + res[3])){
            downfallBiomeText += "too wet";
            downfallColour = ChatFormatting.RED;
            suitableDownfall = false;
        }

        if(!suitableTemp && !suitableDownfall){
            textComponent.append(Component.literal(" , ").withStyle(ChatFormatting.WHITE));
        }else if(!suitableDownfall){
            textComponent.append(Component.literal(downfallBiomeText).withStyle(downfallColour));
        }

        if(suitableTemp && suitableDownfall){
            textComponent.append(Component.literal("Suitable").withStyle(ChatFormatting.GREEN));
        }
        
        textComponent.append(Component.literal(" for this crop.").withStyle(ChatFormatting.WHITE));
        Utils.sendMessageToPlayer(player, textComponent);
    }

    private Double[] DisplayCrop(Player player, String blockID){
        List<Double> allowed = BiomeSpecificGrowth.CROPCONFIG.getCharacteristicsForCrop(blockID);
        if (allowed == null) {
            //nothing listed for this plant, everythings fine stop blocking the event
            return new Double[0];
        }
        Utils.sendMessageToPlayer(player, "");
        Double baseTemperature = allowed.get(0);
        Double baseDownfall = allowed.get(1);

        List<Double> variance = BiomeSpecificGrowth.CROPCONFIG.getVarianceForCrop(blockID);

        Double tempVariance = variance.get(0);
        Double downfallVariance = variance.get(1);
        
        MutableComponent tempComponent = (Component.literal("Temperature Range: ").withStyle(ChatFormatting.GOLD))
            .append(Component.literal(""+ String.format("%.02f", (baseTemperature-tempVariance))).withStyle(ChatFormatting.WHITE))
            .append(Component.literal("-").withStyle(ChatFormatting.WHITE))
            .append(Component.literal(""+ String.format("%.02f", (baseTemperature+tempVariance))).withStyle(ChatFormatting.WHITE))
            ;
        Utils.sendMessageToPlayer(player, tempComponent);
        MutableComponent downfallComponent = (Component.literal("Downfall Range: ").withStyle(ChatFormatting.AQUA))
            .append(Component.literal("" + String.format("%.02f", (baseDownfall-downfallVariance))).withStyle(ChatFormatting.WHITE))
            .append(Component.literal("-").withStyle(ChatFormatting.WHITE))
            .append(Component.literal("" + String.format("%.02f", (baseDownfall+downfallVariance))).withStyle(ChatFormatting.WHITE))
            ;
        Utils.sendMessageToPlayer(player, downfallComponent);
        Double res[] = new Double[4];
        res[0] = baseTemperature;
        res[1] = tempVariance;
        res[2] = baseDownfall;
        res[3] = downfallVariance;
        return res;
    }

    private void DisplayTree(Player player, String blockID, Biome biome, LevelAccessor world){
        List<String> allowed = BiomeSpecificGrowth.TREECONFIG.getBiomesBySaplingId(blockID);

        Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registries.BIOME);
        boolean treeAllowedToGrow = Utils.isInList(allowed, biomeReg.getKey(biome).getNamespace() + ":" + biomeReg.getKey(biome).getPath());
        Boolean sapDie = BiomeSpecificGrowth.TREECONFIG.GetTreeDead();
        MutableComponent component = Component.literal("This Sapling ");
        if(treeAllowedToGrow){
            component
                .append(Component.literal("grow normally").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(" in this biome").withStyle(ChatFormatting.WHITE))
                ;
            Utils.sendMessageToPlayer(player, component);
        }else{
            String text = "grow slowly";
            if(sapDie){
                text = "will Die";
            }
            component
                .append(Component.literal(text).withStyle(ChatFormatting.RED))
                .append(Component.literal(" in this biome ").withStyle(ChatFormatting.WHITE))
                ;
            if(player.isCrouching()){
                component.append("\n");
                component.append("Allowed in: [\n");
                for (String biomeName : allowed) {
                    var colour = ChatFormatting.WHITE;
                    if((biomeReg.getKey(biome).getNamespace() + ":" + biomeReg.getKey(biome).getPath()) == biomeName){
                        colour = ChatFormatting.YELLOW;
                    }
                    component.append(Component.literal("  " + biomeName + "\n").withStyle(colour));
                }
                component.append("]");
            }else{
                component.append(Component.literal(" (Shift click for more details)").withStyle(ChatFormatting.WHITE));
            }
            Utils.sendMessageToPlayer(player, component);
        }
    }

    private Double[] DisplayTree(Player player, String blockID){
        List<String> allowed = BiomeSpecificGrowth.TREECONFIG.getBiomesBySaplingId(blockID);
        if (allowed == null) {
            //nothing listed for this plant, everythings fine stop blocking the event
            return new Double[0];
        }
        Utils.sendMessageToPlayer(player, "");
        return new Double[0];
    }
}
