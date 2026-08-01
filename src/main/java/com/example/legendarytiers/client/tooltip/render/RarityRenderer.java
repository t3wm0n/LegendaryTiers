package com.example.legendarytiers.client.tooltip.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class RarityRenderer {

    private RarityRenderer() {
    }

    public static void render(

            GuiGraphics graphics,

            String text,

            int centerX,

            int y,

            int color

    ) {

        Font font =
                Minecraft.getInstance().font;

        int width =
                font.width(text);

        graphics.drawString(

                font,

                text,

                centerX - width / 2,

                y,

                color,

                false

        );

    }

}