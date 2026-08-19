package com.example.legendarytiers.mixin;

import com.example.legendarytiers.*;
import com.example.legendarytiers.event.ModEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
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
        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier == null) return;

        // Достаем дефолтную прочность предмета из базового реестра компонентов
        Integer baseObj = stack.getItem().components().get(DataComponents.MAX_DAMAGE);
        int base = (baseObj != null) ? baseObj : 0;
        if (base <= 0) return;

        // Вызываем общую функцию расчета
        int calculatedMax = ModEvents.calculateMaxDamage(stack, base, tier);
        cir.setReturnValue(calculatedMax);
    }
}