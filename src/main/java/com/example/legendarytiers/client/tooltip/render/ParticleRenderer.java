package com.example.legendarytiers.client.tooltip.render;

import com.example.legendarytiers.client.tooltip.TooltipParticles;
import net.minecraft.client.gui.GuiGraphics;

public final class ParticleRenderer {

    private ParticleRenderer() {
    }

    public static void draw(

            GuiGraphics graphics,

            int frame,

            int x,

            int y,

            int size

    ) {

        graphics.blit(

                TooltipParticles.TEXTURE,

                x,
                y,

                size,
                size,

                frame * TooltipParticles.PARTICLE_SIZE,
                0,

                TooltipParticles.PARTICLE_SIZE,
                TooltipParticles.PARTICLE_SIZE,

                TooltipParticles.PARTICLE_SIZE * TooltipParticles.FRAMES,
                TooltipParticles.PARTICLE_SIZE

        );

    }

}