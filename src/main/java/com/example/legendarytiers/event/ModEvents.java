package com.example.legendarytiers.event;

import com.example.legendarytiers.*;
import com.example.legendarytiers.command.LegendaryCommand;
import com.example.legendarytiers.config.RPGITConfig;
import com.example.legendarytiers.util.ExperienceUtil;
import com.example.legendarytiers.util.RepairCostHelper;
import com.example.legendarytiers.util.RepairEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.Optional;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public class ModEvents {

    private static final String SPAWNER_TAG = "legendarytiers:from_spawner";

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {

        LegendaryCommand.register(event);

    }

    public static void addExperience(ItemStack stack, int amount, Player player) {
        if (stack.isEmpty() || !stack.is(ModTags.TIERABLE_ITEMS)) return;
        if (player != null && player.level().isClientSide()) return;

        int currentExp = stack.getOrDefault(ModDataComponents.EXPERIENCE, 0);
        int oldLevel = ExperienceUtil.getLevel(currentExp);

        int newExp = currentExp + amount;
        int newLevel = ExperienceUtil.getLevel(newExp);

        stack.set(ModDataComponents.EXPERIENCE, newExp);

        // Пересчитываем прочность ТОЛЬКО при смене уровня (например, с 1 на 2)
        if (newLevel > oldLevel) {
            if (player != null) {
                player.level().playSound(null, player.blockPosition(),
                        SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.5F, 1.5F);
            }

        }
        recalculateMaxDamage(stack);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.containerMenu.broadcastChanges();
        }
    }

    public static void recalculateMaxDamage(ItemStack stack) {
        if (stack.isEmpty()) return;

        // Считываем ЧИСТУЮ базовую прочность предмета (например, 250 для железной кирки)
        Integer baseObj = stack.getItem().components().get(DataComponents.MAX_DAMAGE);
        int base = (baseObj != null) ? baseObj : 0;
        if (base <= 0) return;

        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier == null) {
            stack.remove(DataComponents.MAX_DAMAGE);
            return;
        }

        int calculatedMax = calculateMaxDamage(stack, base, tier);

        // Записываем прямо в компонент Minecraft
        stack.set(DataComponents.MAX_DAMAGE, calculatedMax);
    }

    public static int calculateMaxDamage(ItemStack stack, int base, TierData tier) {
        int exp = stack.getOrDefault(ModDataComponents.EXPERIENCE, 0);
        double levelMultiplier = ExperienceUtil.getMultiplier(exp);

        double durabilityMult = 1.0;
        double durabilityAdd = 0.0;

        for (ModifierEntry entry : tier.modifiers()) {
            if (!entry.target().equals("durability")) continue;

            double scaledVal = getScaledModifierValue(entry, levelMultiplier);

            switch (entry.operation()) {
                case "multiply_total", "multiply_base" -> durabilityMult *= (1.0 + scaledVal);
                case "addition" -> durabilityAdd += scaledVal;
            }
        }

        return Math.max(1, (int) Math.round((base * durabilityMult) + durabilityAdd));
    }

    public static double getScaledModifierValue(ModifierEntry entry, double levelMultiplier) {
        double val = entry.value();
        String target = entry.target().toLowerCase();

        // Проверяем, является ли атрибут инвертированным (чем меньше, тем лучше)
        boolean isInverted = target.contains("gravity");

        if (isInverted) {
            if (val < 0) {
                // БАФФ (уменьшение гравитации): с уровнем гравитация падает ещё сильнее
                return val * levelMultiplier;
            } else {
                // ШТРАФ (увеличение гравитации): с уровнем штраф уменьшается к 0
                return Math.max(0.0, val - (val * (levelMultiplier - 1.0) * 2.0));
            }
        } else {
            if (val > 0) {
                // БАФФ для обычных атрибутов (прочность, урон): растёт с уровнем
                return val * levelMultiplier;
            } else {
                // ШТРАФ для обычных атрибутов: уменьшается к 0
                return Math.min(0.0, val + (Math.abs(val) * (levelMultiplier - 1.0) * 2.0));
            }
        }
    }

    // Опыт за добычу блоков
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getMainHandItem();
        if (tool.isEmpty()) return;

        BlockState state = event.getState();
        if (tool.getItem() instanceof PickaxeItem && tool.isCorrectToolForDrops(state)) {
            int xp = getOreXpFromConfig(state);
            if (xp > 0) addExperience(tool, xp, player);
        }
        else if (state.is(BlockTags.LOGS) && tool.isCorrectToolForDrops(state)) {
            addExperience(tool, RPGITConfig.INSTANCE.experience.axe_xp.get(), player);
        }
        else if (state.is(BlockTags.MINEABLE_WITH_SHOVEL) && tool.isCorrectToolForDrops(state)) {
            addExperience(tool, RPGITConfig.INSTANCE.experience.shovel_xp.get(), player);
        }
    }

    // Опыт за убийство мобов
    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        if (event.getSpawnType() == MobSpawnType.SPAWNER) {
            event.getEntity().getPersistentData().putBoolean(SPAWNER_TAG, true);
        }
    }


    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();

        // Проверяем, что убийца — игрок
        if (!(source.getEntity() instanceof Player player)) {
            return;
        }

        LivingEntity victim = event.getEntity();
        ItemStack weapon = ItemStack.EMPTY;
        Entity directEntity = source.getDirectEntity();

        if (directEntity instanceof AbstractArrow arrow) {
            ItemStack firedWeapon = arrow.getWeaponItem();
            if (firedWeapon != null) weapon = firedWeapon;
        } else if (directEntity instanceof ThrowableItemProjectile throwable) {
            weapon = throwable.getItem();
        }

        if (weapon == null || weapon.isEmpty()) {
            weapon = player.getMainHandItem();
        }

        if (weapon == null || weapon.isEmpty()) {
            return;
        }

        // --- Расчет опыта ---

        // 1. Базовый опыт по максимальному здоровью моба
        float xp = (float) (victim.getMaxHealth() * RPGITConfig.INSTANCE.experience.mob_multiplier.get());

        // 2. Штраф за спавнер (дает только 25% опыта)
        if (victim.getPersistentData().getBoolean(SPAWNER_TAG)) {
            xp *= 0.25f;
        }

        int finalXp = Math.round(xp);

        // 3. Минимальный порог опыта из конфига
        if (finalXp < RPGITConfig.INSTANCE.experience.mob_min.get()) {
            finalXp = RPGITConfig.INSTANCE.experience.mob_min.get();
        }

        // 4. Корректировка для топоров (секир)
        if (weapon.getItem() instanceof AxeItem) {
            finalXp = Math.max(1, finalXp / 2);
        }

        // Начисляем опыт предмету
        addExperience(weapon, finalXp, player);
    }

    // Опыт за вспахивание земли
    @SubscribeEvent
    public static void onHoeUse(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof HoeItem) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT_PATH)
                    || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.ROOTED_DIRT)) {
                addExperience(stack, RPGITConfig.INSTANCE.experience.hoe_xp.get(), event.getEntity());
            }
        }
    }

    // Опыт для брони
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            float damage = event.getAmount();
            if (damage <= 0) return;
            int xpPerPiece = Math.round(damage * RPGITConfig.INSTANCE.experience.armor_xp_per_damage.get() / 2);
            if (xpPerPiece <= 0) return;
            EquipmentSlot[] armorSlots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
            for (EquipmentSlot slot : armorSlots) {
                ItemStack armor = player.getItemBySlot(slot);
                if (!armor.isEmpty() && armor.is(ModTags.TIERABLE_ITEMS)) {
                    addExperience(armor, xpPerPiece, player);
                }
            }
        }
    }

    private static boolean isBroken(ItemStack stack) {
        return stack.is(ModItems.BROKEN_ITEM.get());
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (isBroken(event.getEntity().getMainHandItem())) {
            event.setNewSpeed(0);
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (isBroken(player.getMainHandItem())) {
                event.setNewDamage(0);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (isBroken(event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.BROKEN_ITEM.get());
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {

        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (!left.is(ModItems.BROKEN_ITEM.get())) {
            return;
        }

        ResourceLocation originalId =
                left.get(ModDataComponents.ORIGINAL_ITEM_ID);

        if (originalId == null) {
            return;
        }

        Item originalItem =
                BuiltInRegistries.ITEM.get(originalId);

        if (originalItem == null) {
            return;
        }

        Optional<RepairEntry> repair =
                RepairCostHelper.get(originalItem);

        if (repair.isEmpty()) {
            return;
        }

        RepairEntry info = repair.get();

        /*
         * Проверяем материал ремонта.
         */

        if (!info.ingredient().test(right)) {
            return;
        }

        /*
         * Проверяем количество материала.
         */

        if (right.getCount() < info.amount()) {
            return;
        }

        /*
         * Создаём восстановленный предмет.
         */

        ItemStack result =
                new ItemStack(originalItem);

        result.applyComponents(left.getComponents());

        result.remove(ModDataComponents.ORIGINAL_ITEM_ID);

        result.setDamageValue(0);

        /*
         * Стоимость ремонта.
         */

        TierData tier =
                left.get(ModDataComponents.TIER_DATA);

        int rarityCost =
                tier != null
                        ? tier.rarity().ordinal()
                        : 0;

        int enchantmentCost = 0;

        ItemEnchantments enchantments =
                left.getOrDefault(
                        DataComponents.ENCHANTMENTS,
                        ItemEnchantments.EMPTY
                );

        for (var entry : enchantments.entrySet()) {

            enchantmentCost += entry.getIntValue();

        }

        int enchantmentSurcharge =
                (int) Math.ceil(enchantmentCost * 1.2);

        int totalCost =
                info.amount()
                        + rarityCost
                        + enchantmentSurcharge;

        event.setCost(
                Math.max(
                        1,
                        totalCost
                )
        );

        event.setOutput(result);

    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack cloth = event.getItemStack();
        Player player = event.getEntity();
        if (cloth.is(ModItems.CLEANSING_CLOTH.get())) {
            InteractionHand otherHand = event.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            ItemStack target = player.getItemInHand(otherHand);
            if (!target.isEmpty() && target.has(ModDataComponents.REFORGE_ATTEMPTS) && target.getOrDefault(ModDataComponents.REFORGE_ATTEMPTS, 0) > 0) {
                target.set(ModDataComponents.REFORGE_ATTEMPTS, 0);
                cloth.shrink(1);
                event.setCanceled(true);
                player.level().playSound(
                        null,
                        player.blockPosition(),
                        SoundEvents.BRUSH_GENERIC,
                        SoundSource.PLAYERS,
                        1.50F,
                        1.2F
                );
                if (player.level() instanceof ServerLevel level) {
                    level.sendParticles(
                            ParticleTypes.ENCHANT,
                            player.getX(),
                            player.getY() + player.getBbHeight() * 0.5,
                            player.getZ(),
                            18,
                            0.25,
                            0.35,
                            0.25,
                            0.02
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.TOOLSMITH) {
            event.getTrades().get(3).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 25),
                    new ItemStack(ModItems.TOOL_STENCIL.get()),
                    3, 10, 0.05f
            ));
        }
        else if (event.getType() == VillagerProfession.WEAPONSMITH) {
            var trades = event.getTrades().get(3);
            trades.add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 25),
                    new ItemStack(ModItems.WEAPON_STENCIL.get()),
                    3, 10, 0.05f
            ));
            trades.add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 28),
                    new ItemStack(ModItems.RANGED_STENCIL.get()),
                    3, 10, 0.05f
            ));
        }
        else if (event.getType() == VillagerProfession.ARMORER) {
            var trades = event.getTrades().get(3);
            trades.add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 20),
                    new ItemStack(ModItems.ARMOR_STENCIL.get()),
                    3, 10, 0.05f
            ));
            trades.add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 15),
                    new ItemStack(ModItems.SHIELD_STENCIL.get()),
                    3, 10, 0.05f
            ));
        }
    }

    private static int getOreXpFromConfig(BlockState state) {
        // 1. Получаем список руд напрямую из CONFIG.experience.pickaxe_ores
        for (String entry : RPGITConfig.INSTANCE.experience.pickaxe_ores) {
            String[] parts = entry.split("@");
            if (parts.length != 2) continue;
            String blockOrTag = parts[0];
            int xp;
            try {
                xp = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            if (blockOrTag.startsWith("#")) {
                TagKey<Block> tag = TagKey.create(Registries.BLOCK, ResourceLocation.parse(blockOrTag.substring(1)));
                if (state.is(tag)) return xp;
            } else {
                Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockOrTag));
                if (state.is(block)) return xp;
            }
        }

        // 2. Дефолтный опыт за руды
        if (state.is(TagKey.create(Registries.BLOCK, ResourceLocation.parse("forge:ores"))) ||
                state.is(TagKey.create(Registries.BLOCK, ResourceLocation.parse("c:ores")))) {
            return RPGITConfig.INSTANCE.experience.default_ore_xp.get();
        }

        // 3. Опыт за обычный камень / блоки
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return RPGITConfig.INSTANCE.experience.stone_xp.get();
        }

        return 0;
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {

        RepairCostHelper.buildCache(
                event.getServer().overworld()
        );

    }
}