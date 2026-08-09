package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import com.example.legendarytiers.util.RepairCostHelper;
import com.example.legendarytiers.util.RepairEntry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class BrokenSection {

    private static final int BANNER_HEIGHT = 15;
    private static final int SLOT_SIZE = 18;

    private BrokenSection() {
    }

    public static int getHeight() {
        return 60; // Общая высота 3-строчного блока
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {
        if (context == null || !context.broken()) {
            return;
        }

        int containerX = x + TooltipLayout.PADDING;
        int containerWidth = width - (TooltipLayout.PADDING * 2);
        int currentY = y;

        // ----------------------------------
        // 1. Верхний Баннер (Строка 1)
        // ----------------------------------
        int bannerBg = 0x993B0A0A;
        int bannerBorder = 0xFF8B0000;

        graphics.fill(containerX, currentY, containerX + containerWidth, currentY + BANNER_HEIGHT, bannerBg);
        graphics.renderOutline(containerX, currentY, containerWidth, BANNER_HEIGHT, bannerBorder);

        IconRenderer.draw(
                graphics,
                containerX + 3,
                currentY - 1,
                TooltipIcons.BROKEN_X,
                TooltipIcons.BROKEN_Y
        );

        String brokenTitle = Component.translatable("tooltip.legendarytiers.broken").getString();
        TextRenderer.drawShadow(
                graphics,
                font,
                brokenTitle,
                containerX + 24,
                currentY + 3,
                0xFFFF5555
        );

        currentY += BANNER_HEIGHT + 3;

        // ----------------------------------
        // 2. Основная карточка починки (Строки 2 и 3)
        // ----------------------------------
        int cardHeight = 42;
        graphics.fill(containerX, currentY, containerX + containerWidth, currentY + cardHeight, 0xAA0D0D12);
        graphics.renderOutline(containerX, currentY, containerWidth, cardHeight, 0x44FF5555);

        // --- Строка 2: Инструкция с наковальней ---
        IconRenderer.draw(
                graphics,
                containerX + 4,
                currentY + 2,
                TooltipIcons.REPAIR_X,
                TooltipIcons.REPAIR_Y
        );

        String repairHint = Component.translatable("tooltip.legendarytiers.repair_hint").getString();
        TextRenderer.draw(
                graphics,
                font,
                repairHint,
                containerX + 24,
                currentY + 6,
                TooltipColors.TEXT_MUTED
        );

        // --- Строка 3: Ингредиенты и Количество ---
        ResourceLocation originalId = context.stack().get(ModDataComponents.ORIGINAL_ITEM_ID);
        if (originalId != null) {
            Item originalItem = BuiltInRegistries.ITEM.get(originalId);
            Optional<RepairEntry> repair = RepairCostHelper.get(originalItem);

            if (repair.isPresent()) {
                RepairEntry info = repair.get();
                ItemStack displayStack = info.displayStack();

                int row3Y = currentY + 20;
                int slotX = containerX + 4;

                // Слот 18x18 для иконки материала
                graphics.fill(slotX, row3Y, slotX + SLOT_SIZE, row3Y + SLOT_SIZE, TooltipColors.SLOT_BG);
                graphics.renderOutline(slotX, row3Y, SLOT_SIZE, SLOT_SIZE, TooltipColors.SLOT_BORDER);

                // Рендер иконки (например, Железный слиток)
                graphics.renderItem(displayStack, slotX + 1, row3Y + 1);

                // Название материала и количество рядом со слотом
                String countText = "x" + info.amount();
                String matName = displayStack.getHoverName().getString();

                int textX = slotX + SLOT_SIZE + 6;
                TextRenderer.draw(graphics, font, countText, textX, row3Y + 5, 0xFFFFAA00); // Золотой цвет количества
                TextRenderer.draw(graphics, font, matName, textX + font.width(countText) + 4, row3Y + 5, TooltipColors.TEXT_NORMAL);
            }
        }
    }
}