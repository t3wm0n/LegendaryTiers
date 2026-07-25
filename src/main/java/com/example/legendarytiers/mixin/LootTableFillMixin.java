package com.example.legendarytiers.mixin;

import com.example.legendarytiers.*;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootTable.class)
public class LootTableFillMixin {

    @Inject(method = "fill", at = @At("RETURN"))
    private void onFill(Container container, LootParams params, long seed, CallbackInfo ci) {
        Player player = null;
        if (params.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player p) {
            player = p;
        } else if (params.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER) instanceof Player p) {
            player = p;
        }

        net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(ModTags.TIERABLE_ITEMS) && !stack.has(ModDataComponents.TIER_DATA)) {
                Rarity rarity = Rarity.getRandomRarity(player, random);
                TierData data = TierModifierLoader.generate(stack, rarity, random);
                stack.set(ModDataComponents.TIER_DATA, data);
                stack.set(ModDataComponents.EXPERIENCE, 0);
            }
        }

        if (random.nextFloat() < 0.05f) {
            // Ищем свободный слот
            int freeSlot = -1;
            for (int i = 0; i < container.getContainerSize(); i++) {
                if (container.getItem(i).isEmpty()) {
                    freeSlot = i;
                    break;
                }
            }
            if (freeSlot != -1) {
                ItemStack stencil = switch (random.nextInt(5)) {
                    case 0 -> new ItemStack(ModItems.WEAPON_STENCIL.get());
                    case 1 -> new ItemStack(ModItems.RANGED_STENCIL.get());
                    case 2 -> new ItemStack(ModItems.ARMOR_STENCIL.get());
                    case 3 -> new ItemStack(ModItems.TOOL_STENCIL.get());
                    default -> new ItemStack(ModItems.SHIELD_STENCIL.get());
                };
                container.setItem(freeSlot, stencil);
            }
        }
    }
}
