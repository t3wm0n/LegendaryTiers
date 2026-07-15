package com.example.legendarytiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record ModifierEntry(String target, Optional<String> attribute, String operation, double value) {
    public static final Codec<ModifierEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("target", "attribute").forGetter(ModifierEntry::target),
                    Codec.STRING.optionalFieldOf("attribute").forGetter(ModifierEntry::attribute),
                    Codec.STRING.fieldOf("operation").forGetter(ModifierEntry::operation),
                    Codec.DOUBLE.fieldOf("value").forGetter(ModifierEntry::value)
            ).apply(instance, ModifierEntry::new)
    );
}
