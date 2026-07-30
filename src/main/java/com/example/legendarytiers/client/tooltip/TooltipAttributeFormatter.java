package com.example.legendarytiers.client.tooltip;

import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public final class TooltipAttributeFormatter {

    private static final DecimalFormat DECIMAL = new DecimalFormat("+#0.00;-#0.00");
    private static final DecimalFormat INTEGER = new DecimalFormat("+#0;-#0");

    public record Result(
            String name,
            String totalValue,
            String bonusValue,
            boolean hasBonus,
            boolean positiveBonus
    ) {}

    private TooltipAttributeFormatter() {
    }

    public static Result format(
            String attributeId,
            double totalValue,
            double bonusValue
    ) {

        String key = attributeId;

        int separator = key.indexOf(':');

        if (separator >= 0) {
            key = key.substring(separator + 1);
        }

        key = key.replace(':', '.');

        String name =
                Component.translatable(
                        "attribute.name." + key
                ).getString();

        AttributeFormat format =
                AttributeFormatRegistry.get(attributeId);

        String total =
                formatValue(
                        totalValue,
                        format
                );

        boolean hasBonus =
                Math.abs(bonusValue) > 0.000001D;

        String bonus =
                hasBonus
                        ? formatValue(
                        bonusValue,
                        format
                )
                        : "";

        return new Result(
                name,
                total,
                bonus,
                hasBonus,
                bonusValue >= 0.0D
        );
    }

    private static String formatValue(
            double value,
            AttributeFormat format
    ) {

        return switch (format) {

            case INTEGER ->
                    INTEGER.format(value);

            case DECIMAL ->
                    DECIMAL.format(value);

            case PERCENT ->
                    INTEGER.format(value * 100.0) + "%";
        };

    }

}