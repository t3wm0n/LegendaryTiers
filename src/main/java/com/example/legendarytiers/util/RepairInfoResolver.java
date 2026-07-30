package com.example.legendarytiers.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public final class RepairInfoResolver {

    private RepairInfoResolver() {
    }

    public static RepairInfo resolve(ItemStack stack) {

        if (stack.isEmpty()) {

            return new RepairInfo(
                    false,
                    ItemStack.EMPTY,
                    0
            );

        }

        /**Repairable repairable =
                stack.get(DataComponents.REPAIRABLE);

        if (repairable == null) {

            return new RepairInfo(
                    false,
                    ItemStack.EMPTY,
                    0
            );

        }

        Ingredient ingredient =
                repairable.items();

        ItemStack[] stacks =
                ingredient.getItems();

        if (stacks.length == 0) {

            return new RepairInfo(
                    false,
                    ItemStack.EMPTY,
                    0
            );

        }

        return new RepairInfo(

                true,

                stacks[0].copy(),

                getMaterialAmount(stack.getItem())

        );**/
        return null;
    }

    private static int getMaterialAmount(Item item) {

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
        else return 5;
    }

}