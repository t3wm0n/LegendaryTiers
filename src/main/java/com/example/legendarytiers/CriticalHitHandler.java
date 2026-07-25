package com.example.legendarytiers;

import com.example.legendarytiers.util.TierAttributeHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public class CriticalHitHandler {

    @SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event) {

        if (!(event.getSource().getEntity() instanceof LivingEntity attacker))
            return;

        ItemStack weapon = attacker.getMainHandItem();

        if (weapon.isEmpty())
            return;

        double critChance = TierAttributeHelper.getAttribute(
                weapon,
                "legendarytiers:generic.crit_chance",
                0.05
        );

        double critDamage = TierAttributeHelper.getAttribute(
                weapon,
                "legendarytiers:generic.crit_damage",
                0.5
        );

        System.out.println("========== CRIT ==========");
        System.out.println("Weapon: " + weapon.getHoverName().getString());
        System.out.println("Chance: " + critChance);
        System.out.println("Crit Damage Bonus: " + critDamage);

        if (attacker.getRandom().nextDouble() >= critChance) {
            System.out.println("No crit");
            System.out.println("==========================");
            return;
        }

        float damage = event.getAmount();

        // x1.5 + бонус от атрибута
        damage *= (float)(1.5 + critDamage);

        event.setAmount(damage);

        attacker.level().playSound(
                null,
                attacker.blockPosition(),
                SoundEvents.PLAYER_ATTACK_STRONG,
                SoundSource.PLAYERS,
                1.5F,
                0.8F
        );

        attacker.level().playSound(
                null,
                attacker.blockPosition(),
                SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.PLAYERS,
                1.45F,
                1.9F
        );

        if (attacker.level() instanceof ServerLevel level) {
            level.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    attacker.getX(),
                    attacker.getY() + attacker.getBbHeight() * 0.5,
                    attacker.getZ(),
                    18,
                    0.25,
                    0.35,
                    0.25,
                    0.02
            );

            level.sendParticles(
                    ParticleTypes.ENCHANTED_HIT,
                    attacker.getX(),
                    attacker.getY() + attacker.getBbHeight() * 0.5,
                    attacker.getZ(),
                    25,
                    0.15,
                    0.2,
                    0.15,
                    0.02
            );

            level.sendParticles(
                    ParticleTypes.CRIT,
                    attacker.getX(),
                    attacker.getY() + attacker.getBbHeight() * 0.5,
                    attacker.getZ(),
                    10,
                    0.2,
                    0.3,
                    0.2,
                    0.05
            );
        }

        System.out.println("CRITICAL HIT!");
        System.out.println("Final Damage: " + damage);
        System.out.println("==========================");
    }
}