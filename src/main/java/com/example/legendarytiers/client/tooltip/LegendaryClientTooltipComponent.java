package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.client.tooltip.section.AttributeSection;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LegendaryClientTooltipComponent implements ClientTooltipComponent {

    private final LegendaryTooltipContext context;

    public LegendaryClientTooltipComponent(LegendaryTooltipContext context) {
        this.context = context;
    }

    public LegendaryTooltipContext getContext() {
        return context;
    }

    @Override
    public int getWidth(Font font) {
        return context.tooltipWidth();
    }

    @Override
    public int getHeight() {

        int attributeCount = AttributeSection.visibleCount(context);

        int enchantmentCount =
                context.stack()
                        .getOrDefault(
                                DataComponents.ENCHANTMENTS,
                                ItemEnchantments.EMPTY
                        )
                        .size();

        return TooltipLayout.calculateHeight(
                enchantmentCount,
                attributeCount,
                context.broken(),
                true,
                true,
                !Screen.hasShiftDown(),
                !Screen.hasControlDown()
        );
    }

    @Override
    public void renderImage(
            Font font,
            int x,
            int y,
            GuiGraphics graphics
    ) {

        LegendaryTooltipRenderer.render(
                graphics,
                font,
                context,
                x,
                y
        );

    }

    @Override
    public void renderText(
            Font font,
            int x,
            int y,
            org.joml.Matrix4f matrix,
            net.minecraft.client.renderer.MultiBufferSource.BufferSource buffer
    ) {
    }

}