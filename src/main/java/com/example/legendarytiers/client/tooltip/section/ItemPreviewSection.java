package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip.render.ItemPreviewRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemPreviewSection {

    public static int getHeight() {

        return ItemPreviewRenderer.DISPLAY_HEIGHT;
    }

    public static int getWidth() {

        return ItemPreviewRenderer.DISPLAY_WIDTH;
    }

    public static void render(
            GuiGraphics graphics,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {
        int previewX = x + width - getWidth() - 6;

        ItemPreviewRenderer.renderPreviewBox(
                graphics,
                context,
                previewX,
                y,
                height,
                theme
        );
    }
}
