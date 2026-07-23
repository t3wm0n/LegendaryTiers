package com.example.legendarytiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record TierData(Rarity rarity, List<ModifierEntry> modifiers, float quality) {

    public static final Codec<TierData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Rarity.CODEC.fieldOf("rarity").forGetter(TierData::rarity),
                    ModifierEntry.CODEC.listOf().fieldOf("modifiers").forGetter(TierData::modifiers),
                    Codec.FLOAT.optionalFieldOf("quality", -1.0f).forGetter(TierData::quality)
            ).apply(instance, TierData::new)
    );

    public static final StreamCodec<ByteBuf, TierData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
}