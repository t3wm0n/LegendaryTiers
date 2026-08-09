package com.example.legendarytiers;

import com.example.legendarytiers.screen.RunicTableScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID, value = Dist.CLIENT)
public class LegendaryTiersClient {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.RUNIC_TABLE.get(), RunicTableScreen::new);

    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Регистрируем встроенную генерацию меню конфигурации NeoForge
            net.neoforged.fml.ModList.get()
                    .getModContainerById(LegendaryTiers.MOD_ID)
                    .ifPresent(container -> container.registerExtensionPoint(
                            IConfigScreenFactory.class,
                            ConfigurationScreen::new
                    ));
        });
    }


}