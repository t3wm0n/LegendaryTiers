package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.render.TooltipRenderUtil;
import com.example.legendarytiers.client.tooltip.TooltipTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class BackgroundRenderer {

    private BackgroundRenderer() {
    }

    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    LegendaryTiers.MOD_ID,
                    "textures/gui/tooltip/tooltip.png"
            );

    public static void render(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height
    ) {

        graphics.pose().pushPose();

        graphics.setColor(1F, 1F, 1F, 1F);

        int b = TooltipTextures.BORDER_SIZE;

        // ---------- Центр ----------

        TooltipRenderUtil.drawTextureScaled(
                graphics,
                TEXTURE,
                x + b,
                y + b,
                width - b * 2,
                height - b * 2,
                TooltipTextures.CENTER_X,
                TooltipTextures.CENTER_Y,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        // ---------- Верх ----------

        TooltipRenderUtil.drawTextureScaled(
                graphics,
                TEXTURE,
                x + b,
                y,
                width - b * 2,
                b,
                TooltipTextures.TOP_X,
                TooltipTextures.TOP_Y,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        // ---------- Низ ----------

        TooltipRenderUtil.drawTextureScaled(
                graphics,
                TEXTURE,
                x + b,
                y + height - b,
                width - b * 2,
                b,
                TooltipTextures.BOTTOM_X,
                TooltipTextures.BOTTOM_Y,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        // ---------- Лево ----------

        TooltipRenderUtil.drawTextureScaled(
                graphics,
                TEXTURE,
                x,
                y + b,
                b,
                height - b * 2,
                TooltipTextures.LEFT_X,
                TooltipTextures.LEFT_Y,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        // ---------- Право ----------

        TooltipRenderUtil.drawTextureScaled(
                graphics,
                TEXTURE,
                x + width - b,
                y + b,
                b,
                height - b * 2,
                TooltipTextures.RIGHT_X,
                TooltipTextures.RIGHT_Y,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.SOURCE_SIZE,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        // ---------- Углы ----------

        TooltipRenderUtil.drawTexture(
                graphics,
                TEXTURE,
                x,
                y,
                TooltipTextures.TOP_LEFT_X,
                TooltipTextures.TOP_LEFT_Y,
                b,
                b,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        TooltipRenderUtil.drawTexture(
                graphics,
                TEXTURE,
                x + width - b,
                y,
                TooltipTextures.TOP_RIGHT_X,
                TooltipTextures.TOP_RIGHT_Y,
                b,
                b,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        TooltipRenderUtil.drawTexture(
                graphics,
                TEXTURE,
                x,
                y + height - b,
                TooltipTextures.BOTTOM_LEFT_X,
                TooltipTextures.BOTTOM_LEFT_Y,
                b,
                b,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        TooltipRenderUtil.drawTexture(
                graphics,
                TEXTURE,
                x + width - b,
                y + height - b,
                TooltipTextures.BOTTOM_RIGHT_X,
                TooltipTextures.BOTTOM_RIGHT_Y,
                b,
                b,
                TooltipTextures.ATLAS_SIZE,
                TooltipTextures.ATLAS_SIZE
        );

        graphics.pose().popPose();
    }

}