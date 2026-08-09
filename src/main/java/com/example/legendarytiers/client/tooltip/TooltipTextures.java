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

    public static final ResourceLocation DIVIDER =
            rl(
                    "textures/gui/tooltip/background/divider.png"
            );

    public static final ResourceLocation FRAME_CORNER_LU =
            rl(
                    "textures/gui/tooltip/frame/frame_corner_lu.png"
            );
    public static final ResourceLocation FRAME_CORNER_RU =
            rl(
                    "textures/gui/tooltip/frame/frame_corner_ru.png"
            );
    public static final ResourceLocation FRAME_CORNER_LD =
            rl(
                    "textures/gui/tooltip/frame/frame_corner_ld.png"
            );
    public static final ResourceLocation FRAME_CORNER_RD =
            rl(
                    "textures/gui/tooltip/frame/frame_corner_rd.png"
            );

    public static final ResourceLocation FRAME_EDGE_HU =
            rl(
                    "textures/gui/tooltip/frame/frame_edge_hu.png"
            );
    public static final ResourceLocation FRAME_EDGE_HD =
            rl(
                    "textures/gui/tooltip/frame/frame_edge_hd.png"
            );

    public static final ResourceLocation FRAME_EDGE_VL =
            rl(
                    "textures/gui/tooltip/frame/frame_edge_vl.png"
            );
    public static final ResourceLocation FRAME_EDGE_VR =
            rl(
                    "textures/gui/tooltip/frame/frame_edge_vr.png"
            );

    public static final ResourceLocation HEADER_LEFT =
            rl(
                    "textures/gui/tooltip/header/header_left.png"
            );

    public static final ResourceLocation HEADER_CENTER_L =
            rl(
                    "textures/gui/tooltip/header/header_center_l.png"
            );

    public static final ResourceLocation HEADER_CENTER =
            rl(
                    "textures/gui/tooltip/header/header_center.png"
            );

    public static final ResourceLocation HEADER_CENTER_R =
            rl(
                    "textures/gui/tooltip/header/header_center_r.png"
            );

    public static final ResourceLocation HEADER_RIGHT =
            rl(
                    "textures/gui/tooltip/header/header_right.png"
            );

    public static final ResourceLocation RARITY_STONE =
            rl(
                    "textures/gui/tooltip/header/rarity_stone.png"
            );

    public static final ResourceLocation RARITY_STONE_GLOW =
            rl(
                    "textures/gui/tooltip/header/rarity_stone_glow.png"
            );

    public static final ResourceLocation RARITY_FRAME =
            rl(
                    "textures/gui/tooltip/header/rarity_frame.png"
            );

    public static final ResourceLocation ITEM_FRAME =
            rl(
                    "textures/gui/tooltip/item_frame/item_frame.png"
            );

    public static final ResourceLocation MAGIC_CIRCLE =
            rl(
                    "textures/gui/tooltip/item_frame/magic_circle.png"
            );

    public static final ResourceLocation PARTICLES =
            rl(
                    "textures/gui/tooltip/particles.png"
            );

    public static final ResourceLocation PIC_SHIFT =
            rl(
                    "textures/gui/tooltip/hint/pic_shift.png"
            );

    public static final ResourceLocation PIC_CTRL =
            rl(
                    "textures/gui/tooltip/hint/pic_ctrl.png"
            );


    private static ResourceLocation rl(String path){
        return ResourceLocation.fromNamespaceAndPath(
                LegendaryTiers.MOD_ID,
                path
        );
    }
}