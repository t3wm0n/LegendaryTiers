package com.example.legendarytiers.event;

import com.example.legendarytiers.*;
import com.example.legendarytiers.util.ExperienceUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.awt.*;

import static com.example.legendarytiers.util.ExperienceUtil.BASE_EXPERIENCE;

@EventBusSubscriber(value = Dist.CLIENT, modid = LegendaryTiers.MOD_ID)
public class ModClientEvents {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        //Тир
        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier != null) {
            Rarity rarity = tier.rarity();
            if (rarity == Rarity.DIVINE) {

                float time = (System.currentTimeMillis() % 1_000_000L) / 1000.0F;
                float hueStart = (time * 0.3F) % 1.0F;
                float hueEnd = (hueStart + 0.15F) % 1.0F;
                int rgbStart = Color.HSBtoRGB(hueStart, 0.55F, 1.0F) & 0x00FFFFFF;
                int rgbEnd = Color.HSBtoRGB(hueEnd, 0.55F, 1.0F) & 0x00FFFFFF;

                String localizedName = Component.translatable(rarity.getTranslationKey()).getString();
                Component rainbowText = createRainbowComponent(localizedName, 0.3F, 0.55F);

                event.getToolTip().add(rainbowText);
            } else {
                // Для обычных редкостей используем стандартное имя
                event.getToolTip().add(rarity.getDisplayName());
            }

            // Редкость
            double quality = tier.quality();
            if (quality >= 0) {
                int stars = (int) Math.round(quality * 5);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 5; i++) sb.append(i < stars ? "★" : "☆");
                event.getToolTip().add(Component.literal(sb.toString()).withStyle(ChatFormatting.GOLD));
            }
            // Бонусы
            for (ModifierEntry entry : tier.modifiers()) {
                if (entry.target().equals("durability")) {
                    double val = entry.value();
                    String sign = val >= 0 ? "+" : "";
                    int percent = (int) Math.round(val * 100);
                    String resTip = sign + percent;
                    event.getToolTip().add(Component.translatable("attribute.name.generic.durability", resTip).withStyle(ChatFormatting.GRAY));
                    break;
                }
            }
        }

        if (stack.is(ModItems.BROKEN_ITEM.get())) {
            event.getToolTip().add(Component.translatable("tooltip.legendarytiers.broken").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
            ResourceLocation originalId = stack.get(ModDataComponents.ORIGINAL_ITEM_ID);
            if (originalId != null) {
                Item originalItem = BuiltInRegistries.ITEM.get(originalId);
                if (originalItem != null) {
                    String originalName = originalItem.getDescription().getString();
                    event.getToolTip().add(Component.translatable("tooltip.legendarytiers.was").append(Component.literal(originalName)).withStyle(ChatFormatting.GRAY));
                    int repairAmount = 2;
                    event.getToolTip().add(Component.translatable("tooltip.legendarytiers.repair", repairAmount).withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_GRAY));
                }
            }
        }

        // Опыт и уровень
        Integer exp = stack.get(ModDataComponents.EXPERIENCE);
        if (exp != null) {
            int level = ExperienceUtil.getLevel(exp);
            int currentLevelExp = ExperienceUtil.getCurrentLevelExperience(exp);

            // Динамически получаем требуемый опыт для ТЕКУЩЕГО уровня
            int nextLevelExp = ExperienceUtil.getExperienceToNextLevel(level);

            // Считаем процент прогресса от 0.0 до 100.0%
            double progressPercent = (currentLevelExp / (double) nextLevelExp) * 100.0;

            // Locale.ROOT гарантирует, что разделителем всегда будет точка (например, "45.0%", а не "45,0%")
            String progressStr = String.format(java.util.Locale.ROOT, "%.1f", progressPercent);

            Component levelText = Component.translatable("tooltip.legendarytiers.level", level)
                    .withStyle(ChatFormatting.DARK_AQUA);
            Component expText = Component.translatable("tooltip.legendarytiers.exp", currentLevelExp, nextLevelExp, progressStr)
                    .withStyle(ChatFormatting.DARK_AQUA);

            event.getToolTip().add(levelText);
            event.getToolTip().add(expText);
        }

        Integer attempts = stack.get(ModDataComponents.REFORGE_ATTEMPTS);
        if (attempts != null && attempts > 0) {
            int remaining = Math.max(0, 3 - attempts);
            ChatFormatting color = remaining > 0 ? ChatFormatting.YELLOW : ChatFormatting.RED;
            event.getToolTip().add(Component.translatable("tooltip.legendarytiers.reforge_attempts",remaining, 3).withStyle(color));
        }
    }

    public static Component createRainbowComponent(String text, float speed, float saturation) {
        MutableComponent result = Component.empty();
        float time = (System.currentTimeMillis() % 1_000_000L) / 1000.0F;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Расчёт HSB цвета для символа
            float hue = (time * speed + (i * 0.04F)) % 1.0F;
            int rgb = java.awt.Color.HSBtoRGB(hue, saturation, 1.0F) & 0x00FFFFFF;

            // Создаем символ со своим цветом
            Component charComponent = Component.literal(String.valueOf(c))
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb)));

            result.append(charComponent);
        }

        return result;
    }
}
