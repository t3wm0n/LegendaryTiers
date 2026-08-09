package com.example.legendarytiers.mixin;

import com.example.legendarytiers.*;
import com.example.legendarytiers.util.ExperienceUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemMaxDamageMixin {

    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
    private void modifyMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        // Если у предмета УЖЕ записан DataComponents.MAX_DAMAGE через новый метод,
        // ничего не делаем — ванильная система Minecraft всё сделает сама!
        if (stack.has(DataComponents.MAX_DAMAGE)) {
            return;
        }

        // Если компонента еще нет (старый предмет), считаем через миксин "на лету":
        int base = stack.getItem().getMaxDamage(stack);
        if (base <= 0) return;

        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier == null) return;

        // Вызываем общую функцию расчета
        int calculatedMax = ModEvents.calculateMaxDamage(stack, base, tier);
        cir.setReturnValue(calculatedMax);
    }
}