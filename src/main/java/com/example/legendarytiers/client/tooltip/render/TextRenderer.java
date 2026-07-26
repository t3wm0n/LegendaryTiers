package com.example.legendarytiers.client.tooltip.render;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class TextRenderer {

    private TextRenderer() {
    }


    public static void draw(
            GuiGraphics graphics,
            Font font,
            String text,
            int x,
            int y,
            int color
    ) {

        graphics.drawString(
                font,
                text,
                x,
                y,
                color,
                false
        );

    }


    public static void drawShadow(
            GuiGraphics graphics,
            Font font,
            String text,
            int x,
            int y,
            int color
    ) {

        graphics.drawString(
                font,
                text,
                x,
                y,
                color,
                true
        );

    }


    public static void drawCentered(
            GuiGraphics graphics,
            Font font,
            String text,
            int centerX,
            int y,
            int color
    ) {

        int width = font.width(text);

        graphics.drawString(
                font,
                text,
                centerX - width / 2,
                y,
                color,
                false
        );

    }


    public static void drawCenteredShadow(
            GuiGraphics graphics,
            Font font,
            String text,
            int centerX,
            int y,
            int color
    ) {

        int width = font.width(text);

        graphics.drawString(
                font,
                text,
                centerX - width / 2,
                y,
                color,
                true
        );

    }

}