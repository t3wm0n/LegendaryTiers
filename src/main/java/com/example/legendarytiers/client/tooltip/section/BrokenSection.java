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

public final class BrokenSection {

    private BrokenSection() {
    }


    public static int getHeight() {
        return TooltipLayout.BROKEN_HEIGHT;
    }


    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {

        if (context == null || !context.broken())
            return;

        graphics.fill(
                x + TooltipLayout.PADDING,
                y,
                x + width - TooltipLayout.PADDING,
                y + TooltipLayout.BROKEN_HEIGHT,
                0x33111111
        );

        graphics.renderOutline(
                x + TooltipLayout.PADDING,
                y,
                width - TooltipLayout.PADDING * 2,
                TooltipLayout.BROKEN_HEIGHT,
                0x55FF5555
        );

        int currentY = y;


        // Иконка сломанного предмета

        IconRenderer.draw(
                graphics,
                x + TooltipLayout.PADDING,
                currentY + 4,
                TooltipIcons.BROKEN_X,
                TooltipIcons.BROKEN_Y
        );


        TextRenderer.drawShadow(
                graphics,
                font,
                Component.translatable(
                        "tooltip.legendarytiers.broken"
                ).getString(),
                x + TooltipLayout.PADDING + 22,
                currentY + 6,
                TooltipColors.TEXT_NEGATIVE
        );


        currentY += 22;


        // Оригинальный предмет

        String original =
                context.originalItemName();


        if (original != null && !original.isEmpty()) {

            TextRenderer.draw(
                    graphics,
                    font,
                    Component.translatable(
                            "tooltip.legendarytiers.was"
                    ).getString()
                            + original,
                    x + TooltipLayout.PADDING,
                    currentY,
                    TooltipColors.TEXT_SECONDARY
            );

            currentY += 18;
        }


        // Стоимость ремонта
        IconRenderer.draw(
                graphics,
                x + TooltipLayout.PADDING,
                currentY - 2,
                TooltipIcons.REPAIR_X,
                TooltipIcons.REPAIR_Y
        );

        TextRenderer.draw(
                graphics,
                font,
                Component.translatable(
                        "tooltip.legendarytiers.repair",
                        context.repairCost()
                ).getString(),
                x + TooltipLayout.PADDING + 22,
                currentY,
                TooltipColors.TEXT_NORMAL
        );


        currentY += 18;


        // Подсказка

        var lines = font.split(
                Component.translatable("tooltip.legendarytiers.repair_hint"),
                width - TooltipLayout.PADDING * 2
        );

        for (var line : lines) {

            graphics.drawString(
                    font,
                    line,
                    x + TooltipLayout.PADDING,
                    currentY,
                    TooltipColors.TEXT_TITLE,
                    false
            );

            currentY += 10;
        }

    }

}