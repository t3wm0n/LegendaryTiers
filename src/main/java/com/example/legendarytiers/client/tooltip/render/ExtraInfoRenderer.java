package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip.section.ExtraInfoSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class ExtraInfoRenderer {

    private ExtraInfoRenderer() {
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            List<Component> extraLines,
            int x,
            int y,
            int width,
            TooltipTheme theme
    ) {
        int extraHeight = ExtraInfoSection.getHeight(font, extraLines, width);
        int heightP2 = TooltipLayout.calculatePage2Height(extraHeight);

        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int correctedY = Math.clamp(y, 5, Math.max(5, screenHeight - heightP2 - 5));

        // 1. Задний фон
        BackgroundRenderer.render(
                graphics,
                x - TooltipLayout.PADDING,
                correctedY - TooltipLayout.PADDING,
                width + (TooltipLayout.PADDING * 2),
                heightP2,
                theme
        );

        // 2. Шапка (верх тултипа)
        HeaderRenderer.render(
                graphics,
                context,
                context.rarity().name(),
                context.rarity(),
                context.quality(),
                x - TooltipLayout.PADDING,
                correctedY - TooltipLayout.PADDING,
                width,
                theme
        );

        int currentY = correctedY + HeaderRenderer.HEADER_HEIGHT - TooltipLayout.PADDING;

        // 3. Блок внешнего текста
        ExtraInfoSection.render(
                graphics,
                font,
                extraLines,
                x,
                currentY,
                width
        );

        // 4. Подсказка переключения внизу
        String tabHint = Component.translatable("tooltip.legendarytiers.extrainfo.tab2").getString();
        int hintWidth = font.width(tabHint);
        graphics.drawString(
                font,
                tabHint,
                x + (width - hintWidth) / 2,
                correctedY + heightP2 - TooltipLayout.PADDING - 20,
                0xFFFFAA00,
                true
        );
    }
}