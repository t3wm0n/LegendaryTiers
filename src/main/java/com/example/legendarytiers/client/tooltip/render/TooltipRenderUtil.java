package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class TooltipRenderUtil {

    private TooltipRenderUtil() {
    }

    /*
     * ============================================
     * TEXTURES
     * ============================================
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

    /*
     * ============================================
     * TEXT
     * ============================================
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

    /*
     * ============================================
     * DIVIDER
     * ============================================
     */

    public static void drawDivider(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            TooltipTheme theme
    ) {

        int dividerWidth =
                width - TooltipLayout.PADDING * 2;

        int dividerX =
                x + TooltipLayout.PADDING;

        int dividerY =
                y + TooltipLayout.DIVIDER_HEIGHT / 2;

        int color =
                (theme.backgroundDark() & 0x00FFFFFF)
                        | 0x44000000;

        graphics.fill(
                dividerX,
                dividerY,
                dividerX + dividerWidth,
                dividerY + 1,
                color
        );

    }

    /*
     * ============================================
     * BACKGROUND
     * ============================================
     */

    public static void drawBackground(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {

        BackgroundRenderer.render(
                graphics,
                x,
                y,
                width,
                height,
                theme
        );

        graphics.renderOutline(
                x,
                y,
                width,
                height,
                theme.borderColor()
        );

    }

    public static void drawFrame(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {

        /*
         * Внешняя линия
         */

        graphics.fill(
                x,
                y,
                x + width,
                y + 1,
                theme.borderDark()
        );

        graphics.fill(
                x,
                y + height - 1,
                x + width,
                y + height,
                theme.borderDark()
        );

        graphics.fill(
                x,
                y,
                x + 1,
                y + height,
                theme.borderDark()
        );

        graphics.fill(
                x + width - 1,
                y,
                x + width,
                y + height,
                theme.borderDark()
        );

        /*
         * Центральная линия
         */

        graphics.fill(
                x + 1,
                y + 1,
                x + width - 1,
                y + 3,
                theme.border()
        );

        graphics.fill(
                x + 1,
                y + height - 3,
                x + width - 1,
                y + height - 1,
                theme.border()
        );

        graphics.fill(
                x + 1,
                y + 1,
                x + 3,
                y + height - 1,
                theme.border()
        );

        graphics.fill(
                x + width - 3,
                y + 1,
                x + width - 1,
                y + height - 1,
                theme.border()
        );

        /*
         * Внутренняя линия
         */

        graphics.fill(
                x + 3,
                y + 3,
                x + width - 3,
                y + 4,
                theme.borderLight()
        );

        graphics.fill(
                x + 3,
                y + height - 4,
                x + width - 3,
                y + height - 3,
                theme.borderLight()
        );

        graphics.fill(
                x + 3,
                y + 3,
                x + 4,
                y + height - 3,
                theme.borderLight()
        );

        graphics.fill(
                x + width - 4,
                y + 3,
                x + width - 3,
                y + height - 3,
                theme.borderLight()
        );

    }

}