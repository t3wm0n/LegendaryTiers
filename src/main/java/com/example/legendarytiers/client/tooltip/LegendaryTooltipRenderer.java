package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.client.tooltip.render.HeaderRenderer;
import com.example.legendarytiers.client.tooltip.render.TooltipRenderUtil;
import com.example.legendarytiers.client.tooltip.section.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;

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
        TooltipTheme theme =
                TooltipThemes.get(
                        context.rarity()
                );

        int width = context.tooltipWidth();
        int attributeCount =
                AttributeSection.visibleCount(context);
        int enchantmentCount =
                context.stack()
                        .getOrDefault(
                                DataComponents.ENCHANTMENTS,
                                ItemEnchantments.EMPTY
                        )
                        .size();
        int height = TooltipLayout.calculateHeight(
                enchantmentCount,
                attributeCount,
                context.broken(),
                context.maxDurability() > 0,
                true,
                !context.showAdvancedAttributes(),
                context.showEnchantments()
        );

        TooltipRenderUtil.drawBackground(
                graphics,
                x,
                y,
                width,
                height,
                theme
        );

        HeaderRenderer.render(

                graphics,

                context.itemName(),

                context.rarity().name(),

                context.rarity(),

                x,

                y,

                width,

                theme

        );

        int currentY =
                y
                        + HeaderRenderer.HEIGHT
                        + TooltipLayout.PADDING;

        QualitySection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                width
        );

        currentY += QualitySection.getHeight();

        if (context.maxDurability() > 0) {

            DurabilitySection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY,
                    width
            );

            currentY += DurabilitySection.getHeight();

        }

        TooltipRenderUtil.drawDivider(
                graphics,
                x,
                currentY,
                width,
                theme
        );

        currentY += TooltipLayout.DIVIDER_HEIGHT;

        ExperienceSection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                width
        );

        currentY += ExperienceSection.getHeight();

        if (attributeCount > 0) {

            TooltipRenderUtil.drawDivider(
                    graphics,
                    x,
                    currentY,
                    width,
                    theme
            );

            currentY += TooltipLayout.DIVIDER_HEIGHT;

            AttributeSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY,
                    width
            );

            currentY += AttributeSection.getHeight(attributeCount);

        }

        int enchantHeight =
                EnchantmentSection.getHeight(context);

        if (enchantHeight > 0) {

            currentY += 6;

            TooltipRenderUtil.drawDivider(
                    graphics,
                    x,
                    currentY,
                    width,
                    theme
            );

            currentY += 8;

            EnchantmentSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY,
                    width
            );

            currentY += enchantHeight;
        }

        if (context.broken()) {

            TooltipRenderUtil.drawDivider(
                    graphics,
                    x,
                    currentY,
                    width,
                    theme
            );

            currentY += TooltipLayout.DIVIDER_HEIGHT;

            BrokenSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY,
                    width
            );

            currentY += BrokenSection.getHeight();

        }

        TooltipRenderUtil.drawDivider(
                graphics,
                x,
                currentY,
                width,
                theme
        );

        currentY += TooltipLayout.DIVIDER_HEIGHT;

        ReforgeSection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                width
        );

        currentY += ReforgeSection.getHeight();

        if (!context.showAdvancedAttributes()) {

            TooltipRenderUtil.drawDivider(
                    graphics,
                    x,
                    currentY,
                    width,
                    theme
            );

            currentY += TooltipLayout.DIVIDER_HEIGHT;

            HintSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY,
                    width
            );

            //currentY += HintSection.getHeight();

        }

    }

}