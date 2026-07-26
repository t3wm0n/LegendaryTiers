package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class QualitySection {

    private static final int STAR_SPACING = 2;

    private QualitySection() {
    }


    public static int getHeight() {
        return TooltipLayout.QUALITY_HEIGHT;
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


        double quality = context.quality();


        double stars = quality * 5.0;

        int filledStars = (int) stars;

        double remainder = stars - filledStars;

        boolean halfStar = remainder >= 0.25 && remainder < 0.75;

        if (remainder >= 0.75) {
            filledStars++;
            halfStar = false;
        }

        int totalWidth =
                5 * TooltipIcons.DRAW_SIZE
                        + 4 * STAR_SPACING;

        int startX = x + (width - totalWidth) / 2;


        for (int i = 0; i < 5; i++) {

            int iconX;
            int iconY = TooltipIcons.STAR_EMPTY_Y;


            if (i < filledStars) {

                iconX = TooltipIcons.STAR_FILLED_X;
                iconY = TooltipIcons.STAR_FILLED_Y;

            } else if (i == filledStars && halfStar) {

                iconX = TooltipIcons.STAR_HALF_X;
                iconY = TooltipIcons.STAR_HALF_Y;

            } else {

                iconX = TooltipIcons.STAR_EMPTY_X;
                iconY = TooltipIcons.STAR_EMPTY_Y;

            }


            IconRenderer.draw(
                    graphics,
                    startX + i * (TooltipIcons.DRAW_SIZE + STAR_SPACING),
                    y + 3,
                    iconX,
                    iconY
            );

        }

    }

}