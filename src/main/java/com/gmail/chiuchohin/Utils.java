package com.gmail.chiuchohin;

import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Utils
{
    public static void toastMessage(String message) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null) {
        mc.player.displayClientMessage(Component.literal(message), true);
      }
      //mc.close();
    }

    public static boolean isInRange(float fromBiome, float fromCrop, float acceptedVariance){
      float highTolerance = fromCrop + acceptedVariance;
      float lowTolerance = fromCrop - acceptedVariance;
      return fromBiome >= lowTolerance && fromBiome <= highTolerance ;
    }

    public static boolean isInList(final List<String> list, String toMatch) {
      for (String strFromList : list) {
        if (strFromList == null || strFromList.isEmpty()) {
          continue;
        }
        if (strFromList.equals(toMatch)) {
          return true;
        }
        String[] blockIdArray = strFromList.split(":");
        if (blockIdArray.length <= 1) {
          return false;
        }
        String modIdFromList = blockIdArray[0];
        String blockIdFromList = blockIdArray[1];
        String[] blockToMatch = toMatch.split(":");
        if (blockToMatch.length <= 1) {
          return false;
        }
        String modIdToMatch = blockToMatch[0];
        String blockIdToMatch = blockToMatch[1];
        if (modIdFromList.equals(modIdToMatch) == false) {
          continue;
        }
        String blockIdListWC = blockIdFromList.replace("*", "");
        if (blockIdToMatch.contains(blockIdListWC)) {
          return true;
        }
      }
      return false;
    }
    public static boolean isChanceSuccess(float successPercentage){
      successPercentage = successPercentage>1?1:successPercentage;
      successPercentage = successPercentage<0?0:successPercentage;
      var rand = Math.random();
      return rand <= successPercentage;
    }
    public static void sendMessageToPlayer(Player player, String msg){
      player.sendSystemMessage(Component.literal(msg));
    }
    public static void sendMessageToPlayer(Player player, Component component){
      player.sendSystemMessage(component);
    }
}
