package com.example.legendarytiers.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BrokenItem extends Item {
    public BrokenItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return true;
    }
}
