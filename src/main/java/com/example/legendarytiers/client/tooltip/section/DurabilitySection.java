package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.example.legendarytiers.client.tooltip.render.ProgressBarRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class DurabilitySection {

    private DurabilitySection() {
    }

    public static int getHeight() {
        return 34;
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {

        if (context.maxDurability() <= 0) {
            return;
        }

        int current = context.durability();
        int max = context.maxDurability();

        float progress = (float) current / (float) max;

        //----------------------------------------
        // Иконка
        //----------------------------------------

        IconRenderer.draw(
                graphics,
                x + TooltipLayout.PADDING,
                y,
                TooltipIcons.DURABILITY_X,
                TooltipIcons.DURABILITY_Y
        );

        //----------------------------------------
        // Название
        //----------------------------------------

        TextRenderer.draw(
                graphics,
                font,
                Component.translatable("attribute.name.generic.durability").getString(),
                x + TooltipLayout.PADDING + TooltipIcons.DRAW_SIZE + 6,
                y + 5,
                TooltipColors.TEXT_NORMAL
        );

        //----------------------------------------
        // Числа
        //----------------------------------------

        String durabilityText;

        if (context.durabilityBonus() != 0) {

            int percent = (int) Math.round(context.durabilityBonus() * 100.0);

            String sign = percent > 0 ? "+" : "";

            durabilityText =
                    current +
                            " / " +
                            max +
                            " (" +
                            sign +
                            percent +
                            "%)";

        } else {

            durabilityText =
                    current +
                            " / " +
                            max;

        }

        int textWidth = font.width(durabilityText);

        TextRenderer.draw(
                graphics,
                font,
                durabilityText,
                x + width - TooltipLayout.PADDING - textWidth,
                y + 5,
                TooltipColors.TEXT_NORMAL
        );

        //----------------------------------------
        // Полоса
        //----------------------------------------

        int barX = x + 20;
        int barY = y + 20;

        int barWidth = width - 40;
        int barHeight = 12;

        ProgressBarRenderer.draw(
                graphics,
                barX,
                barY,
                barWidth,
                barHeight,
                progress,
                ProgressBarRenderer.ProgressBarType.DURABILITY
        );
    }
}