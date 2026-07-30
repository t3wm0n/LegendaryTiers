package com.example.legendarytiers.client.tooltip;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;

public final class AttributeFormatRegistry {

    private static final Map<ResourceLocation, AttributeFormat> FORMATS = new HashMap<>();

    static {

        /*
         * INTEGER
         */

        register("minecraft:generic.luck", AttributeFormat.INTEGER);
        register("minecraft:generic.max_health", AttributeFormat.INTEGER);
        register("minecraft:generic.armor_toughness", AttributeFormat.INTEGER);

        /*
         * DECIMAL
         */

        register("minecraft:generic.attack_damage", AttributeFormat.DECIMAL);
        register("minecraft:generic.attack_speed", AttributeFormat.DECIMAL);
        register("minecraft:generic.armor", AttributeFormat.DECIMAL);
        register("minecraft:generic.knockback_resistance", AttributeFormat.DECIMAL);

        register("minecraft:player.block_interaction_range", AttributeFormat.DECIMAL);
        register("minecraft:player.entity_interaction_range", AttributeFormat.DECIMAL);

        /*
         * PERCENT
         */

        register("minecraft:generic.movement_speed", AttributeFormat.PERCENT);
        register("minecraft:player.block_break_speed", AttributeFormat.PERCENT);
        register("minecraft:generic.jump_strength", AttributeFormat.PERCENT);
        register("minecraft:generic.gravity", AttributeFormat.PERCENT);

        register("legendarytiers:generic.crit_chance", AttributeFormat.PERCENT);
        register("legendarytiers:generic.crit_damage", AttributeFormat.PERCENT);
        register("legendarytiers:generic.bow_draw_speed", AttributeFormat.PERCENT);

        /*
         * Mixin attributes
         */

        register("legendarytiers:generic.durability", AttributeFormat.PERCENT);
    }

    private AttributeFormatRegistry() {
    }

    private static void register(
            String id,
            AttributeFormat format
    ) {

        FORMATS.put(
                ResourceLocation.parse(id),
                format
        );

    }

    public static AttributeFormat get(
            Attribute attribute
    ) {

        ResourceLocation id =
                BuiltInRegistries.ATTRIBUTE.getKey(attribute);

        return FORMATS.getOrDefault(
                id,
                AttributeFormat.DECIMAL
        );

    }

    public static AttributeFormat get(String attributeId) {

        return FORMATS.getOrDefault(
                ResourceLocation.parse(attributeId),
                AttributeFormat.DECIMAL
        );

    }

}