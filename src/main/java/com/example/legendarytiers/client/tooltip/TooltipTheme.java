package com.example.legendarytiers.client.tooltip;

public record TooltipTheme(

        /*
         * Основные цвета
         */
        int headerBackground,

        int headerHighlight,

        int borderColor,

        int borderGlow,

        /*
         * Фон
         */

        int backgroundDark,

        int backgroundLight,

        int backgroundHighlight,

        /*
         * Частицы
         */

        int particleColor,

        int particleCount,

        float particleSpeed,

        float particleSize,


        int border,

        int borderDark,

        int borderLight
) {
    public int glowOuter() {
        return withAlpha(borderColor(), 70);
    }

    public int glowMiddle() {
        return withAlpha(borderColor(), 35);
    }

    public int glowInner() {
        return withAlpha(borderColor(), 18);
    }

    private static int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0xFFFFFF);
    }
}