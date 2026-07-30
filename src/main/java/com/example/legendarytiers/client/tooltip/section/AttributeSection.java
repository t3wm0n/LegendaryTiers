package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipAttributeEntry;
import com.example.legendarytiers.client.tooltip.TooltipAttributeFormatter;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public final class AttributeSection {

    private AttributeSection() {
    }

    public static int getHeight(int count) {
        return count * TooltipLayout.ATTRIBUTE_LINE_HEIGHT;
    }

    public static void render(
            GuiGraphics graphics,
            Font font,
            LegendaryTooltipContext context,
            int x,
            int y,
            int width
    ) {

        List<TooltipAttributeEntry> attributes;

        if (context.showAdvancedAttributes()) {

            attributes = context.attributes();

        } else {

            attributes = context.attributes()
                    .stream()
                    .filter(AttributeSection::isPrimaryAttribute)
                    .toList();

        }

        if (attributes.isEmpty()) {
            return;
        }

        int currentY = y;

        for (TooltipAttributeEntry entry : attributes) {

            String attributeId =
                    entry.attribute()
                            .unwrapKey()
                            .map(key -> key.location().toString())
                            .orElse("");

            TooltipAttributeFormatter.Result formatted =
                    TooltipAttributeFormatter.format(
                            attributeId,
                            entry.finalValue(),
                            entry.bonusValue()
                    );

            //----------------------------------
            // Icon
            //----------------------------------

            IconRenderer.draw(
                    graphics,
                    x + TooltipLayout.PADDING,
                    currentY + 2,
                    TooltipIcons.getIconX(entry.descriptionId()),
                    TooltipIcons.getIconY(entry.descriptionId())
            );

            //----------------------------------
            // Name
            //----------------------------------

            TextRenderer.draw(
                    graphics,
                    font,
                    formatted.name(),
                    x + TooltipLayout.PADDING + TooltipIcons.DRAW_SIZE + 6,
                    currentY + 5,
                    TooltipColors.TEXT_NORMAL
            );

            //----------------------------------
            // Right side
            //----------------------------------

            final int totalColumn =
                    x + width - 92;

            final int bonusColumn =
                    x + width - 34;

            final int arrowColumn =
                    x + width - 14;

            /*
             * Правая часть строится справа налево.
             * Поэтому элементы никогда не пересекаются.
             */

            int right =
                    x + width - TooltipLayout.PADDING;

            /*
             * Стрелка
             */

            if (formatted.hasBonus()) {

                right -= 12;

                IconRenderer.draw(
                        graphics,
                        right,
                        currentY + 5,
                        formatted.positiveBonus()
                                ? TooltipIcons.BONUS_UP_X
                                : TooltipIcons.BONUS_DOWN_X,
                        formatted.positiveBonus()
                                ? TooltipIcons.BONUS_UP_Y
                                : TooltipIcons.BONUS_DOWN_Y,
                        12
                );

                right -= 6;

                /*
                 * Бонус
                 */

                int bonusWidth =
                        font.width(formatted.bonusValue());

                right -= bonusWidth;

                TextRenderer.draw(
                        graphics,
                        font,
                        formatted.bonusValue(),
                        right,
                        currentY + 5,
                        formatted.positiveBonus()
                                ? TooltipColors.TEXT_POSITIVE
                                : TooltipColors.TEXT_NEGATIVE
                );

                right -= 18;
            }

            /*
             * Итоговое значение
             */

            int totalWidth =
                    font.width(formatted.totalValue());

            right -= totalWidth;

            TextRenderer.draw(
                    graphics,
                    font,
                    formatted.totalValue(),
                    right,
                    currentY + 5,
                    TooltipColors.TEXT_NORMAL
            );

            currentY += TooltipLayout.ATTRIBUTE_LINE_HEIGHT;
        }
    }
    private static boolean isPrimaryAttribute(
        TooltipAttributeEntry entry )
    {

    String id =
            entry.attribute()
                    .unwrapKey()
                    .map(key -> key.location().toString())
                    .orElse("");

    return false;/**switch (id) {

        case "minecraft:generic.attack_damage",
             "minecraft:generic.attack_speed",
             "minecraft:generic.armor",
             "minecraft:generic.armor_toughness",
             "minecraft:generic.max_health",
             "minecraft:generic.knockback_resistance",
             "legendarytiers:generic.crit_chance",
             "legendarytiers:generic.crit_damage",
             "legendarytiers:generic.arrow_damage",
             "legendarytiers:generic.bow_draw_speed" -> true;

        default -> false;
        };**/
    }

public static int visibleCount(
        LegendaryTooltipContext context
) {

    if (context.showAdvancedAttributes()) {
        return context.attributes().size();
    }

    int count = 0;

    for (TooltipAttributeEntry entry : context.attributes()) {

        if (isPrimaryAttribute(entry)) {
            count++;
        }

    }

    return count;
}

}