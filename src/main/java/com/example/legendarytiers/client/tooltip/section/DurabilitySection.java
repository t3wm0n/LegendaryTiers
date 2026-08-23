package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.TierData;
import com.example.legendarytiers.client.tooltip.*;
import com.example.legendarytiers.client.tooltip.helpers.DurabilityEnergyHelper;
import com.example.legendarytiers.client.tooltip.render.ProgressBarRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import com.example.legendarytiers.event.ModEvents;
import com.example.legendarytiers.util.ExperienceUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

        DurabilityEnergyHelper.Info info = DurabilityEnergyHelper.getDurabilityOrEnergy(stack);
        TierData tier = stack.get(ModDataComponents.TIER_DATA);

        // Если у предмета нет энергии/прочности/жидкости и нет тира — пропускаем
        if (info == null && tier == null) return;

        TooltipTheme theme = TooltipThemes.get(context.rarity());
        int barX = x + TooltipLayout.PADDING;
        int barY = y + 5;
        int barWidth = width - TooltipLayout.PADDING * 2;

        float progress;
        String textEnd;
        boolean forceBrokenRed = false;

        if (info != null) {
            progress = Math.clamp(info.getRatio(), 0.0f, 1.0f);

            switch (info.type()) {
                case ENERGY -> {
                    String formattedCurrent = formatEnergyValue(info.current());
                    String formattedMax = formatEnergyValue(info.max());
                    textEnd = Component.translatable("tooltip.legendarytiers.charge").getString()
                            + ": " + formattedCurrent + " / " + formattedMax + " " + info.unit();
                }
                case FLUID -> {
                    String formattedCurrent = formatEnergyValue(info.current());
                    String formattedMax = formatEnergyValue(info.max());
                    textEnd = Component.translatable("tooltip.legendarytiers.fuel").getString()
                            + ": " + formattedCurrent + " / " + formattedMax + " " + info.unit();
                }
                case DURABILITY -> {
                    long currentDurability = info.current();
                    long maxDurability = info.max();

                    String text = currentDurability + " / " + maxDurability;

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
                default -> {
                    forceBrokenRed = true;
                    progress = 1.0f;
                    textEnd = Component.translatable("attribute.name.generic.durability").getString() + ": 0 / 0";
                }
            }
        } else {
            // Фолбэк для предмета с TierData, у которого полностью отсутствует прочность/энергия
            forceBrokenRed = true;
            progress = 1.0f;
            textEnd = Component.translatable("attribute.name.generic.durability").getString() + ": N/A";
        }

        // Отрисовка шкалы (если forceBrokenRed = true, рисуем полностью красную шкалу)
        if (forceBrokenRed) {
            renderFullRedBar(graphics, barX, barY, barWidth, 14);
        } else {
            ProgressBarRenderer.draw(
                    graphics,
                    barX,
                    barY,
                    barWidth,
                    14,
                    progress,
                    theme
            );
        }

        // Отрисовка текста по центру
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
     * Отрисовывает полностью закрашенный красный прогресс-бар для предметов без ресурса прочности.
     */
    private static void renderFullRedBar(GuiGraphics graphics, int x, int y, int width, int height) {
        // Задняя рамка/фон
        graphics.fill(x, y, x + width, y + height, 0xFF220000);
        // Заполняющий красный цвет
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF8B0000);
        // Верхний градиент/светлый акцент
        graphics.fill(x + 1, y + 1, x + width - 1, y + 3, 0xFFFF3333);
    }

    private static String formatEnergyValue(long value) {
        if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000.0f);
        } else if (value >= 1_000) {
            return String.format("%.1fk", value / 1_000.0f);
        }
        return String.valueOf(value);
    }

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