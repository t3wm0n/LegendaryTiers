package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.Rarity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record LegendaryTooltipContext(
        LocalPlayer player,
        ItemStack stack,
        String itemName,
        Rarity rarity,
        double quality,
        int level,
        int experience,
        int experienceToNextLevel,
        List<ModifierEntry> modifiers,
        boolean broken,
        String originalItemName,
        int repairCost,
        int reforgeAttempts
) {

    public LocalPlayer player() {
        return player;
    }

}