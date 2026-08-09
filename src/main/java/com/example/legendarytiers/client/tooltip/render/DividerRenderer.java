package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;

public final class DividerRenderer {

    // Размеры текстурного файла
    private static final int TEX_WIDTH = 512;
    private static final int TEX_HEIGHT = 32;

    // 1. Кончики (края): 22x24 на текстуре
    private static final int CAP_WIDTH = 22;
    private static final int CAP_HEIGHT = 24;

    // 2. Центральный элемент: 30x32 на текстуре по X=241
    private static final int CENTER_X = 241;
    private static final int CENTER_WIDTH = 30;
    private static final int CENTER_HEIGHT = 32;

    // Требуемая высота разделителя на экране
    private static final int DISPLAY_HEIGHT = 12;

    private DividerRenderer() {}

    public static void drawDivider(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            TooltipTheme theme
    ) {
        int padding = 6;
        int dividerWidth = width - (padding * 2);

        // --- ПРОПОРЦИОНАЛЬНЫЙ РАСЧЕТ ЭКРАННОЙ ШИРИНЫ (ASPECT RATIO) ---
        // Рассчитываем ширину краев и центра так, чтобы они сохраняли родные пропорции при высоте DISPLAY_HEIGHT (12px)
        int renderCapWidth = Math.round(CAP_WIDTH * ((float) DISPLAY_HEIGHT / CAP_HEIGHT));     // 22 * (12/24) = 11px
        int renderCenterWidth = Math.round(CENTER_WIDTH * ((float) DISPLAY_HEIGHT / CENTER_HEIGHT)); // 30 * (12/32) = 11px

        int minWidth = (renderCapWidth * 2) + renderCenterWidth;
        if (dividerWidth < minWidth) {
            return;
        }

        int dividerX = x + padding;
        int dividerY = y + (TooltipLayout.DIVIDER_HEIGHT - DISPLAY_HEIGHT) / 2;

        // Доступная ширина для двух растягивающихся полос
        int totalStretchWidth = dividerWidth - minWidth;
        int leftStretchWidth = totalStretchWidth / 2;
        int rightStretchWidth = totalStretchWidth - leftStretchWidth;

        // Ширина полосы в файле (от 22px до 241px = 219px)
        int stretchTexWidth = CENTER_X - CAP_WIDTH;

        // Цвет темы
        int color = theme.borderColor();
        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        float a = ((color >> 24) & 0xFF) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(r, g, b, a > 0 ? a : 1.0F);

        int currentX = dividerX;

        // --- 1. ЛЕВЫЙ КОНЧИК (11x12 на экране) ---
        graphics.blit(
                TooltipTextures.DIVIDER,
                currentX, dividerY,
                renderCapWidth, DISPLAY_HEIGHT,
                0, 0,
                CAP_WIDTH, CAP_HEIGHT,
                TEX_WIDTH, TEX_HEIGHT
        );
        currentX += renderCapWidth;

        // --- 2. ЛЕВАЯ РАСТЯГИВАЮЩАЯСЯ ПОЛОСА ---
        if (leftStretchWidth > 0) {
            graphics.blit(
                    TooltipTextures.DIVIDER,
                    currentX, dividerY,
                    leftStretchWidth, DISPLAY_HEIGHT,
                    CAP_WIDTH, 0,
                    stretchTexWidth, CAP_HEIGHT,
                    TEX_WIDTH, TEX_HEIGHT
            );
            currentX += leftStretchWidth;
        }

        // --- 3. ЦЕНТРАЛЬНЫЙ ДЕКОР (11x12 на экране) ---
        graphics.blit(
                TooltipTextures.DIVIDER,
                currentX, dividerY + 2,
                renderCenterWidth, DISPLAY_HEIGHT,
                CENTER_X, 0,
                CENTER_WIDTH, CENTER_HEIGHT,
                TEX_WIDTH, TEX_HEIGHT
        );
        currentX += renderCenterWidth;

        // --- 4. ПРАВАЯ РАСТЯГИВАЮЩАЯСЯ ПОЛОСА ---
        if (rightStretchWidth > 0) {
            int rightStretchU = CENTER_X + CENTER_WIDTH; // 271
            graphics.blit(
                    TooltipTextures.DIVIDER,
                    currentX, dividerY,
                    rightStretchWidth, DISPLAY_HEIGHT,
                    rightStretchU, 0,
                    stretchTexWidth, CAP_HEIGHT,
                    TEX_WIDTH, TEX_HEIGHT
            );
            currentX += rightStretchWidth;
        }

        // --- 5. ПРАВЫЙ КОНЧИК (11x12 на экране) ---
        graphics.blit(
                TooltipTextures.DIVIDER,
                currentX, dividerY,
                renderCapWidth, DISPLAY_HEIGHT,
                TEX_WIDTH - CAP_WIDTH, 0,
                CAP_WIDTH, CAP_HEIGHT,
                TEX_WIDTH, TEX_HEIGHT
        );

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}