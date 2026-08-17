package com.example.legendarytiers.event;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.util.TierAttributeHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public class BowEventHandler {

    @SubscribeEvent
    public static void onArrowLoose(ArrowLooseEvent event) {
        // Достаём натягиваемый лук
        var stack = event.getBow();

        // Считываем твой атрибут скорости натяжения
        double speed = TierAttributeHelper.getAttribute(
                stack,
                "legendarytiers:generic.bow_draw_speed",
                1.0
        );

        if (speed != 1.0) {
            // Умножаем charge (время натяжения) на множитель скорости
            int originalCharge = event.getCharge();
            int modifiedCharge = (int) Math.round(originalCharge * speed);

            // Устанавливаем новое значение charge для NeoForge!
            event.setCharge(modifiedCharge);
        }
    }
}