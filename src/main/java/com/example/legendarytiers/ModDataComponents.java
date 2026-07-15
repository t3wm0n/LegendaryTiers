package com.example.legendarytiers;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> REGISTER =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, LegendaryTiers.MOD_ID);

    // Компонент редкости
    public static final Supplier<DataComponentType<TierData>> TIER_DATA =
            REGISTER.register("tier_data", () -> DataComponentType.<TierData>builder()
                    .persistent(TierData.CODEC)
                    .networkSynchronized(TierData.STREAM_CODEC)
                    .build());

    // Компонент опыта (целое число)
    public static final Supplier<DataComponentType<Integer>> EXPERIENCE =
            REGISTER.register("experience", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static final Supplier<DataComponentType<ResourceLocation>> ORIGINAL_ITEM_ID =
            REGISTER.register("original_item_id", () -> DataComponentType.<ResourceLocation>builder()
                    .persistent(ResourceLocation.CODEC)
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
                    .build());

    public static final Supplier<DataComponentType<Integer>> REFORGE_ATTEMPTS =
            REGISTER.register("reforge_attempts", () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build());

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
