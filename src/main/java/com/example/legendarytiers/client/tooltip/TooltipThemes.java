package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.Rarity;

import java.util.EnumMap;
import java.util.Map;

public final class TooltipThemes {

    private static final Map<Rarity, TooltipTheme> THEMES =
            new EnumMap<>(Rarity.class);

    static {

        THEMES.put(
                Rarity.COMMON,
                new TooltipTheme(

                        0xFF6E6E6E,
                        0x226E6E6E,
                        0xCC2A2A2A,
                        0x22FFFFFF,

                        0xCC171717,
                        0xCC2A2A2A,
                        0x22FFFFFF,

                        0x88FFFFFF,
                        2,
                        0.15f,
                        1.5f,

                        0xCC0C1B2F,
                        0xCC163B68,
                        0x334AA8FF
                )
        );

        THEMES.put(
                Rarity.RARE,
                new TooltipTheme(

                        0xFF4AA8FF,
                        0x334AA8FF,
                        0xCC163B68,
                        0x334AA8FF,

                        0xCC0C1B2F,
                        0xCC163B68,
                        0x334AA8FF,

                        0xFF6FC7FF,
                        5,
                        0.20f,
                        1.8f,

                        0xCC171717,
                        0xCC2A2A2A,
                        0x22FFFFFF
                )
        );

        THEMES.put(
                Rarity.EPIC,
                new TooltipTheme(

                        0xFFC05CFF,
                        0x44C05CFF,
                        0xCC4B2272,
                        0x44C05CFF,

                        0xCC241337,
                        0xCC4B2272,
                        0x44C05CFF,

                        0xFFE18CFF,
                        8,
                        0.22f,
                        2.0f,

                        0xCC2B210B,
                        0xCC5E4510,
                        0x55FFD44A
                )
        );

        THEMES.put(
                Rarity.LEGENDARY,
                new TooltipTheme(

                        0xFFFFC94A,
                        0x55FFD44A,
                        0xCC5E4510,
                        0x55FFD44A,

                        0xCC2B210B,
                        0xCC5E4510,
                        0x55FFD44A,

                        0xFFFFD95A,
                        12,
                        0.24f,
                        2.3f,

                        0xCC241337,
                        0xCC4B2272,
                        0x44C05CFF
                )
        );

        THEMES.put(
                Rarity.MYTHIC,
                new TooltipTheme(

                        0xFFFF4444,
                        0x66FF4444,
                        0xCC5A1414,
                        0x55FF5555,

                        0xCC2B0C0C,
                        0xCC5A1414,
                        0x55FF5555,

                        0xFFFF6666,
                        18,
                        0.27f,
                        2.5f,

                        0xCC202020,
                        0xCC3A3A3A,
                        0x88FFFFFF
                )
        );

        THEMES.put(
                Rarity.DIVINE,
                new TooltipTheme(

                        0xFFFFFFFF,
                        0x88FFFFFF,
                        0xCC3A3A3A,
                        0x88FFFFFF,

                        0xCC202020,
                        0xCC3A3A3A,
                        0x88FFFFFF,

                        0xFFFFFFFF,
                        30,
                        0.32f,
                        3.0f,

                        0xCC2B0C0C,
                        0xCC5A1414,
                        0x55FF5555
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