package com.example.legendarytiers.client.tooltip;

public final class TooltipLayout {

    private TooltipLayout() {
    }

    //подсказка на шифт
    public static final int HINT_HEIGHT = 18;

    //мин ширина подсказки
    public static final int MIN_WIDTH = 260;

    // Внутренний отступ от рамки
    public static final int PADDING = 16;

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
    public static final int DIVIDER_HEIGHT = 10;

    // Блок Broken Item
    public static final int BROKEN_HEIGHT = 86;

    // Блок перековки
    public static final int REFORGE_HEIGHT = 24;

    // Зачарования
    public static final int ENCHANTMENT_LINE_HEIGHT = 18;

    // STATUS BAR
    public static final int BAR_HEIGHT = 14;

    public static final int XP_ICON_SIZE = 24;

    public static final int XP_BAR_X = 34;

    public static final int LEVEL_ICON_SIZE = 24;

    public static final int XP_BAR_HEIGHT = 14;

    public static final int BAR_TEXT_OFFSET_Y = 3;

    public static final int BAR_TOP_MARGIN = 4;

    public static int calculateHeight(
            int enchantmentCount,
            int attributeCount,
            boolean broken,
            boolean durability,
            boolean reforge,
            boolean showHint,
            boolean showEnchantments
    ) {

        int height = 0;

        height += HEADER_HEIGHT;
        height += RARITY_HEIGHT;
        height += QUALITY_HEIGHT;

        if (durability) {
            height += DURABILITY_HEIGHT;
        }

        if (attributeCount > 0) {
            height += DIVIDER_HEIGHT;
            height += attributeCount * ATTRIBUTE_LINE_HEIGHT;
        }

        if (showEnchantments && enchantmentCount > 0) {

            height += 14;
            height += enchantmentCount * ENCHANTMENT_LINE_HEIGHT;

        }

        height += DIVIDER_HEIGHT;
        height += EXPERIENCE_HEIGHT;

        if (broken) {
            height += DIVIDER_HEIGHT;
            height += BROKEN_HEIGHT;
        }

        if (reforge) {
            height += DIVIDER_HEIGHT;
            height += REFORGE_HEIGHT;
        }

        if (showHint) {

            height += DIVIDER_HEIGHT;
            height += HINT_HEIGHT;

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

    public static int enchantmentHeight(
            int count
    ) {

        return count * ENCHANTMENT_LINE_HEIGHT;

    }
}