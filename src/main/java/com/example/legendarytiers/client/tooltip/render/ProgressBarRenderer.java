package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public final class ProgressBarRenderer {

    private ProgressBarRenderer() {
    }

    public static void draw(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            float progress,
            TooltipTheme theme
    ) {
        progress = Mth.clamp(progress, 0.0F, 1.0F);

        float SLANT_FACTOR = 1.5F; // 1.0F = 45°, 2.0F = пологий наклон, 0.5F = почти вертикальный

        int fillWidth = (int) ((width - 2) * progress);

        RenderSystem.enableBlend();

        /*
         * 1. ФОН И РАМКА
         */
        // Тёмный подстилающий фон полосы
        graphics.fill(x, y, x + width, y + height, 0xF00A0A0C);

        // Рамка цвета темы
        graphics.renderOutline(x, y, width, height, theme.borderColor());

        // Внутренний темный объем фона
        graphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 0xAA111115);

        if (fillWidth > 0) {
            int innerX = x + 1;
            int innerY = y + 1;
            int innerH = height - 2;

            /*
             * 2. ОСНОВНАЯ ЗАЛИВКА И ДИАГОНАЛЬНЫЙ СРЕЗ (\)
             */
            for (int py = 0; py < innerH; py++) {
                // Сдвиг по X увеличивается с вышиной py -> скос идет вправо-вниз (\)
                int slantOffset = (int) (py * SLANT_FACTOR);
                int currentLineWidth = Math.min(fillWidth, fillWidth - slantOffset);

                if (currentLineWidth > 0) {
                    graphics.fill(
                            innerX,
                            innerY + py,
                            innerX + currentLineWidth,
                            innerY + py + 1,
                            theme.backgroundLight()
                    );
                }
            }

            /*
             * 3. ТЕКСТУРНЫЕ ДИАГОНАЛЬНЫЕ ПОЛОСЫ (\)
             */
            for (int px = 0; px < fillWidth; px += 6) {
                int stripeX = innerX + px;
                int stripeW = Math.min(2, fillWidth - px);

                for (int py = 0; py < innerH; py++) {
                    int slantedX = stripeX + (int) (py * SLANT_FACTOR);; // Плюс py для согласования наклона
                    if (slantedX >= innerX && slantedX < innerX + fillWidth) {
                        graphics.fill(slantedX, innerY + py, slantedX + stripeW, innerY + py + 1, 0x22000000);
                    }
                }
            }

            /*
             * 4. БЕГУЩИЙ БЛИК (Исправлено: обрезается строго по скосу правого края)
             */
            float time = (System.currentTimeMillis() % 10_000L) / 1000.0F;
            float shineProgress = (time * 0.4F) % 1.5F; // От 0.0 до 1.5

            if (shineProgress <= 1.0F) {
                int shineX = innerX + (int) (fillWidth * shineProgress);
                int shineWidth = 8; // Ширина полосы блика

                for (int py = 0; py < innerH; py++) {
                    int slantOffset = (int) (py * SLANT_FACTOR);

                    // Правый край закрашенной линии на ТЕКУЩЕЙ строке py с учётом скоса
                    int maxAllowedRightForLine = innerX + fillWidth - slantOffset;

                    // Положение блика на текущей строке
                    int slantingShineX = shineX - slantOffset;

                    // Рисуем блик только в пределах [innerX ... maxAllowedRightForLine]
                    int drawLeft = Math.max(innerX, slantingShineX);
                    int drawRight = Math.min(maxAllowedRightForLine, slantingShineX + shineWidth);

                    // Дополнительная защита: блик не рисуется за пределами реальной длины
                    if (drawRight > drawLeft && drawLeft < maxAllowedRightForLine) {
                        graphics.fill(drawLeft, innerY + py, drawRight, innerY + py + 1, 0x55FFFFFF);
                    }
                }
            }

            /*
             * 5. ВЕРХНЯЯ ПОДСВЕТКА И НИЖНЯЯ ТЕНЬ
             */
            // Верхний горизонтальный блик
            graphics.fill(innerX, innerY, innerX + fillWidth, innerY + 1, 0x66FFFFFF);

            // Нижняя тень
            graphics.fill(innerX, innerY + innerH - 1, innerX + fillWidth, innerY + innerH, 0x55000000);

            /*
             * 6. ЯРКИЙ НАКОНЕЧНИК (Строго в границах скошенной линии)
             */
            float pulse = 0.7F + Mth.sin(time * 6.0F) * 0.3F;
            int glowAlpha = (int) (255 * pulse) << 24;
            int tipGlowColor = glowAlpha | (theme.borderGlow() & 0x00FFFFFF);

            for (int py = 0; py < innerH; py++) {
                int slantOffset = (int) (py * SLANT_FACTOR);

                // Точный край текущей строки пикселей с учётом скоса
                int currentLineRight = innerX + fillWidth - slantOffset;

                // Рисуем огонек только если текущая строка вообще существует (не ушла в минус)
                if (currentLineRight > innerX) {
                    // Ограничиваем координаты отрисовки, чтобы они не выходили за правый край текущей строки
                    int drawStartX = Math.max(innerX, currentLineRight - 1);
                    int drawEndX = Math.min(innerX + fillWidth, currentLineRight + 1);

                    if (drawEndX > drawStartX) {
                        graphics.fill(drawStartX, innerY + py, drawEndX, innerY + py + 1, tipGlowColor);
                    }
                }
            }
        }

        RenderSystem.disableBlend();
    }
}