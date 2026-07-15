package com.example.legendarytiers.mixin;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    private void onTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (!stack.is(ModItems.BROKEN_ITEM.get())) return;

        AnvilMenu self = (AnvilMenu) (Object) this;
        ItemStack left = self.getSlot(0).getItem();
        ResourceLocation originalId = left.get(ModDataComponents.ORIGINAL_ITEM_ID);
        if (originalId == null) return;

        Item originalItem = BuiltInRegistries.ITEM.get(originalId);
        if (originalItem == null) return;

        int requiredAmount = getMaterialCost(originalItem);
        ItemStack right = self.getSlot(1).getItem();
        right.shrink(requiredAmount);
        self.getSlot(1).setChanged();
        left.shrink(1);
        right.shrink(requiredAmount);
        ci.cancel();
        left.shrink(1);
        right.shrink(requiredAmount);
        if (requiredAmount > 1) {
            right.shrink(requiredAmount - 1);
        }
    }

    private static int getMaterialCost(Item item) {
        int maxDurability = new ItemStack(item).getMaxDamage();
        if (maxDurability <= 0) return 2;
        if (maxDurability < 100) return 1;
        else if (maxDurability < 300) return 2;
        else if (maxDurability < 800) return 3;
        else if (maxDurability < 2000) return 4;
        else return 5;
    }
}
