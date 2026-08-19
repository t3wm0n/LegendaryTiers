package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public final class ExtraInfoSection {

    private ExtraInfoSection() {}

    public static int getHeight(Font font, List<Component> extraLines, int maxWidth) {
        if (extraLines.isEmpty()) return 0;

        int totalHeight = 20; // Отступ + Заголовок секции
        int availableWidth = maxWidth - TooltipLayout.PADDING * 2;

        for (Component line : extraLines) {
            List<FormattedCharSequence> split = font.split(line, availableWidth);
            totalHeight += split.size() * (font.lineHeight + 1);
        }

        return totalHeight + 10;
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            List<Component> extraLines,
            int x,
            int y,
            int width
    ) {
        if (extraLines.isEmpty()) return;

        int currentY = y + 5;
        int barX = x + TooltipLayout.PADDING;
        int availableWidth = width - TooltipLayout.PADDING * 2;

        // Заголовок второй страницы
        TextRenderer.drawShadow(
                graphics,
                font,
                Component.translatable("tooltip.legendarytiers.extrainfo.header").getString(),
                barX,
                currentY,
                0xFFFFAA00 // Оранжевый акцент
        );

        currentY += font.lineHeight + 4;

        // Вывод всех сторонних строк
        for (Component line : extraLines) {
            List<FormattedCharSequence> splitLines = font.split(line, availableWidth);

            for (FormattedCharSequence splitLine : splitLines) {
                graphics.drawString(
                        font,
                        splitLine,
                        barX,
                        currentY,
                        0xFFE0E0E0,
                        true
                );
                currentY += font.lineHeight + 1;
            }
        }
    }
}
