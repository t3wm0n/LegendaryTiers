package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class BackgroundRenderer {

    private BackgroundRenderer() {}

    public static void render(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        graphics.blit(

                TooltipTextures.BACKGROUND,

                x,
                y,

                0,
                0,

                width,
                height,

                512,
                512

        );

        drawBackground(
                graphics,
                x,
                y,
                width,
                height,
                theme
                );

        RenderSystem.disableBlend();

        FrameRenderer.render(
                graphics,
                x,
                y,
                width,
                height,
                theme
        );

    }


    public static void drawBackground(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {

        // 1. Отрисовка витающих частиц (МЕЖДУ ФОНОМ И РАМКОЙ)
        ParticleRenderer.renderParticles(graphics, x, y, width, height, theme);

        // 2. Отрисовка рамки
        FrameRenderer.render(graphics, x, y, width, height, theme);
    }

}