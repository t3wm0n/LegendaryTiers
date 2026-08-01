package com.example.legendarytiers.client.tooltip2.render;

import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip2.layout.TooltipLayout;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;

public final class BackgroundRenderer {

    private BackgroundRenderer() {}

    public static void render(

            GuiGraphics graphics,

            TooltipLayout layout,

            TooltipTheme theme

    ) {

        renderGlow(
                graphics,
                layout,
                theme
        );

        renderBackground(
                graphics,
                layout
        );

        renderInnerShadow(
                graphics,
                layout
        );

    }

    // ============================================================
    // BACKGROUND
    // ============================================================

    private static void renderBackground(

            GuiGraphics graphics,

            TooltipLayout layout

    ) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        graphics.blit(

                TooltipTextures.BACKGROUND,

                layout.x,
                layout.y,

                0,
                0,

                layout.width,
                layout.height,

                256,
                256

        );

        RenderSystem.disableBlend();

    }

    // ============================================================
    // OUTER GLOW
    // ============================================================

    private static void renderGlow(

            GuiGraphics graphics,

            TooltipLayout layout,

            TooltipTheme theme

    ) {

        int glow = theme.borderColor() & 0x00FFFFFF;

        graphics.fill(

                layout.x - 6,
                layout.y - 6,

                layout.x + layout.width + 6,
                layout.y + layout.height + 6,

                0x22000000 | glow

        );

        graphics.fill(

                layout.x - 3,
                layout.y - 3,

                layout.x + layout.width + 3,
                layout.y + layout.height + 3,

                0x14000000 | glow

        );

    }

    // ============================================================
    // INNER SHADOW
    // ============================================================

    private static void renderInnerShadow(

            GuiGraphics graphics,

            TooltipLayout layout

    ) {

        graphics.fill(

                layout.x + 1,
                layout.y + 1,

                layout.x + layout.width - 1,
                layout.y + layout.height - 1,

                0x06000000

        );

        graphics.fill(

                layout.x + 2,
                layout.y + 2,

                layout.x + layout.width - 2,
                layout.y + layout.height - 2,

                0x10000000

        );

    }

}