package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipTheme;
import net.minecraft.client.gui.GuiGraphics;

public final class BackgroundParticleRenderer {

    private BackgroundParticleRenderer() {
    }

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

            double seed = i * 173.381;

            double t =
                    time * theme.particleSpeed() * 0.001
                            + seed;

            /*
             * Положение
             */

            float px =
                    x + width * 0.5f
                            + (float)Math.sin(t * 0.9)
                            * width * 0.42f;

            float py =
                    y + height * 0.5f
                            + (float)Math.cos(t * 0.7)
                            * height * 0.42f;

            /*
             * Мерцание
             */

            float pulse =
                    0.65f
                            + 0.35f
                            * (float)Math.sin(
                            t * 2.3
                    );

            int size =
                    Math.max(
                            6,
                            (int)(
                                    theme.particleSize()
                                            * 8f
                                            * pulse
                            )
                    );

            int frame = i & 3;

            ParticleRenderer.draw(

                    graphics,

                    frame,

                    (int)px,

                    (int)py,

                    size

            );

        }

    }

}