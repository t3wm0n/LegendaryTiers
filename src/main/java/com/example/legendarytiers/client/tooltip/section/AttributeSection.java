package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.ModDataComponents;
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
import java.util.Locale;

public final class AttributeSection {

    private static final int SLOT_SIZE = 22;

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

            String attributeId = entry.attribute()
                    .unwrapKey()
                    .map(key -> key.location().toString())
                    .orElse("");

            TooltipAttributeFormatter.Result formatted = TooltipAttributeFormatter.format(
                    attributeId,
                    entry.finalValue(),
                    entry.bonusValue()
            );

            // ----------------------------------
            // Динамическое определение полезности по ИТОГОВОМУ значению (finalValue)
            // ----------------------------------
            boolean isGravity = attributeId.contains("gravity");

            // Если гравитация: положительно при finalValue < 0
            // Для остальных: положительно, если итоговое значение >= 0
            boolean isPositive = isGravity
                    ? entry.finalValue() < 0
                    : entry.finalValue() >= 0;

            // ----------------------------------
            // 1. Слот и Иконка
            // ----------------------------------
            int slotX = x + TooltipLayout.PADDING;
            int slotY = currentY + 2;

            graphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, TooltipColors.SLOT_BG);
            graphics.renderOutline(slotX, slotY, SLOT_SIZE, SLOT_SIZE, TooltipColors.SLOT_BORDER);

            IconRenderer.draw(
                    graphics,
                    slotX + 1,
                    slotY + 1,
                    TooltipIcons.getIconX(entry.descriptionId()),
                    TooltipIcons.getIconY(entry.descriptionId())
            );

            // ----------------------------------
            // 2. Название атрибута
            // ----------------------------------
            int nameX = slotX + SLOT_SIZE + 6;
            int textY = currentY + 6;
            int nameWidth = 0;
            int maxlength = 14;

            String attributeName = formatted.name();

            if (attributeName.length() > maxlength) {

                int splitIndex = maxlength;

                // Пытаемся перенести по последнему пробелу до 10 символов
                int spaceIndex = attributeName.lastIndexOf(' ', maxlength);

                if (spaceIndex > 0) {
                    splitIndex = spaceIndex;
                }

                String firstLine =
                        attributeName.substring(0, splitIndex).trim();

                String secondLine =
                        attributeName.substring(splitIndex).trim();

                TextRenderer.draw(
                        graphics,
                        font,
                        firstLine,
                        nameX,
                        textY,
                        TooltipColors.TEXT_MUTED
                );

                TextRenderer.draw(
                        graphics,
                        font,
                        secondLine,
                        nameX,
                        textY + font.lineHeight,
                        TooltipColors.TEXT_MUTED
                );

                int firstWidth =
                        font.width(firstLine);

                int secondWidth =
                        font.width(secondLine);

                nameWidth =
                        Math.max(firstWidth, secondWidth);

            } else {

                TextRenderer.draw(
                        graphics,
                        font,
                        attributeName,
                        nameX,
                        textY,
                        TooltipColors.TEXT_MUTED
                );

                nameWidth =
                        font.width(attributeName);
            }

            // ----------------------------------
            // 3. Числа и Расчет Прироста
            // ----------------------------------
            int rightBoundary = x + width - TooltipLayout.PADDING;

            // Вычисляем чистый прирост от уровня
            double pureLevelBonus = Math.abs(entry.finalValue() - entry.bonusValue());

            boolean isPercent = formatted.totalValue().contains("%");

            // Если показатель процентный, умножаем значение на 100.0 (так как 2.0 = 200%)
            String levelBonusFormatted = isPercent
                    ? String.format(Locale.ROOT, "+%.0f%%", pureLevelBonus * 100.0)
                    : String.format(Locale.ROOT, "+%.2f", pureLevelBonus);

            String arrowStr = isPositive ? "↑" : "↓";

            String levelText = String.format(Locale.ROOT, "(%s %sLv %s)",
                    formatted.bonusValue(),
                    levelBonusFormatted,
                    arrowStr
            );

            int levelTextWidth = font.width(levelText);
            int totalValueWidth = font.width(formatted.totalValue());

            // 3.1 Детализация в скобках (цвет зависит от итогового isPositive)
            int levelTextX = rightBoundary - levelTextWidth;
            int bonusColor = isPositive ? TooltipColors.TEXT_POSITIVE : TooltipColors.TEXT_NEGATIVE;

            TextRenderer.draw(
                    graphics,
                    font,
                    levelText,
                    levelTextX,
                    textY,
                    bonusColor
            );

            // 3.2 Итоговое фактическое значение
            int totalValueX = levelTextX - 6 - totalValueWidth;

            TextRenderer.draw(
                    graphics,
                    font,
                    formatted.totalValue(),
                    totalValueX,
                    textY,
                    TooltipColors.TEXT_NORMAL
            );

            // ----------------------------------
            // 4. Точки-направители
            // ----------------------------------
            int dotsStartX = nameX + nameWidth + 4;
            int dotsEndX = totalValueX - 4;

            if (dotsEndX > dotsStartX) {
                String dotTile = ". ";
                int dotWidth = font.width(dotTile);
                int availableWidth = dotsEndX - dotsStartX;
                int dotCount = availableWidth / dotWidth;

                if (dotCount > 0) {
                    StringBuilder dotsBuilder = new StringBuilder();
                    for (int i = 0; i < dotCount; i++) {
                        dotsBuilder.append(dotTile);
                    }
                    TextRenderer.draw(
                            graphics,
                            font,
                            dotsBuilder.toString().trim(),
                            dotsStartX,
                            textY,
                            TooltipColors.TEXT_DOTS
                    );
                }
            }

            currentY += TooltipLayout.ATTRIBUTE_LINE_HEIGHT;
        }
    }

    private static boolean isPrimaryAttribute(TooltipAttributeEntry entry) {
        return false;
    }

    public static int visibleCount(LegendaryTooltipContext context) {
        if (context.showAdvancedAttributes()) {
            return context.attributes().size();
        }
        return 0;
    }
}