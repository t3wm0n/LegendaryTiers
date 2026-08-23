package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.ModDataComponents;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import com.example.legendarytiers.config.RPGITConfig;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = LegendaryTiers.MOD_ID, value = Dist.CLIENT)
public final class LegendaryTooltipHandler {

    private LegendaryTooltipHandler() {
    }

    @SubscribeEvent
    public static void gatherComponents(RenderTooltipEvent.GatherComponents event) {

        if (!RPGITConfig.INSTANCE.client.enable_custom_tooltips) {
            return;
        }

        ItemStack stack = event.getItemStack();

        if (stack.isEmpty()) {
            return;
        }

        if (!stack.has(ModDataComponents.TIER_DATA)) {
            return;
        }

        LegendaryTooltipContext context = LegendaryTooltipBuilder.build(stack);

        if (context == null) {
            return;
        }

        event.getTooltipElements().clear();

        event.getTooltipElements().add(
                Either.right(new LegendaryTooltipData(context))
        );

        event.setMaxWidth(context.tooltipWidth());
    }
}