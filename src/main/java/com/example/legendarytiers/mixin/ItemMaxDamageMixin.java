package com.example.legendarytiers.mixin;

import com.example.legendarytiers.*;
import com.example.legendarytiers.util.ExperienceUtil;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemMaxDamageMixin {

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void modifyMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        int base = cir.getReturnValue();
        if (base <= 0) return;

        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier == null) return;

        int exp = stack.getOrDefault(ModDataComponents.EXPERIENCE, 0);
        int level = ExperienceUtil.getLevel(exp);
        double levelMultiplier = 1.0 + (level * 0.01);

        double durabilityMult = 1.0;
        int durabilityAdd = 0;

        for (ModifierEntry entry : tier.modifiers()) {
            if (!entry.target().equals("durability")) continue;
            double val = entry.value();
            switch (entry.operation()) {
                case "multiply_total", "multiply_base" ->
                        durabilityMult *= (1.0 + val * levelMultiplier);
                case "addition" ->
                        durabilityAdd += (int)(val * levelMultiplier);
            }
        }

        int newMax = (int) (base * durabilityMult) + durabilityAdd;
        cir.setReturnValue(Math.max(1, newMax));
    }
}
