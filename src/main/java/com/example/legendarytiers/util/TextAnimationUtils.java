package com.example.legendarytiers.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class TextAnimationUtils {

    private TextAnimationUtils() {}

    /**
     * Волнистый текст с возможностью управления амлитудой и скоростью.
     */
    public static void drawWavyString(
            GuiGraphics graphics,
            Font font,
            String text,
            float startX,
            float startY,
            float textScale,
            int color,
            boolean shadow,
            float waveSpeed,
            float waveHeight,
            float waveFrequency
    ) {
        PoseStack pose = graphics.pose();
        float time = getAnimationTime();
        float x = startX;

        for (int i = 0; i < text.length(); i++) {
            String c = String.valueOf(text.charAt(i));

            float offsetY = (float) Math.sin((time * waveSpeed) + (i * waveFrequency)) * waveHeight;

            pose.pushPose();
            pose.translate(x * textScale, (startY + offsetY) * textScale, 0.0F);
            pose.scale(textScale, textScale, 1.0F);

            font.drawInBatch(
                    c,
                    0.0F,
                    0.0F,
                    color,
                    shadow,
                    pose.last().pose(),
                    graphics.bufferSource(),
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880
            );

            pose.popPose();

            x += font.width(c);
        }

        graphics.bufferSource().endBatch();
    }

    /**
     * Отрисовывает текст названия предмета с эффектом тёмного фэнтези-глитча.
     * Буквы статичны, но периодически случайно подрагивают и вспыхивают чистым белым светом.
     */
    public static void drawGlitchString(
            GuiGraphics graphics,
            Font font,
            String text,
            float x,
            float y,
            float scale,
            int baseColor
    ) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0.0F);
        pose.scale(scale, scale, 1.0F);

        long timeMs = System.currentTimeMillis();
        float curX = 0;

        for (int i = 0; i < text.length(); i++) {
            String ch = String.valueOf(text.charAt(i));
            int charWidth = font.width(ch);

            // Частота глитча: проверка смены состояния каждые ~110 мс
            long frame = timeMs / 110L;
            boolean isGlitching = ((frame + i * 13L) % 37L) == 0;

            float offsetX = 0.0F;
            float offsetY = 0.0F;
            int charColor = baseColor;

            if (isGlitching) {
                // Легкий микро-сдвиг на 1px и вспышка белым цветом
                offsetX = (i % 2 == 0) ? 1.0F : -1.0F;
                offsetY = (i % 3 == 0) ? -0.5F : 0.5F;
                charColor = 0xFFFFFFFF; // Белоснежная вспышка
            }

            // 1. Тень под буквой
            graphics.drawString(
                    font,
                    ch,
                    (int) (curX + offsetX + 1.0F),
                    (int) (offsetY + 1.0F),
                    0xFF000000,
                    false
            );

            // 2. Основная буква
            graphics.drawString(
                    font,
                    ch,
                    (int) (curX + offsetX),
                    (int) offsetY,
                    charColor,
                    false
            );

            curX += charWidth;
        }

        pose.popPose();
    }

    private static float getAnimationTime() {
        return (System.currentTimeMillis() % 1_000_000L) / 1000.0F;
    }
}