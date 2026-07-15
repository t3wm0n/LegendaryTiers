package com.example.legendarytiers.mixin;

import com.example.legendarytiers.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingMenu.class)
public class CraftingMenuQuickMoveMixin {
    @Inject(method = "quickMoveStack", at = @At("RETURN"))
    private void onQuickMoveStack(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (player.level().isClientSide()) return;
        System.out.println("CraftingMenu.quickMoveStack");
        // После перемещения проверяем все слоты инвентаря игрока (слоты 0-35)
        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(ModTags.TIERABLE_ITEMS) && !stack.has(ModDataComponents.TIER_DATA)) {
                Rarity rarity = Rarity.getRandomRarity(player, player.level().random);
                TierData data = TierModifierLoader.generate(stack, rarity, player.level().random);
                stack.set(ModDataComponents.TIER_DATA, data);
                stack.set(ModDataComponents.EXPERIENCE, 0);
            }
        }
    }
}
