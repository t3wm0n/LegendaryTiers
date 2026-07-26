package com.example.legendarytiers;

import com.example.legendarytiers.item.BrokenItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, LegendaryTiers.MOD_ID);

    public static final Supplier<Item> BROKEN_ITEM = ITEMS.register("broken_item",
            () -> new BrokenItem(new Item.Properties().stacksTo(1)));

    // Блок рунного стола
    public static final Supplier<Item> RUNIC_TABLE_ITEM = ITEMS.register("runic_table",
            () -> new BlockItem(ModBlocks.RUNIC_TABLE.get(), new Item.Properties()));

    // Чернила
    public static final Supplier<Item> RUNIC_INK = ITEMS.register("runic_ink",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final Supplier<Item> NETHER_INK = ITEMS.register("nether_ink",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final Supplier<Item> ENDER_INK = ITEMS.register("ender_ink",
            () -> new Item(new Item.Properties().stacksTo(16)));

    // Трафареты
    public static final Supplier<Item> WEAPON_STENCIL = ITEMS.register("weapon_stencil",
            () -> new Item(new Item.Properties().stacksTo(3)));
    public static final Supplier<Item> RANGED_STENCIL = ITEMS.register("ranged_stencil",
            () -> new Item(new Item.Properties().stacksTo(3)));
    public static final Supplier<Item> ARMOR_STENCIL = ITEMS.register("armor_stencil",
            () -> new Item(new Item.Properties().stacksTo(3)));
    public static final Supplier<Item> TOOL_STENCIL = ITEMS.register("tool_stencil",
            () -> new Item(new Item.Properties().stacksTo(3)));
    public static final Supplier<Item> SHIELD_STENCIL = ITEMS.register("shield_stencil",
            () -> new Item(new Item.Properties().stacksTo(3)));

    // Очищающий платок
    public static final Supplier<Item> CLEANSING_CLOTH = ITEMS.register("cleansing_cloth",
            () -> new Item(new Item.Properties().stacksTo(1)));
}