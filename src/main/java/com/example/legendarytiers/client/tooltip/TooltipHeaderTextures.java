package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.Rarity;
import net.minecraft.resources.ResourceLocation;

public final class TooltipHeaderTextures {

    private TooltipHeaderTextures() {}

    public static ResourceLocation get(Rarity rarity) {

        return switch (rarity) {

            case COMMON ->
                    texture("common");

            case RARE ->
                    texture("rare");

            case EPIC ->
                    texture("epic");

            case LEGENDARY ->
                    texture("legendary");

            case MYTHIC ->
                    texture("mythic");

            case DIVINE ->
                    texture("divine");

        };

    }

    private static ResourceLocation texture(String name) {

        return ResourceLocation.fromNamespaceAndPath(

                LegendaryTiers.MOD_ID,

                "textures/gui/tooltip/header/" + name + ".png"

        );

    }

}