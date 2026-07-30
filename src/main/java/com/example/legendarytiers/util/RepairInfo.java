package com.example.legendarytiers.util;

import net.minecraft.world.item.ItemStack;

public record RepairInfo(

        boolean repairable,

        ItemStack material,

        int amount

) {
}