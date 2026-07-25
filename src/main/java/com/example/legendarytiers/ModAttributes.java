package com.example.legendarytiers;

import com.example.legendarytiers.LegendaryTiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, LegendaryTiers.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> BOW_DRAW_SPEED =
            ATTRIBUTES.register("generic.bow_draw_speed",
                    () -> new RangedAttribute(
                            "attribute.name.generic.bow_draw_speed",
                            1.0,
                            0.1,
                            2.0
                    ).setSyncable(true));

    public static final DeferredHolder<Attribute, Attribute> ARROW_DAMAGE =
            ATTRIBUTES.register("generic.arrow_damage",
                    () -> new RangedAttribute(
                            "attribute.name.generic.arrow_damage",
                            1.0,
                            0.5,
                            3.0
                    ).setSyncable(true));

    // Шанс критического удара (0.15 = 15%)
    public static final DeferredHolder<Attribute, Attribute> CRIT_CHANCE = ATTRIBUTES.register(
            "generic.crit_chance",
            () -> new RangedAttribute(
                    "attribute.name.generic.crit_chance",
                    0.05,
                    0.0,
                    0.95
            ).setSyncable(true)
    );

    // Множитель критического удара (1.5 = x1.5)
    public static final DeferredHolder<Attribute, Attribute> CRIT_DAMAGE = ATTRIBUTES.register(
            "generic.crit_damage",
            () -> new RangedAttribute(
                    "attribute.name.generic.crit_damage",
                    0.5,
                    0.0,
                    9.0
            ).setSyncable(true)
    );

    public static void register(IEventBus bus) {
        ATTRIBUTES.register(bus);
    }
}