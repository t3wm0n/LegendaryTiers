package com.example.legendarytiers.event;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.util.TierAttributeHelper;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public class CrossbowEventHandler {

    @SubscribeEvent
    public static void onCrossbowCharging(LivingEntityUseItemEvent.Tick event) {
        ItemStack stack = event.getItem();

        // Проверяем, что игрок использует именно Арбалет
        if (stack.getItem() instanceof CrossbowItem) {
            double speed = TierAttributeHelper.getAttribute(
                    stack,
                    "legendarytiers:generic.bow_draw_speed",
                    1.0
            );

            // Если есть бонус к скорости зарядки
            if (speed > 1.0) {
                // Вычисляем бонусные тики (на сколько ускоряем процесс за один тик)
                int bonusTicks = (int) Math.round(speed - 1.0);
                if (bonusTicks > 0) {
                    // Сокращаем оставшееся время использования на предмет
                    event.setDuration(event.getDuration() - bonusTicks);
                }
            }
        }
    }
}