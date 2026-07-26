package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;

public final class ExperienceSection {

    private ExperienceSection() {
    }


    public static int getHeight() {
        return TooltipLayout.EXPERIENCE_HEIGHT;
    }


    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {

        if (context == null)
            return;


        int current = context.experience();
        int max = context.experienceToNextLevel();

        if (max <= 0)
            max = 1;


        // Иконка опыта

        IconRenderer.draw(
                graphics,
                x + TooltipLayout.PADDING,
                y + 4,
                TooltipIcons.LEVEL_X,
                TooltipIcons.LEVEL_Y
        );


        // Уровень

        TextRenderer.drawCenteredShadow(
                graphics,
                font,
                "Level " + context.level(),
                x + width / 2,
                y + 6,
                TooltipColors.TEXT_TITLE
        );


        // Полоса опыта

        int barX = x + TooltipLayout.PADDING;
        int barY = y + 22;

        int barWidth = width - TooltipLayout.PADDING * 4;
        barX += TooltipLayout.PADDING;
        int barHeight = 8;


        // фон

        graphics.fill(
                barX,
                barY,
                barX + barWidth,
                barY + barHeight,
                0x66000000
        );

        graphics.renderOutline(
                barX,
                barY,
                barWidth,
                barHeight,
                0xFFAAAAAA
        );


        // заполнение

        float progress =
                Math.min(1.0F, current / (float) max);


        graphics.fill(
                barX,
                barY,
                barX + (int)(barWidth * progress),
                barY + barHeight,
                0xFF4FA8FF
        );


        // Текст опыта

        int percent = Math.round(progress * 100);

        String expText =
                current +
                        " / " +
                        max +
                        " XP (" +
                        percent +
                        "%)";


        TextRenderer.drawCenteredShadow(
                graphics,
                font,
                expText,
                x + width / 2,
                barY + 10,
                TooltipColors.XP_TEXT
        );

    }

}