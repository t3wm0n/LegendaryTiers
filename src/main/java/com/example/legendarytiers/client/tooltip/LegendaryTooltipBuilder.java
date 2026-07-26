package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModItems;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.TierData;
import com.example.legendarytiers.util.ExperienceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public final class LegendaryTooltipBuilder {

    private LegendaryTooltipBuilder() {
    }

    public static LegendaryTooltipContext build(ItemStack stack) {

        Minecraft minecraft = Minecraft.getInstance();

        LocalPlayer player = minecraft.player;

        TierData tierData =
                stack.get(ModDataComponents.TIER_DATA);

        if (tierData == null) {
            return null;
        }

        int experience =
                stack.getOrDefault(
                        ModDataComponents.EXPERIENCE,
                        0
                );

        int level =
                ExperienceUtil.getLevel(experience);

        int currentLevelExperience =
                ExperienceUtil.getCurrentLevelExperience(experience);

        int experienceToNextLevel =
                ExperienceUtil.getExperienceToNextLevel();

        int reforgeAttempts =
                stack.getOrDefault(
                        ModDataComponents.REFORGE_ATTEMPTS,
                        0
                );

        boolean broken =
                stack.is(ModItems.BROKEN_ITEM.get());

        String originalItemName = "";

        ResourceLocation originalItemId =
                stack.get(ModDataComponents.ORIGINAL_ITEM_ID);

        if (originalItemId != null) {

            var item =
                    BuiltInRegistries.ITEM.getOptional(originalItemId);

            if (item.isPresent()) {
                originalItemName =
                        item.get().getDescription().getString();
            }

        }

        List<ModifierEntry> modifiers =
                tierData.modifiers() == null
                        ? Collections.emptyList()
                        : tierData.modifiers();

        return new LegendaryTooltipContext(

                player,

                stack,

                stack.getHoverName().getString(),

                tierData.rarity(),

                tierData.quality(),

                level,

                currentLevelExperience,

                experienceToNextLevel,

                modifiers,

                broken,

                originalItemName,

                calculateRepairCost(tierData.rarity()),

                reforgeAttempts
        );
    }

    /**
     * Пока используется временная формула.
     * Позже будет заменена на настоящую систему ремонта.
     */
    private static int calculateRepairCost(Rarity rarity) {

        return switch (rarity) {

            case COMMON -> 1;

            case RARE -> 2;

            case EPIC -> 3;

            case LEGENDARY -> 4;

            case MYTHIC -> 5;

            case DIVINE -> 6;
        };

    }

}