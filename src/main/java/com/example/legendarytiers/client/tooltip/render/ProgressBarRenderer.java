package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipColors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public final class ProgressBarRenderer {

    public enum ProgressBarType {
        EXPERIENCE,
        DURABILITY
    }

    private ProgressBarRenderer() {}

    private static float displayedXp = 0.0F;
    private static float displayedDurability = 0.0F;

    public static void draw(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            float progress,
            ProgressBarType type
    ) {

        progress = Mth.clamp(progress, 0.0F, 1.0F);

        float displayed;

        if (type == ProgressBarType.EXPERIENCE) {
            displayedXp += (progress - displayedXp) * 0.15F;
            displayed = displayedXp;
        } else {
            displayedDurability += (progress - displayedDurability) * 0.15F;
            displayed = displayedDurability;
        }

        displayed = Mth.clamp(displayed, 0.0F, 1.0F);

        int fill = Math.round((width - 2) * displayed);

        //------------------------------------
        // Outer border
        //------------------------------------

        graphics.renderOutline(
                x,
                y,
                width,
                height,
                0xFF707070
        );

        //------------------------------------
        // Inner background
        //------------------------------------

        graphics.fill(
                x + 1,
                y + 1,
                x + width - 1,
                y + height - 1,
                0xFF1A1A1A
        );

        if (fill <= 0)
            return;

        //------------------------------------
        // Colors
        //------------------------------------

        int colorTop;
        int colorBottom;

        if (type == ProgressBarType.EXPERIENCE) {

            colorTop = 0xFF7FD8FF;
            colorBottom = 0xFF1E84FF;

        } else {

            if (displayed > 0.66F) {

                colorTop = 0xFF95F77F;
                colorBottom = 0xFF2FA82A;

            } else if (displayed > 0.33F) {

                colorTop = 0xFFFFD65A;
                colorBottom = 0xFFE79A00;

            } else {

                // ------------------------------
                // Пульсация красной зоны (<33%)
                // Чем меньше прочность, тем сильнее.
                // Максимальный эффект начинается после 10%.
                // ------------------------------

                float pulseStrength = 0.0F;

                if (displayed <= 0.10F) {

                    float time = System.currentTimeMillis() / 220.0F;

                    pulseStrength =
                            (float)((Math.sin(time) + 1.0) * 0.5);

                }

                int topGreen = (int)(124 - pulseStrength * 55);
                int topBlue  = (int)(124 - pulseStrength * 55);

                int bottomGreen = (int)(42 - pulseStrength * 22);
                int bottomBlue  = (int)(42 - pulseStrength * 22);

                colorTop =
                        0xFF000000 |
                                (255 << 16) |
                                (topGreen << 8) |
                                topBlue;

                colorBottom =
                        0xFF000000 |
                                (210 << 16) |
                                (bottomGreen << 8) |
                                bottomBlue;

            }

        }

        //------------------------------------
        // Gradient fill
        //------------------------------------

        graphics.fillGradient(
                x + 1,
                y + 1,
                x + 1 + fill,
                y + height - 1,
                colorTop,
                colorBottom
        );

        //------------------------------------
        // Highlight
        //------------------------------------

        graphics.fill(
                x + 2,
                y + 2,
                x + 1 + fill,
                y + height / 2,
                0x33FFFFFF
        );

        //------------------------------------
        // Small glow
        //------------------------------------

        graphics.fillGradient(
                x + 1,
                y + 1,
                x + 1 + fill,
                y + height - 1,
                0x10FFFFFF,
                0x00000000
        );
    }
}