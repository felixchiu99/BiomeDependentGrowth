package com.gmail.chiuchohin.item.custom;

import com.gmail.chiuchohin.BiomeSpecificGrowth;
import com.gmail.chiuchohin.GrowEvents;
import com.gmail.chiuchohin.Utils;
import java.util.List;

import javax.annotation.Nullable;

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
        
        if(player.isCrouching()){
            String infoMsg = "The Temperature in this biome is " + biome.getModifiedClimateSettings().temperature() + "\n"
                + "The Downfall in this biome is " +biome.getModifiedClimateSettings().downfall();
            Utils.sendMessageToPlayer(player, infoMsg);
        }else{
            String biomeInfoMsg = "You are in " + biomeReg.getKey(biome).getPath() + " Biome "
                + "(Shift click for more details)";
            Utils.sendMessageToPlayer(player, biomeInfoMsg);
        }

    }
    
    private void UseOnBlock (LevelAccessor world, Player player, Block block, Biome biome){
        CheckCrop(player, block, biome);
        CheckSap(player, block, biome, world);
    }

    private void CheckCrop(Player player, Block block, Biome biome){
        List<Float> allowed = BiomeSpecificGrowth.CROPCONFIG.getCharacteristicsForCrop(block);
        if (allowed == null) {
            //nothing listed for this plant, everythings fine stop blocking the event
            return;
        }
        float baseTemperature = allowed.get(0);
        float baseDownfall = allowed.get(1);

        List<Float> variance = BiomeSpecificGrowth.CROPCONFIG.getVarianceForCrop(block);

        float tempVariance = variance.get(0);
        float downfallVariance = variance.get(1);

        boolean tempInRange = Utils.isInRange(biome.getModifiedClimateSettings().temperature(), baseTemperature, tempVariance);
        boolean downfallInRange = Utils.isInRange(biome.getModifiedClimateSettings().downfall(), baseDownfall, downfallVariance);
        MutableComponent component = Component.literal("This crop ");
        if(tempInRange && downfallInRange){
            component
                .append(Component.literal("can ").withStyle(ChatFormatting.GREEN))
                .append(Component.literal("grow in this biome").withStyle(ChatFormatting.WHITE))
                ;
            Utils.sendMessageToPlayer(player, component);
        }else{
            component
                .append(Component.literal("cannot ").withStyle(ChatFormatting.RED))
                .append(Component.literal("grow in this biome").withStyle(ChatFormatting.WHITE))
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

        Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registries.BIOME);
        boolean treeAllowedToGrow = Utils.isInList(allowed, biomeReg.getKey(biome));
        MutableComponent component = Component.literal("This Sapling ");
        if(treeAllowedToGrow){
            component
                .append(Component.literal("can ").withStyle(ChatFormatting.GREEN))
                .append(Component.literal("grow in this biome").withStyle(ChatFormatting.WHITE))
                ;
            Utils.sendMessageToPlayer(player, component);
        }else{
            component
                .append(Component.literal("cannot ").withStyle(ChatFormatting.RED))
                .append(Component.literal("grow in this biome").withStyle(ChatFormatting.WHITE))
                ;
            Utils.sendMessageToPlayer(player, component);
        }
    }
}
