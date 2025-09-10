package com.gmail.chiuchohin;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolTipEvent
{
    @SubscribeEvent
    public void ItemTooltipEvent(ItemTooltipEvent event)
    {
        Item item = event.getItemStack().getItem();
        List<String> isCrop = BiomeSpecificGrowth.CROPCONFIG.getItemMap(item);

        if (isCrop != null) {
            DisplayCrop(isCrop.toArray()[0].toString(), event);
        }

        if(isCrop == null){
            return;
        }



        //MutableComponent component = Component.literal("This is a test");
        //event.getToolTip().add(component);
    }

    private void DisplayCrop(String blockID, ItemTooltipEvent event){
        List<Double> allowed = BiomeSpecificGrowth.CROPCONFIG.getCharacteristicsForCrop(blockID);
        if (allowed == null) {
            //nothing listed for this plant, everythings fine stop blocking the event
            return;
        }
        Double baseTemperature = allowed.get(0);
        Double baseDownfall = allowed.get(1);

        List<Double> variance = BiomeSpecificGrowth.CROPCONFIG.getVarianceForCrop(blockID);

        Double tempVariance = variance.get(0);
        Double downfallVariance = variance.get(1);

        MutableComponent tempComponent = (Component.literal("Temperature Range: ").withStyle(ChatFormatting.GREEN))
            .append(Component.literal("" + String.format("%.02f", (baseTemperature-tempVariance)) ).withStyle(ChatFormatting.WHITE))
            .append(Component.literal(" - ").withStyle(ChatFormatting.WHITE))
            .append(Component.literal("" + String.format("%.02f", (baseTemperature+tempVariance)) ).withStyle(ChatFormatting.WHITE))
            ;
        event.getToolTip().add(tempComponent);
        MutableComponent downfallComponent = (Component.literal("Downfall Range: ").withStyle(ChatFormatting.AQUA))
            .append(Component.literal("" + String.format("%.02f", (baseDownfall-downfallVariance)) ).withStyle(ChatFormatting.WHITE))
            .append(Component.literal(" - ").withStyle(ChatFormatting.WHITE))
            .append(Component.literal("" + String.format("%.02f", (baseDownfall+downfallVariance)) ).withStyle(ChatFormatting.WHITE))
            ;
        event.getToolTip().add(downfallComponent);
    }

}
