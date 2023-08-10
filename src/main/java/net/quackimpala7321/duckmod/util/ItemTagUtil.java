package net.quackimpala7321.duckmod.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

public class ItemTagUtil {
    public static boolean itemIsOf(ItemStack stack, TagKey<Item> tagKey) {
        return stack.streamTags().anyMatch(tagKey1 -> tagKey1 == tagKey);
    }
}
