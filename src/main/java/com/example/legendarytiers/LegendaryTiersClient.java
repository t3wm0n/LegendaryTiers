package com.example.legendarytiers;

import com.example.legendarytiers.screen.RunicTableScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID, value = Dist.CLIENT)
public class LegendaryTiersClient {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.RUNIC_TABLE.get(), RunicTableScreen::new);
    }
}