package com.example.legendarytiers;

import com.example.legendarytiers.config.RPGITConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.registry.ClientConfigRegistry;
import me.fzzyhmstrs.fzzy_config.screen.ConfigScreenProvider;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod(LegendaryTiers.MOD_ID)
public class LegendaryTiers {
    public static final String MOD_ID = "legendarytiers";

    public LegendaryTiers(IEventBus modEventBus, ModContainer modContainer) {

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(LegendaryTiersClient::registerScreens);
            modEventBus.addListener(LegendaryTiersClient::registerTooltipFactories);
        }
        ModDataComponents.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModAttributes.register(modEventBus);
        RPGITConfig.INSTANCE.getId();
        CREATIVE_MODE_TABS.register(modEventBus);
        TierModifierLoader.loadBuiltin();

    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final Supplier<CreativeModeTab> LEGENDARY_TIERS_TAB = CREATIVE_MODE_TABS.register(
            "legendary_tiers_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.legendarytiers"))
                    .icon(() -> new ItemStack(ModItems.RUNIC_TABLE_ITEM.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.BROKEN_ITEM.get());
                        output.accept(ModItems.RUNIC_TABLE_ITEM.get());
                        output.accept(ModItems.RUNIC_INK.get());
                        output.accept(ModItems.NETHER_INK.get());
                        output.accept(ModItems.ENDER_INK.get());
                        output.accept(ModItems.WEAPON_STENCIL.get());
                        output.accept(ModItems.RANGED_STENCIL.get());
                        output.accept(ModItems.ARMOR_STENCIL.get());
                        output.accept(ModItems.TOOL_STENCIL.get());
                        output.accept(ModItems.SHIELD_STENCIL.get());
                        output.accept(ModItems.CLEANSING_CLOTH.get());
                    })
                    .build()
    );
}
