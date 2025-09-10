package com.gmail.chiuchohin;

import java.util.List;

import com.gmail.chiuchohin.Config.CropConfig;
import com.gmail.chiuchohin.Config.TreeConfig;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.level.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrowEvents
{
    @SubscribeEvent
    public void onCropGrowEvent(CropGrowEvent.Pre event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        if (world.isEmptyBlock(pos)) {
            Block blockBelow = world.getBlockState(pos.below()).getBlock();
            if (blockBelow == Blocks.CACTUS || blockBelow == Blocks.CHORUS_FLOWER) {
              //with reeds, its the base growing. with cactus its the air block above that its 'growing' into
              pos = pos.below();
            }
            else {
              return;
            }
        }
        Block block = world.getBlockState(pos).getBlock();
        Biome biome = getBiome(world, pos);

        boolean breakWhenFail = CropConfig.CROP_BREAK_WHEN_FAIL.get().booleanValue();

        boolean isCropGrow = onCropGrow(event, world, pos, block, biome, breakWhenFail);
    }

    @SubscribeEvent
    public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        Block block = world.getBlockState(pos).getBlock();
        Biome biome = getBiome(world, pos);

        boolean replaceWithDead = TreeConfig.SAPLING_TURN_DEAD_WHEN_FAIL.get().booleanValue();

        boolean isTreeGrow = onTreeGrow(event, world, pos, block, biome, replaceWithDead);

        if(!isTreeGrow){
            if(replaceWithDead){
                world.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
            }
        }
    }

    @SubscribeEvent
    public void onBone(BonemealEvent event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Block block = world.getBlockState(pos).getBlock();
        Biome biome = getBiome(world, pos);
        //boolean replaceWithDead = TreeConfig.SAPLING_TURN_DEAD_WHEN_FAIL.get().booleanValue();
        if(!isTreeAllowed(world, block, biome)){
            // if(replaceWithDead){
            //     Utils.toastMessage("Seems like this Plant does not grow well this biome. replace");
            // }else{
            //     Utils.toastMessage("Seems like this Plant does not grow well this biome. no replace");
            // }
            Utils.toastMessage("Seems like this Plant does not grow well this biome.");
        }
    }

    public static boolean isTreeAllowed(LevelAccessor world, Block block, Biome biome){
        List<String> allowed = BiomeSpecificGrowth.TREECONFIG.getBiomesForSapling(block);
        if (allowed == null) {
            //nothing listede for this plant, everythings fine stop blocking the event
            return true;
        }

        Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registries.BIOME);
        boolean treeAllowedToGrow = Utils.isInList(allowed, biomeReg.getKey(biome).getNamespace() + ":" + biomeReg.getKey(biome).getPath());
        if(treeAllowedToGrow){
            return true;
        }
        return false;
    }

    private boolean onTreeGrow(Event event, LevelAccessor world, BlockPos pos, Block block, Biome biome, boolean replaceWithDead){
        if(isTreeAllowed(world, block, biome)){
            return true;
        }
        if(Utils.isChanceSuccess(TreeConfig.SAPLING_SUCCESS_RATE.get())){
            /* 
            Utils.toastMessage("success");
            */
            return true;
        }
        event.setResult(Event.Result.DENY);
        if(replaceWithDead){
            world.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
        }
        this.doSmoke(world, pos);
        return false;
    }

    public static boolean isCropAllowed(LevelAccessor world, Block block, Biome biome){
        List<Double> allowed = BiomeSpecificGrowth.CROPCONFIG.getCharacteristicsForCrop(block);
        if (allowed == null) {
            //nothing listed for this plant, everythings fine stop blocking the event
            return true;
        }
        Double baseTemperature = allowed.get(0);
        Double baseDownfall = allowed.get(1);

        List<Double> variance = BiomeSpecificGrowth.CROPCONFIG.getVarianceForCrop(block);

        Double tempVariance = variance.get(0);
        Double downfallVariance = variance.get(1);

        boolean tempInRange = Utils.isInRange( (double) biome.getModifiedClimateSettings().temperature(), baseTemperature, tempVariance);
        boolean downfallInRange = Utils.isInRange( (double) biome.getModifiedClimateSettings().downfall(), baseDownfall, downfallVariance);
        if(tempInRange && downfallInRange){
            /* 
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal(
                    "Plant good, biome: "+ biome.getModifiedClimateSettings().temperature() 
                    + " Crop: " + baseTemperature
                    ), true);
            }
            Utils.toastMessage("Plant good, biome: "+ biome.getModifiedClimateSettings().temperature() 
                    + " Crop: " + baseTemperature);        
            */
            return true;
        }
        return false;
    }

    private boolean onCropGrow(Event event, LevelAccessor world, BlockPos pos, Block block, Biome biome, boolean breakWhenFail){
        if(isCropAllowed(world, block, biome)){
            return true;
        }

        if(Utils.isChanceSuccess(CropConfig.CROP_SUCCESS_RATE.get())){
            return true;
        }

        event.setResult(Event.Result.DENY);
        if(breakWhenFail){
            world.destroyBlock(pos, true);
        }
        return false;
    }

    public static Biome getBiome(LevelAccessor world, BlockPos pos) {
        return world.getBiomeManager().getBiome(pos).value();
    }

    private void doSmoke(LevelAccessor world, BlockPos pos) {
        double x = pos.getX() + .5;
        double y = pos.getY();
        double z = pos.getZ() + .5;
        double ySpeed = 0.05;
        for (int i = 0; i < 20; i++) {
          world.addParticle(ParticleTypes.FLAME, x + (Math.random()-0.5)*0.5, y+ (Math.random()-0.5)*0.5, z + (Math.random()-0.5)*0.5, 0.0D, ySpeed, 0.0D);
        }
      }

}
