package com.example.legendarytiers.mixin;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.TierData;
import com.example.legendarytiers.util.TierAttributeHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Redirect(
            method = "onUseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/CrossbowItem;getChargeDuration(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)I"
            )
    )
    private int legendarytiers$chargeDuration(ItemStack stack, LivingEntity entity) {

        int original = CrossbowItem.getChargeDuration(stack, entity);

        double speed = TierAttributeHelper.getAttribute(
                stack,
                "legendarytiers:generic.bow_draw_speed",
                1.0
        );

        return Math.max(1, (int)Math.round(original / speed));
    }
}