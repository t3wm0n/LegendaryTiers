package com.example.legendarytiers.client.tooltip2;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip.TooltipThemes;
import com.example.legendarytiers.client.tooltip2.layout.TooltipLayout;
import com.example.legendarytiers.client.tooltip2.render.AttributesRenderer;
import com.example.legendarytiers.client.tooltip2.render.BackgroundRenderer;
import com.example.legendarytiers.client.tooltip2.render.BarsRenderer;
import com.example.legendarytiers.client.tooltip2.render.EffectsRenderer;
import com.example.legendarytiers.client.tooltip2.render.FooterRenderer;
import com.example.legendarytiers.client.tooltip2.render.FrameRenderer;
import com.example.legendarytiers.client.tooltip2.render.HeaderRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class LegendaryTooltipRendererV2 {

    private LegendaryTooltipRendererV2() {}

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

        int width =
                context.tooltipWidth();

        /*
         * Пока фиксированная высота.
         * Позже будет вычисляться автоматически.
         */

        int height = 300;

        TooltipLayout layout =
                TooltipLayout.create(

                        x,

                        y,

                        width,

                        height

                );

        BackgroundRenderer.render(

                graphics,

                layout,

                theme

        );

        FrameRenderer.render(

                graphics,

                layout,

                theme

        );

        HeaderRenderer.render(

                graphics,

                layout,

                theme,

                context.itemName(),

                context.rarity(),

                context.rarity().stars()

        );

        BarsRenderer.render(

                graphics,

                layout,

                theme,

                context

        );

        AttributesRenderer.render(

                graphics,

                font,

                layout,

                theme,

                context

        );

        FooterRenderer.render(

                graphics,

                font,

                layout,

                theme,

                context

        );

        EffectsRenderer.render(

                graphics,

                layout,

                theme

        );

    }

}