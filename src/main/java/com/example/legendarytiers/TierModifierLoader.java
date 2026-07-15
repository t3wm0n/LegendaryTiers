package com.example.legendarytiers;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TierModifierLoader {
    private static final Map<String, Map<Rarity, List<Variant>>> typeModifiers = new HashMap<>();

    public static final String TYPE_WEAPON = "weapon";
    public static final String TYPE_RANGED = "ranged";
    public static final String TYPE_ARMOR = "armor";
    public static final String TYPE_TOOL = "tool";
    public static final String TYPE_SHIELD = "shield";

    public static void loadBuiltin() {
        typeModifiers.clear();
        loadForType(TYPE_WEAPON, "data/legendarytiers/tier_modifiers/weapon_modifiers.json");
        loadForType(TYPE_RANGED, "data/legendarytiers/tier_modifiers/ranged_modifiers.json");
        loadForType(TYPE_ARMOR, "data/legendarytiers/tier_modifiers/armor_modifiers.json");
        loadForType(TYPE_TOOL, "data/legendarytiers/tier_modifiers/tool_modifiers.json");
        loadForType(TYPE_SHIELD, "data/legendarytiers/tier_modifiers/shield_modifiers.json");
    }

    private static void loadForType(String type, String resourcePath) {
        try (InputStream stream = TierModifierLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                return;
            }
            JsonObject json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            Map<Rarity, List<Variant>> map = new EnumMap<>(Rarity.class);
            parseVariants(json, map);
            typeModifiers.put(type, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseVariants(JsonObject json, Map<Rarity, List<Variant>> map) {
        JsonObject variants = json.getAsJsonObject("variants");
        for (Rarity rarity : Rarity.values()) {
            String name = rarity.name().toLowerCase();
            if (variants.has(name)) {
                List<Variant> list = new ArrayList<>();
                for (JsonElement elem : variants.getAsJsonArray(name)) {
                    JsonObject obj = elem.getAsJsonObject();
                    int weight = obj.has("weight") ? obj.get("weight").getAsInt() : 1;
                    List<ModifierEntry> modifiers = new ArrayList<>();
                    for (JsonElement modElem : obj.getAsJsonArray("modifiers")) {
                        modifiers.add(parseModifierEntry(modElem.getAsJsonObject()));
                    }
                    list.add(new Variant(weight, modifiers));
                }
                map.put(rarity, list);
            }
        }
    }

    private static ModifierEntry parseModifierEntry(JsonObject obj) {
        String target = obj.has("target") ? obj.get("target").getAsString() : "attribute";
        Optional<String> attribute = obj.has("attribute") ? Optional.of(obj.get("attribute").getAsString()) : Optional.empty();
        String operation = obj.get("operation").getAsString();
        double value = obj.get("value").getAsDouble();
        return new ModifierEntry(target, attribute, operation, value);
    }

    public static TierData generate(ItemStack stack, Rarity rarity, RandomSource random) {
        String type = getItemType(stack);
        Map<Rarity, List<Variant>> rarityMap = typeModifiers.getOrDefault(type, Collections.emptyMap());
        List<Variant> variants = rarityMap.get(rarity);
        if (variants == null || variants.isEmpty()) {
            return new TierData(rarity, List.of());
        }
        int totalWeight = variants.stream().mapToInt(Variant::weight).sum();
        int choice = random.nextInt(totalWeight);
        int cum = 0;
        for (Variant v : variants) {
            cum += v.weight;
            if (choice < cum) {
                return new TierData(rarity, v.modifiers);
            }
        }
        return new TierData(rarity, variants.get(0).modifiers);
    }

    private static String getItemType(ItemStack stack) {
        if (stack.is(ModTags.WEAPON)) return TYPE_WEAPON;
        if (stack.is(ModTags.RANGED_WEAPON)) return TYPE_RANGED;
        if (stack.is(ModTags.ARMOR)) return TYPE_ARMOR;
        if (stack.is(ModTags.TOOL)) return TYPE_TOOL;
        if (stack.is(ModTags.SHIELD)) return TYPE_SHIELD;
        return TYPE_TOOL; // запасной
    }


    public record Variant(int weight, List<ModifierEntry> modifiers) {}
}