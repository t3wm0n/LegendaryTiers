package com.example.legendarytiers.client.tooltip.helpers;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ItemBarHelper {

    public record BarInfo(
            int current,
            int max,
            boolean isEnergy,
            String label,
            int mainColor,
            int bgColor
    ) {
        public float getRatio() {
            return max > 0 ? Math.clamp((float) current / max, 0.0f, 1.0f) : 0.0f;
        }
    }

    public static BarInfo getBarInfo(ItemStack stack) {
        if (stack.isEmpty()) return null;

        // 1. Проверка на Энергию (NeoForge Energy / FE / RF)
        IEnergyStorage energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energy != null && energy.getMaxEnergyStored() > 0) {
            int current = energy.getEnergyStored();
            int max = energy.getMaxEnergyStored();

            // Электрический голубой / неоновый цвет для заряда
            int energyColor = 0xFF00E5FF; // Cyan / Electric Blue
            int energyBg = 0xFF003344;

            return new BarInfo(
                    current,
                    max,
                    true,
                    "Заряд",
                    energyColor,
                    energyBg
            );
        }

        // 2. Стандартная прочность Minecraft
        if (stack.isDamageableItem()) {
            int max = stack.getMaxDamage();
            int current = Math.max(0, max - stack.getDamageValue());
            float ratio = (float) current / max;

            // Динамический цвет прочности (зеленый -> желтый -> красный)
            int durabilityColor = getDurabilityColor(ratio);
            int durabilityBg = 0xFF221111;

            return new BarInfo(
                    current,
                    max,
                    false,
                    "Прочность",
                    durabilityColor,
                    durabilityBg
            );
        }

        return null; // Предмет не имеет ни прочности, ни заряда
    }

    // Красивое форматирование больших чисел энергии (1.5M FE, 250k FE)
    public static String formatValue(int value, boolean isEnergy) {
        if (!isEnergy) return String.valueOf(value);

        if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000.0f);
        } else if (value >= 1_000) {
            return String.format("%.1fk", value / 1_000.0f);
        }
        return String.valueOf(value);
    }

    private static int getDurabilityColor(float ratio) {
        if (ratio > 0.5f) {
            // Зеленый -> Желтый
            int red = (int) ((1.0f - ratio) * 2.0f * 255);
            return (0xFF << 24) | (red << 16) | (0xFF << 8) | 0x55;
        } else {
            // Желтый -> Красный
            int green = (int) (ratio * 2.0f * 255);
            return (0xFF << 24) | (0xFF << 16) | (green << 8) | 0x33;
        }
    }
}