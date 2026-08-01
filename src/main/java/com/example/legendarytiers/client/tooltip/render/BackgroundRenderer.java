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

                256,
                256

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

        renderGlowTexture(
                graphics,
                x,
                y,
                width,
                height,
                theme
        );

        BackgroundDustRenderer.render(
                graphics,
                x,
                y,
                width,
                height,
                theme
        );

        renderGlow(graphics, x, y, width, height, theme);

        renderInnerShadow(graphics, x, y, width, height);

    }

    private static void renderGlowTexture(

            GuiGraphics graphics,

            int x,

            int y,

            int width,

            int height,

            TooltipTheme theme

    ) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int color = theme.borderColor();

        float r = ((color >> 16) & 255) / 255F;
        float g = ((color >> 8) & 255) / 255F;
        float b = (color & 255) / 255F;

        RenderSystem.setShaderColor(r, g, b, 0.45F);

        graphics.blit(

                TooltipTextures.GLOW,

                x - 24,
                y - 24,

                0,
                0,

                width + 48,
                height + 48,

                64,
                64

        );

        RenderSystem.setShaderColor(1,1,1,1);

        RenderSystem.disableBlend();

    }

    private static void renderGlow(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {

        graphics.fillGradient(

                x,
                y,

                x + width,
                y + height / 3,

                theme.backgroundHighlight(),
                0x00000000

        );

    }

    private static void renderInnerShadow(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height
    ) {

        graphics.fill(
                x + 1,
                y + 1,
                x + width - 1,
                y + height - 1,
                0x08000000
        );

        graphics.fill(
                x + 2,
                y + 2,
                x + width - 2,
                y + height - 2,
                0x10000000
        );

        graphics.fill(
                x + 3,
                y + 3,
                x + width - 3,
                y + height - 3,
                0x08000000
        );

    }

}