package com.example.legendarytiers.client.tooltip;

public final class TooltipTextures {

    private TooltipTextures() {
    }


    /*
        Размер одного элемента 9-slice.

        В атласе каждая часть:
        64x64

        При выводе:
        16x16

        Масштабирование:
        4:1
     */

    public static final int ATLAS_SIZE = 512;

    public static final int SOURCE_SIZE = 64;

    public static final int BORDER_SIZE = 16;



    /*
        Атлас:

        ┌────┬────┬────┐
        │ TL │ T  │ TR │
        ├────┼────┼────┤
        │ L  │ C  │ R  │
        ├────┼────┼────┤
        │ BL │ B  │ BR │
        └────┴────┴────┘


        Каждый блок 64x64
     */


    // Верхний левый угол
    public static final int TOP_LEFT_X = 0;
    public static final int TOP_LEFT_Y = 0;


    // Верхняя грань
    public static final int TOP_X = 64;
    public static final int TOP_Y = 0;


    // Верхний правый угол
    public static final int TOP_RIGHT_X = 128;
    public static final int TOP_RIGHT_Y = 0;



    // Левая грань
    public static final int LEFT_X = 0;
    public static final int LEFT_Y = 64;


    // Центр
    public static final int CENTER_X = 64;
    public static final int CENTER_Y = 64;


    // Правая грань
    public static final int RIGHT_X = 128;
    public static final int RIGHT_Y = 64;



    // Нижний левый угол
    public static final int BOTTOM_LEFT_X = 0;
    public static final int BOTTOM_LEFT_Y = 128;


    // Нижняя грань
    public static final int BOTTOM_X = 64;
    public static final int BOTTOM_Y = 128;


    // Нижний правый угол
    public static final int BOTTOM_RIGHT_X = 128;
    public static final int BOTTOM_RIGHT_Y = 128;



    /*
        Дополнительные элементы атласа

        Пока только зарезервированы.
        Координаты добавим после создания UI элементов.
    */


    // Header
    public static int HEADER_X = 192;
    public static int HEADER_Y = 0;


    // Divider
    public static int DIVIDER_X = 192;
    public static int DIVIDER_Y = 64;


    // XP bar background
    public static int XP_BAR_BG_X = 192;
    public static int XP_BAR_BG_Y = 128;


    // XP bar fill
    public static int XP_BAR_FILL_X = 192;
    public static int XP_BAR_FILL_Y = 192;


}