package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipTextures;
import com.example.legendarytiers.client.tooltip.TooltipTheme;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Random;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ParticleRenderer {

    // В атласе 64x16 помещается ровно 4 текстуры по 16x16
    private static final int TEXTURE_COUNT = 4;
    private static final int PARTICLE_UV_SIZE = 16;

    private ParticleRenderer() {}

    public static void renderParticles(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            TooltipTheme theme
    ) {
        int particleCount = theme.particleCount();
        if (particleCount <= 0) return;

        long time = System.currentTimeMillis();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int color = theme.particleColor();
        float baseR = ((color >> 16) & 0xFF) / 255.0F;
        float baseG = ((color >> 8) & 0xFF) / 255.0F;
        float baseB = (color & 0xFF) / 255.0F;

        int padding = 8;
        int innerX = x + padding;
        int innerY = y + padding;
        int innerWidth = Math.max(1, width - padding * 2);
        int innerHeight = Math.max(1, height - padding * 2);

        for (int i = 0; i < particleCount; i++) {
            // Зафиксированный Random для конкретной частицы i
            Random rand = new Random(i * 1337L);

            // 1. ВЫБИРАЕМ ТИП ТЕКСТУРЫ (0, 1, 2 или 3)
            int textureIndex = rand.nextInt(TEXTURE_COUNT);
            int uOffset = textureIndex * PARTICLE_UV_SIZE; // Сдвиг по U (0, 16, 32 или 48)

            // 2. РАСЧЕТ ТРАЕКТОРИИ И ПОЗИЦИИ
            float startX = rand.nextFloat() * innerWidth;
            float startY = rand.nextFloat() * innerHeight;

            float speedX = (rand.nextFloat() - 0.5F) * theme.particleSpeed();
            float speedY = -0.3F - (rand.nextFloat() * theme.particleSpeed());

            long lifetime = 2000L + (long)(rand.nextFloat() * 2000L);
            long offsetTime = rand.nextInt(10000);

            float progress = ((time + offsetTime) % lifetime) / (float) lifetime;

            float currentX = innerX + ((startX + (speedX * progress * 100)) % innerWidth);
            if (currentX < innerX) currentX += innerWidth;

            float currentY = innerY + ((startY + (speedY * progress * 100)) % innerHeight);
            if (currentY < innerY) currentY += innerHeight;

            // 3. ПЛАВНЫЙ FADE IN / FADE OUT
            float alpha;
            if (progress < 0.2F) {
                alpha = progress / 0.2F;
            } else if (progress > 0.8F) {
                alpha = (1.0F - progress) / 0.2F;
            } else {
                alpha = 1.0F;
            }

            float finalAlpha = alpha * 0.75F;
            float size = theme.particleSize() * (0.8F + rand.nextFloat() * 0.4F);
            int pSize = Math.max(2, (int) size);

            RenderSystem.setShaderColor(baseR, baseG, baseB, finalAlpha);

            // 4. РЕНДЕР С УЧЕТОМ СДВИГА U В ВАШЕМ АТЛАСЕ 64x16
            graphics.blit(
                    TooltipTextures.PARTICLES,   // Ваша текстура 64x16
                    (int) currentX,
                    (int) currentY,
                    pSize, pSize,                // Экранные размеры (например, 3x3, 4x4 px)
                    uOffset, 0,                  // U = (0, 16, 32, 48), V = 0
                    PARTICLE_UV_SIZE, PARTICLE_UV_SIZE, // UV ширина и высота = 16x16
                    64, 16                       // Общие размеры файла PNG (64x16)
            );
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}