package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.Rarity;

public final class TooltipColors {

    private TooltipColors() {
    }

    /*
     * Текст
     */

    public static final int TEXT_NORMAL = 0xFFFFFFFF;
    public static final int TEXT_SECONDARY = 0xFFB8B8B8;

    public static final int TEXT_POSITIVE = 0xFF65FF65;
    public static final int TEXT_NEGATIVE = 0xFFFF6565;

    public static final int TEXT_TITLE = 0xFFFFFFFF;

    public static final int TEXT_DISABLED = 0xFFB8B8B8;

    public static final int TEXT_ENCHANTMENT = 0x55FFFF;
    /*
     * Опыт
     */

    public static final int XP_TEXT = 0xFFE6E6E6;

    /*
     * Фон
     */

    public static final int BACKGROUND_ALPHA = 190;

    /*
     * Разделители
     */

    public static final int DIVIDER = 0x55FFFFFF;

    /*
     * Цвета редкости
     */

    public static int rarityPrimary(Rarity rarity) {

        return switch (rarity) {

            case COMMON -> 0xFF9E9E9E;

            case RARE -> 0xFF4DA6FF;

            case EPIC -> 0xFFB266FF;

            case LEGENDARY -> 0xFFFFAA00;

            case MYTHIC -> 0xFFFF5555;

            case DIVINE -> 0xFFFFFFFF;
        };
    }

    public static int rarityBackground(Rarity rarity) {

        return switch (rarity) {

            case COMMON -> argb(BACKGROUND_ALPHA, 90, 90, 90);

            case RARE -> argb(BACKGROUND_ALPHA, 30, 70, 120);

            case EPIC -> argb(BACKGROUND_ALPHA, 70, 40, 110);

            case LEGENDARY -> argb(BACKGROUND_ALPHA, 120, 70, 15);

            case MYTHIC -> argb(BACKGROUND_ALPHA, 120, 35, 35);

            case DIVINE -> argb(BACKGROUND_ALPHA, 200, 200, 200);
        };
    }

    public static int rarityBorder(Rarity rarity) {

        return switch (rarity) {

            case COMMON -> 0xFFB0B0B0;

            case RARE -> 0xFF70C0FF;

            case EPIC -> 0xFFD08CFF;

            case LEGENDARY -> 0xFFFFD050;

            case MYTHIC -> 0xFFFF8080;

            case DIVINE -> 0xFFFFFFFF;
        };
    }

    /*
     * Вспомогательные методы
     */

    private static int argb(int a, int r, int g, int b) {

        return (a << 24)
                | (r << 16)
                | (g << 8)
                | b;

    }

}