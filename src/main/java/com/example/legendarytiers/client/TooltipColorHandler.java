package com.example.legendarytiers.client;

import com.example.legendarytiers.*;
import com.example.legendarytiers.config.RPGITConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import java.awt.*;

@EventBusSubscriber(
        modid = LegendaryTiers.MOD_ID,
        value = Dist.CLIENT
)
public final class TooltipColorHandler {

    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {

        if (RPGITConfig.INSTANCE.client.enable_custom_tooltips) {
            ItemStack stack = event.getItemStack();
            if (stack.isEmpty() || !stack.has(ModDataComponents.TIER_DATA)) {
                return;
            }
            event.setBackgroundStart(0x00000000);
            event.setBackgroundEnd(0x00000000);
            event.setBorderStart(0x00000000);
            event.setBorderEnd(0x00000000);

        }
        else {
            ItemStack stack = event.getItemStack();
            if (stack.isEmpty() || !stack.has(ModDataComponents.TIER_DATA)) {
                return;
            }

            var tierData = stack.get(ModDataComponents.TIER_DATA);
            Rarity rarity = tierData.rarity();

            int borderStart;
            int borderEnd;

            // ---------- ОСОБАЯ ЛОГИКА ДЛЯ DIVINE (РАДУГА) ----------
            if (rarity == Rarity.DIVINE) {
                // Время в секундах
                float time = (System.currentTimeMillis() % 1_000_000L) / 1000.0F;

                // 1. Рассчитываем оттенок (Hue) от 0.0 до 1.0 (скорость вращения 0.3)
                float hueStart = (time * 0.3F) % 1.0F;

                // 2. Для нижней границы рамки делаем небольшой сдвиг по фазе (+0.15),
                // чтобы радуга красиво переливалась сверху вниз!
                float hueEnd = (hueStart + 0.15F) % 1.0F;

                // Saturation = 0.6f (чтобы была пастельная/бело-радужная, а не ядовито-кислотная)
                // Brightness = 1.0f (максимальная яркость)
                int rgbStart = Color.HSBtoRGB(hueStart, 0.55F, 1.0F) & 0x00FFFFFF;
                int rgbEnd = Color.HSBtoRGB(hueEnd, 0.55F, 1.0F) & 0x00FFFFFF;

                borderStart = 0xFF000000 | rgbStart;
                borderEnd = 0xFF000000 | rgbEnd;
            }
            // ---------- ДЛЯ ВСЕХ ОСТАЛЬНЫХ РЕДКОСТЕЙ ----------
            else {
                ChatFormatting formatting = rarity.getColor();
                Integer rgbColor = formatting.getColor();
                if (rgbColor == null) return;

                borderStart = 0xFF000000 | rgbColor;
                borderEnd = (borderStart & 0x00FFFFFF) | 0xAA000000;
            }

            // Применяем тёмный графитовый фон и наши цвета
            event.setBackground(0xF00D0D12);
            event.setBorderStart(borderStart);
            event.setBorderEnd(borderEnd);
        }
    }

}