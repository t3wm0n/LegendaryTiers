package com.example.legendarytiers;

import com.example.legendarytiers.block.RunicTableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LegendaryTiers.MOD_ID);

    public static final Supplier<BlockEntityType<RunicTableBlockEntity>> RUNIC_TABLE =
            BLOCK_ENTITIES.register("runic_table",
                    () -> BlockEntityType.Builder.of(RunicTableBlockEntity::new, ModBlocks.RUNIC_TABLE.get()).build(null));
}
