package com.example.legendarytiers.client.tooltip2.render;

import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip2.layout.TooltipLayout;
import com.example.legendarytiers.TierData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class BarsRenderer {

    private static final int BAR_WIDTH = 180;
    private static final int BAR_HEIGHT = 8;

    private BarsRenderer() {}

    public static void render(

            GuiGraphics graphics,

            TooltipLayout layout,

            TooltipTheme theme,

            TierData data

    ) {

        renderDurability(
                graphics,
                layout,
                theme,
                data
        );

        renderExperience(
                graphics,
                layout,
                theme,
                data
        );

    }

    //========================================================

    private static void renderDurability(

            GuiGraphics graphics,

            TooltipLayout layout,

            TooltipTheme theme,

            TierData data

    ) {

        Font font = Minecraft.getInstance().font;

        int x = layout.contentLeft;
        int y = layout.durabilityY;

        graphics.drawString(

                font,

                "Durability",

                x,

                y,

                0xFFFFFFFF,

                false

        );

        drawBar(

                graphics,

                x,

                y + 12,

                BAR_WIDTH,

                BAR_HEIGHT,

                data.durability(),

                data.maxDurability(),

                theme.borderColor()

        );

        String value = data.durability() + " / " + data.maxDurability();

        graphics.drawString(

                font,

                value,

                x + BAR_WIDTH - font.width(value),

                y,

                0xFFB0B0B0,

                false

        );

    }

    //========================================================

    private static void renderExperience(

            GuiGraphics graphics,

            TooltipLayout layout,

            TooltipTheme theme,

            TierData data

    ) {

        Font font = Minecraft.getInstance().font;

        int x = layout.contentLeft;
        int y = layout.xpY;

        graphics.drawString(

                font,

                "Experience",

                x,

                y,

                0xFFFFFFFF,

                false

        );

        drawBar(

                graphics,

                x,

                y + 12,

                BAR_WIDTH,

                BAR_HEIGHT,

                data.experience(),

                data.nextLevelExperience(),

                theme.borderColor()

        );

        String value =
                data.experience() +
                        " / " +
                        data.nextLevelExperience();

        graphics.drawString(

                font,

                value,

                x + BAR_WIDTH - font.width(value),

                y,

                0xFFB0B0B0,

                false

        );

    }

    //========================================================

    private static void drawBar(

            GuiGraphics graphics,

            int x,

            int y,

            int width,

            int height,

            int value,

            int max,

            int color

    ) {

        float progress =
                max == 0
                        ? 0
                        : (float)value / (float)max;

        progress = Math.max(0F, Math.min(progress, 1F));

        graphics.fill(

                x,
                y,

                x + width,
                y + height,

                0x50000000

        );

        graphics.fill(

                x,

                y,

                x + (int)(width * progress),

                y + height,

                color | 0xFF000000

        );

        graphics.fill(

                x,

                y,

                x + width,

                y + 1,

                0x40FFFFFF

        );

    }

}