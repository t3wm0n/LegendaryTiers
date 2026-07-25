package com.example.legendarytiers.util;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.TierData;
import net.minecraft.world.item.ItemStack;

public class TierAttributeHelper {

    public static double getAttribute(ItemStack stack, String attributeId, double baseValue) {

        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier == null)
            return baseValue;

        int exp = stack.getOrDefault(ModDataComponents.EXPERIENCE, 0);
        int level = exp / 100;
        double levelMultiplier = 1.0 + level * 0.005;

        double value = baseValue;

        for (ModifierEntry entry : tier.modifiers()) {

            if (!entry.target().equals("attribute"))
                continue;

            if (entry.attribute().isEmpty())
                continue;

            if (!entry.attribute().get().equals(attributeId))
                continue;

            double modifier = entry.value() * levelMultiplier;

            switch (entry.operation()) {

                case "addition" ->
                        value += modifier;

                case "multiply_base" ->
                        value += baseValue * modifier;

                case "multiply_total" ->
                        value *= 1.0 + modifier;
            }
        }

        return value;
    }
}