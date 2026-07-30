package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class IconRenderer {

    private IconRenderer() {
    }


    private static final ResourceLocation ICONS =
            ResourceLocation.fromNamespaceAndPath(
                    LegendaryTiers.MOD_ID,
                    "textures/gui/tooltip/icons.png"
            );


    public static void draw(
            GuiGraphics graphics,
            int x,
            int y,
            int textureX,
            int textureY
    ) {

        graphics.blit(
                ICONS,
                x,
                y,
                TooltipIcons.DRAW_SIZE,
                TooltipIcons.DRAW_SIZE,
                textureX,
                textureY,
                TooltipIcons.ICON_SIZE,
                TooltipIcons.ICON_SIZE,
                512,
                512
        );

    }

    public static void draw(
            GuiGraphics graphics,
            int x,
            int y,
            int textureX,
            int textureY,
            int size
    ) {

        graphics.blit(
                ICONS,
                x,
                y,
                size,
                size,
                textureX,
                textureY,
                TooltipIcons.ICON_SIZE,
                TooltipIcons.ICON_SIZE,
                512,
                512
        );

    }

}