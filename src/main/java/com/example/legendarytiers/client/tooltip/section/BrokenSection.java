package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.util.RepairCostHelper;
import com.example.legendarytiers.util.RepairEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class BrokenSection {

    private BrokenSection() {
    }


    public static int getHeight() {
        return TooltipLayout.BROKEN_HEIGHT;
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

        graphics.fill(
                x + TooltipLayout.PADDING,
                y,
                x + width - TooltipLayout.PADDING,
                y + TooltipLayout.BROKEN_HEIGHT,
                0x33111111
        );

        graphics.renderOutline(
                x + TooltipLayout.PADDING,
                y,
                width - TooltipLayout.PADDING * 2,
                TooltipLayout.BROKEN_HEIGHT,
                0x55FF5555
        );

        int currentY = y;

        /*
         * Заголовок
         */

        IconRenderer.draw(
                graphics,
                x + TooltipLayout.PADDING + 2,
                currentY,
                TooltipIcons.BROKEN_X,
                TooltipIcons.BROKEN_Y
        );

        TextRenderer.drawShadow(
                graphics,
                font,
                Component.translatable(
                        "tooltip.legendarytiers.broken"
                ).getString(),
                x + TooltipLayout.PADDING + 26,
                currentY + 6,
                TooltipColors.TEXT_NEGATIVE
        );

        currentY += 22;

        /*
         * Оригинальный предмет
         */

        ResourceLocation originalId =
                context.stack().get(
                        ModDataComponents.ORIGINAL_ITEM_ID
                );

        if (originalId == null) {
            return;
        }

        Item originalItem =
                BuiltInRegistries.ITEM.get(originalId);

        ItemStack originalStack =
                new ItemStack(originalItem);

        graphics.renderItem(
                originalStack,
                x + TooltipLayout.PADDING + 2,
                currentY
        );

        TextRenderer.draw(
                graphics,
                font,
                originalStack.getHoverName().getString(),
                x + TooltipLayout.PADDING + 26,
                currentY + 5,
                TooltipColors.TEXT_SECONDARY
        );

        currentY += 22;

        /*
         * Информация о ремонте
         */

        Optional<RepairEntry> repair =
                RepairCostHelper.get(originalItem);

        if (repair.isEmpty()) {
            return;
        }

        RepairEntry info =
                repair.get();

        IconRenderer.draw(
                graphics,
                x + TooltipLayout.PADDING + 2,
                currentY,
                TooltipIcons.REPAIR_X,
                TooltipIcons.REPAIR_Y
        );

        TextRenderer.draw(
                graphics,
                font,
                Component.translatable("tooltip.legendarytiers.repair_hint").getString(),
                x + TooltipLayout.PADDING + 26,
                currentY + 5,
                TooltipColors.TEXT_NORMAL
        );

        currentY += 22;

        /*
         * Материал
         */

        graphics.renderItem(
                info.displayStack(),
                x + TooltipLayout.PADDING + 4,
                currentY
        );

        TextRenderer.draw(
                graphics,
                font,
                info.displayStack().getHoverName().getString() + "×" + info.amount(),
                x + TooltipLayout.PADDING + 26,
                currentY + 5,
                TooltipColors.TEXT_POSITIVE
        );

    }

}