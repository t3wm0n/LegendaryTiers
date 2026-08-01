package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class FrameRenderer {

    private FrameRenderer() {}

    public static void render(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {

        int color = theme.borderColor();

        float r = ((color >> 16) & 255) / 255F;
        float g = ((color >> 8) & 255) / 255F;
        float b = (color & 255) / 255F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(r, g, b, 1F);

        // Верх
        for (int xx = x + 16; xx < x + width - 16; xx += 16) {

            graphics.blit(
                    TooltipTextures.FRAME_EDGE_H,
                    xx,
                    y,
                    0,
                    0,
                    16,
                    4,
                    16,
                    4
            );

            graphics.blit(
                    TooltipTextures.FRAME_EDGE_H,
                    xx,
                    y + height - 4,
                    0,
                    0,
                    16,
                    4,
                    16,
                    4
            );

        }

        // Левые/правые стороны
        for (int yy = y + 16; yy < y + height - 16; yy += 16) {

            graphics.blit(
                    TooltipTextures.FRAME_EDGE_V,
                    x,
                    yy,
                    0,
                    0,
                    4,
                    16,
                    4,
                    16
            );

            graphics.blit(
                    TooltipTextures.FRAME_EDGE_V,
                    x + width - 4,
                    yy,
                    0,
                    0,
                    4,
                    16,
                    4,
                    16
            );

        }

        // Углы
        graphics.blit(TooltipTextures.FRAME_CORNER, x, y, 0, 0, 16, 16, 16, 16);
        graphics.blit(TooltipTextures.FRAME_CORNER, x + width - 16, y, 0, 0, 16, 16, 16, 16);
        graphics.blit(TooltipTextures.FRAME_CORNER, x, y + height - 16, 0, 0, 16, 16, 16, 16);
        graphics.blit(TooltipTextures.FRAME_CORNER, x + width - 16, y + height - 16, 0, 0, 16, 16, 16, 16);

        renderShine(
                graphics,
                x,
                y,
                width,
                theme
        );

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.disableBlend();
    }

    private static void renderShine(

            GuiGraphics graphics,

            int x,

            int y,

            int width,

            TooltipTheme theme

    ) {

        long ticks =
                Minecraft.getInstance().level == null
                        ? 0
                        : Minecraft.getInstance().level.getGameTime();

        float cycle = 120F;

        float t = (ticks % (int)cycle) / cycle;

        int shineX =
                (int)(x - 64 + (width + 128) * t);

        int color = theme.borderColor();

        float r = ((color >> 16) & 255) / 255F;
        float g = ((color >> 8) & 255) / 255F;
        float b = (color & 255) / 255F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderColor(r, g, b, 0.65F);

        graphics.blit(

                TooltipTextures.FRAME_SHINE,

                shineX,

                y - 2,

                0,

                0,

                64,

                8,

                64,

                8

        );

        RenderSystem.setShaderColor(1,1,1,1);

        RenderSystem.disableBlend();

    }
}