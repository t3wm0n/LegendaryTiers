package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipTheme;
import net.minecraft.client.gui.GuiGraphics;

public final class BackgroundDustRenderer {

    private BackgroundDustRenderer() {}

    public static void render(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {

        long time = System.currentTimeMillis();

        int count = theme.particleCount();

        for (int i = 0; i < count; i++) {

            double seed = i * 83.481;

            double t =
                    seed
                            + time * 0.00002 * theme.particleSpeed();

            float px =
                    x + 6
                            + (float)((Math.sin(t) * 0.5 + 0.5) * (width - 12));

            float py =
                    y + 6
                            + (float)((Math.cos(t * 1.4) * 0.5 + 0.5) * (height - 12));

            float pulse =
                    0.3f
                            + 0.7f *
                            (float)Math.sin(t * 2.5);

            int alpha =
                    (int)(pulse * 12);

            int color =
                    (alpha << 24)
                            | (theme.particleColor() & 0xFFFFFF);

            graphics.fill(

                    (int)px,
                    (int)py,

                    (int)px + 2,
                    (int)py + 2,

                    color

            );

        }

    }

}