package com.example.legendarytiers.client.tooltip2.layout;

public final class TooltipLayout {

    // Общие размеры

    public final int x;
    public final int y;

    public final int width;
    public final int height;

    // ---------- Header ----------

    public final int headerHeight;

    public final int starsY;

    public final int titleY;

    public final int rarityY;

    // ---------- Bars ----------

    public final int durabilityY;

    public final int xpY;

    // ---------- Attributes ----------

    public final int attributesY;

    public final int attributeSpacing;

    // ---------- Footer ----------

    public final int footerY;

    // ---------- Decorations ----------

    public final int contentLeft;

    public final int contentRight;

    public final int centerX;

    private TooltipLayout(

            int x,
            int y,

            int width,
            int height

    ) {

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        centerX = x + width / 2;

        contentLeft = x + 16;
        contentRight = x + width - 16;

        // ======================================================
        // HEADER
        // ======================================================

        headerHeight = 72;

        starsY = y + 8;

        titleY = starsY + 26;

        rarityY = titleY + 16;

        // ======================================================
        // BARS
        // ======================================================

        durabilityY = y + headerHeight + 12;

        xpY = durabilityY + 34;

        // ======================================================
        // ATTRIBUTES
        // ======================================================

        attributesY = xpY + 42;

        attributeSpacing = 18;

        // ======================================================
        // FOOTER
        // ======================================================

        footerY = y + height - 26;

    }

    public static TooltipLayout create(

            int x,
            int y,

            int width,
            int height

    ) {

        return new TooltipLayout(

                x,
                y,

                width,
                height

        );

    }

}