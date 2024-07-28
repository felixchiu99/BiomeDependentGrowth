package com.gmail.chiuchohin.Config;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

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
@Mod.EventBusSubscriber(modid = BiomeSpecificGrowth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreeConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final String DELIM = "->";
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Float> SAPLING_SUCCESS_RATE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SAPLING_TURN_DEAD_WHEN_FAIL;
    
    public static final ForgeConfigSpec.ConfigValue<List<String>> SAPLING_WHITELIST_BIOMES;

    public TreeConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();
        configData.load();
        spec.setConfig(configData);
    }
    static {
        BUILDER.push("BiomeSpecificGrowth Tree Configs");

        SAPLING_SUCCESS_RATE = BUILDER.comment("Sapling Growth Rate At Incorrect Biome. (Default: 0.2)").define("Sapling Success Rate", 0.2f);
        
        SAPLING_TURN_DEAD_WHEN_FAIL = BUILDER.comment("Will Saplings turn into a Dead bush when grow attempt failed. (Default: true)").define("Dead on failed", true);
        
        List<String> mSaplingWhiteList = new ArrayList<>();
        {
            mSaplingWhiteList.add(Blocks.ACACIA_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:savanna", 
                "minecraft:shattered_savanna", 
                "minecraft:shattered_savanna_plateau", 
                "minecraft:savanna_plateau", 
                "minecraft:modified_wooded_badlands_plateau",
                "minecraft:wooded_badlands_plateau"
            }));
            mSaplingWhiteList.add(Blocks.BIRCH_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:birch_forest", 
                "minecraft:forest", 
                "minecraft:birch_forest_hills", 
                "minecraft:tall_birch_forest", 
                "minecraft:tall_birch_hills" 
            }));
            mSaplingWhiteList.add(Blocks.SPRUCE_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:taiga", 
                "minecraft:giant_tree_taiga", 
                "minecraft:snowy_tundra", 
                "minecraft:taiga_hills", 
                "minecraft:snowy_taiga", 
                "minecraft:snowy_taiga_hills",
                "minecraft:giant_tree_taiga_hills" 
            }));
            mSaplingWhiteList.add(Blocks.OAK_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:forest", 
                "minecraft:dark_forest", 
                "minecraft:wooded_mountains", 
                "minecraft:wooded_hills", 
                "minecraft:swamp", 
                "minecraft:swamp_hills", 
                "minecraft:flower_forest"
            }));
            mSaplingWhiteList.add(Blocks.DARK_OAK_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:dark_forest", 
                "minecraft:dark_forest_hills", 
                "minecraft:flower_forest"
            }));
            mSaplingWhiteList.add(Blocks.JUNGLE_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:jungle_edge", 
                "minecraft:jungle", 
                "minecraft:jungle_hills", 
                "minecraft:modified_jungle", 
                "minecraft:bamboo_jungle", 
                "minecraft:bamboo_jungle_hills",
                "minecraft:modified_jungle_edge", 
                "minecraft:modified_jungle" 
            }));
            mSaplingWhiteList.add(Blocks.CHERRY_SAPLING.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:cherry_grove"
            }));
            mSaplingWhiteList.add(Blocks.MANGROVE_PROPAGULE.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:swamp"
            }));
        }
        SAPLING_WHITELIST_BIOMES = BUILDER.comment(
            "Map growable block to CSV list of biomes no spaces, -> in between.  "+
            "It SHOULD be fine to add modded saplings. An empty list means the sapling can grow nowhere.  "+ 
            "Delete the key-entry for a sapling to let it grow everywhere.")
            .define("SaplingWhitelistBiome", mSaplingWhiteList
        );

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public List<String> getBiomesForSapling(Block block) {
        Map<String, List<String>> mapInit = this.getMapBiome(SAPLING_WHITELIST_BIOMES);
        String key = block.getDescriptionId();
        if (mapInit.containsKey(key) == false) {
            //null means no list set, so everything allowed
            return null;
        }
        //my list is allowed
        return mapInit.get(key);
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

}
