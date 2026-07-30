package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.ModDataComponents;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public final class LegendaryTooltipHandler {

    private LegendaryTooltipHandler() {
    }

    @SubscribeEvent
    public static void gatherComponents(RenderTooltipEvent.GatherComponents event) {

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