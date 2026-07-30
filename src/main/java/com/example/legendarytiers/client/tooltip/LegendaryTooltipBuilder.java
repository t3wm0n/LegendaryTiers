package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModItems;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.TierData;
import com.example.legendarytiers.util.ExperienceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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

        List<ModifierEntry> modifiers =
                tierData.modifiers() == null
                        ? Collections.emptyList()
                        : tierData.modifiers();

        int durability =
                stack.getMaxDamage() - stack.getDamageValue();

        int maxDurability =
                stack.getMaxDamage();

        boolean showAdvanced =
                Screen.hasShiftDown();

        boolean showEnchantments =
                Screen.hasControlDown();

        double durabilityBonus = 0.0;

        for (ModifierEntry modifier : modifiers) {

            if ("durability".equals(modifier.target())) {

                durabilityBonus += modifier.value();

            }

        }

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

        List<TooltipAttributeEntry> attributes =
                TooltipAttributeCollector.collect(
                        stack,
                        player,
                        modifiers
                );

        Font font = minecraft.font;

        int longestLine = font.width(stack.getHoverName());

        for (ModifierEntry modifier : modifiers) {

            if (modifier.attribute().isEmpty()) {
                continue;
            }

            String attributeId = modifier.attribute().get();

            String key = attributeId;

            int separator = key.indexOf(':');

            if (separator >= 0) {
                key = key.substring(separator + 1);
            }

            key = key.replace(':', '.');

            String name =
                    Component.translatable(
                            "attribute.name." + key
                    ).getString();

            AttributeFormat format =
                    AttributeFormatRegistry.get(attributeId);

            String bonus;

            switch (format) {

                case INTEGER ->
                        bonus = String.format("%+d",
                                (int)Math.round(modifier.value()));

                case DECIMAL ->
                        bonus = String.format("%+.2f",
                                modifier.value());

                case PERCENT ->
                        bonus = String.format("%+d%%",
                                (int)Math.round(modifier.value() * 100));

                default ->
                        bonus = "";

            }

            /*
             * Полная строка:
             * Название + итог + бонус
             */

            int lineWidth =
                    font.width(name)
                            + 90
                            + font.width(bonus);

            longestLine =
                    Math.max(longestLine, lineWidth);

        }

        int tooltipWidth =
                TooltipLayout.calculateWidth(longestLine);

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

                attributes,

                broken,

                originalItemName,

                durability,

                maxDurability,

                durabilityBonus,

                tooltipWidth,

                reforgeAttempts,

                showAdvanced,

                showEnchantments
        );
    }

}