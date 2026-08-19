package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class QualitySection {

    private static final int STAR_SPACING = 2;
    public static final int STAR_SIZE = 15;
    private static final int DEFAULT_GLOW_COLOR = 0xFFFFD700; // Золотое свечение по умолчанию

    private QualitySection() {
    }

    public static int getHeight() {
        return TooltipLayout.QUALITY_HEIGHT;
    }

    // --- Перегрузка с передачей темы ---
    public static void render(
            GuiGraphics graphics,
            Font font,
            double quality,
            int x,
            int y,
            int width,
            TooltipTheme theme
    ) {
        render(graphics, font, quality, x, y, width, theme != null ? theme.borderColor() : DEFAULT_GLOW_COLOR);
    }

    // --- Базовый метод для обратной совместимости ---
    public static void render(
            GuiGraphics graphics,
            Font font,
            double quality,
            int x,
            int y,
            int width
    ) {
        render(graphics, font, quality, x, y, width, DEFAULT_GLOW_COLOR);
    }

    // --- Основной рендер с поддержкой ореола и блика ---
    public static void render(
            GuiGraphics graphics,
            Font font,
            double quality,
            int x,
            int y,
            int width,
            int glowColor
    ) {
        if (quality <= 0) return;

        double stars = quality * 5.0;
        int filledStars = (int) stars;
        double remainder = stars - filledStars;

        boolean halfStar = remainder >= 0.25 && remainder < 0.75;
        if (remainder >= 0.75) {
            filledStars++;
            halfStar = false;
        }

        int totalWidth = 5 * STAR_SIZE + 4 * STAR_SPACING;
        // Если width > 0, центрируем внутри width, иначе стартуем прямо с x
        int startX = x;
        int starY = y + 3;

        float timeSec = (System.currentTimeMillis() % 1_000_000L) / 1000.0F;

        // Распаковка ARGB для свечения
        float r = ((glowColor >> 16) & 0xFF) / 255.0F;
        float g = ((glowColor >> 8) & 0xFF) / 255.0F;
        float b = (glowColor & 0xFF) / 255.0F;

        var pose = graphics.pose();

        for (int i = 0; i < 5; i++) {
            int iconX;
            int iconY;
            boolean isActive = false;

            if (i < filledStars) {
                iconX = TooltipIcons.STAR_FILLED_X;
                iconY = TooltipIcons.STAR_FILLED_Y;
                isActive = true;
            } else if (i == filledStars && halfStar) {
                iconX = TooltipIcons.STAR_HALF_X;
                iconY = TooltipIcons.STAR_HALF_Y;
                isActive = true;
            } else {
                iconX = TooltipIcons.STAR_EMPTY_X;
                iconY = TooltipIcons.STAR_EMPTY_Y;
            }

            int currentStarX = startX + i * (STAR_SIZE + STAR_SPACING);

            // 1. АДДИТИВНЫЙ ОРЕОЛ (GLOW) — рисуем только для активных/полузаполненных звезд
            if (isActive) {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

                pose.pushPose();
                float centerX = currentStarX + STAR_SIZE / 2.0F;
                float centerY = starY + STAR_SIZE / 2.0F;

                // Масштабируем от центра звезды
                pose.translate(centerX, centerY, 0.0F);

                // Плавная фазовая пульсация (каждая звезда чуть-чуть смещена по времени)
                double sin = Math.sin(timeSec * 3.0F + i * 0.4F);
                float pulse = (float) (sin * 0.1F + 1.2F);
                pose.scale(pulse, pulse, 1.0F);

                pose.translate(-centerX, -centerY, 0.0F);

                float glowAlpha = (float) (sin * 0.15F + 0.40F);
                RenderSystem.setShaderColor(r, g, b, glowAlpha);

                IconRenderer.draw(graphics, currentStarX, starY, iconX, iconY, STAR_SIZE);

                pose.popPose();
                RenderSystem.defaultBlendFunc();
            }

            // 2. ОСНОВНАЯ ТЕКСТУРА ЗВЕЗДЫ
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            IconRenderer.draw(graphics, currentStarX, starY, iconX, iconY, STAR_SIZE);

            // 3. ПРОБЕГАЮЩИЙ СВЕТОВОЙ БЛИК (SHINE/GLINT)
            if (isActive) {
                // Волна блика с небольшой задержкой от первой звезды к последней
                float shineCycle = (timeSec * 0.5F - i * 0.08F) % 1.5F;

                if (shineCycle >= 0.0F && shineCycle < 0.25F) {
                    float progress = shineCycle / 0.25F;
                    float shineAlpha = (float) Math.sin(progress * Math.PI) * 0.65F;

                    RenderSystem.enableBlend();
                    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, shineAlpha);

                    // Отрисовываем поверх с белой подсветкой
                    IconRenderer.draw(graphics, currentStarX, starY, iconX, iconY, STAR_SIZE);

                    RenderSystem.defaultBlendFunc();
                }
            }

            // Сброс цвета шейдера
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}