package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.client.tooltip.TooltipHeaderTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class DecorationRenderer {

    private DecorationRenderer() {}

    public static void render(

            GuiGraphics graphics,

            int x,

            int y,

            int width,

            TooltipTheme theme,

            Rarity rarity

    ) {

        ResourceLocation texture =
                TooltipHeaderTextures.get(rarity);

        graphics.blit(

                texture,

                x,

                y,

                width,

                HeaderRenderer.HEIGHT,

                0,

                0,

                256,

                32,

                256,

                32

        );

    }

}