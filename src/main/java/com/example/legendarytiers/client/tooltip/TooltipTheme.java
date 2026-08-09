package com.example.legendarytiers.client.tooltip;

public record TooltipTheme(
        /* Основные цвета */
        int headerBackground,
        int headerHighlight,
        int borderColor,
        int borderGlow,

        /* Фон */
        int backgroundDark,
        int backgroundLight,
        int backgroundHighlight,

        /* Частицы */
        int particleColor,
        int particleCount,
        float particleSpeed,
        float particleSize,

        /* Границы и разделители */
        int border,
        int borderDark,
        int borderLight,

        /* Флаг для динамической радуги (DIVINE) */
        boolean isRainbow
) {
    // Вспомогательный конструктор по умолчанию (без радуги)
    public TooltipTheme(
            int headerBackground, int headerHighlight, int borderColor, int borderGlow,
            int backgroundDark, int backgroundLight, int backgroundHighlight,
            int particleColor, int particleCount, float particleSpeed, float particleSize,
            int border, int borderDark, int borderLight
    ) {
        this(
                headerBackground, headerHighlight, borderColor, borderGlow,
                backgroundDark, backgroundLight, backgroundHighlight,
                particleColor, particleCount, particleSpeed, particleSize,
                border, borderDark, borderLight,
                false
        );
    }

    // --- Динамический расчет божественного цвета (HSV) ---
    private static int getRainbowColor(float speed, float minSat, float maxSat, float brightness) {
        long timeMs = System.currentTimeMillis();
        float timeSec = (timeMs % 1_000_000L) / 1000.0F;

        // 1. Полный цикл ровно 7 секунд
        float hue = ((timeSec / 7.0F) * speed) % 1.0F;

        // 2. Динамическая насыщенность строго в пределах [minSat, maxSat] (например, 0.15 .. 0.25)
        float pulse = (float) (Math.sin(timeSec * 2.2F) * 0.5F + 0.5F); // Волна от 0.0 до 1.0
        float saturation = minSat + pulse * (maxSat - minSat);

        int rgb = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
        return 0xFF000000 | (rgb & 0x00FFFFFF);
    }

    // --- ПЕРЕОПРЕДЕЛЕНИЕ ГЕТТЕРОВ ДЛЯ ЯРКОЙ DIVINE-ТЕМЫ ---

    @Override
    public int borderColor() {
        // Насыщенность 0.15 - 0.25, максимальная яркость 1.0
        if (isRainbow) return getRainbowColor(1.0f, 0.15f, 0.25f, 1.0f);
        return borderColor;
    }

    @Override
    public int headerBackground() {
        if (isRainbow) return getRainbowColor(1.0f, 0.12f, 0.20f, 1.0f);
        return headerBackground;
    }

    @Override
    public int particleColor() {
        // Частицы чуть более насыщенные (0.18 - 0.28) для эффекта искрения
        if (isRainbow) return getRainbowColor(1.2f, 0.18f, 0.28f, 1.0f);
        return particleColor;
    }

    @Override
    public int borderGlow() {
        if (isRainbow) {
            // Альфа-канал поднят до 0xAA (~67% плотности свечения вместо старых 40%)
            return (0xAA << 24) | (getRainbowColor(1.0f, 0.15f, 0.25f, 1.0f) & 0x00FFFFFF);
        }
        return borderGlow;
    }

    @Override
    public int borderLight() {
        // Яркий светлый блик на гранях
        if (isRainbow) return getRainbowColor(1.0f, 0.10f, 0.18f, 1.0f);
        return borderLight;
    }
}