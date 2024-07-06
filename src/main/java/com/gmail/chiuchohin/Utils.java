package com.gmail.chiuchohin;

import java.util.List;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.TranslatableComponent;

public class Utils
{
    public static void chatMessage(Player player, String message) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("Hello, friend."), true);
        }
    }

    public static boolean isInList(final List<String> list, ResourceLocation toMatch) {
        if (toMatch == null) {
          return false;
        }
        String id = toMatch.getNamespace();
        for (String strFromList : list) {
          if (strFromList == null || strFromList.isEmpty()) {
            continue;
          }
          if (strFromList.equals(id)) {
            return true;
          }
          String[] blockIdArray = strFromList.split(":");
          if (blockIdArray.length <= 1) {
            return false;
          }
          String modIdFromList = blockIdArray[0];
          String blockIdFromList = blockIdArray[1];
          String modIdToMatch = toMatch.getNamespace();
          String blockIdToMatch = toMatch.getPath();
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
}
