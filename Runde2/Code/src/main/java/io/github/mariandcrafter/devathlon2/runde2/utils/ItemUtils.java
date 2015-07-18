package io.github.mariandcrafter.devathlon2.runde2.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utils for item stacks.
 */
public final class ItemUtils {

    private ItemUtils() {
    }

    /**
     * Creates a new item stack with the given material and the given display name.
     *
     * @param material the material of the item stack
     * @param name     the display name of the item stack
     * @return the created item stack
     */
    public static ItemStack createItemStackWithName(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

}
