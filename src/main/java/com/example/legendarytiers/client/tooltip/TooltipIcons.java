package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.LegendaryTiers;
import net.minecraft.resources.ResourceLocation;

public final class TooltipIcons {

    private TooltipIcons() {
    }
    public static final int ICON_SIZE = 64;
    public static final int DRAW_SIZE = 20;

    // ===== Атрибуты =====

    public static final int ATTACK_X = 0;
    public static final int ATTACK_Y = 0;

    public static final int ATTACK_SPEED_X = 64;
    public static final int ATTACK_SPEED_Y = 0;

    public static final int CRIT_CHANCE_X = 128;
    public static final int CRIT_CHANCE_Y = 0;

    public static final int CRIT_DAMAGE_X = 192;
    public static final int CRIT_DAMAGE_Y = 0;

    public static final int ARROW_DAMAGE_X = 256;
    public static final int ARROW_DAMAGE_Y = 0;

    public static final int BOW_DRAW_SPEED_X = 320;
    public static final int BOW_DRAW_SPEED_Y = 0;

    public static final int DURABILITY_X = 384;
    public static final int DURABILITY_Y = 0;

    public static final int LUCK_X = 448;
    public static final int LUCK_Y = 0;

    // ===== Защита =====

    public static final int BLOCK_INTERACTION_RANGE_X = 0;
    public static final int BLOCK_INTERACTION_RANGE_Y = 64;

    public static final int MOVEMENT_SPEED_X = 64;
    public static final int MOVEMENT_SPEED_Y = 64;

    public static final int ARMOR_X = 128;
    public static final int ARMOR_Y = 64;

    public static final int ARMOR_TOUGHNESS_X = 192;
    public static final int ARMOR_TOUGHNESS_Y = 64;

    public static final int KNOCKBACK_RESISTANCE_X = 256;
    public static final int KNOCKBACK_RESISTANCE_Y = 64;

    public static final int HEALTH_X = 320;
    public static final int HEALTH_Y = 64;

    public static final int BLOCK_BREAK_SPEED_X = 384;
    public static final int BLOCK_BREAK_SPEED_Y = 64;

    public static final int ENTITY_INTERACTION_RANGE_X = 448;
    public static final int ENTITY_INTERACTION_RANGE_Y = 64;

    // ===== Качество =====

    public static final int STAR_EMPTY_X = 0;
    public static final int STAR_EMPTY_Y = 128;

    public static final int STAR_HALF_X = 64;
    public static final int STAR_HALF_Y = 128;

    public static final int STAR_FILLED_X = 128;
    public static final int STAR_FILLED_Y = 128;

    // ===== Дополнительные =====

    public static final int EXPERIENCE_X = 192;
    public static final int EXPERIENCE_Y = 128;

    public static final int LEVEL_X = 256;
    public static final int LEVEL_Y = 128;

    // ===== Состояния =====

    public static final int BROKEN_X = 320;
    public static final int BROKEN_Y = 128;

    public static final int REPAIR_X = 384;
    public static final int REPAIR_Y = 128;

    public static final int REFORGE_X = 448;
    public static final int REFORGE_Y = 128;

    // Стрелки

    public static final int BONUS_UP_X = 0;
    public static final int BONUS_UP_Y = 256;

    public static final int BONUS_DOWN_X = 64;
    public static final int BONUS_DOWN_Y = 256;

    public static int getIconX(String descriptionId) {

        return switch (descriptionId) {

            case "attribute.name.generic.attack_damage" -> ATTACK_X;
            case "attribute.name.generic.attack_speed" -> ATTACK_SPEED_X;

            case "attribute.name.generic.armor" -> ARMOR_X;
            case "attribute.name.generic.armor_toughness" -> ARMOR_TOUGHNESS_X;
            case "attribute.name.generic.max_health" -> HEALTH_X;
            case "attribute.name.generic.knockback_resistance" -> KNOCKBACK_RESISTANCE_X;
            case "attribute.name.generic.luck" -> LUCK_X;

            case "attribute.name.generic.movement_speed" -> MOVEMENT_SPEED_X;
            case "attribute.name.player.block_break_speed" -> BLOCK_BREAK_SPEED_X;
            case "attribute.name.player.block_interaction_range" -> BLOCK_INTERACTION_RANGE_X;
            case "attribute.name.player.entity_interaction_range" -> ENTITY_INTERACTION_RANGE_X;

            case "attribute.name.generic.crit_chance" -> CRIT_CHANCE_X;
            case "attribute.name.generic.crit_damage" -> CRIT_DAMAGE_X;
            case "attribute.name.generic.arrow_damage" -> ARROW_DAMAGE_X;
            case "attribute.name.generic.bow_draw_speed" -> BOW_DRAW_SPEED_X;
            case "attribute.name.generic.durability" -> DURABILITY_X;

            default -> ATTACK_X;
        };
    }

    public static int getIconY(String descriptionId) {

        return switch (descriptionId) {

            case "attribute.name.generic.attack_damage" -> ATTACK_Y;
            case "attribute.name.generic.attack_speed" -> ATTACK_SPEED_Y;

            case "attribute.name.generic.armor" -> ARMOR_Y;
            case "attribute.name.generic.armor_toughness" -> ARMOR_TOUGHNESS_Y;
            case "attribute.name.generic.max_health" -> HEALTH_Y;
            case "attribute.name.generic.knockback_resistance" -> KNOCKBACK_RESISTANCE_Y;
            case "attribute.name.generic.luck" -> LUCK_Y;

            case "attribute.name.generic.movement_speed" -> MOVEMENT_SPEED_Y;
            case "attribute.name.generic.block_break_speed" -> BLOCK_BREAK_SPEED_Y;
            case "attribute.name.generic.block_interaction_range" -> BLOCK_INTERACTION_RANGE_Y;
            case "attribute.name.generic.entity_interaction_range" -> ENTITY_INTERACTION_RANGE_Y;

            case "attribute.name.generic.crit_chance" -> CRIT_CHANCE_Y;
            case "attribute.name.generic.crit_damage" -> CRIT_DAMAGE_Y;
            case "attribute.name.generic.arrow_damage" -> ARROW_DAMAGE_Y;
            case "attribute.name.generic.bow_draw_speed" -> BOW_DRAW_SPEED_Y;
            case "attribute.name.generic.durability" -> DURABILITY_Y;

            default -> ATTACK_Y;
        };
    }

    public static ResourceLocation levelTexture(int level) {

        level = Math.clamp(level, 1, 100);

        int page = (level - 1) / 10 + 1;

        System.out.println(
                "Level = " + level +
                        " page = " + page +
                        " u = " + ((level - 1) % 10) * 64
        );

        return ResourceLocation.fromNamespaceAndPath(
                LegendaryTiers.MOD_ID,
                String.format(
                        "textures/gui/tooltip/icons/level_icons_%02d.png",
                        page
                )
        );

    }

    public static int levelU(int level) {

        level = Math.clamp(level, 1, 100);

        int iconIndex = (level - 1) % 10;

        return iconIndex * 64;

    }

    public static int levelV() {
        return 0;
    }
}