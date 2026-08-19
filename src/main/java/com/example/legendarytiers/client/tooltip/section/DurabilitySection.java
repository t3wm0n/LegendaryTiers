package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.event.ModEvents;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.TierData;
import com.example.legendarytiers.client.tooltip.*;
import com.example.legendarytiers.client.tooltip.render.ProgressBarRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import com.example.legendarytiers.util.ExperienceUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public final class DurabilitySection {

    private DurabilitySection() {
    }

    public static int getHeight() {
        return 30;
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {
        ItemStack stack = context.stack();
        if (stack == null || stack.isEmpty()) return;

        // 1. Проверяем наличие энергии (FE / RF из техно-модов)
        IEnergyStorage energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);

        float progress;
        String textEnd;

        TooltipTheme theme = TooltipThemes.get(context.rarity());
        int barX = x + TooltipLayout.PADDING;
        int barY = y + 5;
        int barWidth = width - TooltipLayout.PADDING * 2;

        if (energy != null && energy.getMaxEnergyStored() > 0) {
            // --- РЕЖИМ ЭНЕРГИИ (Электроинструменты / Буры) ---
            int maxEnergy = energy.getMaxEnergyStored();
            int currentEnergy = energy.getEnergyStored();

            progress = Math.clamp((float) currentEnergy / maxEnergy, 0.0f, 1.0f);

            String formattedCurrent = formatEnergyValue(currentEnergy);
            String formattedMax = formatEnergyValue(maxEnergy);

            textEnd = Component.translatable("tooltip.legendarytiers.charge").getString() + ": " + formattedCurrent + " / " + formattedMax + " FE";
        } else {
            // --- РЕЖИМ ОБЫЧНОЙ ПРОЧНОСТИ ---
            Integer baseObj = stack.getItem().components().get(DataComponents.MAX_DAMAGE);
            int baseDurability = (baseObj != null) ? baseObj : stack.getItem().getDefaultInstance().getMaxDamage();
            if (baseDurability <= 0) return;

            TierData tier = stack.get(ModDataComponents.TIER_DATA);

            int maxDurability = (tier != null)
                    ? ModEvents.calculateMaxDamage(stack, baseDurability, tier)
                    : stack.getMaxDamage();

            if (maxDurability <= 0) return;

            int currentDurability = Math.max(0, maxDurability - stack.getDamageValue());
            progress = Math.clamp((float) currentDurability / maxDurability, 0.0f, 1.0f);

            String text = currentDurability + " / " + maxDurability;

            // Вычисляем процент бонуса
            double bonusMultiplier = calculateDurabilityBonus(stack);
            if (Math.abs(bonusMultiplier) > 0.0001) {
                int percent = (int) Math.round(bonusMultiplier * 100);
                if (percent > 0) {
                    text += " (+" + percent + "%)";
                } else if (percent < 0) {
                    text += " (" + percent + "%)";
                }
            }

            textEnd = Component.translatable("attribute.name.generic.durability").getString() + ": " + text;
        }

        // 2. Рисуем шкалу прогресса
        ProgressBarRenderer.draw(
                graphics,
                barX,
                barY,
                barWidth,
                14,
                progress,
                theme
        );

        // 3. Выводим текст по центру шкалы
        int textWidth = font.width(textEnd);

        TextRenderer.drawShadow(
                graphics,
                font,
                textEnd,
                barX + (barWidth - textWidth) / 2,
                barY + 3,
                0xFFFFFFFF
        );
    }

    /**
     * Форматирует большие числа энергии (150000 -> 150k, 2500000 -> 2.5M)
     */
    private static String formatEnergyValue(int value) {
        if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000.0f);
        } else if (value >= 1_000) {
            return String.format("%.1fk", value / 1_000.0f);
        }
        return String.valueOf(value);
    }

    /**
     * Вычисляет процентный бонус прочности с учётом уровня предмета
     * и ослабления штрафов.
     */
    private static double calculateDurabilityBonus(ItemStack stack) {
        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier == null) return 0.0;

        int exp = stack.getOrDefault(ModDataComponents.EXPERIENCE, 0);
        double levelMultiplier = ExperienceUtil.getMultiplier(exp);

        double totalMult = 1.0;
        double baseMult = 0.0;

        for (ModifierEntry entry : tier.modifiers()) {
            if (!entry.target().equals("durability")) continue;

            double scaledVal = ModEvents.getScaledModifierValue(entry, levelMultiplier);

            switch (entry.operation()) {
                case "multiply_total" -> totalMult *= (1.0 + scaledVal);
                case "multiply_base" -> baseMult += scaledVal;
            }
        }

        return ((1.0 + baseMult) * totalMult) - 1.0;
    }
}