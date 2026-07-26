package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class TooltipRenderUtil {

    private TooltipRenderUtil() {
    }

    /**
     * Рисование части текстуры.
     */
    public static void drawTexture(
            GuiGraphics graphics,
            ResourceLocation texture,
            int x,
            int y,
            int u,
            int v,
            int width,
            int height,
            int textureWidth,
            int textureHeight
    ) {

        graphics.blit(
                texture,
                x,
                y,
                u,
                v,
                width,
                height,
                textureWidth,
                textureHeight
        );

    }

    /**
     * Рисование части текстуры с масштабированием.
     */
    public static void drawTextureScaled(
            GuiGraphics graphics,
            ResourceLocation texture,
            int x,
            int y,
            int width,
            int height,
            int u,
            int v,
            int regionWidth,
            int regionHeight,
            int textureWidth,
            int textureHeight
    ) {

        graphics.blit(
                texture,
                x,
                y,
                width,
                height,
                u,
                v,
                regionWidth,
                regionHeight,
                textureWidth,
                textureHeight
        );

    }

    /**
     * Рисование строки.
     */
    public static void drawText(
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

    /**
     * Центрированный текст.
     */
    public static void drawCenteredText(
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

    /**
     * Текст с тенью.
     */
    public static void drawShadowText(
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

    public static void drawDivider(
            GuiGraphics graphics,
            int x,
            int y,
            int width
    ) {

        int dividerWidth = width - TooltipLayout.PADDING * 2;

        int dividerX = x + TooltipLayout.PADDING;

        int dividerY = y + (TooltipLayout.DIVIDER_HEIGHT / 2);

        graphics.fill(
                dividerX,
                dividerY,
                dividerX + dividerWidth,
                dividerY + 1,
                TooltipColors.DIVIDER
        );

    }

    public static void drawBackground(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height
    ) {

        graphics.fill(
                x,
                y,
                x + width,
                y + height,
                0xCC101010
        );

    }
}