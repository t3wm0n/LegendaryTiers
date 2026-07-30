package com.example.legendarytiers.client.tooltip;

public final class TooltipAttributeCalculator {

    private TooltipAttributeCalculator() {
    }

    public static double calculate(
            TooltipAttributeCollector.Accumulator accumulator
    ) {

        boolean hasBase =
                accumulator.baseValue != 0.0
                        || accumulator.addValue != 0.0;

        /*
         * Vanilla attributes
         * (attack damage, attack speed, armor...)
         */
        if (hasBase) {

            return (accumulator.baseValue + accumulator.addValue)
                    * (1.0 + accumulator.multipliedBase)
                    * (1.0 + accumulator.multipliedTotal);
        }

        /*
         * Pure percentage modifiers
         * (mining speed, movement speed, gravity...)
         */
        return accumulator.multipliedBase
                + accumulator.multipliedTotal;
    }

}