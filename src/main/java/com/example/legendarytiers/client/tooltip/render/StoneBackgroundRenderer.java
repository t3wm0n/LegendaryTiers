package com.example.legendarytiers.client.tooltip.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.example.legendarytiers.client.tooltip.TooltipTextures.RARITY_STONE;
import static com.example.legendarytiers.client.tooltip.TooltipTextures.RARITY_STONE_GLOW;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StoneBackgroundRenderer {

    public static void renderGlowingStone(
            GuiGraphics graphics,
            float x,
            float y,
            float width,
            float height,
            int themeColor // RGB цвет тира (например, 0xFF55FF)
    ) {
        // 1. Отрисовка базовой текстуры камня (Обычный рендер)
        graphics.blit(
                RARITY_STONE,
                (int) x, (int) y,
                0, 0,
                (int) width, (int) height,
                (int) width, (int) height
        );

        // 2. Вычисление пульсации свечения (Плавный синус от 0.5f до 1.0f)
        float time = (System.currentTimeMillis() % 1_000_000L) / 1000.0F;
        float pulse = 0.75F + Mth.sin(time * 3.0F) * 0.25F; // Скорость 3.0

        // Извлекаем R, G, B из цвета темы
        float r = ((themeColor >> 16) & 0xFF) / 255.0F;
        float g = ((themeColor >> 8) & 0xFF) / 255.0F;
        float b = (themeColor & 0xFF) / 255.0F;

        // 3. Отрисовка Glow-маски с покраской и прозрачностью
        RenderSystem.enableBlend();
        // Включаем аддитивное смешивание для эффекта "свечения изнутри"
        RenderSystem.defaultBlendFunc();

        // Окрашиваем маску в цвет тира + применяем пульсацию в Alpha
        graphics.setColor(r, g, b, pulse);

        // Рисуем маску поверх камня
        graphics.blit(
                RARITY_STONE_GLOW,
                (int) x, (int) y,
                0, 0,
                (int) width, (int) height,
                (int) width, (int) height
        );

        // Сбрасываем цвет graphics к дефолтному (белый, альфа 1.0), чтобы не испортить рендер текста!
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}