package com.example.legendarytiers.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class RepairCostHelper {

    private static final Map<Item, RepairEntry> CACHE =
            new HashMap<>();

    private RepairCostHelper() {
    }

    public static void buildCache(Level level) {

        CACHE.clear();

        RecipeManager manager =
                level.getRecipeManager();

        /*
         * Ванильные предметы
         */

        for (Item item : BuiltInRegistries.ITEM) {

            if (item instanceof TieredItem tiered) {

                Ingredient ingredient =
                        tiered.getTier().getRepairIngredient();

                ItemStack display =
                        firstStack(ingredient);

                CACHE.put(

                        item,

                        new RepairEntry(

                                ingredient,

                                display,

                                getBaseCostByItem(item)

                        )

                );

                continue;

            }

            if (item instanceof ArmorItem armor) {

                Ingredient ingredient =
                        armor.getMaterial()
                                .value()
                                .repairIngredient().get();

                ItemStack display =
                        firstStack(ingredient);

                CACHE.put(

                        item,

                        new RepairEntry(

                                ingredient,

                                display,

                                getArmorBaseCost(armor)

                        )

                );

            }

        }

        /*
         * Остальные предметы
         */

        for (RecipeHolder<CraftingRecipe> holder
                : manager.getAllRecipesFor(RecipeType.CRAFTING)) {

            CraftingRecipe recipe =
                    holder.value();

            ItemStack result =
                    recipe.getResultItem(level.registryAccess());

            if (result.isEmpty()) {
                continue;
            }

            Item resultItem =
                    result.getItem();

            if (CACHE.containsKey(resultItem)) {
                continue;
            }

            collectRecipe(recipe).ifPresent(entry ->

                    CACHE.put(
                            resultItem,
                            entry
                    )

            );

        }

    }

    private static Optional<RepairEntry> collectRecipe(CraftingRecipe recipe) {

        Map<Item, Integer> frequency =
                new HashMap<>();

        Map<Item, Ingredient> ingredientMap =
                new HashMap<>();

        for (Ingredient ingredient : recipe.getIngredients()) {

            if (ingredient.isEmpty()) {
                continue;
            }

            ItemStack[] stacks =
                    ingredient.getItems();

            if (stacks.length == 0) {
                continue;
            }

            /*
             * Берём первый предмет как отображаемый.
             */

            Item item =
                    stacks[0].getItem();

            frequency.merge(
                    item,
                    1,
                    Integer::sum
            );

            ingredientMap.putIfAbsent(
                    item,
                    ingredient
            );

        }

        if (frequency.isEmpty()) {
            return Optional.empty();
        }

        /*
         * Ищем материал, который встречается чаще всего.
         */

        Item mainMaterial =
                frequency.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .orElseThrow()
                        .getKey();

        Ingredient ingredient =
                ingredientMap.get(mainMaterial);

        ItemStack display =
                new ItemStack(mainMaterial);

        int amount =
                frequency.get(mainMaterial) + 1;

        return Optional.of(

                new RepairEntry(

                        ingredient,

                        display,

                        amount

                )

        );

    }

    private static ItemStack firstStack(
            Ingredient ingredient
    ) {

        ItemStack[] stacks =
                ingredient.getItems();

        if (stacks.length == 0) {
            return ItemStack.EMPTY;
        }

        return stacks[0].copy();

    }

    private static int getBaseCostByItem(Item item) {

        if (item instanceof SwordItem) {
            return 2;
        }

        if (item instanceof PickaxeItem) {
            return 3;
        }

        if (item instanceof AxeItem) {
            return 3;
        }

        if (item instanceof HoeItem) {
            return 2;
        }

        if (item instanceof ShovelItem) {
            return 1;
        }

        if (item instanceof MaceItem) {
            return 2;
        }

        return getFallbackCost(item);

    }

    private static int getArmorBaseCost(ArmorItem armor) {

        return switch (armor.getEquipmentSlot()) {

            case HEAD -> 5;

            case CHEST -> 8;

            case LEGS -> 7;

            case FEET -> 4;

            default -> getFallbackCost(armor);

        };

    }

    public static int getFallbackCost(Item item) {

        int maxDurability =
                new ItemStack(item).getMaxDamage();

        if (maxDurability <= 0) return 3;
        if (maxDurability < 100) return 2;
        else if (maxDurability < 300) return 4;
        else if (maxDurability < 600) return 5;
        else if (maxDurability < 1000) return 6;
        else if (maxDurability < 2000) return 8;
        else if (maxDurability < 3000) return 12;
        else if (maxDurability < 4000) return 16;
        return 8;
    }

    public static Optional<RepairEntry> get(Item item) {

        return Optional.ofNullable(
                CACHE.get(item)
        );

    }

}