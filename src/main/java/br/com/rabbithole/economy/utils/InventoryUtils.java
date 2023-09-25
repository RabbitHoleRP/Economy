package br.com.rabbithole.economy.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    public static boolean hasEmptySlot(Inventory inventory) {
        for (ItemStack item : inventory.getStorageContents()) {
            if (item == null) return true;
        }
        return false;
    }
}
