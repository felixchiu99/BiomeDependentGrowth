package com.gmail.chiuchohin.item;

import com.gmail.chiuchohin.BiomeSpecificGrowth;
import com.gmail.chiuchohin.item.custom.GrowthInspectorItem;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, BiomeSpecificGrowth.MODID);

    public static final RegistryObject<Item> GROWTH_INSPECTOR = ITEMS.register("growth_inspector",
        () -> new GrowthInspectorItem( 
            new Item.Properties()
            )
        );

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
