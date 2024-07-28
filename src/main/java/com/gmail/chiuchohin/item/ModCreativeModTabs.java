package com.gmail.chiuchohin.item;

import com.gmail.chiuchohin.BiomeSpecificGrowth;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BiomeSpecificGrowth.MODID);
    
    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register(
        "biome_growth_tab",
        () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GROWTH_INSPECTOR.get()))
            .title(Component.translatable("creativetab.biome_growth_tab"))
            .displayItems((pParameters,pOutput) -> {
                pOutput.accept(ModItems.GROWTH_INSPECTOR.get());
            })
            .build()
        );
    
    

    public static void register (IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
