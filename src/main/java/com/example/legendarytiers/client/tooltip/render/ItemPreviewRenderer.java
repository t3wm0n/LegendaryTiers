package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ItemPreviewRenderer {

    private static final int FRAME_TEX_WIDTH = 256;
    private static final int FRAME_TEX_HEIGHT = 320;

    public static final int DISPLAY_HEIGHT = 120;
    public static final int DISPLAY_WIDTH = (DISPLAY_HEIGHT * FRAME_TEX_WIDTH) / FRAME_TEX_HEIGHT;

    private ItemPreviewRenderer() {}

    public static void renderPreviewBox(
            GuiGraphics graphics,
            LegendaryTooltipContext context,
            int x,
            int y,
            int tooltipHeight,
            TooltipTheme theme
    ) {
        if (context == null || context.stack().isEmpty()) return;

        // Определяем, какой предмет будем рендерить в 3D
        ItemStack displayStack = context.stack();
        boolean isBroken = context.broken();

        if (isBroken) {
            ResourceLocation originalId = context.stack().get(ModDataComponents.ORIGINAL_ITEM_ID);
            if (originalId != null) {
                Item originalItem = BuiltInRegistries.ITEM.get(originalId);
                displayStack = new ItemStack(originalItem);
            }
        }

        int boxX = x;
        int boxY = y;

        int glowColor = theme.borderColor();
        float r = ((glowColor >> 16) & 0xFF) / 255.0F;
        float g = ((glowColor >> 8) & 0xFF) / 255.0F;
        float b = (glowColor & 0xFF) / 255.0F;

        float timeSec = (System.currentTimeMillis() % 1_000_000L) / 1000.0F;
        float rotationAngle = (timeSec * 50.0F) % 360.0F;

        float centerX = boxX + (DISPLAY_WIDTH / 2.0F);
        float itemY = boxY + (DISPLAY_HEIGHT * 0.45F);

        var pose = graphics.pose();

        // -------------------------------------------------------------
        // 1. ПОДЛОЖКА И ВЕРТИКАЛЬНЫЙ ЛУЧ СВЕТА
        // -------------------------------------------------------------
        graphics.fill(boxX + 4, boxY + 4, boxX + DISPLAY_WIDTH - 4, boxY + DISPLAY_HEIGHT - 4, 0xF008080C);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        int beamWidth = 32;
        int beamLeft = (int) (centerX - beamWidth / 2.0F);
        graphics.fillGradient(
                beamLeft, boxY + 16,
                beamLeft + beamWidth, (int) itemY + 12,
                0x00000000,
                (0x66 << 24) | (glowColor & 0x00FFFFFF)
        );

        // -------------------------------------------------------------
        // 2. МАГИЧЕСКИЙ КРУГ ПОД ПРЕДМЕТОМ
        // -------------------------------------------------------------
        pose.pushPose();
        pose.translate(centerX, itemY + 30.0F, 100.0F);

        pose.mulPose(Axis.XP.rotationDegrees(75.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(-rotationAngle * 0.7F));

        RenderSystem.setShaderColor(r, g, b, 0.85F);

        int circleRadius = 32;
        int circleDiameter = circleRadius * 2;

        graphics.blit(
                TooltipTextures.MAGIC_CIRCLE,
                -circleRadius, -circleRadius,
                circleDiameter, circleDiameter,
                0.0F, 0.0F,
                256, 256,
                256, 256
        );

        pose.popPose();

        // -------------------------------------------------------------
        // 3. ВЕРТИКАЛЬНЫЙ ТРЕХМЕРНЫЙ ПРЕДМЕТ
        // -------------------------------------------------------------
        pose.pushPose();
        pose.translate(centerX, itemY, 150.0F);

        float scale = 44.0F;
        pose.scale(scale, -scale, scale);

        pose.mulPose(Axis.YP.rotationDegrees(rotationAngle));

        if (shouldCompensateToolAngle(displayStack)) {
            pose.mulPose(Axis.ZP.rotationDegrees(45.0F));
        }

        // Если предмет сломан — накладываем тёмно-красный оттенок
        if (isBroken) {
            RenderSystem.setShaderColor(1.0F, 0.25F, 0.25F, 1.0F);
        } else {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        Lighting.setupFor3DItems();

        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource bufferSource = graphics.bufferSource();

        mc.getItemRenderer().renderStatic(
                displayStack,
                ItemDisplayContext.GUI,
                0xF000F0,
                OverlayTexture.NO_OVERLAY,
                pose,
                bufferSource,
                mc.level,
                0
        );

        graphics.flush();
        pose.popPose();

        // -------------------------------------------------------------
        // 4. ТЕКСТУРА РАМКИ ITEM_FRAME
        // -------------------------------------------------------------
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderColor(r, g, b, 1.0F);

        graphics.blit(
                TooltipTextures.ITEM_FRAME,
                boxX, boxY,
                DISPLAY_WIDTH, DISPLAY_HEIGHT,
                0.0F, 0.0F,
                FRAME_TEX_WIDTH, FRAME_TEX_HEIGHT,
                FRAME_TEX_WIDTH, FRAME_TEX_HEIGHT
        );

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Lighting.setupForFlatItems();
        RenderSystem.disableBlend();
    }

    private static boolean shouldCompensateToolAngle(ItemStack stack) {
        var item = stack.getItem();

        if (item instanceof ArmorItem || item instanceof BlockItem) {
            return false;
        }

        return item instanceof SwordItem
                || item instanceof DiggerItem
                || item instanceof ProjectileWeaponItem;
    }
}