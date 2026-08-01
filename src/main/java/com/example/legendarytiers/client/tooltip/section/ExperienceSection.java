package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.*;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.example.legendarytiers.client.tooltip.render.ProgressBarRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class ExperienceSection {

    private ExperienceSection() {
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

        IconRenderer.drawLevelIcon(
                graphics,
                context.level(),
                x + TooltipLayout.PADDING,
                y,
                32
        );

        int barX =
                x
                        + TooltipLayout.PADDING
                        + 31;
        int barY = y + 8;

        int barWidth =
                width
                        - 34
                        - TooltipLayout.PADDING * 2;

        float progress =
                context.experienceToNextLevel() <= 0
                        ? 1f
                        : (float) context.experience()
                          / context.experienceToNextLevel();

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
                context.experience()
                        + " / "
                        + context.experienceToNextLevel()
                        + " XP";

        int textWidth = font.width(text);

        TextRenderer.drawShadow(
                graphics,
                font,
                text,
                barX + (barWidth - textWidth) / 2,
                barY + 3,
                0xFFFFFFFF
        );

    }

}