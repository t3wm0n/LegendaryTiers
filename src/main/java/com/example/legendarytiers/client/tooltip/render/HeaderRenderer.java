package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip.section.QualitySection;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.example.legendarytiers.util.TextAnimationUtils;

public final class HeaderRenderer {

    private static final int TEXTURE_SIDE_WIDTH = 256;
    private static final int TEXTURE_CENTER_SIDE_WIDTH = 192;
    private static final int TEXTURE_CENTER_MID_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 298;

    private static final int SIDE_WIDTH = 60;
    public static final int HEADER_HEIGHT = 70;
    private static final int CENTER_STEP = 32;

    private static final int CENTER_MID_W = 24;
    private static final int ADD_PADD = 6;
    private static final float TEXT_SCALE = 1.25F;

    private HeaderRenderer() {}

    public static void render(
            GuiGraphics graphics,
            LegendaryTooltipContext context,
            String rarity,
            Rarity tier,
            double quality,
            int x,
            int y,
            int width_def,
            TooltipTheme theme
    ) {
        int width = width_def + (TooltipLayout.PADDING * 2);
        int effectiveSideWidth = Math.min(SIDE_WIDTH, width / 2);

        Font font = Minecraft.getInstance().font;
        int color = theme.borderColor();

        // -------------------------------------------------------------
        // Формирование имени: если сломан -> "Золотая кирка (Сломан)"
        // -------------------------------------------------------------
        String displayTitle;
        if (context != null && context.broken()) {
            ResourceLocation originalId = context.stack().get(ModDataComponents.ORIGINAL_ITEM_ID);
            if (originalId != null) {
                Item originalItem = BuiltInRegistries.ITEM.get(originalId);
                String originalName = new ItemStack(originalItem).getHoverName().getString();
                displayTitle = String.format("%s %s", "⚠", originalName);
            } else {
                displayTitle = context.stack().getHoverName().getString();
            }
        } else {
            displayTitle = context.stack().getHoverName().getString();
        }

        float r = ((color >> 16) & 0xFF) / 255.0F;
        float g = ((color >> 8) & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(r, g, b, 1.0F);

        try {
            int renderX = x - ADD_PADD;
            int renderY = y - ADD_PADD;
            int totalWidth = width + (ADD_PADD * 2);

            graphics.blit(
                    TooltipTextures.HEADER_LEFT,
                    renderX, renderY,
                    effectiveSideWidth, HEADER_HEIGHT,
                    0, 0,
                    TEXTURE_SIDE_WIDTH, TEXTURE_HEIGHT,
                    TEXTURE_SIDE_WIDTH, TEXTURE_HEIGHT
            );

            int innerLeft = renderX + effectiveSideWidth;
            int innerRight = renderX + totalWidth - effectiveSideWidth;

            int headerCenterX = renderX + (totalWidth / 2);
            int midLeft = headerCenterX - (CENTER_MID_W / 2);
            int midRight = headerCenterX + (CENTER_MID_W / 2);

            if (innerRight > innerLeft) {
                if (midLeft > innerLeft) {
                    float lUvScale = (float) TEXTURE_CENTER_SIDE_WIDTH / CENTER_STEP;
                    for (int xx = innerLeft; xx < midLeft; xx += CENTER_STEP) {
                        int drawWidth = Math.min(CENTER_STEP, midLeft - xx);
                        int uvWidth = (int) (drawWidth * lUvScale);

                        graphics.blit(
                                TooltipTextures.HEADER_CENTER_L,
                                xx, renderY,
                                drawWidth, HEADER_HEIGHT,
                                0, 0,
                                uvWidth, TEXTURE_HEIGHT,
                                TEXTURE_CENTER_SIDE_WIDTH, TEXTURE_HEIGHT
                        );
                    }
                }

                graphics.blit(
                        TooltipTextures.HEADER_CENTER,
                        midLeft, renderY,
                        CENTER_MID_W, HEADER_HEIGHT,
                        0, 0,
                        TEXTURE_CENTER_MID_WIDTH, TEXTURE_HEIGHT,
                        TEXTURE_CENTER_MID_WIDTH, TEXTURE_HEIGHT
                );

                if (innerRight > midRight) {
                    float rUvScale = (float) TEXTURE_CENTER_SIDE_WIDTH / CENTER_STEP;
                    for (int xx = midRight; xx < innerRight; xx += CENTER_STEP) {
                        int drawWidth = Math.min(CENTER_STEP, innerRight - xx);
                        int uvWidth = (int) (drawWidth * rUvScale);

                        graphics.blit(
                                TooltipTextures.HEADER_CENTER_R,
                                xx, renderY,
                                drawWidth, HEADER_HEIGHT,
                                0, 0,
                                uvWidth, TEXTURE_HEIGHT,
                                TEXTURE_CENTER_SIDE_WIDTH, TEXTURE_HEIGHT
                        );
                    }
                }
            }

            graphics.blit(
                    TooltipTextures.HEADER_RIGHT,
                    renderX + totalWidth - effectiveSideWidth, renderY,
                    effectiveSideWidth, HEADER_HEIGHT,
                    0, 0,
                    TEXTURE_SIDE_WIDTH, TEXTURE_HEIGHT,
                    TEXTURE_SIDE_WIDTH, TEXTURE_HEIGHT
            );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        // ---------- ТЕКСТ И ЗВЕЗДЫ ----------
        PoseStack pose = graphics.pose();

        float scaledHeaderHeight = HEADER_HEIGHT / TEXT_SCALE;
        float baseTextY = (scaledHeaderHeight / 2.0F) - (font.lineHeight / 2.0F);

        float mainTextY = baseTextY - 9.0F;
        float titleX = effectiveSideWidth * 0.8F / TEXT_SCALE;

        pose.pushPose();
        pose.translate(x, y, 0.0F);

        // Цвет названия: светло-красный при поломке, белый в обычном состоянии
        int titleColor = (context != null && context.broken()) ? 0xFFFF5555 : 0xFFFFFF;

        // 1. Название предмета
        TextAnimationUtils.drawGlitchString(
                graphics,
                font,
                displayTitle,
                titleX,
                mainTextY,
                TEXT_SCALE,
                titleColor
        );

        // 2. Звезды качества
        int starsY = (int) (mainTextY + (font.lineHeight * TEXT_SCALE));
        int starsX = (int) ((effectiveSideWidth * 0.8F) / TEXT_SCALE);

        QualitySection.render(
                graphics,
                font,
                quality,
                starsX,
                starsY,
                width,
                theme
        );

        // 3. Редкость справа
        float rarityScale = TEXT_SCALE * 1.38F;
        int rw = font.width(rarity);

        float rarityX = width - (rw * rarityScale) - (float) effectiveSideWidth / 1.5F;
        float rarityY = HEADER_HEIGHT / 3F;

        drawShimmerRarityText(
                graphics,
                font,
                rarity,
                rarityX,
                rarityY,
                rarityScale,
                tier,
                theme
        );

        pose.popPose();
        RenderSystem.disableBlend();
    }

    private static void drawShimmerRarityText(
            GuiGraphics graphics,
            Font font,
            String text,
            float x,
            float y,
            float scale,
            Rarity tier,
            TooltipTheme theme
    ) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0.0F);
        pose.scale(scale, scale, 1.0F);

        float time = (System.currentTimeMillis() % 1_000_000L) / 1000.0F;

        int baseColor = theme.borderColor();
        float[] hsv = new float[3];
        int r = (baseColor >> 16) & 0xFF;
        int g = (baseColor >> 8) & 0xFF;
        int b = baseColor & 0xFF;
        java.awt.Color.RGBtoHSB(r, g, b, hsv);

        boolean isDivine = tier != null && "DIVINE".equalsIgnoreCase(tier.name());
        boolean isCommon = tier != null && "COMMON".equalsIgnoreCase(tier.name());

        float currentX = 0;

        for (int i = 0; i < text.length(); i++) {
            String ch = String.valueOf(text.charAt(i));
            int charWidth = font.width(ch);

            int charColor;

            if (isDivine) {
                float divineTime = time * 0.12F;
                float hue = (divineTime + i * 0.04F) % 1.0F;
                float divinePulse = (float) Math.sin(time * 1.5F + i * 0.25F);
                float saturation = Math.max(0.0F, 0.06F + divinePulse * 0.10F);
                float brightness = 0.94F + (float) Math.cos(time * 1.2F + i * 0.2F) * 0.06F;

                int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
                charColor = 0xFF000000 | (rgb & 0xFFFFFF);

                graphics.drawString(font, ch, (int) (currentX + 1), 1, 0xAA000000, false);
                graphics.drawString(font, ch, (int) currentX, 0, charColor, false);

            } else if (isCommon) {
                float phase = time * 3.0F + i * 0.35F;
                float waveY = (float) Math.sin(time * 2.5F + i * 0.3F) * 0.8F;
                float brightness = 0.55F + (float) Math.sin(phase) * 0.45F;

                int rgb = java.awt.Color.HSBtoRGB(0.0F, 0.0F, brightness);
                charColor = 0xFF000000 | (rgb & 0xFFFFFF);

                graphics.drawString(font, ch, (int) (currentX + 1), (int) (waveY + 1), 0xDD000000, false);
                graphics.drawString(font, ch, (int) currentX, (int) waveY, charColor, false);

            } else {
                float phase = time * 4.0F + i * 0.45F;
                float waveY = (float) Math.sin(time * 3.5F + i * 0.3F) * 0.8F;

                float hueShift = hsv[0] + (float) Math.sin(phase) * 0.07F;
                float saturation = Math.max(0.75F, hsv[1]);
                float brightness = 0.85F + (float) Math.cos(phase) * 0.15F;

                int rgb = java.awt.Color.HSBtoRGB((hueShift + 1.0F) % 1.0F, saturation, brightness);
                charColor = 0xFF000000 | (rgb & 0xFFFFFF);

                graphics.drawString(font, ch, (int) (currentX + 1), (int) (waveY + 1), 0xDD000000, false);
                graphics.drawString(font, ch, (int) currentX, (int) waveY, charColor, false);
            }

            currentX += charWidth;
        }

        pose.popPose();
    }
}