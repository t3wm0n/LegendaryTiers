package com.example.legendarytiers;

import com.example.legendarytiers.block.RunicTableBlockEntity;
import com.example.legendarytiers.screen.RunicTableMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, LegendaryTiers.MOD_ID);

    public static final Supplier<MenuType<RunicTableMenu>> RUNIC_TABLE =
            MENU_TYPES.register("runic_table",
                    () -> IMenuTypeExtension.create((windowId, inv, data) -> {
                        net.minecraft.core.BlockPos pos = data.readBlockPos();
                        RunicTableBlockEntity be = (RunicTableBlockEntity) inv.player.level().getBlockEntity(pos);
                        return new RunicTableMenu(windowId, inv, be);
                    }));
}