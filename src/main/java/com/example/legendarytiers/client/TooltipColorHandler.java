package com.example.legendarytiers.client;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.ModDataComponents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

@EventBusSubscriber(
        modid = LegendaryTiers.MOD_ID,
        value = Dist.CLIENT
)
public final class TooltipColorHandler {

    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {

        if (!event.getItemStack().has(ModDataComponents.TIER_DATA))
            return;

        event.setBackgroundStart(0x00000000);
        event.setBackgroundEnd(0x00000000);
        event.setBorderStart(0x00000000);
        event.setBorderEnd(0x00000000);

    }

}