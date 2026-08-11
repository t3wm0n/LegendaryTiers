package com.example.legendarytiers;

import com.example.legendarytiers.ModAttributes;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public class ModBusEvents {

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.CRIT_CHANCE);
        event.add(EntityType.PLAYER, ModAttributes.CRIT_DAMAGE);
        event.add(EntityType.PLAYER, ModAttributes.BOW_DRAW_SPEED);
        event.add(EntityType.PLAYER, ModAttributes.ARROW_DAMAGE);
    }
}