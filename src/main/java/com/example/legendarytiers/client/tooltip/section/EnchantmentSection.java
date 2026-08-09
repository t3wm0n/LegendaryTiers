package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class EnchantmentSection {

    private EnchantmentSection() {
    }

    public static int getHeight(
            LegendaryTooltipContext context
    ) {
        if (!context.showEnchantments()) {
            return 0;
        }

        ItemEnchantments enchantments =
                context.stack().getOrDefault(
                        DataComponents.ENCHANTMENTS,
                        ItemEnchantments.EMPTY
                );

        if (enchantments.isEmpty()) {
            return 0;
        }

        return enchantments.size() * TooltipLayout.ENCHANTMENT_LINE_HEIGHT;
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {

        if (!context.showEnchantments()) {
            return;
        }

        ItemEnchantments enchantments =
                context.stack().getOrDefault(
                        DataComponents.ENCHANTMENTS,
                        ItemEnchantments.EMPTY
                );
        if (enchantments.isEmpty()) {
            return;
        }

        int currentY = y;

        for (var entry : enchantments.entrySet()) {

            Holder<Enchantment> enchantment = entry.getKey();

            int level = entry.getIntValue();

            Component text =
                    Enchantment.getFullname(
                            enchantment,
                            level
                    );

            boolean isBadEnchantment = enchantment.is(EnchantmentTags.CURSE)
                    || enchantment.is(Enchantments.VANISHING_CURSE)
                    || enchantment.is(Enchantments.BINDING_CURSE);

            int color = isBadEnchantment ? TooltipColors.TEXT_ENCHANTMENT_BAD : TooltipColors.TEXT_ENCHANTMENT;

            TextRenderer.draw(
                    graphics,
                    font,
                    text.getString(),
                    x + TooltipLayout.PADDING,
                    currentY + 4,
                    color
            );

            currentY += TooltipLayout.ENCHANTMENT_LINE_HEIGHT;

        }

    }

}