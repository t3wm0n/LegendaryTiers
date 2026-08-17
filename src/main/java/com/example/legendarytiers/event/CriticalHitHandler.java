package com.example.legendarytiers.event;

import com.example.legendarytiers.LegendaryTiers;
import com.example.legendarytiers.ModAttributes;
import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.TierData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public class CriticalHitHandler {

    private static final RandomSource RANDOM = RandomSource.create();

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0) {
            return;
        }

        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        Entity directEntity = event.getSource().getDirectEntity();
        boolean isRanged = directEntity instanceof Projectile;

        // 1. Проверка шанса критического удара

        var critAttribute = attacker.getAttribute(ModAttributes.CRIT_CHANCE);
        if (critAttribute == null) {
            return;
        }

        double critChance = critAttribute.getValue();
        if (critChance <= 0 || RANDOM.nextDouble() >= critChance) {
            return; // Крит не прошел
        }

        // 2. Расчет урона
        var critDamageAttr = attacker.getAttribute(ModAttributes.CRIT_DAMAGE);
        double critBonus = (critDamageAttr != null) ? critDamageAttr.getValue() : 0.5;
        float baseDamage = event.getAmount();
        float finalDamage = baseDamage * (float) (1.0 + critBonus);
        event.setAmount(finalDamage);

        // 3. Запуск звуков и частиц
        ItemStack weapon = getWeapon(attacker, isRanged, directEntity);

        if (target.level() instanceof ServerLevel serverLevel) {
            playTierCritEffects(serverLevel, target, weapon);
        }
    }

    private static @NotNull ItemStack getWeapon(LivingEntity attacker, boolean isRanged, Entity directEntity) {
        ItemStack weapon = ItemStack.EMPTY;

        if (isRanged && directEntity instanceof AbstractArrow arrow) {
            // В 1.21.1 у AbstractArrow есть поле/метод получения оружия, из которого выстрелили
            weapon = arrow.getWeaponItem();
        }

        // Если оружие не удалось достать из стрелы — берем из руки игрока
        if (weapon.isEmpty()) {
            weapon = attacker.getMainHandItem();
            if (weapon.isEmpty()) {
                weapon = attacker.getOffhandItem();
            }
        }
        return weapon;
    }

    private static void playTierCritEffects(ServerLevel level, LivingEntity target, ItemStack weapon) {
        // Получаем имя редкости безопасным путем
        String tierName = "common";
        TierData tier = weapon.get(ModDataComponents.TIER_DATA);
        if (tier != null && tier.rarity() != null) {
            tierName = tier.rarity().name().toLowerCase();
        }

        double x = target.getX();
        double y = target.getY() + (target.getBbHeight() * 0.5);
        double z = target.getZ();

        switch (tierName) {
            case "rare" -> {
                // --- RARE ---
                // Звук: Быстрый, сочный, резкий клик
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundSource.PLAYERS, 1.2F, 1.1F);
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundSource.PLAYERS, 0.9F, 1.2F);

                // Частицы: Синий зачарованный удар + немного электричества
                level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 18, 0.25, 0.25, 0.25, 0.08);
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 8, 0.2, 0.2, 0.2, 0.05);
            }

            case "epic" -> {
                // --- EPIC ---
                // Звук: Тяжелый рассекающий удар + звонкий акцент
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG,
                        SoundSource.PLAYERS, 1.2F, 0.9F);
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundSource.PLAYERS, 1.3F, 0.8F);
                level.playSound(null, target.blockPosition(), SoundEvents.TRIDENT_HIT_GROUND,
                        SoundSource.PLAYERS, 0.5F, 1.4F);

                // Частицы: Двойная волна (Крит + Зачарование + Яркие искры)
                level.sendParticles(ParticleTypes.CRIT, x, y, z, 20, 0.3, 0.3, 0.3, 0.12);
                level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 15, 0.25, 0.25, 0.25, 0.08);
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 15, 0.25, 0.25, 0.25, 0.08);
            }

            case "legendary" -> {
                // --- LEGENDARY ---
                level.playSound(null, target.blockPosition(), SoundEvents.MACE_SMASH_GROUND_HEAVY,
                        SoundSource.PLAYERS, 0.8F, 0.9F);
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundSource.PLAYERS, 1.4F, 0.75F);
                level.playSound(null, target.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS, 0.7F, 1.7F);

                level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0, 0, 0, 0); // Сияющий импульс
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 20, 0.3, 0.3, 0.3, 0.08);
                level.sendParticles(ParticleTypes.CRIT, x, y, z, 25, 0.3, 0.3, 0.3, 0.15);
                level.sendParticles(ParticleTypes.SOUL, x, y, z, 12, 0.2, 0.2, 0.2, 0.05); // Огонь душ
            }

            case "mythic" -> {
                // --- MYTHIC ---
                // Звук: Тяжелая булава + Резонирующий гул якоря
                level.playSound(null, target.blockPosition(), SoundEvents.MACE_SMASH_GROUND_HEAVY,
                        SoundSource.PLAYERS, 1.0F, 0.75F);
                level.playSound(null, target.blockPosition(), SoundEvents.RESPAWN_ANCHOR_CHARGE,
                        SoundSource.PLAYERS, 0.8F, 1.2F);
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundSource.PLAYERS, 1.5F, 0.6F);

                // Частицы: Вспышка + Фиолетовая магия + Вход в портал (темная энергия)
                level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0, 0, 0, 0);
                level.sendParticles(ParticleTypes.WITCH, x, y, z, 25, 0.3, 0.3, 0.3, 0.08);
                level.sendParticles(ParticleTypes.REVERSE_PORTAL, x, y, z, 20, 0.35, 0.35, 0.35, 0.05);
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 20, 0.3, 0.3, 0.3, 0.08);
            }

            case "divine" -> {
                // --- DIVINE (Мифический тир) ---
                level.playSound(null, target.blockPosition(), SoundEvents.MACE_SMASH_GROUND_HEAVY,
                        SoundSource.PLAYERS, 1.2F, 0.65F);
                level.playSound(null, target.blockPosition(), SoundEvents.TRIDENT_THUNDER.value(),
                        SoundSource.PLAYERS, 0.6F, 1.4F);
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundSource.PLAYERS, 1.6F, 0.5F);

                level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0, 0, 0, 0);
                level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 25, 0.35, 0.35, 0.35, 0.06);
                level.sendParticles(ParticleTypes.DRAGON_BREATH, x, y, z, 20, 0.3, 0.3, 0.3, 0.05);
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 25, 0.4, 0.4, 0.4, 0.1);
            }

            default -> {
                // --- COMMON / UNCOMMON ---
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundSource.PLAYERS, 1.0F, 1.0F);

                level.sendParticles(ParticleTypes.CRIT, x, y, z, 12, 0.2, 0.2, 0.2, 0.08);
                level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 8, 0.15, 0.15, 0.15, 0.05);
            }
        }
    }
}



