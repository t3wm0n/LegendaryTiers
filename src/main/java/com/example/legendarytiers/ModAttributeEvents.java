package com.example.legendarytiers;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

@EventBusSubscriber(modid = LegendaryTiers.MOD_ID)
public class ModAttributeEvents {

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        TierData tier = stack.get(ModDataComponents.TIER_DATA);
        if (tier == null) return;

        int exp = stack.getOrDefault(ModDataComponents.EXPERIENCE, 0);
        int level = exp / 100;
        double levelMultiplier = 1.0 + (level * 0.005);

        // Определяем группу слотов по типу предмета
        EquipmentSlotGroup slotGroup = getSlotGroupForItem(stack);

        for (ModifierEntry entry : tier.modifiers()) {
            if (entry.target().equals("durability")) {
                // Прочность обрабатывается в ItemMaxDamageMixin, здесь не добавляем
                continue;
            }
            if (entry.target().equals("attribute") && entry.attribute().isPresent()) {
                ResourceLocation attrId = ResourceLocation.parse(entry.attribute().get());

                // Фильтр block_break_speed: только для инструментов и брони
                if (attrId.getPath().contains("block_break_speed") &&
                        !(stack.getItem() instanceof DiggerItem) &&
                        !(stack.getItem() instanceof ArmorItem)) {
                    continue;
                }

                var attrHolderOpt = BuiltInRegistries.ATTRIBUTE.getHolder(attrId);
                if (attrHolderOpt.isEmpty()) continue;
                Holder<Attribute> attrHolder = attrHolderOpt.get();

                AttributeModifier.Operation op = switch (entry.operation()) {
                    case "addition" -> AttributeModifier.Operation.ADD_VALUE;
                    case "multiply_base" -> AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
                    case "multiply_total" -> AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
                    default -> AttributeModifier.Operation.ADD_VALUE;
                };

                double value = entry.value() * levelMultiplier;
                ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                        LegendaryTiers.MOD_ID, "tier_mod_" + attrId.getPath());

                event.addModifier(attrHolder,
                        new AttributeModifier(modifierId, value, op),
                        slotGroup);
            }
        }
    }

    private static EquipmentSlotGroup getSlotGroupForItem(ItemStack stack) {
        if (stack.is(ModTags.ARMOR)) {
            return EquipmentSlotGroup.ARMOR;
        } else if (stack.is(ModTags.WEAPON) || stack.is(ModTags.TOOL) || stack.is(ModTags.RANGED_WEAPON)) {
            return EquipmentSlotGroup.MAINHAND;
        } else if (stack.is(ModTags.SHIELD)) {
            return EquipmentSlotGroup.OFFHAND;
        }
        // Для остальных предметов (например, элитры) используем ANY
        return EquipmentSlotGroup.ANY;
    }
}