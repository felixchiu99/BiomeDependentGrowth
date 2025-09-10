package com.gmail.chiuchohin.Config;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.gmail.chiuchohin.BiomeSpecificGrowth;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
// In some config class
@Mod.EventBusSubscriber(modid = BiomeSpecificGrowth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreeConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final String DELIM = "->";
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Double> SAPLING_SUCCESS_RATE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SAPLING_TURN_DEAD_WHEN_FAIL;
    
    public static final ForgeConfigSpec.ConfigValue<List<String>> SAPLING_WHITELIST_BIOMES;

    public static final ForgeConfigSpec.ConfigValue<List<String>> SAPLING_ITEM_BLOCK_MAP;

    private static final BooleanValue ISDEBUG;

    public TreeConfig(ForgeConfigSpec spec, Path path) {
        File file = new File(path.toFile(), path.getFileName().toString());

        CommentedFileConfig configData;

        if(!file.exists()){
            configData = CommentedFileConfig.builder(path)
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();
        }else{
            configData = CommentedFileConfig.builder(path)
            .sync()
            .autosave()
            .build();
        }

        configData.load();
        spec.setConfig(configData);
    }

    // public TreeConfig(ForgeConfigSpec.Builder builder) {

    //     final CommentedFileConfig configData = CommentedFileConfig.builder(path)
    //         .sync()
    //         .autosave()
    //         .writingMode(WritingMode.REPLACE)
    //         .build();

    //     configData.load();
    //     spec.setConfig(configData);
    // }

    static {
        BUILDER.push("BiomeSpecificGrowth Tree Configs");

        SAPLING_SUCCESS_RATE = BUILDER.comment("Sapling Growth Rate At Incorrect Biome. (Default: 0.2)").define("Sapling Success Rate", 0.2d);
        
        SAPLING_TURN_DEAD_WHEN_FAIL = BUILDER.comment("Will Saplings turn into a Dead bush when grow attempt failed. (Default: true)").define("Dead on failed", true);
        
        List<String> mSaplingWhiteList = new ArrayList<>();
        {
            mSaplingWhiteList.add(Blocks.ACACIA_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:savanna", 
                "minecraft:shattered_savanna", 
                "minecraft:shattered_savanna_plateau", 
                "minecraft:savanna_plateau", 
                "minecraft:modified_wooded_badlands_plateau",
                "minecraft:wooded_badlands_plateau",
                
                "terralith:amethyst_canyon",
                "terralith:amethyst_rainforest",
                "terralith:arid_highlands",
                "terralith:fractured_savanna",
                "terralith:hot_shrubland",
                "terralith:moonlight_grove",
                "terralith:moonlight_valley",
                "terralith:savanna_badlands",
                "terralith:savanna_slopes",

                "terralith:skylands_summer"
            }));
            mSaplingWhiteList.add(Blocks.BIRCH_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:birch_forest", 
                "minecraft:forest", 
                "minecraft:dark_forest", 
                "minecraft:dark_forest_hills",
                "minecraft:birch_forest_hills", 
                "minecraft:tall_birch_forest", 
                "minecraft:tall_birch_hills",

                "terralith:birch_taiga",
                "terralith:haze_mountain",
                "terralith:lavender_forest",
                "terralith:lavender_valley",
                "terralith:lush_valley",
                "terralith:orchid_swamp",
                "terralith:sakura_grove",
                "terralith:sakura_valley",
                "terralith:temperate_highlands",
                "terralith:white_cliffs",

                "terralith:skylands_autumn",
                "terralith:skylands_spring"
            }));
            mSaplingWhiteList.add(Blocks.SPRUCE_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:taiga", 
                "minecraft:giant_tree_taiga", 
                "minecraft:snowy_tundra", 
                "minecraft:taiga_hills", 
                "minecraft:snowy_taiga", 
                "minecraft:snowy_taiga_hills",
                "minecraft:giant_tree_taiga_hills",

                "terralith:alpine_grove",
                "terralith:alpine_highlands",
                "terralith:bryce_canyon",
                "terralith:cloud_forest",
                "terralith:cold_shrubland",
                "terralith:forested_highlands",
                "terralith:highlands",
                "terralith:lush_valley",
                "terralith:moonlight_grove",
                "terralith:moonlight_valley",
                "terralith:shield",
                "terralith:siberian_grove",
                "terralith:siberian_taiga",
                "terralith:snowy_maple_forest",
                "terralith:snowy_shield",
                "terralith:windswept_spires",
                "terralith:wintry_forest",
                "terralith:wintry_lowlands",
                "terralith:yellowstone",
                "terralith:yosemite_cliffs",
                "terralith:yosemite_lowlands",

                "terralith:skylands_winter"
            }));
            mSaplingWhiteList.add(Blocks.OAK_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:forest", 
                "minecraft:dark_forest", 
                "minecraft:dark_forest_hills",
                "minecraft:wooded_mountains", 
                "minecraft:wooded_hills", 
                "minecraft:swamp", 
                "minecraft:swamp_hills", 
                "minecraft:flower_forest",

                "terralith:alpha_islands",
                "terralith:alpha_islands_winter",
                "terralith:alpine_highlands",
                "terralith:birch_taiga",
                "terralith:brushland",
                "terralith:cold_shrubland",
                "terralith:haze_mountain",
                "terralith:rocky_jungle",
                "terralith:shrubland",
                "terralith:temperate_highlands",

                "terralith:skylands_autumn"
            }));
            mSaplingWhiteList.add(Blocks.DARK_OAK_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:dark_forest", 
                "minecraft:dark_forest_hills", 
                "minecraft:flower_forest",

                "terralith:lavender_forest",
                "terralith:lavender_valley",
                "terralith:mirage_isles",
                "terralith:sakura_grove",
                "terralith:sakura_valley",
                "terralith:snowy_maple_forest",
                "terralith:snowy_shield",

                "terralith:skylands_autumn",
                "terralith:skylands_spring",
                "terralith:skylands_winter"
            }));
            mSaplingWhiteList.add(Blocks.JUNGLE_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:jungle_edge", 
                "minecraft:jungle", 
                "minecraft:jungle_hills", 
                "minecraft:modified_jungle", 
                "minecraft:bamboo_jungle", 
                "minecraft:bamboo_jungle_hills",
                "minecraft:modified_jungle_edge", 
                "minecraft:modified_jungle",

                "terralith:desert_oasis",
                "terralith:desert_spires",
                "terralith:jungle_mountains",
                "terralith:red_oasis",
                "terralith:rocky_jungle",
                "terralith:tropical_jungle",

                "terralith:skylands_summer"
            }));
            mSaplingWhiteList.add(Blocks.CHERRY_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:cherry_grove",

                "terralith:sakura_grove",
                "terralith:sakura_valley",
                "terralith:snowy_cherry_grove",

                "terralith:skylands_spring"
            }));
            mSaplingWhiteList.add(Blocks.MANGROVE_PROPAGULE.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:swamp",
                "terralith:cave/underground_jungle",

                "terralith:ice_marsh"
            }));
            mSaplingWhiteList.add(Blocks.AZALEA.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:lush_caves",

                "terralith:blooming_valley",
                "terralith:brushland"
            }));
            mSaplingWhiteList.add(Blocks.FLOWERING_AZALEA.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:lush_caves",
                
                "terralith:blooming_valley",
                "terralith:brushland"
            }));

            //Let's do Vinery
            mSaplingWhiteList.add(
                "block.vinery.apple_tree_sapling"
                + DELIM + String.join(",", 
                new String[] {
                    "minecraft:forest", 
                    "minecraft:dark_forest", 
                    "minecraft:dark_forest_hills",
                    "minecraft:wooded_mountains", 
                    "minecraft:wooded_hills", 
                    "minecraft:swamp", 
                    "minecraft:swamp_hills", 
                    "minecraft:flower_forest",

                    "terralith:alpha_islands",
                    "terralith:alpha_islands_winter",
                    "terralith:alpine_highlands",
                    "terralith:birch_taiga",
                    "terralith:brushland",
                    "terralith:cold_shrubland",
                    "terralith:haze_mountain",
                    "terralith:rocky_jungle",
                    "terralith:shrubland",
                    "terralith:temperate_highlands",

                    "terralith:skylands_autumn"
                }
            ));
            mSaplingWhiteList.add(
                "block.vinery.dark_cherry_sapling"
                + DELIM + String.join(",", 
                new String[] {
                    "minecraft:cherry_grove",

                    "terralith:sakura_grove",
                    "terralith:sakura_valley",
                    "terralith:snowy_cherry_grove",

                    "terralith:skylands_spring"
                }
            ));
        }
        SAPLING_WHITELIST_BIOMES = BUILDER.comment(
            "Map growable block to CSV list of biomes no spaces, -> in between.  "+
            "It SHOULD be fine to add modded saplings (block.MODNAME.SAPLINGNAME). An empty list means the sapling can grow nowhere.  "+ 
            "Delete the key-entry for a sapling to let it grow everywhere.")
            .define("SaplingWhitelistBiome", mSaplingWhiteList
        );

        {
            List<String> mSaplingItemMap = new ArrayList<>();
            mSaplingItemMap.add(Items.ACACIA_SAPLING.getDescriptionId() + DELIM + Blocks.ACACIA_SAPLING.getDescriptionId());
            mSaplingItemMap.add(Items.BIRCH_SAPLING.getDescriptionId() + DELIM + Blocks.BIRCH_SAPLING.getDescriptionId());
            mSaplingItemMap.add(Items.SPRUCE_SAPLING.getDescriptionId() + DELIM + Blocks.SPRUCE_SAPLING.getDescriptionId());
            mSaplingItemMap.add(Items.OAK_SAPLING.getDescriptionId() + DELIM + Blocks.OAK_SAPLING.getDescriptionId());
            mSaplingItemMap.add(Items.DARK_OAK_SAPLING.getDescriptionId() + DELIM + Blocks.DARK_OAK_SAPLING.getDescriptionId());
            mSaplingItemMap.add(Items.JUNGLE_SAPLING.getDescriptionId() + DELIM + Blocks.JUNGLE_SAPLING.getDescriptionId());
            mSaplingItemMap.add(Items.CHERRY_SAPLING.getDescriptionId() + DELIM + Blocks.CHERRY_SAPLING.getDescriptionId());
            mSaplingItemMap.add(Items.MANGROVE_PROPAGULE.getDescriptionId() + DELIM + Blocks.MANGROVE_PROPAGULE.getDescriptionId());
            mSaplingItemMap.add(Blocks.AZALEA.getDescriptionId() + DELIM + Blocks.AZALEA.getDescriptionId());
            mSaplingItemMap.add(Blocks.FLOWERING_AZALEA.getDescriptionId() + DELIM + DELIM + Blocks.FLOWERING_AZALEA.getDescriptionId());
            //Let's do Vinery
            mSaplingItemMap.add(
                "block.vinery.apple_tree_sapling" 
                + DELIM + 
                "block.vinery.apple_tree_sapling"
            );

            mSaplingItemMap.add(
                "block.vinery.dark_cherry_sapling" 
                + DELIM + 
                "block.vinery.dark_cherry_sapling"
            );
            
            SAPLING_ITEM_BLOCK_MAP = BUILDER.comment(
                "Map Item to Growable Block. (block.MODNAME.SAPLINGNAME)"+
                "It only affect tooltips")
                .define("SaplingItemMap", mSaplingItemMap
            );
        }
        ISDEBUG = BUILDER.comment("Should GrowthInspector Display item's description ID").define("IsDebugMode", false);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public List<String> getBiomesForSapling(Block block) {
        String key = block.getDescriptionId();
        return getBiomesBySaplingId(key);
    }

    public List<String> getBiomesBySaplingId(String blockId) {
        Map<String, List<String>> mapInit = this.getMapBiome(SAPLING_WHITELIST_BIOMES);
        if (mapInit.containsKey(blockId) == false) {
            //null means no list set, so everything allowed
            return null;
        }
        //my list is allowed
        return mapInit.get(blockId);
    }

    public Map<String, List<String>> getMapBiome(ForgeConfigSpec.ConfigValue<List<String>> conf) {
        final Map<String, List<String>> mapInit = new HashMap<>();
        for (String splitme : conf.get()) {
            try {
                final String[] split = splitme.split(DELIM);
                final String blockId = split[0];
                final String[] biomes = split[1].split(",");
                mapInit.put(blockId, Arrays.asList(biomes));
            }
            catch (Exception e) {
                //bad config
                BiomeSpecificGrowth.LOGGER.error("Error reading bad config value :" + splitme, e);
            }
        }
        return mapInit;
    }
    public Map<String, List<String>> getMapItem(ForgeConfigSpec.ConfigValue<List<String>> conf) {
        final Map<String, List<String>> mapInit = new HashMap<>();
        for (String splitme : conf.get()) {
            try {
                final String[] split = splitme.split(DELIM);
                final String item = split[0];
                final String[] block = split[1].split(",");
                mapInit.put(item, Arrays.asList(block));
            }
            catch (Exception e) {
                //bad config
                BiomeSpecificGrowth.LOGGER.error("Error reading bad config value :" + splitme, e);
            }
        }
        return mapInit;
    }

    public List<String> getItemMap(Item item) {
        Map<String, List<String>> mapInit = this.getMapItem(SAPLING_ITEM_BLOCK_MAP);
        String key = item.getDescriptionId();
        if (mapInit.containsKey(key) == false) {
            //null means no list set, so everything allowed
            return null;
        }
        //my list is allowed
        return mapInit.get(key);
    }

    public boolean GetTreeDead(){
        return SAPLING_TURN_DEAD_WHEN_FAIL.get();
    }
    public boolean GetIsDebug(){
        return ISDEBUG.get();
    }
}
