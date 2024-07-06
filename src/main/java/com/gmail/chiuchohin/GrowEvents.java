package com.gmail.chiuchohin;

import java.util.List;

import com.gmail.chiuchohin.Config.TreeConfig;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
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

    }

    @SubscribeEvent
    public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {

    }

    @SubscribeEvent
    public void onBone(BonemealEvent event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Block block = world.getBlockState(pos).getBlock();
        Biome biome = getBiome(world, pos);

        List<String> allowed = BiomeSpecificGrowth.TREECONFIG.getBiomesForSapling(block);
        if (allowed == null) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal("Non-Config " + block.getName().toString()), true);
            }
            //nothing listede for this sapling, evertyhings fine stop blocking the event
            return;
        }

        Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registries.BIOME);
        boolean treeAllowedToGrow = Utils.isInList(allowed, biomeReg.getKey(biome));
        if(!treeAllowedToGrow){
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal("Cannot grow this crop in " + biomeReg.getKey(biome) + "biome"), true);
            }
            //this.doSmoke(world, pos);
        }

    }

    public static Biome getBiome(LevelAccessor world, BlockPos pos) {
        return world.getBiomeManager().getBiome(pos).value();
      }
}
