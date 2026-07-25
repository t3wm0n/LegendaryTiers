package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.TierData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class LegendaryTooltipBuilder {

    private LegendaryTooltipBuilder() {
    }


    public static LegendaryTooltipContext build(ItemStack stack) {

        TierData tier = stack.get(ModDataComponents.TIER_DATA);

        if (tier == null) {
            return null;
        }


        String itemName =
                stack.getHoverName().getString();


        Rarity rarity =
                tier.rarity();


        double quality =
                tier.quality();


        int experience =
                stack.getOrDefault(
                        ModDataComponents.EXPERIENCE,
                        0
                );


        int level =
                experience / 100;


        int experienceToNextLevel =
                100;


        List<ModifierEntry> modifiers =
                tier.modifiers();


        boolean broken =
                stack.is(ModDataComponents.BROKEN_ITEM.get());


        String originalItemName =
                getOriginalItemName(stack);


        int repairCost =
                getRepairCost(stack);


        int reforgeAttempts =
                stack.getOrDefault(
                        ModDataComponents.REFORGE_ATTEMPTS,
                        0
                );


        return new LegendaryTooltipContext(
                stack,
                itemName,
                rarity,
                quality,
                level,
                experience % experienceToNextLevel,
                experienceToNextLevel,
                modifiers,
                broken,
                originalItemName,
                repairCost,
                reforgeAttempts
        );
    }


    private static String getOriginalItemName(ItemStack stack) {

        var id =
                stack.get(ModDataComponents.ORIGINAL_ITEM_ID);


        if (id == null) {
            return "";
        }


        Item item =
                BuiltInRegistries.ITEM.get(id);


        if (item == null) {
            return "";
        }


        return item.getDescription()
                .getString();
    }


    private static int getRepairCost(ItemStack stack) {

        if (!stack.has(ModDataComponents.ORIGINAL_ITEM_ID)) {
            return 0;
        }


        return 0;
    }

}