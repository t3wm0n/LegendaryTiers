package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class ReforgeSection {

    private ReforgeSection() {
    }


    public static int getHeight() {
        return TooltipLayout.REFORGE_HEIGHT;
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


        int attempts = context.reforgeAttempts();


        int color;

        if (attempts >= 3) {
            color = TooltipColors.TEXT_NEGATIVE;
        } else {
            color = TooltipColors.TEXT_POSITIVE;
        }


        IconRenderer.draw(
                graphics,
                x + TooltipLayout.PADDING,
                y + 4,
                TooltipIcons.REFORGE_X,
                TooltipIcons.REFORGE_Y
        );


        TextRenderer.draw(
                graphics,
                font,
                Component.translatable(
                        "tooltip.legendarytiers.reforge_attempts",
                        attempts
                ).getString(),
                x + TooltipLayout.PADDING + TooltipIcons.DRAW_SIZE + 6,
                y + 7,
                color
        );

    }

}