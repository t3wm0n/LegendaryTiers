package com.example.legendarytiers.client.tooltip;

public final class TooltipLayout {

    private TooltipLayout() {
    }

    //подсказка на шифт
    public static final int HINT_HEIGHT = 14;

    //мин ширина подсказки
    public static final int MIN_WIDTH = 260;

    // Внутренний отступ от рамки
    public static final int PADDING = 8;

    // Высота верхней части
    public static final int HEADER_HEIGHT = 22;

    // Блок редкости
    public static final int RARITY_HEIGHT = 24;

    // Блок звезд качества
    public static final int QUALITY_HEIGHT = 32;

    // Полоса опыта
    public static final int EXPERIENCE_HEIGHT = 30;

    // Прочность
    public static final int DURABILITY_HEIGHT = 30;

    // Одна строка атрибута
    public static final int ATTRIBUTE_LINE_HEIGHT = 22;

    // Разделитель между секциями
    public static final int DIVIDER_HEIGHT = 12;

    // Блок Broken Item
    public static final int BROKEN_HEIGHT = 60;

    // Блок перековки
    public static final int REFORGE_HEIGHT = 24;

    // Зачарования
    public static final int ENCHANTMENT_LINE_HEIGHT = 18;

    public static int calculateHeight(
            int enchantmentCount,
            int attributeCount,
            boolean broken,
            boolean durability,
            boolean reforge,
            boolean showAttributes,
            boolean showEnchantments
    ) {

        int height = 0;

        //height += HEADER_HEIGHT;
        height += RARITY_HEIGHT;

        if (durability) {
            height += DURABILITY_HEIGHT;
        }

        height += EXPERIENCE_HEIGHT;

        if (reforge) {
            height += REFORGE_HEIGHT;
        }
        height += DIVIDER_HEIGHT;

        height += HINT_HEIGHT + 20;
        if (showAttributes || showEnchantments) {

            height += HINT_HEIGHT + 20;

        }

        if (attributeCount > 0 && !showAttributes) {
            height += DIVIDER_HEIGHT;
            height += attributeCount * ATTRIBUTE_LINE_HEIGHT + 10;
        }

        if (enchantmentCount > 0 && !showEnchantments) {

            height += DIVIDER_HEIGHT;
            height += enchantmentCount * ENCHANTMENT_LINE_HEIGHT + 10;

        }

        if (broken) {
            height += DIVIDER_HEIGHT;
            height += BROKEN_HEIGHT + 20;
        }

        height += PADDING * 2;

        return height;
    }

    public static int calculateWidth(
            int longestLineWidth
    ) {

        return Math.max(
                MIN_WIDTH,
                longestLineWidth + PADDING * 2 + 24
        );

    }
}