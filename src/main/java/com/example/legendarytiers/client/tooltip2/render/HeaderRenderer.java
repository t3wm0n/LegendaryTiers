package com.example.legendarytiers.client.tooltip2.render;

import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip2.layout.TooltipLayout;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class HeaderRenderer {

    private HeaderRenderer() {}

    public static void render(

            GuiGraphics graphics,

            TooltipLayout layout,

            TooltipTheme theme,

            String itemName,

            Rarity rarity,

            int stars

    ) {

        Font font = Minecraft.getInstance().font;

        int color = theme.borderColor();

        float r = ((color >> 16) & 255) / 255F;
        float g = ((color >> 8) & 255) / 255F;
        float b = (color & 255) / 255F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderColor(r, g, b, 1F);

        drawHeaderBackground(graphics, layout);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        drawStars(graphics, font, layout, stars);

        drawTitle(graphics, font, layout, itemName);

        drawRarity(graphics, font, layout, theme, rarity);

        RenderSystem.disableBlend();

    }

    private static void drawHeaderBackground(

            GuiGraphics graphics,

            TooltipLayout layout

    ) {

        graphics.blit(

                TooltipTextures.HEADER_LEFT,

                layout.x,

                layout.y,

                0,

                0,

                32,

                24,

                32,

                24

        );

        int xx = layout.x + 32;

        while (xx < layout.x + layout.width - 32) {

            graphics.blit(

                    TooltipTextures.HEADER_CENTER,

                    xx,

                    layout.y,

                    0,

                    0,

                    16,

                    24,

                    16,

                    24

            );

            xx += 16;

        }

        graphics.blit(

                TooltipTextures.HEADER_RIGHT,

                layout.x + layout.width - 32,

                layout.y,

                0,

                0,

                32,

                24,

                32,

                24

        );

    }

    private static void drawStars(

            GuiGraphics graphics,

            Font font,

            TooltipLayout layout,

            int stars

    ) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < stars; i++) {
            builder.append("★");
        }

        String text = builder.toString();

        int w = font.width(text);

        graphics.drawString(

                font,

                text,

                layout.centerX - w / 2,

                layout.starsY,

                0xFFF6C74A,

                false

        );

    }

    private static void drawTitle(

            GuiGraphics graphics,

            Font font,

            TooltipLayout layout,

            String title

    ) {

        graphics.drawString(

                font,

                title,

                layout.contentLeft,

                layout.titleY,

                0xFFFFFFFF,

                false

        );

    }

    private static void drawRarity(

            GuiGraphics graphics,

            Font font,

            TooltipLayout layout,

            TooltipTheme theme,

            Rarity rarity

    ) {

        String text = rarity.getDisplayName().getString();

        int w = font.width(text);

        graphics.drawString(

                font,

                text,

                layout.width + layout.x - w - 16,

                layout.titleY,

                theme.borderColor(),

                false

        );

    }

}