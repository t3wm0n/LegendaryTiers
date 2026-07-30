package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.ModifierEntry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import com.example.legendarytiers.client.tooltip.TooltipAttributeCalculator;

import java.util.*;

public final class TooltipAttributeCollector {

    private TooltipAttributeCollector() {
    }

    public static final class Accumulator {

        public double baseValue;
        public double addValue;
        public double multipliedBase;
        public double multipliedTotal;
        public double bonusValue;

    }

    public static List<TooltipAttributeEntry> collect(
            ItemStack stack,
            LocalPlayer player,
            List<ModifierEntry> tierModifiers
    ) {

        Map<Holder<Attribute>, Accumulator> values =
                new LinkedHashMap<>();

        ItemAttributeModifiers modifiers =
                stack.getAttributeModifiers();

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {

            Holder<Attribute> attribute = entry.attribute();

            AttributeModifier modifier = entry.modifier();

            Accumulator accumulator =
                    values.computeIfAbsent(
                            attribute,
                            a -> new Accumulator()
                    );

            switch (modifier.operation()) {

                case ADD_VALUE -> {

                    if (player != null) {

                        if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {

                            accumulator.baseValue =
                                    player.getAttributeBaseValue(
                                            net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE
                                    );

                        } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {

                            accumulator.baseValue =
                                    player.getAttributeBaseValue(
                                            net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED
                                    );

                        }

                    }

                    accumulator.addValue += modifier.amount();
                }

                case ADD_MULTIPLIED_BASE ->
                        accumulator.multipliedBase += modifier.amount();

                case ADD_MULTIPLIED_TOTAL ->
                        accumulator.multipliedTotal += modifier.amount();
            }

        }

        if (tierModifiers != null) {

            for (ModifierEntry modifier : tierModifiers) {

                Optional<String> id = modifier.attribute();

                if (id.isEmpty()) {
                    continue;
                }

                values.forEach((holder, accumulator) -> {

                    String attributeId =
                            holder.unwrapKey()
                                    .map(key -> key.location().toString())
                                    .orElse("");
                    if (attributeId.equals(id.get())) {

                        accumulator.bonusValue = modifier.value();

                    }

                });

            }

        }

        List<TooltipAttributeEntry> result =
                new ArrayList<>();

        values.forEach((attribute, accumulator) -> {

            double finalValue =
                    TooltipAttributeCalculator.calculate(accumulator);

            if (attribute.is(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE)) {

                finalValue *= 10.0;
            }

            if (Math.abs(finalValue) > 0.0001) {

                result.add(

                        new TooltipAttributeEntry(

                                attribute,

                                attribute.value().getDescriptionId(),

                                finalValue,

                                accumulator.bonusValue

                        )

                );

            }

        });
        result.sort(
                Comparator.comparingInt(
                        entry -> priority(entry.descriptionId())
                )
        );
        return result;
    }

    private static int priority(String id) {

        return switch (id) {

            case "attribute.name.attack_damage" -> 0;

            case "attribute.name.attack_speed" -> 1;

            case "attribute.name.generic.armor" -> 2;

            case "attribute.name.generic.armor_toughness" -> 3;

            case "attribute.name.generic.max_health" -> 4;

            case "attribute.name.generic.knockback_resistance" -> 5;

            case "attribute.name.generic.luck" -> 6;

            default -> 100;
        };
    }

}