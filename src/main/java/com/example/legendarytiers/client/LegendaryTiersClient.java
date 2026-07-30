package com.example.legendarytiers.client;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.client.tooltip.LegendaryClientTooltipComponent;
import com.example.legendarytiers.client.tooltip.LegendaryTooltipData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public final class LegendaryTiersClient {

    private LegendaryTiersClient() {
    }

    @SubscribeEvent
    public static void registerTooltipFactories(RegisterClientTooltipComponentFactoriesEvent event) {

        event.register(
                LegendaryTooltipData.class,
                data -> new LegendaryClientTooltipComponent(data.context())
        );

    }

}