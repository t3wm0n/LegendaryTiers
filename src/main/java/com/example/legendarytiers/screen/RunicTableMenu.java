package com.example.legendarytiers.screen;

import com.example.legendarytiers.*;
import com.example.legendarytiers.block.RunicTableBlockEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RunicTableMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerLevelAccess access;
    //private final Player player; // <-- сохраняем игрока

    public RunicTableMenu(int id, Inventory playerInv, RunicTableBlockEntity blockEntity) {
        super(ModMenuTypes.RUNIC_TABLE.get(), id);
        this.container = blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        //this.player = playerInv.player; // <-- запоминаем игрока

        // Слот 0: инструмент/броня
        addSlot(new Slot(container, 0, 88, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModTags.TIERABLE_ITEMS) && stack.has(ModDataComponents.TIER_DATA);
            }
        });
        // Слот 1: чернила
        addSlot(new Slot(container, 1, 44, 78) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.RUNIC_INK.get()) || stack.is(ModItems.NETHER_INK.get()) || stack.is(ModItems.ENDER_INK.get());
            }
        });
        // Слот 2: трафарет
        addSlot(new Slot(container, 2, 132, 77) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == ModItems.WEAPON_STENCIL.get() ||
                        stack.getItem() == ModItems.RANGED_STENCIL.get() ||
                        stack.getItem() == ModItems.ARMOR_STENCIL.get() ||
                        stack.getItem() == ModItems.TOOL_STENCIL.get() ||
                        stack.getItem() == ModItems.SHIELD_STENCIL.get();
            }
        });
        // Слот 3: катализатор
        addSlot(new Slot(container, 3, 88, 118) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return isCatalyst(stack);
            }
        });

        // Инвентарь игрока
        final int playerInvStartX = 15;   // левый верхний угол первого слота (по X)
        final int playerInvStartY = 165;  // левый верхний угол первого слота (по Y)
        final int slotSpacing = 18;      // расстояние между слотами (можно уменьшить до 17)

// 3 ряда инвентаря
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlot(new Slot(playerInv, col + row * 9 + 9,
                        playerInvStartX + col * slotSpacing,
                        playerInvStartY + row * (slotSpacing + 1 )));
            }
        }
// Горячий ряд (нижний)
        for (int col = 0; col < 9; ++col) {
            addSlot(new Slot(playerInv, col,
                    playerInvStartX + col * slotSpacing,
                    playerInvStartY + 60));
        }
    }

    private boolean isCatalyst(ItemStack stack) {
        return stack.is(net.minecraft.world.item.Items.RABBIT_HIDE) ||
                stack.is(net.minecraft.world.item.Items.GHAST_TEAR) ||
                stack.is(net.minecraft.world.item.Items.ENDER_EYE) ||
                stack.is(net.minecraft.world.item.Items.HEART_OF_THE_SEA) ||
                stack.is(net.minecraft.world.item.Items.ECHO_SHARD) ||
                stack.is(net.minecraft.world.item.Items.POPPED_CHORUS_FRUIT) ||
                stack.is(net.minecraft.world.item.Items.NETHER_STAR);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            performReforge(player);
            return true;
        }
        return super.clickMenuButton(player, id);
    }

    private void performReforge(Player player) {
        if (player.level().isClientSide()) return;

        ItemStack toolStack = container.getItem(0);
        ItemStack inkStack = container.getItem(1);
        ItemStack stencilStack = container.getItem(2);
        ItemStack catalystStack = container.getItem(3);

        if (toolStack.isEmpty() || inkStack.isEmpty() || stencilStack.isEmpty()) return;

        TierData tier = toolStack.get(ModDataComponents.TIER_DATA);
        if (tier == null) return;

        String requiredStencil = getRequiredStencil(toolStack);
        if (!isMatchingStencil(stencilStack, requiredStencil)) return;

        int attempts = toolStack.getOrDefault(ModDataComponents.REFORGE_ATTEMPTS, 0);
        if (attempts >= 3) return;

        Rarity maxRarity = getMaxRarity(inkStack);
        if (tier.rarity().ordinal() >= maxRarity.ordinal()) return;

        double baseChance = 0.3 + player.getLuck() * 0.01;
        double catalystBonus = getCatalystBonus(catalystStack, toolStack);
        double successChance = baseChance + catalystBonus;

        if (isGuaranteed(catalystStack, toolStack)) {
            successChance = 1.0;
        }

        boolean success = player.level().random.nextDouble() < successChance;

        if (success) {
            int newIndex = Math.min(tier.rarity().ordinal() + 1, maxRarity.ordinal());
            if (catalystStack.is(net.minecraft.world.item.Items.NETHER_STAR)) {
                newIndex = Math.min(newIndex + 1, Rarity.values().length - 1);
            }
            Rarity newRarity = Rarity.values()[newIndex];
            TierData newTier = TierModifierLoader.generate(toolStack, newRarity, player.level().random);
            toolStack.set(ModDataComponents.TIER_DATA, newTier);

            if (!catalystStack.isEmpty()) {
                catalystStack.shrink(1);
            }

            // 🔊 Звук успеха
            player.level().playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            // 🔇 Звук неудачи
            player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_BREAK, SoundSource.BLOCKS, 0.5F, 1.0F);
        }

        inkStack.shrink(1);
        stencilStack.shrink(1);
        toolStack.set(ModDataComponents.REFORGE_ATTEMPTS, attempts + 1);

        container.setChanged();
        if (container instanceof RunicTableBlockEntity be) {
            be.setChanged();
            be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
        }
    }

    private Rarity getMaxRarity(ItemStack ink) {
        if (ink.is(ModItems.RUNIC_INK.get())) return Rarity.LEGENDARY;
        if (ink.is(ModItems.NETHER_INK.get())) return Rarity.MYTHIC;
        if (ink.is(ModItems.ENDER_INK.get())) return Rarity.DIVINE;
        return Rarity.COMMON;
    }

    private String getRequiredStencil(ItemStack tool) {
        if (tool.is(ModTags.WEAPON)) return "weapon";
        if (tool.is(ModTags.RANGED_WEAPON)) return "ranged";
        if (tool.is(ModTags.ARMOR)) return "armor";
        if (tool.is(ModTags.TOOL)) return "tool";
        if (tool.is(ModTags.SHIELD)) return "shield";
        return "";
    }

    private boolean isMatchingStencil(ItemStack stencil, String type) {
        return switch (type) {
            case "weapon" -> stencil.is(ModItems.WEAPON_STENCIL.get());
            case "ranged" -> stencil.is(ModItems.RANGED_STENCIL.get());
            case "armor" -> stencil.is(ModItems.ARMOR_STENCIL.get());
            case "tool" -> stencil.is(ModItems.TOOL_STENCIL.get());
            case "shield" -> stencil.is(ModItems.SHIELD_STENCIL.get());
            default -> false;
        };
    }

    private double getCatalystBonus(ItemStack catalyst, ItemStack tool) {
        if (catalyst.is(net.minecraft.world.item.Items.RABBIT_HIDE)) return 0.1;
        if (catalyst.is(net.minecraft.world.item.Items.GHAST_TEAR)) return 0.2;
        if (catalyst.is(net.minecraft.world.item.Items.ENDER_EYE)) return 0.3;
        return 0;
    }

    private boolean isGuaranteed(ItemStack catalyst, ItemStack tool) {
        if (catalyst.is(net.minecraft.world.item.Items.HEART_OF_THE_SEA) &&
                (tool.is(ModTags.ARMOR) || tool.is(ModTags.SHIELD))) return true;
        if (catalyst.is(net.minecraft.world.item.Items.ECHO_SHARD) &&
                tool.is(ModTags.WEAPON)) return true;
        if (catalyst.is(net.minecraft.world.item.Items.POPPED_CHORUS_FRUIT) &&
                tool.is(ModTags.TOOL)) return true;
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Если слот из рунного стола (0-3) – переносим в инвентарь игрока
            if (index < 4) {
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
            }
            // Если слот из инвентаря игрока – пытаемся перенести в подходящий слот стола
            else {
                // Сначала пробуем в слоты 0-3 по порядку
                if (!this.moveItemStackTo(itemstack1, 0, 4, false)) {
                    // Если не получилось, переносим в инвентарь игрока (из хотбара в основной и наоборот)
                    if (index < 31) {
                        if (!this.moveItemStackTo(itemstack1, 31, 40, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 4, 31, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}