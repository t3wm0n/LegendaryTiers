package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class RaritySection {

    private RaritySection() {
    }


    public static int getHeight() {
        return TooltipLayout.RARITY_HEIGHT;
    }


    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {

        if (context == null)
            return;

        Rarity rarity = context.rarity();

        if (rarity == null)
            return;

        graphics.fill(
                x + TooltipLayout.PADDING,
                y,
                x + width - TooltipLayout.PADDING,
                y + TooltipLayout.RARITY_HEIGHT,
                TooltipColors.rarityBackground(rarity)
        );

        graphics.renderOutline(
                x + TooltipLayout.PADDING,
                y,
                width - TooltipLayout.PADDING * 2,
                TooltipLayout.RARITY_HEIGHT,
                TooltipColors.rarityBorder(rarity)
        );

        TextRenderer.drawCenteredShadow(
                graphics,
                font,
                rarity.getDisplayName().getString(),
                x + width / 2,
                y + 6,
                TooltipColors.rarityPrimary(rarity)
        );

    }

}