package com.example.legendarytiers.client.tooltip2.render;

import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip2.layout.TooltipLayout;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;

public final class FrameRenderer {

    private static final int CORNER = 16;
    private static final int EDGE = 16;

    private FrameRenderer() {}

    public static void render(

            GuiGraphics graphics,

            TooltipLayout layout,

            TooltipTheme theme

    ) {

        int color = theme.borderColor();

        float r = ((color >> 16) & 255) / 255F;
        float g = ((color >> 8) & 255) / 255F;
        float b = (color & 255) / 255F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderColor(r, g, b, 1F);

        drawHorizontal(graphics, layout);

        drawVertical(graphics, layout);

        drawCorners(graphics, layout);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        RenderSystem.disableBlend();

    }

    // =====================================================

    private static void drawHorizontal(

            GuiGraphics graphics,

            TooltipLayout layout

    ) {

        int start = layout.x + CORNER;
        int end = layout.x + layout.width - CORNER;

        for (int xx = start; xx < end; xx += EDGE) {

            graphics.blit(

                    TooltipTextures.FRAME_EDGE_H,

                    xx,
                    layout.y,

                    0,
                    0,

                    EDGE,
                    4,

                    EDGE,
                    4

            );

            graphics.blit(

                    TooltipTextures.FRAME_EDGE_H,

                    xx,
                    layout.y + layout.height - 4,

                    0,
                    0,

                    EDGE,
                    4,

                    EDGE,
                    4

            );

        }

    }

    // =====================================================

    private static void drawVertical(

            GuiGraphics graphics,

            TooltipLayout layout

    ) {

        int start = layout.y + CORNER;
        int end = layout.y + layout.height - CORNER;

        for (int yy = start; yy < end; yy += EDGE) {

            graphics.blit(

                    TooltipTextures.FRAME_EDGE_V,

                    layout.x,
                    yy,

                    0,
                    0,

                    4,
                    EDGE,

                    4,
                    EDGE

            );

            graphics.blit(

                    TooltipTextures.FRAME_EDGE_V,

                    layout.x + layout.width - 4,
                    yy,

                    0,
                    0,

                    4,
                    EDGE,

                    4,
                    EDGE

            );

        }

    }

    // =====================================================

    private static void drawCorners(

            GuiGraphics graphics,

            TooltipLayout layout

    ) {

        graphics.blit(

                TooltipTextures.FRAME_CORNER,

                layout.x,
                layout.y,

                0,
                0,

                CORNER,
                CORNER,

                CORNER,
                CORNER

        );

        graphics.blit(

                TooltipTextures.FRAME_CORNER,

                layout.x + layout.width - CORNER,
                layout.y,

                0,
                0,

                CORNER,
                CORNER,

                CORNER,
                CORNER

        );

        graphics.blit(

                TooltipTextures.FRAME_CORNER,

                layout.x,
                layout.y + layout.height - CORNER,

                0,
                0,

                CORNER,
                CORNER,

                CORNER,
                CORNER

        );

        graphics.blit(

                TooltipTextures.FRAME_CORNER,

                layout.x + layout.width - CORNER,
                layout.y + layout.height - CORNER,

                0,
                0,

                CORNER,
                CORNER,

                CORNER,
                CORNER

        );

    }

}