package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.*;
import com.example.legendarytiers.client.tooltip.render.ProgressBarRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class DurabilitySection {

    private DurabilitySection() {
    }

    public static int getHeight() {
        return 30;
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {

        int barX =
                x + TooltipLayout.PADDING;

        int barY =
                y + 5;

        int barWidth =
                width
                        - TooltipLayout.PADDING * 2;

        float progress =
                context.maxDurability() <= 0
                        ? 1f
                        : (float) context.durability()
                          / context.maxDurability();

        TooltipTheme theme =
                TooltipThemes.get(context.rarity());

        ProgressBarRenderer.draw(
                graphics,
                barX,
                barY,
                barWidth,
                14,
                progress,
                theme
        );

        String text =
                context.durability()
                        + " / "
                        + context.maxDurability();

        if (Math.abs(context.durabilityBonus()) > 0.0001) {

            int percent =
                    (int) Math.round(
                            context.durabilityBonus()
                                    * 100
                    );

            text += " (+" + percent + "%)";
        }

        String textEnd = Component.translatable("attribute.name.generic.durability").getString() + " " + text;

        int textWidth =
                font.width(textEnd);

        TextRenderer.drawShadow(
                graphics,
                font,
                textEnd,
                barX
                        + (barWidth - textWidth) / 2,
                barY + 3,
                0xFFFFFFFF
        );

    }

}