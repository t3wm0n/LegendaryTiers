package com.example.legendarytiers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final TagKey<Item> WEAPON = createItemTag("weapon");
    public static final TagKey<Item> RANGED_WEAPON = createItemTag("ranged_weapon");
    public static final TagKey<Item> ARMOR = createItemTag("armor");
    public static final TagKey<Item> TOOL = createItemTag("tool");
    public static final TagKey<Item> SHIELD = createItemTag("shield");

    private static TagKey<Item> createItemTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(LegendaryTiers.MOD_ID, name));
    }
    public static final TagKey<Item> TIERABLE_ITEMS = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(LegendaryTiers.MOD_ID, "tierable_items"));
}
