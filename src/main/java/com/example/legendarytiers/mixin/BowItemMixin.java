package com.example.legendarytiers.mixin;

import com.example.legendarytiers.TierData;
import com.example.legendarytiers.ModAttributes;
import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModifierEntry;
import com.example.legendarytiers.util.TierAttributeHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowItem.class)
public class BowItemMixin {

    @Redirect(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"
            )
    )
    private float legendarytiers$modifyPower(
            int charge,
            ItemStack stack,
            Level level,
            LivingEntity entity,
            int timeLeft
    ) {

        double speed = TierAttributeHelper.getAttribute(
                stack,
                "legendarytiers:generic.bow_draw_speed",
                1.0
        );

        int modifiedCharge = (int) Math.round(charge * speed);

        return BowItem.getPowerForTime(modifiedCharge);
    }


}