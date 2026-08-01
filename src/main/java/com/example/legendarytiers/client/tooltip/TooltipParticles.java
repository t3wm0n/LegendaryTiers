package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.LegendaryTiers;
import net.minecraft.resources.ResourceLocation;

public final class TooltipParticles {

    private TooltipParticles(){}

    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    LegendaryTiers.MOD_ID,
                    "textures/gui/tooltip/particles.png"
            );

    public static final int PARTICLE_SIZE = 16;

    public static final int FRAMES = 4;

}