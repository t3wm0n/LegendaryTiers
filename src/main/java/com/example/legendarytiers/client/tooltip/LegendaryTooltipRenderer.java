package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.client.tooltip.render.BackgroundRenderer;
import com.example.legendarytiers.client.tooltip.render.DividerRenderer;
import com.example.legendarytiers.client.tooltip.render.HeaderRenderer;
import com.example.legendarytiers.client.tooltip.section.*;
import net.minecraft.client.Minecraft;
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
                !context.showEnchantments()
        );
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int correctedY = y;
        if (correctedY + height > screenHeight - 5) { // 5px отступ от края
            correctedY = screenHeight - height - 5;
        }

        if (correctedY < 5) {
            correctedY = 5;
        }

        //Задний фон
        BackgroundRenderer.render(
                graphics,
                x - TooltipLayout.PADDING,
                correctedY - TooltipLayout.PADDING,
                width + (TooltipLayout.PADDING * 2),
                height + (TooltipLayout.PADDING * 2),
                theme
        );

        //Заголовок (верх тултипа)
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

        int currentY =
                correctedY + HeaderRenderer.HEADER_HEIGHT - TooltipLayout.PADDING;

        //3д моделька предмета
        int IPS_height = TooltipLayout.calculateHeight(
                0,
                0,
                false,
                true,
                true,
                false,
                true
        );;

        ItemPreviewSection.render(
                graphics,
                context,
                x,
                currentY,
                width,
                IPS_height,
                theme
        );

        int yAfterPreview = currentY + ItemPreviewSection.getHeight();
        int oldwidth = width;
        width -= ItemPreviewSection.getWidth();


        //Прочность
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

        //Опыт
        ExperienceSection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                width
        );

        currentY += ExperienceSection.getHeight();

        //Попытка перековки
        ReforgeSection.render(
                graphics,
                font,
                context,
                x,
                currentY,
                width
        );
        currentY += ReforgeSection.getHeight();

        DividerRenderer.drawDivider(
                graphics,
                x,
                currentY,
                width,
                theme
        );

        currentY += TooltipLayout.DIVIDER_HEIGHT;

        //Атрибуты
        if (attributeCount > 0) {

            AttributeSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    yAfterPreview,
                    oldwidth
            );

            yAfterPreview += AttributeSection.getHeight(attributeCount);

        }

        //Чары
        int enchantHeight =
                EnchantmentSection.getHeight(context);

        if (enchantHeight > 0) {

            if (attributeCount > 0) {
                DividerRenderer.drawDivider(
                        graphics,
                        x,
                        yAfterPreview,
                        oldwidth,
                        theme
                );
                yAfterPreview += TooltipLayout.DIVIDER_HEIGHT + 2;
            }

            EnchantmentSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    yAfterPreview,
                    oldwidth
            );

            yAfterPreview += enchantHeight;
        }

        //Блок "Поломки"
        if (context.broken()) {

            DividerRenderer.drawDivider(
                    graphics,
                    x,
                    yAfterPreview,
                    oldwidth,
                    theme
            );
            yAfterPreview += TooltipLayout.DIVIDER_HEIGHT + 4;

            BrokenSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    yAfterPreview,
                    oldwidth
            );

            //yAfterPreview += BrokenSection.getHeight() + 2;

        }

        //Подсказки кнопок

        boolean hasEnchantments = !context.stack()
                .getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .isEmpty();

        boolean hasAnyHint = !context.showAdvancedAttributes()
                || (!context.showEnchantments() && hasEnchantments);

        if (hasAnyHint) {

            HintSection.render(
                    graphics,
                    font,
                    context,
                    x,
                    currentY + 2,
                    width
            );

        }

    }

}