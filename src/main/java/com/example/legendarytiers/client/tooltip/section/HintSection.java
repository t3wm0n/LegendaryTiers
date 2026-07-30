package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class HintSection {

    private HintSection() {
    }

    public static int getHeight() {
        return 18;
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {

        if (context.showAdvancedAttributes()) {
            return;
        }

        String text_shift = Component
                .translatable("legendarytiers.tooltip.hold_shift")
                .getString();

        int textWidth = font.width(text_shift);

        TextRenderer.draw(
                graphics,
                font,
                text_shift,
                x + (width - textWidth) / 2,
                y + 4,
                TooltipColors.TEXT_DISABLED
        );

        ItemEnchantments enchantments =
                context.stack().getOrDefault(
                        DataComponents.ENCHANTMENTS,
                        ItemEnchantments.EMPTY
                );

        if (!context.showEnchantments() && !enchantments.isEmpty()) {

            String text_ctrl = Component
                    .translatable("legendarytiers.tooltip.hold_ctrl")
                    .getString();

            int textCtrlWidth = font.width(text_ctrl);
            y += 12;

            TextRenderer.draw(
                    graphics,
                    font,
                    text_ctrl,
                    x + (width - textCtrlWidth) / 2,
                    y + 4,
                    TooltipColors.TEXT_DISABLED
            );
        }
    }
}