package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipTextures; // Ваша ссылка на картинки
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip.render.DividerRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class HintSection {

    // Размеры текстур клавиш на экране
    private static final int KEY_WIDTH = 35;
    private static final int KEY_HEIGHT = 14;
    private static final int GAP = 4;
    private static final int TEX_REAL_WIDTH = 249;
    private static final int TEX_REAL_HEIGHT = 96;

    private HintSection() {
    }

    public static int getHeight(LegendaryTooltipContext context) {
        boolean showShiftHint = !context.showAdvancedAttributes();
        ItemEnchantments enchantments = context.stack().getOrDefault(
                DataComponents.ENCHANTMENTS,
                ItemEnchantments.EMPTY
        );
        boolean showCtrlHint = !context.showEnchantments() && !enchantments.isEmpty();

        if (!showShiftHint && !showCtrlHint) return 0;

        return 16;
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {
        boolean showShiftHint = !context.showAdvancedAttributes();

        ItemEnchantments enchantments = context.stack().getOrDefault(
                DataComponents.ENCHANTMENTS,
                ItemEnchantments.EMPTY
        );
        boolean showCtrlHint = !context.showEnchantments() && !enchantments.isEmpty();

        if (!showShiftHint && !showCtrlHint) {
            return;
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();

        int currentY = y + 2;
        if (showShiftHint && showCtrlHint) {
            int quatWidth = width / 4;

            String textShift = Component.translatable("legendarytiers.tooltip.show_stats").getString();
            renderKeyHint(
                    graphics,
                    font,
                    TooltipTextures.PIC_SHIFT,
                    textShift,
                    x - quatWidth,
                    currentY,
                    width
            );

            String textCtrl = Component.translatable("legendarytiers.tooltip.show_enchants").getString();
            renderKeyHint(
                    graphics,
                    font,
                    TooltipTextures.PIC_CTRL,
                    textCtrl,
                    x + quatWidth,
                    currentY,
                    width
            );
        }
        // Вариант Б: Видна ТОЛЬКО подсказка Shift (по центру)
        else if (showShiftHint) {
            String textShift = Component.translatable("legendarytiers.tooltip.show_stats").getString();
            renderKeyHint(
                    graphics,
                    font,
                    TooltipTextures.PIC_SHIFT,
                    textShift,
                    x,
                    currentY,
                    width
            );
        }
        // Вариант В: Видна ТОЛЬКО подсказка Ctrl (по центру)
        else {
            String textCtrl = Component.translatable("legendarytiers.tooltip.show_enchants").getString();
            renderKeyHint(
                    graphics,
                    font,
                    TooltipTextures.PIC_CTRL,
                    textCtrl,
                    x,
                    currentY,
                    width
            );
        }

        RenderSystem.disableBlend();
    }

    /**
     * Вспомогательный метод для отрисовки пары [ ИКОНКА_КЛАВИШИ ] + [ ТЕКСТ ]
     */
    private static void renderKeyHint(
            GuiGraphics graphics,
            Font font,
            ResourceLocation keyTexture,
            String label,
            int startX,
            int y,
            int containerWidth
    ) {
        int labelWidth = font.width(label);
        int totalWidth = KEY_WIDTH + GAP + labelWidth;

        int drawX = startX + (containerWidth - totalWidth) / 2;

        // Рисуем уменьшенную картинку клавиши
        graphics.blit(
                keyTexture,
                drawX, y - 2,
                KEY_WIDTH, KEY_HEIGHT,        // Экранные размеры (14x9)
                0, 0,                         // U, V координаты начала (верхний левый угол)
                TEX_REAL_WIDTH, TEX_REAL_HEIGHT, // Ширина и высота региона в файлe (32x16)
                TEX_REAL_WIDTH, TEX_REAL_HEIGHT  // Полный размер текстуры .png
        );

        // Выравнивание текста по центру относительно уменьшенной плашки
        int textY = y + (KEY_HEIGHT - font.lineHeight) / 2;

        TextRenderer.draw(
                graphics,
                font,
                label,
                drawX + KEY_WIDTH + GAP,
                textY,
                TooltipColors.TEXT_DISABLED
        );
    }
}