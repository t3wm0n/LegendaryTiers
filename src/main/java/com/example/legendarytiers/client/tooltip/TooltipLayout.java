package com.example.legendarytiers.client.tooltip;

public final class TooltipLayout {

    private TooltipLayout() {
    }


    // Общая ширина tooltip
    public static final int WIDTH = 260;


    // Внутренний отступ от рамки
    public static final int PADDING = 16;


    // Высота верхней части
    public static final int HEADER_HEIGHT = 42;


    // Блок редкости
    public static final int RARITY_HEIGHT = 24;


    // Блок звезд качества
    public static final int QUALITY_HEIGHT = 32;


    // Полоса опыта
    public static final int EXPERIENCE_HEIGHT = 42;


    // Одна строка атрибута
    public static final int ATTRIBUTE_LINE_HEIGHT = 22;


    // Разделитель между секциями
    public static final int DIVIDER_HEIGHT = 10;


    // Блок Broken Item
    public static final int BROKEN_HEIGHT = 70;


    // Блок перековки
    public static final int REFORGE_HEIGHT = 24;


    // Нижняя часть
    public static final int FOOTER_HEIGHT = 20;



    public static int calculateHeight(
            int attributes,
            boolean broken,
            boolean reforges
    ) {

        int height = 0;


        height += HEADER_HEIGHT;

        height += RARITY_HEIGHT;

        height += QUALITY_HEIGHT;

        height += EXPERIENCE_HEIGHT;


        if (attributes > 0) {

            height += DIVIDER_HEIGHT;

            height += attributes * ATTRIBUTE_LINE_HEIGHT;

        }


        if (broken) {

            height += DIVIDER_HEIGHT;

            height += BROKEN_HEIGHT;

        }


        if (reforges) {

            height += DIVIDER_HEIGHT;

            height += REFORGE_HEIGHT;

        }


        height += FOOTER_HEIGHT;


        height += PADDING * 2;


        return height;
    }


}