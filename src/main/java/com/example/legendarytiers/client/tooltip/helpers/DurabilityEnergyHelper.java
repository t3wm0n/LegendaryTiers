package com.example.legendarytiers.client.tooltip.helpers;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public final class DurabilityEnergyHelper {

    private DurabilityEnergyHelper() {}

    public enum StorageType {
        ENERGY,
        FLUID,
        DURABILITY,
        NONE
    }

    public record Info(long current, long max, StorageType type, String unit) {
        public float getRatio() {
            return max <= 0 ? 0.0f : (float) ((double) current / max);
        }
    }

    public static Info getDurabilityOrEnergy(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;

        // 1. Попытка #1: Стандартный NeoForge Energy Capability
        try {
            IEnergyStorage energy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energy != null && energy.getMaxEnergyStored() > 0) {
                return new Info(energy.getEnergyStored(), energy.getMaxEnergyStored(), StorageType.ENERGY, "FE/RF");
            }
        } catch (Exception ignored) {}

        // 2. Попытка #2: NeoForge Fluid Capability
        try {
            IFluidHandlerItem fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
            if (fluidHandler != null && fluidHandler.getTanks() > 0) {
                long totalCurrent = 0;
                long totalCapacity = 0;
                String fluidName = "mB";

                for (int i = 0; i < fluidHandler.getTanks(); i++) {
                    FluidStack fluidStack = fluidHandler.getFluidInTank(i);
                    totalCapacity += fluidHandler.getTankCapacity(i);
                    totalCurrent += fluidStack.getAmount();

                    if (!fluidStack.isEmpty() && fluidName.equals("mB")) {
                        fluidName = fluidStack.getHoverName().getString() + " (mB)";
                    }
                }

                if (totalCapacity > 0) {
                    return new Info(totalCurrent, totalCapacity, StorageType.FLUID, fluidName);
                }
            }
        } catch (Exception ignored) {}

        // 3. Попытка #3: Проверка DataComponents & Custom NBT
        Info componentEnergy = getEnergyFromComponents(stack);
        if (componentEnergy != null) {
            return componentEnergy;
        }

        // 4. Попытка #4: Стандартная прочность Minecraft
        if (stack.isDamageableItem() || stack.getMaxDamage() > 0) {
            int maxDamage = stack.getMaxDamage();
            int currentDamage = Math.max(0, maxDamage - stack.getDamageValue());
            return new Info(currentDamage, maxDamage, StorageType.DURABILITY, "");
        }

        return null;
    }

    private static Info getEnergyFromComponents(ItemStack stack) {
        // --- Вариант А: Сканируем кастомные DataComponents предметов ---
        for (TypedDataComponent<?> component : stack.getComponents()) {
            DataComponentType<?> type = component.type();
            ResourceLocation id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);

            if (id != null && id.getNamespace().equals("oritech")) {
                Object value = component.value();
                // Если компонент от Oritech хранит числовое значение энергии
                if (value instanceof Number num) {
                    long cur = num.longValue();
                    return new Info(cur, 2_000_000L, StorageType.ENERGY, "FE/RF");
                }
            }
        }

        // --- Вариант Б: Чтение из NBT / CustomData ---
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null && !customData.isEmpty()) {
            var tag = customData.copyTag();

            if (tag.contains("oritech:energy")) {
                long cur = tag.getLong("oritech:energy");
                long max = tag.contains("oritech:max_energy") ? tag.getLong("oritech:max_energy") : 2_000_000L;
                if (max > 0) return new Info(cur, max, StorageType.ENERGY, "FE/RF");
            }

            if (tag.contains("energy")) {
                long cur = tag.getLong("energy");
                long max = tag.contains("max_energy") ? tag.getLong("max_energy") : (tag.contains("maxEnergy") ? tag.getLong("maxEnergy") : 0L);
                if (max <= 0) max = 100_000L; // Дефолтный фолбэк
                return new Info(cur, max, StorageType.ENERGY, "FE/RF");
            }

            if (tag.contains("Energy")) {
                long cur = tag.getLong("Energy");
                long max = tag.contains("MaxEnergy") ? tag.getLong("MaxEnergy") : 100_000L;
                return new Info(cur, max, StorageType.ENERGY, "FE/RF");
            }
        }

        return null;
    }
}