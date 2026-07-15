package com.example.legendarytiers;

import com.example.legendarytiers.block.RunicTableBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, LegendaryTiers.MOD_ID);

    public static final Supplier<Block> RUNIC_TABLE = BLOCKS.register("runic_table",
            () -> new RunicTableBlock(Block.Properties.of().strength(2.5F).sound(SoundType.WOOD).noOcclusion()));
}