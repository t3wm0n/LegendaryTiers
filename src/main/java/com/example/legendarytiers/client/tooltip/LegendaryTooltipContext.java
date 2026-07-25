package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.Rarity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class LegendaryTooltipContext {

    private final ItemStack stack;

    private final String itemName;

    private final Rarity rarity;

    private final double quality;

    private final int level;

    private final int experience;

    private final int experienceToNextLevel;

    private final List<ModifierEntry> modifiers;

    private final boolean broken;

    private final String originalItemName;

    private final int repairCost;

    private final int reforgeAttempts;


    public LegendaryTooltipContext(
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

        this.stack = stack;
        this.itemName = itemName;
        this.rarity = rarity;
        this.quality = quality;
        this.level = level;
        this.experience = experience;
        this.experienceToNextLevel = experienceToNextLevel;
        this.modifiers = modifiers;
        this.broken = broken;
        this.originalItemName = originalItemName;
        this.repairCost = repairCost;
        this.reforgeAttempts = reforgeAttempts;

    }


    public ItemStack stack() {
        return stack;
    }


    public String itemName() {
        return itemName;
    }


    public Rarity rarity() {
        return rarity;
    }


    public double quality() {
        return quality;
    }


    public int level() {
        return level;
    }


    public int experience() {
        return experience;
    }


    public int experienceToNextLevel() {
        return experienceToNextLevel;
    }


    public List<ModifierEntry> modifiers() {
        return modifiers;
    }


    public boolean broken() {
        return broken;
    }


    public String originalItemName() {
        return originalItemName;
    }


    public int repairCost() {
        return repairCost;
    }


    public int reforgeAttempts() {
        return reforgeAttempts;
    }

}