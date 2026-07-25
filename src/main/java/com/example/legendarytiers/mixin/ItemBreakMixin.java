package com.example.legendarytiers.mixin;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemBreakMixin {

    @Inject(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"), cancellable = true)
    private void onHurtAndBreak(int damage, ServerLevel level, LivingEntity entity,
                                Consumer<Item> onBreak, CallbackInfo ci) {
        if (entity == null || entity.level().isClientSide()) return;

        ItemStack original = (ItemStack) (Object) this;
        if (original.getMaxDamage() == 0) return;

        // Проверяем, сломается ли предмет после этого урона
        if (original.getDamageValue() + damage >= original.getMaxDamage()) {
            // Работаем только если entity – игрок (для нежити сломанные предметы не нужны)
            if (entity instanceof ServerPlayer player) {
                // Создаём сломанный предмет
                ItemStack broken = new ItemStack(ModItems.BROKEN_ITEM.get());
                broken.applyComponents(original.getComponents());
                broken.set(ModDataComponents.ORIGINAL_ITEM_ID, BuiltInRegistries.ITEM.getKey(original.getItem()));
                broken.setDamageValue(broken.getMaxDamage());

                // Добавляем в инвентарь или выбрасываем
                if (!player.addItem(broken)) {
                    player.drop(broken, false);
                }
            }
            // Уничтожаем оригинал (обнуляем количество)
            original.setCount(0);
            // Отменяем стандартное выполнение (shrink и onBreak)
            ci.cancel();
        }
    }
}
