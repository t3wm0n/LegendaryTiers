package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class HeaderSection {

    private HeaderSection() {
    }


    public static int getHeight() {
        return TooltipLayout.HEADER_HEIGHT;
    }


    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y
    ) {

        if (context == null || context.stack() == null)
            return;


        String name =
                context.stack()
                        .getHoverName()
                        .getString();


        TextRenderer.drawShadow(
                graphics,
                font,
                name,
                x + TooltipLayout.PADDING,
                y + 14,
                TooltipColors.rarityPrimary(context.rarity())
        );

    }

}