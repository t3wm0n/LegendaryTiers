package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.Rarity;

public final class TooltipColors {

    private TooltipColors() {
    }

    /*
     * Текст
     */

    public static final int TEXT_NORMAL = 0xFFFFFFFF;       // Белоснежный для итогового значения[cite: 2]
    public static final int TEXT_MUTED = 0xFFB0A8A0;        // Приглушенный песочно-серый для названий
    public static final int TEXT_SECONDARY = 0xFF888899;    // Серый для базовых значений
    public static final int TEXT_DOTS = 0xFF3F3F4E;         // Темный графит для точек-направителей

    public static final int TEXT_POSITIVE = 0xFF55FF77;     // Сочный изумрудный для прироста и стрелки ↑[cite: 2]
    public static final int TEXT_NEGATIVE = 0xFFFF5555;     // Красный для штрафов и стрелки ↓[cite: 2]

    public static final int TEXT_TITLE = 0xFFFFFFFF;
    public static final int TEXT_DISABLED = 0xFFB8B8B8;

    public static final int TEXT_ENCHANTMENT = 0x55FFFF;
    public static final int TEXT_ENCHANTMENT_BAD = 0x8D0000;

    /*
     * Слоты и Слои
     */
    public static final int SLOT_BG = 0xF00D0D12;           // Темный фон плашки под иконкой
    public static final int SLOT_BORDER = 0xFF3A3A4A;       // Контурная рамка слота

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

    //PROGRESS BAR
    public static final int PROGRESS_BACKGROUND = 0x55000000;
    public static final int PROGRESS_FILL = 0xFF55FF55;

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