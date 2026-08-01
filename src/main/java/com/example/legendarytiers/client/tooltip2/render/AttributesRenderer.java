package com.example.legendarytiers.client.tooltip2.render;

import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip2.layout.TooltipLayout;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public final class AttributesRenderer {

    private static final int ICON_SIZE = 12;

    private AttributesRenderer() {}

    public static void render(

            GuiGraphics graphics,

            Font font,

            TooltipLayout layout,

            TooltipTheme theme,

            LegendaryTooltipContext context

    ) {

        List<ModifierEntry> modifiers =
                context.modifiers();

        if (modifiers.isEmpty())
            return;

        int y = layout.attributesY;

        for (ModifierEntry modifier : modifiers) {

            drawModifier(

                    graphics,

                    font,

                    layout,

                    modifier,

                    y,

                    theme

            );

            y += layout.attributeSpacing;

        }

    }

    //======================================================

    private static void drawModifier(

            GuiGraphics graphics,

            Font font,

            TooltipLayout layout,

            ModifierEntry modifier,

            int y,

            TooltipTheme theme

    ) {

        int left = layout.contentLeft;

        int right = layout.contentRight;

        // -------------------------
        // Пока вместо иконки кружок
        // Потом заменим PNG
        // -------------------------

        graphics.fill(

                left,

                y + 2,

                left + ICON_SIZE,

                y + ICON_SIZE,

                theme.borderColor()

        );

        // -------------------------
        // Название
        // -------------------------

        graphics.drawString(

                font,

                modifier.attribute().toString(),

                left + 18,

                y,

                0xFFFFFFFF,

                false

        );

        // -------------------------
        // Итоговое значение
        // -------------------------

        String value =
                String.valueOf(modifier.value());

        int valueWidth =
                font.width(value);

        graphics.drawString(

                font,

                value,

                right - valueWidth,

                y,

                theme.borderColor(),

                false

        );

    }

}