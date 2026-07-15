package com.example.legendarytiers.mixin;

import com.example.legendarytiers.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slot.class)
public class SlotMixin {
    @Inject(method = "onTake", at = @At("HEAD"))
    private void onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (player.level().isClientSide()) return;
        if (stack.is(ModTags.TIERABLE_ITEMS) && !stack.has(ModDataComponents.TIER_DATA)) {
            Rarity rarity = Rarity.getRandomRarity(player, player.level().random);
            TierData data = TierModifierLoader.generate(stack, rarity, player.level().random);
            stack.set(ModDataComponents.TIER_DATA, data);
            stack.set(ModDataComponents.EXPERIENCE, 0);
        }
    }
}
