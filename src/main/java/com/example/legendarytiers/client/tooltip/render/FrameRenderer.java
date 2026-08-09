package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;

public final class FrameRenderer {

    private static final int CORNER_W = 48;
    private static final int CORNER_H = 48;

    private static final int TEX_HD_W = 395;
    private static final int TEX_HD_H = 52;

    private static final int TEX_V_W = 121;
    private static final int TEX_V_H = 392;

    private static final int EDGE_H_THICKNESS = 12;
    private static final int EDGE_V_THICKNESS = 24;

    private static final int EDGE_H_STEP = 64;
    private static final int EDGE_V_STEP = 64;

    // --- ИСПРАВЛЕННЫЕ НАСТРОЙКИ СМЕЩЕНИЙ ---

    // На сколько пикселей раздвинуть боковые колонны наружу (влево и вправо)
    private static final int EXPAND_SIDES = 4;

    // На сколько пикселей опустить нижнюю грань вниз к углам
    private static final int SHIFT_BOTTOM_DOWN = 18;

    // Позиция старта боковых граней по высоте (чтобы они были ровно под хедером)
    private static final int SIDE_START_Y_OFFSET = 38;

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

        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(r, g, b, 1.0F);

        try {
            int innerLeft = x + CORNER_W - 12;
            int innerRight = x + width - CORNER_W + 12;

            int innerTop = y + SIDE_START_Y_OFFSET;
            int innerBottom = y + height - CORNER_H + 10;

            // --- 1. ГОРИЗОНТАЛЬНЫЕ ГРАНИ ---
            if (innerRight > innerLeft) {
                float hUvScale = (float) TEX_HD_W / EDGE_H_STEP;

                for (int xx = innerLeft; xx < innerRight; xx += EDGE_H_STEP) {
                    int drawWidth = Math.min(EDGE_H_STEP, innerRight - xx);
                    int uvWidth = (int) (drawWidth * hUvScale);

                    // Верхняя грань
                    graphics.blit(
                            TooltipTextures.FRAME_EDGE_HU,
                            xx, y,
                            drawWidth, EDGE_H_THICKNESS,
                            0, 0,
                            uvWidth, 32,
                            128, 32
                    );

                    // Нижняя грань (сдвигаем ВНИЗ плюсом)
                    graphics.blit(
                            TooltipTextures.FRAME_EDGE_HD,
                            xx, y + height - EDGE_H_THICKNESS,
                            drawWidth, EDGE_H_THICKNESS,
                            0, 0,
                            uvWidth, TEX_HD_H,
                            TEX_HD_W, TEX_HD_H
                    );
                }
            }

            // --- 2. ВЕРТИКАЛЬНЫЕ ГРАНИ ---
            if (innerBottom > innerTop) {
                float vUvScale = (float) TEX_V_H / EDGE_V_STEP;

                for (int yy = innerTop; yy < innerBottom; yy += EDGE_V_STEP) {
                    int drawHeight = Math.min(EDGE_V_STEP, innerBottom - yy);
                    int uvHeight = (int) (drawHeight * vUvScale);

                    // Левая грань (Выдвигаем влево, отнимая EXPAND_SIDES)
                    graphics.blit(
                            TooltipTextures.FRAME_EDGE_VL,
                            x - EXPAND_SIDES, yy,
                            EDGE_V_THICKNESS, drawHeight,
                            0, 0,
                            TEX_V_W, uvHeight,
                            TEX_V_W, TEX_V_H
                    );

                    // Правая грань (Выдвигаем вправо, прибавляя EXPAND_SIDES)
                    graphics.blit(
                            TooltipTextures.FRAME_EDGE_VR,
                            x + width - EDGE_V_THICKNESS + EXPAND_SIDES, yy,
                            EDGE_V_THICKNESS, drawHeight,
                            0, 0,
                            TEX_V_W, uvHeight,
                            TEX_V_W, TEX_V_H
                    );
                }
            }
            // --- 3. УГЛЫ (48x48) ---
            //graphics.blit(TooltipTextures.FRAME_CORNER_LU, x, y, CORNER_W, CORNER_H, 0, 0, 64, 64, 64, 64);
            //graphics.blit(TooltipTextures.FRAME_CORNER_RU, x + width - CORNER_W, y, CORNER_W, CORNER_H, 0, 0, 64, 64, 64, 64);
            graphics.blit(TooltipTextures.FRAME_CORNER_LD, x - 6, y + height - CORNER_H + 9, CORNER_W, CORNER_H, 0, 0, 256, 256, 256, 256);
            graphics.blit(TooltipTextures.FRAME_CORNER_RD, x + width - CORNER_W + 6, y + height - CORNER_H + 9, CORNER_W, CORNER_H, 0, 0, 256, 256, 256, 256);

        } finally {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
    }
}