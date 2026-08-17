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
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

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

        // 1. Берем истинную базовую прочность ванильного предмета
        int baseDurability = stack.getItem().getDefaultInstance().getMaxDamage();
        if (baseDurability <= 0) return;

        TierData tier = stack.get(ModDataComponents.TIER_DATA);

        // 2. Рассчитываем актуальную прочность на лету для тултипа
        int maxDurability = (tier != null)
                ? ModEvents.calculateMaxDamage(stack, baseDurability, tier)
                : stack.getMaxDamage();

        if (maxDurability <= 0) return;

        // Оставшаяся прочность
        int currentDurability = Math.max(0, maxDurability - stack.getDamageValue());

        int barX = x + TooltipLayout.PADDING;
        int barY = y + 5;
        int barWidth = width - TooltipLayout.PADDING * 2;

        float progress = (float) currentDurability / maxDurability;

        TooltipTheme theme = TooltipThemes.get(context.rarity());

        // Рисуем шкалу прочности
        ProgressBarRenderer.draw(
                graphics,
                barX,
                barY,
                barWidth,
                14,
                progress,
                theme
        );

        // 3. Формируем текст (теперь сразу будет "270 / 270")
        String text = currentDurability + " / " + maxDurability;

        // 4. Вычисляем процент бонуса
        double bonusMultiplier = calculateDurabilityBonus(stack);

        if (Math.abs(bonusMultiplier) > 0.0001) {
            int percent = (int) Math.round(bonusMultiplier * 100);
            if (percent > 0) {
                text += " (+" + percent + "%)";
            } else if (percent < 0) {
                text += " (" + percent + "%)";
            }
        }

        String textEnd = Component.translatable("attribute.name.generic.durability").getString() + " " + text;

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