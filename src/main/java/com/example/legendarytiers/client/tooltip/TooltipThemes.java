package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.Rarity;

import java.util.EnumMap;
import java.util.Map;

public final class TooltipThemes {

    private static final Map<Rarity, TooltipTheme> THEMES =
            new EnumMap<>(Rarity.class);

    static {

        // ==================== COMMON (Бело-серебристый, светлый) ====================
        THEMES.put(
                Rarity.COMMON,
                new TooltipTheme(
                        0xFFE6E8FA, // headerBackground
                        0x33FFFFFF, // headerHighlight
                        0xFFD1D5DB, // borderColor (серебристый)
                        0x33FFFFFF, // borderGlow

                        0xDD1E2022, // backgroundDark
                        0xDD2C2F33, // backgroundLight
                        0x33FFFFFF, // backgroundHighlight

                        0xFFF0F2FF, // particleColor
                        5,          // particleCount
                        0.40f,      // particleSpeed
                        2.0f,       // particleSize

                        0xDD1E2022, // border
                        0xDD2C2F33, // borderDark
                        0x33FFFFFF  // borderLight
                )
        );

// ==================== RARE (Синий, светлый и мягкий) ====================
        THEMES.put(
                Rarity.RARE,
                new TooltipTheme(
                        0xFF52B2FF, // headerBackground
                        0x4452B2FF, // headerHighlight
                        0xFF4D8DFF, // borderColor (нежно-синий)
                        0x4452B2FF, // borderGlow

                        0xDD0F2338, // backgroundDark
                        0xDD183554, // backgroundLight
                        0x4452B2FF, // backgroundHighlight

                        0xFFA6D8FF, // particleColor
                        10,         // particleCount
                        0.55f,      // particleSpeed
                        2.2f,       // particleSize

                        0xDD0F2338, // border
                        0xDD183554, // borderDark
                        0x4452B2FF  // borderLight
                )
        );

// ==================== EPIC (Фиолетовый, сильный, светлый) ====================
        THEMES.put(
                Rarity.EPIC,
                new TooltipTheme(
                        0xFFC864FF, // headerBackground
                        0x44C864FF, // headerHighlight
                        0xFFB56CFF, // borderColor (сочный аметист)
                        0x44C864FF, // borderGlow

                        0xDD2A123D, // backgroundDark
                        0xDD421B61, // backgroundLight
                        0x44C864FF, // backgroundHighlight

                        0xFFEBB8FF, // particleColor
                        16,         // particleCount
                        0.70f,      // particleSpeed
                        2.5f,       // particleSize

                        0xDD2A123D, // border
                        0xDD421B61, // borderDark
                        0x44C864FF  // borderLight
                )
        );

// ==================== LEGENDARY (Яркое сверкающее золото) ====================
        THEMES.put(
                Rarity.LEGENDARY,
                new TooltipTheme(
                        0xFFFFD700, // headerBackground
                        0x66FFD700, // headerHighlight
                        0xFFFFC54A, // borderColor (сверкающее золото)
                        0x66FFD700, // borderGlow

                        0xDD3B2B00, // backgroundDark
                        0xDD5C4300, // backgroundLight
                        0x66FFD700, // backgroundHighlight

                        0xFFFFF0A6, // particleColor
                        22,         // particleCount
                        0.85f,      // particleSpeed
                        2.8f,       // particleSize

                        0xDD3B2B00, // border
                        0xDD5C4300, // borderDark
                        0x66FFD700  // borderLight
                )
        );

// ==================== MYTHIC (Кроваво-рубиновый, пугающий) ====================
        THEMES.put(
                Rarity.MYTHIC,
                new TooltipTheme(
                        0xFFFF1A1A, // headerBackground
                        0x66FF1A1A, // headerHighlight
                        0xFFE04646, // borderColor (кроваво-рубиновый)
                        0x66FF1A1A, // borderGlow

                        0xDD330000, // backgroundDark
                        0xDD520000, // backgroundLight
                        0x66FF1A1A, // backgroundHighlight

                        0xFFFF8080, // particleColor
                        32,         // particleCount
                        1.10f,      // particleSpeed
                        3.2f,       // particleSize

                        0xDD330000, // border
                        0xDD520000, // borderDark
                        0x66FF1A1A  // borderLight
                )
        );

// ==================== DIVINE (Радужный, переливающийся) ====================
        THEMES.put(
                Rarity.DIVINE,
                new TooltipTheme(
                        0xFFFFFFFF, // Игнорируется (заменяется на динамическую радугу)
                        0x66FFFFFF,
                        0xFFFFFFFF, // Игнорируется (заменяется на динамическую радугу)
                        0x66FFFFFF,

                        0xDD202028, // backgroundDark (темно-перламутровый фон)
                        0xDD353545, // backgroundLight
                        0x88FFFFFF, // backgroundHighlight

                        0xFFFFFFFF, // Игнорируется (заменяется на динамические радужные частицы)
                        45,         // particleCount
                        1.30f,      // particleSpeed
                        3.5f,       // particleSize

                        0xDD202028, // border
                        0xDD353545, // borderDark
                        0x88FFFFFF, // borderLight
                        true        // <-- ФЛАГ РАДУГИ ВКЛЮЧЕН!
                )
        );
    }

    private TooltipThemes() {
    }

    public static TooltipTheme get(Rarity rarity) {
        return THEMES.getOrDefault(
                rarity,
                THEMES.get(Rarity.COMMON)
        );
    }
}