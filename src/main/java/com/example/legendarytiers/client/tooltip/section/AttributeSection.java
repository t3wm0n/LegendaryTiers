package com.example.legendarytiers.client.tooltip.section;

import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.client.tooltip.LegendaryTooltipContext;
import com.example.legendarytiers.client.tooltip.TooltipColors;
import com.example.legendarytiers.client.tooltip.TooltipIcons;
import com.example.legendarytiers.client.tooltip.TooltipLayout;
import com.example.legendarytiers.client.tooltip.render.IconRenderer;
import com.example.legendarytiers.client.tooltip.render.TextRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

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

        if (context == null || context.modifiers() == null)
            return;


        int offsetY = y;


        for (ModifierEntry modifier : context.modifiers()) {


            int iconX = x + TooltipLayout.PADDING;
            int textX = iconX + TooltipIcons.DRAW_SIZE + 6;


            int iconTextureX = getIconX(modifier.target());
            int iconTextureY = getIconY(modifier.target());


            IconRenderer.draw(
                    graphics,
                    iconX,
                    offsetY + 3,
                    iconTextureX,
                    iconTextureY
            );


            String text = formatModifier(modifier);


            int color =
                    modifier.value() >= 0
                            ? TooltipColors.TEXT_POSITIVE
                            : TooltipColors.TEXT_NEGATIVE;


            TextRenderer.draw(
                    graphics,
                    font,
                    text,
                    textX,
                    offsetY + 6,
                    color
            );


            offsetY += TooltipLayout.ATTRIBUTE_LINE_HEIGHT;
        }

    }


    private static String formatModifier(ModifierEntry modifier) {

        int percent = (int) Math.round(modifier.value() * 100);

        String sign = percent >= 0 ? "+" : "";

        String key = modifier.attribute()
                .orElse("minecraft:generic." + modifier.target());

        // Убираем namespace minecraft
        int separator = key.indexOf(':');

        if (separator >= 0) {
            key = key.substring(separator + 1);
        }

        key = key.replace(':', '.');

        Component name = Component.translatable("attribute.name." + key);

        return sign + percent + "% " + name.getString();
    }


    private static int getIconX(String target) {

        return switch (target) {

            case "attack_damage" ->
                    TooltipIcons.ATTACK_X;

            case "attack_speed" ->
                    TooltipIcons.ATTACK_SPEED_X;

            case "crit_chance" ->
                    TooltipIcons.CRIT_CHANCE_X;

            case "crit_damage" ->
                    TooltipIcons.CRIT_DAMAGE_X;

            case "arrow_damage" ->
                    TooltipIcons.ARROW_DAMAGE_X;

            case "bow_draw_speed" ->
                    TooltipIcons.BOW_DRAW_SPEED_X;

            case "durability" ->
                    TooltipIcons.DURABILITY_X;

            case "luck" ->
                    TooltipIcons.LUCK_X;

            case "armor" ->
                    TooltipIcons.ARMOR_X;

            case "health" ->
                    TooltipIcons.HEALTH_X;

            default ->
                    TooltipIcons.ATTACK_X;
        };
    }


    private static int getIconY(String target) {

        return switch (target) {

            case "armor",
                 "health" ->
                    TooltipIcons.ARMOR_Y;

            default ->
                    TooltipIcons.ATTACK_Y;
        };
    }

}