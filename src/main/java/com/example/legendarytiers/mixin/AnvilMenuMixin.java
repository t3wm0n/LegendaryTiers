package com.example.legendarytiers.mixin;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.ModItems;
import com.example.legendarytiers.util.RepairCostHelper;
import com.example.legendarytiers.util.RepairEntry;
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

import java.util.Optional;

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
        Optional<RepairEntry> repair =
                RepairCostHelper.get(originalItem);

        if (repair.isEmpty()) {
            return;
        }

        RepairEntry info =
                repair.get();

        ItemStack right =
                self.getSlot(1).getItem();

        if (!info.ingredient().test(right)) {
            return;
        }

        if (right.getCount() < info.amount()) {
            return;
        }

        /*
         * Забираем материалы
         */

        right.shrink(info.amount());
        self.getSlot(1).setChanged();

        /*
         * Удаляем Broken Item
         */

        left.shrink(1);

        /*
         * Отдаём восстановленный предмет
         */

        ItemStack repaired =
                new ItemStack(originalItem);

        repaired.applyComponents(left.getComponents());

        repaired.remove(ModDataComponents.ORIGINAL_ITEM_ID);

        repaired.setDamageValue(0);

        /*
         * Выдаём игроку
         */

        player.addItem(repaired);

        ci.cancel();
    }

}
