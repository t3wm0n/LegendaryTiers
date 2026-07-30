package com.example.legendarytiers.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record RepairEntry(

        Ingredient ingredient,

        ItemStack displayStack,

        int amount

) {
}