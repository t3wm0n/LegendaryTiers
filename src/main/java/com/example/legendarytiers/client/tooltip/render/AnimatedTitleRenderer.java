package com.example.legendarytiers.client.tooltip.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class AnimatedTitleRenderer {

    private AnimatedTitleRenderer() {
    }

    public static void render(

            GuiGraphics graphics,

            String title,

            int centerX,

            int y,

            int color

    ) {

        Font font =
                Minecraft.getInstance().font;

        float t =
                (System.currentTimeMillis() % 1200L)
                        / 1200f;

        int offset =
                (int)(Math.sin(t * Math.PI * 2.0) * 1.5);

        int width =
                font.width(title);

        graphics.drawString(

                font,

                title,

                centerX - width / 2,

                y + offset,

                color,

                true

        );

    }

}