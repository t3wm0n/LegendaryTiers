package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipTheme;
import net.minecraft.client.gui.GuiGraphics;

public final class ProgressBarRenderer {

    private ProgressBarRenderer() {
    }

    public static void draw(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            float progress,
            TooltipTheme theme
    ) {

        progress = Math.max(0f, Math.min(1f, progress));

        int fillWidth =
                (int) ((width - 2) * progress);

        /*
         * Тень
         */

        graphics.fill(
                x,
                y,
                x + width,
                y + height,
                0x99000000
        );

        /*
         * Рамка
         */

        graphics.renderOutline(
                x,
                y,
                width,
                height,
                theme.borderColor()
        );

        /*
         * Фон полосы
         */

        graphics.fill(
                x + 1,
                y + 1,
                x + width - 1,
                y + height - 1,
                theme.backgroundDark()
        );

        if (fillWidth > 0) {

            /*
             * Основная заливка
             */

            drawHorizontalGradient(
                    graphics,
                    x + 1,
                    y + 1,
                    fillWidth,
                    height - 2,
                    theme.backgroundDark(),
                    theme.backgroundLight()
            );

            /*
             * Верхний блик
             */

            graphics.fill(
                    x + 1,
                    y + 1,
                    x + 1 + fillWidth,
                    y + 3,
                    theme.backgroundLight()
            );

            /*
             * Внутреннее свечение
             */

            graphics.fill(
                    x + 1,
                    y + 3,
                    x + 1 + fillWidth,
                    y + height - 3,
                    theme.borderGlow()
            );

            /*
             * Нижняя тень
             */

            graphics.fill(
                    x + 1,
                    y + height - 2,
                    x + 1 + fillWidth,
                    y + height - 1,
                    0x44000000
            );

        }

    }

    public static void drawHorizontalGradient(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            int leftColor,
            int rightColor
    ) {

        if (width <= 0)
            return;

        int a1 = (leftColor >> 24) & 255;
        int r1 = (leftColor >> 16) & 255;
        int g1 = (leftColor >> 8) & 255;
        int b1 = leftColor & 255;

        int a2 = (rightColor >> 24) & 255;
        int r2 = (rightColor >> 16) & 255;
        int g2 = (rightColor >> 8) & 255;
        int b2 = rightColor & 255;

        for (int i = 0; i < width; i++) {

            float t = width <= 1
                    ? 0f
                    : (float) i / (width - 1);

            int a = (int) (a1 + (a2 - a1) * t);
            int r = (int) (r1 + (r2 - r1) * t);
            int g = (int) (g1 + (g2 - g1) * t);
            int b = (int) (b1 + (b2 - b1) * t);

            int color =
                    (a << 24)
                            | (r << 16)
                            | (g << 8)
                            | b;

            graphics.fill(
                    x + i,
                    y,
                    x + i + 1,
                    y + height,
                    color
            );

        }

    }

}