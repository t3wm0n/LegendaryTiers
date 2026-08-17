package com.example.legendarytiers.util; // Укажите ваш пакет

import com.example.legendarytiers.ModTags;
import com.example.legendarytiers.config.RPGITConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TierHelper {

    /**
     * Проверяет, является ли предмет Tierable.
     * Сначала проверяет JSON-тег главного файла (ModTags.TIERABLE_ITEM),
     * а затем дополнительные записи из конфига.
     */
    public static boolean isTierable(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        // 1. Проверка главного JSON тега
        if (stack.is(ModTags.TIERABLE_ITEMS)) {
            return true;
        }

        // 2. Проверка дополнительных записей из конфига
        return matchesList(stack, RPGITConfig.INSTANCE.tiers_system.additional_tierable_items);
    }

    /**
     * Определяет тип предмета ("weapon", "ranged", "armor", "tool", "shield").
     * Сначала смотрит JSON-теги мода, затем дополнительные теги из конфига.
     */
    public static String getItemType(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return "tool";

        // WEAPON
        if (stack.is(ModTags.WEAPON) || matchesList(stack, RPGITConfig.INSTANCE.item_types.additional_weapons)) {
            return "weapon";
        }
        // RANGED
        if (stack.is(ModTags.RANGED_WEAPON) || matchesList(stack, RPGITConfig.INSTANCE.item_types.additional_ranged)) {
            return "ranged";
        }
        // ARMOR
        if (stack.is(ModTags.ARMOR) || matchesList(stack, RPGITConfig.INSTANCE.item_types.additional_armor)) {
            return "armor";
        }
        // SHIELD
        if (stack.is(ModTags.SHIELD) || matchesList(stack, RPGITConfig.INSTANCE.item_types.additional_shields)) {
            return "shield";
        }
        // TOOL
        if (stack.is(ModTags.TOOL) || matchesList(stack, RPGITConfig.INSTANCE.item_types.additional_tools)) {
            return "tool";
        }

        return "tool"; // Fallback
    }

    /**
     * Проверяет, подходит ли stack под список строк (#tag или item_id)
     */
    private static boolean matchesList(ItemStack stack, List<? extends String> entries) {
        if (entries == null || entries.isEmpty()) return false;

        for (String entry : entries) {
            if (entry.startsWith("#")) {
                // Это тег (#some_mod:custom_tag)
                ResourceLocation tagLoc = ResourceLocation.tryParse(entry.substring(1));
                if (tagLoc != null) {
                    TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagLoc);
                    if (stack.is(tagKey)) {
                        return true;
                    }
                }
            } else {
                // Это предмет (some_mod:custom_sword)
                ResourceLocation itemLoc = ResourceLocation.tryParse(entry);
                if (itemLoc != null) {
                    if (BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(itemLoc)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}