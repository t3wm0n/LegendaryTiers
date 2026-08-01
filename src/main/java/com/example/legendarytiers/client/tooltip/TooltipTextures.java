package com.example.legendarytiers.client.tooltip;

import com.example.legendarytiers.LegendaryTiers;
import net.minecraft.resources.ResourceLocation;

public final class TooltipTextures {

    private TooltipTextures() {
    }


    public static final ResourceLocation BACKGROUND =
            rl(
                    "textures/gui/tooltip/background/tooltip_background.png"
            );

    public static final ResourceLocation GLOW =
            rl(
                    "textures/gui/tooltip/glow.png"
            );

    public static final ResourceLocation FRAME_CORNER =
            rl(
                    "textures/gui/tooltip/frame/frame_corner.png"
            );

    public static final ResourceLocation FRAME_EDGE_H =
            rl(
                    "textures/gui/tooltip/frame/frame_edge_h.png"
            );

    public static final ResourceLocation FRAME_EDGE_V =
            rl(
                    "textures/gui/tooltip/frame/frame_edge_v.png"
            );

    public static final ResourceLocation FRAME_SHINE =
            rl(
                    "textures/gui/tooltip/frame/frame_shine.png"
            );

    public static final ResourceLocation HEADER_LEFT =
            rl(
                    "textures/gui/tooltip/header/header_left.png"
            );

    public static final ResourceLocation HEADER_CENTER =
            rl(
                    "textures/gui/tooltip/header/header_center.png"
            );

    public static final ResourceLocation HEADER_RIGHT =
            rl(
                    "textures/gui/tooltip/header/header_right.png"
            );

    public static final int BACKGROUND_FRAME_SIZE = 256;

    public static final int BACKGROUND_FRAMES = 16;

    private static ResourceLocation rl(String path){
        return ResourceLocation.fromNamespaceAndPath(
                LegendaryTiers.MOD_ID,
                path
        );
    }
}