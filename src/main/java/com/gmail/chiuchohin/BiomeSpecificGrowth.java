package com.gmail.chiuchohin;

import com.gmail.chiuchohin.Config.TreeConfig;
import com.gmail.chiuchohin.Config.CropConfig;

import com.gmail.chiuchohin.item.ModCreativeModTabs;
import com.gmail.chiuchohin.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;

import net.minecraftforge.api.distmarker.Dist;

import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BiomeSpecificGrowth.MODID)
public class BiomeSpecificGrowth
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "biomedependentgrowth";

    public static Logger LOGGER = LogManager.getLogger();

    public static TreeConfig TREECONFIG;
    public static CropConfig CROPCONFIG;

    public BiomeSpecificGrowth()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);

        ModCreativeModTabs.register(modEventBus);
        
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        TREECONFIG = new TreeConfig(TreeConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve(MODID + "-Tree-common-config.toml"));
        CROPCONFIG = new CropConfig(CropConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve(MODID + "-Crop-common-config.toml"));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new GrowEvents());
        MinecraftForge.EVENT_BUS.register(new ToolTipEvent());
        
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.accept(ModItems.GROWTH_INSPECTOR);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            
        }
    }
}
