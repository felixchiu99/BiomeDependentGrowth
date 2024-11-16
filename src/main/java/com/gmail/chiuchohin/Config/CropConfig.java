package com.gmail.chiuchohin.Config;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
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
public class CropConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final String DELIM = "->";
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Float> CROP_SUCCESS_RATE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> CROP_BREAK_WHEN_FAIL;

    public static final ForgeConfigSpec.ConfigValue<Float> CROP_DEFAULT_TEMP;
    public static final ForgeConfigSpec.ConfigValue<Float> CROP_DEFAULT_TEMP_TOLERANCE;
    public static final ForgeConfigSpec.ConfigValue<Float> CROP_DEFAULT_DOWNFALL;
    public static final ForgeConfigSpec.ConfigValue<Float> CROP_DEFAULT_DOWNFALL_TOLERANCE;
    
    public static final ForgeConfigSpec.ConfigValue<List<String>> CROP_WHITELIST_BIOMES;

    public static final ForgeConfigSpec.ConfigValue<List<String>> CROP_CHARACTERISTICS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> CROP_VARIATIONS;

    public static final ForgeConfigSpec.ConfigValue<List<String>> CROP_ITEM_BLOCK_MAP;

    private static final BooleanValue ISDEBUG;
    
    public CropConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();
        configData.load();
        spec.setConfig(configData);
    }
    static {
        BUILDER.push("BiomeSpecificGrowth Crop Configs");

        CROP_SUCCESS_RATE = BUILDER.comment("Crop Growth Rate At Incorrect Biome. (Default: 0.2)").define("Crop Success Rate", 0.2f);
        
        CROP_DEFAULT_TEMP = BUILDER.comment("Crop Default Temperature. (Default: 0.5)").define("Crop Default Temperature", 0.5f);
        CROP_DEFAULT_TEMP_TOLERANCE = BUILDER.comment("Crop Tolerance to Temperature.(+/-) (Default: 0.3)").define("Crop Temperature Tolerance", 0.3f);
        CROP_DEFAULT_DOWNFALL = BUILDER.comment("Crop Default Downfall. (Default: 0.5)").define("Crop Default Downfall", 0.5f);
        CROP_DEFAULT_DOWNFALL_TOLERANCE = BUILDER.comment("Crop Tolerance to Downfall.(+/-) (Default: 0.3)").define("Crop Downfall Tolerance", 0.3f);

        CROP_BREAK_WHEN_FAIL = BUILDER.comment("Will Crop break when grow event failed. (Default: false)").define("Dead on failed",false);

        {
            List<String> mCropCharacteristicList = new ArrayList<>();
            // temp, downfall
            mCropCharacteristicList.add(Blocks.WHEAT.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.5",
                "0.7"
            }));
            mCropCharacteristicList.add(Blocks.CARROTS.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));
            mCropCharacteristicList.add(Blocks.POTATOES.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.5",
                "0.5"
            }));
            mCropCharacteristicList.add(Blocks.BEETROOTS.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));
            mCropCharacteristicList.add(Blocks.SUGAR_CANE.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.8",
                "0.7"
            }));
            mCropCharacteristicList.add(Blocks.PUMPKIN_STEM.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.5",
                "0.5"
            }));
            mCropCharacteristicList.add(Blocks.MELON_STEM.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.8",
                "0.65"
            }));
            mCropCharacteristicList.add(Blocks.CACTUS.getDescriptionId() + DELIM + String.join(",", new String[] {
                "1.7",
                "0"
            }));
            mCropCharacteristicList.add(Blocks.COCOA.getDescriptionId() + DELIM + String.join(",", new String[] {
                "1",
                "1.2"
            }));
            mCropCharacteristicList.add(Blocks.SWEET_BERRY_BUSH.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.35",
                "0.45"
            }));

            //farmer's delight
            mCropCharacteristicList.add(
                "block.farmersdelight.rice" + DELIM + String.join(",", new String[] {
                "0.8",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.farmersdelight.budding_tomatoes" + DELIM + String.join(",", new String[] {
                "0.7",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.farmersdelight.tomatoes" + DELIM + String.join(",", new String[] {
                "0.7",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.farmersdelight.cabbages" + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.farmersdelight.onions" + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));

            //let's do brewery
            mCropCharacteristicList.add(
                "block.brewery.barley_crop" + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.brewery.hops_crop" + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.brewery.hops_crop_body" + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.brewery.corn_crop" + DELIM + String.join(",", new String[] {
                "0.8",
                "0.4"
            }));
            
            //let's do winery
            mCropCharacteristicList.add(
                "block.vinery.red_grape_bush" + DELIM + String.join(",", new String[] {
                "0.8",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.vinery.white_grape_bush" + DELIM + String.join(",", new String[] {
                "0.8",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.vinery.savanna_grape_bush_red" + DELIM + String.join(",", new String[] {
                "1.7",
                "0"
            }));
            mCropCharacteristicList.add(
                "block.vinery.savanna_grape_bush_white" + DELIM + String.join(",", new String[] {
                "1.7",
                "0"
            }));
            mCropCharacteristicList.add(
                "block.vinery.jungle_grape_bush_red" + DELIM + String.join(",", new String[] {
                "1",
                "1.2"
            }));
            mCropCharacteristicList.add(
                "block.vinery.jungle_grape_bush_white" + DELIM + String.join(",", new String[] {
                "1",
                "1.2"
            }));
            mCropCharacteristicList.add(
                "block.vinery.taiga_grape_bush_red" + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));
            mCropCharacteristicList.add(
                "block.vinery.taiga_grape_bush_white" + DELIM + String.join(",", new String[] {
                "0.3",
                "0.5"
            }));

            CROP_CHARACTERISTICS = BUILDER.comment(
                "Map growable block to temperature and Downfall for "+
                "It SHOULD be fine to add modded Crop (block.MODNAME.CROPNAME). An empty list means the Crop will use the default temperature and downfall value"+ 
                "Delete the key-entry for a Crop to let it grow everywhere.")
                .define("CropCharacteristics", mCropCharacteristicList
            );
        }

        {
            List<String> mCropItemMap = new ArrayList<>();
            mCropItemMap.add(Items.WHEAT_SEEDS.getDescriptionId() + DELIM + Blocks.WHEAT.getDescriptionId());
            mCropItemMap.add(Items.CARROT.getDescriptionId() + DELIM + Blocks.CARROTS.getDescriptionId());
            mCropItemMap.add(Items.POTATO.getDescriptionId() + DELIM + Blocks.POTATOES.getDescriptionId());
            mCropItemMap.add(Items.BEETROOT_SEEDS.getDescriptionId() + DELIM + Blocks.BEETROOTS.getDescriptionId());
            mCropItemMap.add(Items.SUGAR_CANE.getDescriptionId() + DELIM + Blocks.SUGAR_CANE.getDescriptionId());
            mCropItemMap.add(Items.PUMPKIN_SEEDS.getDescriptionId() + DELIM + Blocks.PUMPKIN_STEM.getDescriptionId());
            mCropItemMap.add(Items.MELON_SEEDS.getDescriptionId() + DELIM + Blocks.MELON_STEM.getDescriptionId());
            mCropItemMap.add(Items.CACTUS.getDescriptionId() + DELIM + Blocks.CACTUS.getDescriptionId());
            mCropItemMap.add(Items.COCOA_BEANS.getDescriptionId() + DELIM + Blocks.COCOA.getDescriptionId());
            mCropItemMap.add(Items.SWEET_BERRIES.getDescriptionId() + DELIM + Blocks.SWEET_BERRY_BUSH.getDescriptionId());
            
            //farmer's delight
            mCropItemMap.add(
                "item.farmersdelight.rice" + 
                DELIM + 
                "block.farmersdelight.rice"
            );
            mCropItemMap.add(
                "item.farmersdelight.tomato_seeds" + 
                DELIM + 
                "block.farmersdelight.budding_tomatoes"
            );
            mCropItemMap.add(
                "item.farmersdelight.cabbage_seeds" + 
                DELIM + 
                "block.farmersdelight.cabbages"
            );
            mCropItemMap.add(
                "item.farmersdelight.onion" + 
                DELIM + 
                "block.farmersdelight.onions"
            );

            //Let's do brewery
            mCropItemMap.add(
                "block.brewery.barley_crop" + 
                DELIM + 
                "block.brewery.barley_crop"
            );
            mCropItemMap.add(
                "block.brewery.hops_crop" + 
                DELIM + 
                "block.brewery.hops_crop"
            );
            mCropItemMap.add(
                "item.brewery.corn_seed" + 
                DELIM + 
                "block.brewery.corn_crop"
            );
            
            //Let's do vinery
            mCropItemMap.add(
                "item.vinery.red_grape_seeds" + 
                DELIM + 
                "block.vinery.red_grape_bush"
            );
            mCropItemMap.add(
                "item.vinery.white_grape_seeds" + 
                DELIM + 
                "block.vinery.white_grape_bush"
            );
            mCropItemMap.add(
                "item.vinery.savanna_grape_seeds_red" + 
                DELIM + 
                "block.vinery.savanna_grape_bush_red"
            );
            mCropItemMap.add(
                "item.vinery.savanna_grape_seeds_white" + 
                DELIM + 
                "block.vinery.savanna_grape_bush_white"
            );
            mCropItemMap.add(
                "item.vinery.jungle_grape_seeds_red" + 
                DELIM + 
                "block.vinery.jungle_grape_bush_red"
            );
            mCropItemMap.add(
                "item.vinery.jungle_grape_seeds_white" + 
                DELIM + 
                "block.vinery.jungle_grape_bush_white"
            );
            mCropItemMap.add(
                "item.vinery.taiga_grape_seeds_red" + 
                DELIM + 
                "block.vinery.taiga_grape_bush_red"
            );
            mCropItemMap.add(
                "item.vinery.taiga_grape_seeds_white" + 
                DELIM + 
                "block.vinery.taiga_grape_bush_white"
            );

            CROP_ITEM_BLOCK_MAP = BUILDER.comment(
                "Map Item to Growable Block. (item.MODNAME.CROPNAME):(block.MODNAME.CROPNAME)"+
                "It only affect tooltips")
                .define("CropItemMap", mCropItemMap
            );
        }

        {
            List<String> mCropVariationList = new ArrayList<>();
            mCropVariationList.add(Blocks.PUMPKIN_STEM.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.35",
                "0.5"
            }));
            mCropVariationList.add(Blocks.COCOA.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.4",
                "0.3"
            }));
            mCropVariationList.add(Blocks.SWEET_BERRY_BUSH.getDescriptionId() + DELIM + String.join(",", new String[] {
                "0.3",
                "0.35"
            }));

            //farmer's delight
            mCropVariationList.add(
                "block.farmersdelight.cabbages"
                + DELIM + String.join(","
                , new String[] {
                    "0.35",
                    "0.5"
                })
            );
            
            //Let's do Brewery
            mCropVariationList.add(
                "block.brewery.barley_crop"
                + DELIM + String.join(","
                , new String[] {
                    "0.3",
                    "0.25"
                })
            );
            mCropVariationList.add(
                "block.brewery.hops_crop"
                + DELIM + String.join(","
                , new String[] {
                    "0.4",
                    "0.3"
                })
            );
            mCropVariationList.add(
                "block.brewery.hops_crop_body"
                + DELIM + String.join(","
                , new String[] {
                    "0.4",
                    "0.4"
                })
            );
            mCropVariationList.add(
                "block.brewery.corn_crop"
                + DELIM + String.join(","
                , new String[] {
                    "0.4",
                    "0.4"
                })
            );
            //Let's do vinery
            mCropVariationList.add(
                "block.vinery.jungle_grape_bush_red"
                + DELIM + String.join(","
                , new String[] {
                    "0.4",
                    "0.3"
                })
            );
            mCropVariationList.add(
                "block.vinery.jungle_grape_bush_white"
                + DELIM + String.join(","
                , new String[] {
                    "0.4",
                    "0.3"
                })
            );


            CROP_VARIATIONS = BUILDER.comment(
                "Map growable block to Variation tolerance in temperature and Downfall "+
                "It SHOULD be fine to add modded Crop. An empty list means the Crop will use the default temperature and downfall value"+ 
                "Delete the key-entry for a Crop to let it grow everywhere.")
                .define("CropVariations", mCropVariationList
            );
        }

        List<String> mCropWhiteList = new ArrayList<>();
        {
            ArrayList<String> plainsList = new ArrayList<String>();
            plainsList.add("minecraft:plains");
            plainsList.add("minecraft:sunflower_plains");

            ArrayList<String> riverList = new ArrayList<String>();
            riverList.add("minecraft:river");
            {
                ArrayList<String> wheatList = new ArrayList<String>();
                wheatList.addAll(plainsList);
                wheatList.addAll(riverList);
                wheatList.add("minecraft:swamp");
                wheatList.add("minecraft:beach");
                String[] wheat = Arrays.copyOf(wheatList.toArray(), wheatList.size(), String[].class);
                
                mCropWhiteList.add(Blocks.WHEAT.getDescriptionId() + DELIM + String.join(",", wheat));
            }
            mCropWhiteList.add(Blocks.CARROTS.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:taiga", 
                "minecraft:savanna", 
                "minecraft:savanna*", 
                "minecraft:*savanna", 
                "minecraft:shattered_savanna_plateau", 
                "minecraft:sunflower_plains",
                "minecraft:giant_tree_taiga_hills" 
            }));
            mCropWhiteList.add(Blocks.POTATOES.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:taiga", 
                "minecraft:snowy*", 
                "minecraft:taiga*", 
                "minecraft:*forest", 
                "minecraft:dark_forest_hills", 
                "minecraft:*taiga", 
                "minecraft:*mountains",
                "minecraft:giant_tree_taiga_hills", 
                "minecraft:mountain_edge"
            }));
            mCropWhiteList.add(Blocks.BEETROOTS.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:taiga", 
                "minecraft:forest", 
                "minecraft:swamp", 
                "minecraft:flower_forest", 
                "minecraft:birch_forest", 
                "minecraft:birch*",
                "minecraft:tall_birch_forest", 
                "minecraft:tall_birch_hills",
                "minecraft:dark_forest", 
                "minecraft:dark_forest_hills"
            }));
            mCropWhiteList.add(Blocks.COCOA.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:jungle_edge", 
                "minecraft:jungle", 
                "minecraft:jungle_hills", 
                "minecraft:modified_jungle", 
                "minecraft:bamboo_jungle", 
                "minecraft:bamboo_jungle_hills",
                "minecraft:modified_jungle_edge", 
                "minecraft:modified_jungle" 
            }));

            mCropWhiteList.add(Blocks.MELON_STEM.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:ocean", 
                "minecraft:river", 
                "minecraft:beach",
                "minecraft:deep_ocean", 
                "minecraft:warm_ocean", 
                "minecraft:lukewarm_ocean", 
                "minecraft:deep_warm_ocean", 
                "minecraft:deep_lukewarm_ocean",
                "minecraft:jungle", 
                "minecraft:jungle*", 
                "minecraft:*jungle", 
                "minecraft:bamboo_jungle",
                "minecraft:bamboo_jungle_hills", 
                "minecraft:modified_jungle_edge", 
                "minecraft:modified_jungle" 
            }));
            mCropWhiteList.add(Blocks.PUMPKIN_STEM.getDescriptionId() + DELIM + String.join(",", new String[] {
                "minecraft:extreme*", 
                "minecraft:taiga", 
                "minecraft:snowy*", 
                "minecraft:taiga*", 
                "minecraft:dark_forest_hills", 
                "minecraft:*taiga",
                "minecraft:giant_tree_taiga_hills" 
            }));

            final String[] cactua = new String[] {
                "minecraft:desert", 
                "minecraft:desert_hills", 
                "minecraft:desert_lakes", 
                "minecraft:badlands", 
                "minecraft:badlands*", 
                "minecraft:*badlands"
            };
            mCropWhiteList.add(Blocks.CACTUS.getDescriptionId() + DELIM + String.join(",", cactua));

            final String[] reeds = new String[] {
                "minecraft:desert_lakes", 
                "minecraft:stone_shore",
                "minecraft:ocean", 
                "minecraft:river", 
                "minecraft:beach",
                "minecraft:deep_ocean", 
                "minecraft:warm_ocean", 
                "minecraft:lukewarm_ocean", 
                "minecraft:deep_warm_ocean", 
                "minecraft:deep_lukewarm_ocean",
            };
            mCropWhiteList.add(Blocks.SUGAR_CANE.getDescriptionId() + DELIM + String.join(",", reeds));
        }
        CROP_WHITELIST_BIOMES = BUILDER.comment(
            "Map growable block to CSV list of biomes no spaces, -> in between.  "+
            "It SHOULD be fine to add modded Crop (block.MODNAME.SAPLINGNAME). An empty list means the Crop can grow nowhere.  "+ 
            "Delete the key-entry for a Crop to let it grow everywhere.")
            .define("CropWhitelistBiome", mCropWhiteList
        );

        ISDEBUG = BUILDER.comment("Should GrowthInspector Display item's description ID").define("IsDebugMode", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public List<Float> getCharacteristicsForCrop(Block block) {
        String key = block.getDescriptionId();
        List<Float> characteristicsList = getCharacteristicsForCrop(key);
        return characteristicsList;
    }

    public List<Float> getCharacteristicsForCrop(String key) {
        Map<String, List<String>> mapInit = this.getMapCharacteristics(CROP_CHARACTERISTICS);
        float temp = CROP_DEFAULT_TEMP_TOLERANCE.get();
        float downfall = CROP_DEFAULT_DOWNFALL_TOLERANCE.get();

        List<Float> characteristicsList = new ArrayList<>();

        if (mapInit.containsKey(key) == false) {
            //null means no list set, so everything allowed
            return null;
        }else{
            try {
                temp = Float.parseFloat(mapInit.get(key).get(0));
            }catch (Exception e) {
                temp = CROP_DEFAULT_TEMP_TOLERANCE.get();
            }
            try {
                downfall = Float.parseFloat(mapInit.get(key).get(1));
            }catch (Exception e) {
                downfall = CROP_DEFAULT_DOWNFALL_TOLERANCE.get();
            }
        }
        characteristicsList.add(temp);
        characteristicsList.add(downfall);
        return characteristicsList;
    }

    public List<Float> getVarianceForCrop(Block block) {
        String key = block.getDescriptionId();
        List<Float> varianceList = getVarianceForCrop(key);
        return varianceList;
    }
    public List<Float> getVarianceForCrop(String key) {
        Map<String, List<String>> mapInit = this.getMapCharacteristics(CROP_VARIATIONS);
        List<Float> varianceList = new ArrayList<>();

        float tempVariance = CROP_DEFAULT_TEMP_TOLERANCE.get();
        float downfallVariance = CROP_DEFAULT_DOWNFALL_TOLERANCE.get();

        if (mapInit.containsKey(key) == false) {
            varianceList.add(tempVariance);
            varianceList.add(downfallVariance);
            return varianceList;
        }else{
            try {
                tempVariance = Float.parseFloat(mapInit.get(key).get(0));
            }catch (Exception e) {
                tempVariance = CROP_DEFAULT_TEMP_TOLERANCE.get();
            }
            try {
                downfallVariance = Float.parseFloat(mapInit.get(key).get(1));
            }catch (Exception e) {
                downfallVariance = CROP_DEFAULT_DOWNFALL_TOLERANCE.get();
            }
        }
        varianceList.add(tempVariance);
        varianceList.add(downfallVariance);
        return varianceList;
    }

    public List<String> getBiomesForCrop(Block block) {
        Map<String, List<String>> mapInit = this.getMapCharacteristics(CROP_WHITELIST_BIOMES);
        String key = block.getDescriptionId();
        if (mapInit.containsKey(key) == false) {
            //null means no list set, so everything allowed
            return null;
        }
        //my list is allowed
        return mapInit.get(key);
    }

    public List<String> getItemMap(Item item) {
        Map<String, List<String>> mapInit = this.getMapItem(CROP_ITEM_BLOCK_MAP);
        String key = item.getDescriptionId();
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

    public Map<String, List<String>> getMapCharacteristics(ForgeConfigSpec.ConfigValue<List<String>> conf) {
        final Map<String, List<String>> mapInit = new HashMap<>();
        for (String splitme : conf.get()) {
            try {
                final String[] split = splitme.split(DELIM);
                final String blockId = split[0];
                final String[] characteristics = split[1].split(",");
                mapInit.put(blockId, Arrays.asList(characteristics));
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

    public boolean GetCropBreak(){
        return CROP_BREAK_WHEN_FAIL.get();
    }
    public boolean GetIsDebug(){
        return ISDEBUG.get();
    }
}
