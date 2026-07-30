package com.example.legendarytiers;

import com.example.legendarytiers.screen.RunicTableScreen;
import com.example.legendarytiers.util.ExperienceUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import static com.example.legendarytiers.util.ExperienceUtil.EXPERIENCE_PER_LEVEL;

@EventBusSubscriber(value = Dist.CLIENT, modid = LegendaryTiers.MOD_ID)
public class ModClientEvents {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        // Редкость
        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier != null) {
            Rarity rarity = tier.rarity();
            event.getToolTip().add(rarity.getDisplayName());
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
            int nextLevelExp = EXPERIENCE_PER_LEVEL;
            double progressPercent = (currentLevelExp / (double) nextLevelExp) * 100.0;
            String progressStr = String.format("%.1f", progressPercent);

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
            event.getToolTip().add(Component.translatable("tooltip.legendarytiers.reforge_attempts",remaining).withStyle(color));
        }
    }
}
