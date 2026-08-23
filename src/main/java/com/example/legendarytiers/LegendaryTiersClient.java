package com.example.legendarytiers;

import com.example.legendarytiers.client.tooltip.LegendaryClientTooltipComponent;
import com.example.legendarytiers.client.tooltip.LegendaryTooltipData;
import com.example.legendarytiers.screen.RunicTableScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@OnlyIn(Dist.CLIENT)
public class LegendaryTiersClient {

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.RUNIC_TABLE.get(), RunicTableScreen::new);
    }

    public static void registerTooltipFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(
                LegendaryTooltipData.class,
                data -> new LegendaryClientTooltipComponent(data.context())
        );
    }

}