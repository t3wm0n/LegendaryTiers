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

        if (context == null) {
            return;
        }

        int current = context.experience();
        int max = context.experienceToNextLevel();

        if (max <= 0) {
            max = 1;
        }

        //------------------------------------------
        // Иконка уровня
        //------------------------------------------

        IconRenderer.draw(
                graphics,
                x + TooltipLayout.PADDING,
                y + 2,
                TooltipIcons.LEVEL_X,
                TooltipIcons.LEVEL_Y
        );

        //------------------------------------------
        // Уровень
        //------------------------------------------

        TextRenderer.drawCenteredShadow(
                graphics,
                font,
                "Level " + context.level(),
                x + width / 2,
                y + 6,
                TooltipColors.TEXT_TITLE
        );

        //------------------------------------------
        // Полоса опыта
        //------------------------------------------

        float progress =
                Math.min(1.0F, current / (float) max);

        int barX = x + 20;
        int barY = y + 22;

        int barWidth = width - 40;
        int barHeight = 12;

        ProgressBarRenderer.draw(
                graphics,
                barX,
                barY,
                barWidth,
                barHeight,
                progress,
                ProgressBarRenderer.ProgressBarType.EXPERIENCE
        );

        //------------------------------------------
        // Текст опыта
        //------------------------------------------

        int percent =
                Math.round(progress * 100);

        String expText =
                current
                        + " / "
                        + max
                        + " XP ("
                        + percent
                        + "%)";

        TextRenderer.drawCenteredShadow(
                graphics,
                font,
                expText,
                x + width / 2,
                y + 36,
                TooltipColors.XP_TEXT
        );

    }

}