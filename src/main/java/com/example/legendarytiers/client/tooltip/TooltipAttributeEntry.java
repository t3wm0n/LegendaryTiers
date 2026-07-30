package com.example.legendarytiers.client.tooltip;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

public record TooltipAttributeEntry(

        Holder<Attribute> attribute,

        String descriptionId,

        double finalValue,

        double bonusValue

) {
}