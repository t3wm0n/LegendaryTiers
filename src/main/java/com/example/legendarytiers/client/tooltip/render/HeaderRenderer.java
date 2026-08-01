package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class HeaderRenderer {

    public static final int HEIGHT = 28;

    private HeaderRenderer() {
    }

    public static void render(

            GuiGraphics graphics,

            String title,

            String rarity,

            Rarity tier,

            int x,

            int y,

            int width,

            TooltipTheme theme

    ) {

        Font font = Minecraft.getInstance().font;

        int color = theme.borderColor();

        float r = ((color >> 16) & 255) / 255F;
        float g = ((color >> 8) & 255) / 255F;
        float b = (color & 255) / 255F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderColor(r, g, b, 1F);

        // ---------- левая часть ----------

        graphics.blit(

                TooltipTextures.HEADER_LEFT,

                x,

                y,

                0,

                0,

                32,

                24,

                32,

                24

        );

        // ---------- центр ----------

        for (int xx = x + 32; xx < x + width - 32; xx += 16) {

            graphics.blit(

                    TooltipTextures.HEADER_CENTER,

                    xx,

                    y,

                    0,

                    0,

                    16,

                    24,

                    16,

                    24

            );

        }

        // ---------- правая ----------

        graphics.blit(

                TooltipTextures.HEADER_RIGHT,

                x + width - 32,

                y,

                0,

                0,

                32,

                24,

                32,

                24

        );

        RenderSystem.setShaderColor(1,1,1,1);

        // ---------- название ----------

        graphics.drawString(

                font,

                title,

                x + 14,

                y + 8,

                0xFFFFFF,

                false

        );

        // ---------- редкость ----------

        int rw = font.width(rarity);

        graphics.drawString(

                font,

                rarity,

                x + width - rw - 14,

                y + 8,

                theme.borderColor(),

                false

        );

    }}