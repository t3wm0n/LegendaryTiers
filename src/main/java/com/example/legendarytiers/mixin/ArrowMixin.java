package com.example.legendarytiers.mixin;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.TierData;
import com.example.legendarytiers.util.TierAttributeHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractArrow.class)
public abstract class ArrowMixin {

    @Shadow
    @Final
    private ItemStack firedFromWeapon;

    @ModifyArg(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            index = 1
    )
    private float legendarytiers$modifyArrowDamage(float damage) {

        if (firedFromWeapon == null || firedFromWeapon.isEmpty())
            return damage;

        double multiplier = TierAttributeHelper.getAttribute(
                firedFromWeapon,
                "legendarytiers:generic.arrow_damage",
                1.0
        );

        float result = damage * (float) multiplier;

        System.out.println("===== Arrow Damage =====");
        System.out.println("Weapon: " + firedFromWeapon.getHoverName().getString());
        System.out.println("Multiplier: " + multiplier);
        System.out.println("Original: " + damage);
        System.out.println("Final: " + result);
        System.out.println("========================");

        return result;
    }
}