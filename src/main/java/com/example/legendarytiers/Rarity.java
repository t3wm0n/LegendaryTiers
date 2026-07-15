package com.example.legendarytiers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum Rarity {
    COMMON(ChatFormatting.AQUA, 60.0, "rarity.legendarytiers.common"),        // Обычный (голубой)
    RARE(ChatFormatting.BLUE, 29.4, "rarity.legendarytiers.rare"),          // Редкий (синий)
    EPIC(ChatFormatting.DARK_PURPLE, 8.0, "rarity.legendarytiers.epic"),   // Эпический (фиолетовый)
    LEGENDARY(ChatFormatting.GOLD, 2.0, "rarity.legendarytiers.legendary"),      // Легендарный (жёлто-золотой)
    MYTHIC(ChatFormatting.RED, 0.5, "rarity.legendarytiers.mythic"),          // Мифический (красный)
    DIVINE(ChatFormatting.WHITE, 0.1, "rarity.legendarytiers.divine");        // Божественный (бело-радужный, цвет пока белый)

    public static final Codec<Rarity> CODEC = Codec.STRING.xmap(Rarity::valueOf, Rarity::name);
    public static final StreamCodec<ByteBuf, Rarity> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
    private final ChatFormatting color;
    private final double baseChance;
    private String translationKey = "";

    Rarity(ChatFormatting color, double baseChance, String translationKey) {
        this.color = color;
        this.baseChance = baseChance;
        this.translationKey = translationKey;
    }

    public ChatFormatting getColor() {
        return color;
    }
    public double getBaseChance() {
        return baseChance;
    }
    public String getTranslationKey() { return translationKey; }

    public Component getDisplayName() {
        return Component.translatable(translationKey).withStyle(color);
    }

    public static Rarity getRandomRarity(Player player, RandomSource random) {
        // Определяем уровень везения (атрибут Luck)
        float luck = 0;
        if (player != null) {
            luck = player.getLuck(); // базовое везение 0
        }

        // Базовые шансы копируем в массив
        double[] chances = new double[values().length];
        for (int i = 0; i < values().length; i++) {
            chances[i] = values()[i].baseChance;
        }

        // За каждый уровень везения увеличиваем шансы для EPIC, LEGENDARY, MYTHIC, DIVINE на 1%
        int luckPercent = Math.round(luck); // целые уровни везения
        for (int i = EPIC.ordinal(); i < values().length; i++) {
            chances[i] += luckPercent * 0.5;
        }

        // Уменьшаем шанс COMMON на сумму всех добавленных процентов, чтобы сумма осталась 100%
        double bonusSum = 0;
        for (int i = EPIC.ordinal(); i < values().length; i++) {
            bonusSum += luckPercent * 0.5;
        }
        chances[COMMON.ordinal()] = Math.max(0, chances[COMMON.ordinal()] - bonusSum);

        // Нормализуем до 100% (если из-за округлений вышло больше 100, пропорционально уменьшим)
        double total = 0;
        for (double c : chances) total += c;
        if (total > 100) {
            for (int i = 0; i < chances.length; i++) {
                chances[i] = chances[i] * 100.0 / total;
            }
        }

        // Теперь выбираем случайное число от 0 до 100
        double roll = random.nextDouble() * 100.0;
        double cumulative = 0;
        for (int i = 0; i < values().length; i++) {
            cumulative += chances[i];
            if (roll < cumulative) {
                return values()[i];
            }
        }
        // Если из-за погрешности ничего не выбрали — возвращаем COMMON
        return COMMON;
    }
}
