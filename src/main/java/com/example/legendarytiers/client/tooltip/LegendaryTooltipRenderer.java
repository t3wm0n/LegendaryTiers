package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.client.tooltip.render.TooltipRenderUtil;
import com.example.legendarytiers.client.tooltip.section.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class LegendaryTooltipRenderer {

    private LegendaryTooltipRenderer() {
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y
    ) {

        int attributeCount = context.modifiers().size();

        int itemStatCount = ItemStatsSection.count(context.stack());

        int height = TooltipLayout.calculateHeight(
                itemStatCount,
                attributeCount,
                context.broken(),
                true
        );

        TooltipRenderUtil.drawBackground(
                graphics,
                x,
                y,
                TooltipLayout.WIDTH,
                height
        );

        int currentY = y + TooltipLayout.PADDING;

        HeaderSection.render(
                graphics,
                font,
                context,
                x,
                currentY
        );

        currentY += HeaderSection.getHeight();

        RaritySection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                TooltipLayout.WIDTH
        );

        currentY += RaritySection.getHeight();

        QualitySection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                TooltipLayout.WIDTH
        );

        currentY += QualitySection.getHeight();

        ExperienceSection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                TooltipLayout.WIDTH
        );

        currentY += ExperienceSection.getHeight();

        if (itemStatCount > 0) {

            TooltipRenderUtil.drawDivider(
                    graphics,
                    x,
                    currentY,
                    TooltipLayout.WIDTH
            );

            currentY += TooltipLayout.DIVIDER_HEIGHT;

            ItemStatsSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY,
                    TooltipLayout.WIDTH
            );

            currentY += ItemStatsSection.getHeight(itemStatCount);
        }
        // =========================
        // Attribute modifiers
        // =========================

        if (attributeCount > 0) {

            TooltipRenderUtil.drawDivider(
                    graphics,
                    x,
                    currentY,
                    TooltipLayout.WIDTH
            );

            currentY += TooltipLayout.DIVIDER_HEIGHT;

            AttributeSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY,
                    TooltipLayout.WIDTH
            );

            currentY += AttributeSection.getHeight(attributeCount);
        }

        // =========================
        // Broken item
        // =========================

        if (context.broken()) {

            TooltipRenderUtil.drawDivider(
                    graphics,
                    x,
                    currentY,
                    TooltipLayout.WIDTH
            );

            currentY += TooltipLayout.DIVIDER_HEIGHT;

            BrokenSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY,
                    TooltipLayout.WIDTH
            );

            currentY += BrokenSection.getHeight();
        }

        // =========================
        // Reforge
        // =========================

        TooltipRenderUtil.drawDivider(
                graphics,
                x,
                currentY,
                TooltipLayout.WIDTH
        );

        currentY += TooltipLayout.DIVIDER_HEIGHT;

        ReforgeSection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                TooltipLayout.WIDTH
        );

    }

}