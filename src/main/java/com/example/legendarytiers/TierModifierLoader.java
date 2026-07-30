package com.example.legendarytiers;

import com.example.legendarytiers.util.ExperienceUtil;
import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TierModifierLoader {

    // Кэш загруженных JSON-файлов: тип -> редкость -> список атрибутов
    private static final Map<String, Map<Rarity, List<AttributeDef>>> attributeCache = new HashMap<>();
    private static final Map<String, int[]> countCache = new HashMap<>(); // мин/макс атрибутов для редкости
    private static boolean loaded = false;

    private static final Set<String> ABSOLUTE_ATTRIBUTES = Set.of(
            "minecraft:generic.armor",
            "minecraft:generic.armor_toughness",
            "minecraft:generic.max_health",
            "minecraft:generic.luck",
            "minecraft:generic.knockback_resistance"
    );

    public static void loadBuiltin() {
        if (loaded) return;
        loadForType("weapon", "data/legendarytiers/tier_modifiers/weapon_modifiers.json");
        loadForType("ranged", "data/legendarytiers/tier_modifiers/ranged_modifiers.json");
        loadForType("armor", "data/legendarytiers/tier_modifiers/armor_modifiers.json");
        loadForType("tool", "data/legendarytiers/tier_modifiers/tool_modifiers.json");
        loadForType("shield", "data/legendarytiers/tier_modifiers/shield_modifiers.json");
        loaded = true;
    }

    private static void loadForType(String type, String resourcePath) {
        try (InputStream stream = TierModifierLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                System.err.println("Tier modifiers file not found: " + resourcePath);
                return;
            }
            JsonObject json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            Map<Rarity, List<AttributeDef>> map = new EnumMap<>(Rarity.class);
            int[] counts = new int[6]; // мин/макс будем хранить отдельно
            for (Rarity rarity : Rarity.values()) {
                String key = rarity.name().toLowerCase();
                if (json.has(key)) {
                    JsonObject rarityObj = json.getAsJsonObject(key);
                    JsonArray attrs = rarityObj.getAsJsonArray("attributes");
                    List<AttributeDef> list = new ArrayList<>();
                    for (JsonElement elem : attrs) {
                        JsonObject attrObj = elem.getAsJsonObject();
                        String id = attrObj.get("id").getAsString();
                        int weight = attrObj.get("weight").getAsInt();
                        double min = attrObj.get("min").getAsDouble();
                        double max = attrObj.get("max").getAsDouble();
                        String operation = attrObj.get("operation").getAsString();
                        list.add(new AttributeDef(id, weight, min, max, operation));
                    }
                    map.put(rarity, list);
                    counts[rarity.ordinal()] = rarityObj.get("min_attrs").getAsInt();
                    // max_attrs не сохраняем в counts, будем брать прямо из JSON при генерации
                }
            }
            attributeCache.put(type, map);
        } catch (Exception e) {
            System.err.println("Failed to load tier modifiers for type: " + type);
            e.printStackTrace();
        }
    }

    /**
     * Генерирует TierData для предмета с учётом его типа, редкости и уровня.
     */
    public static TierData generate(ItemStack stack, Rarity rarity, RandomSource random) {
        loadBuiltin(); // загружаем JSON при первом вызове
        String type = getItemType(stack);
        Map<Rarity, List<AttributeDef>> rarityMap = attributeCache.get(type);
        if (rarityMap == null) {
            return new TierData(rarity, List.of(), -1f); // -1 означает "старый предмет, не показывать звёзды"
        }

        List<AttributeDef> availableAttrs = rarityMap.get(rarity);
        if (availableAttrs == null || availableAttrs.isEmpty()) {
            return new TierData(rarity, List.of(), -1f);
        }

        // Получаем min/max количество атрибутов из JSON
        int[] minMax = getMinMaxAttrs(type, rarity);
        int minAttrs = minMax[0];
        int maxAttrs = minMax[1];
        int count = minAttrs + random.nextInt(maxAttrs - minAttrs + 1);

        int exp = stack.getOrDefault(ModDataComponents.EXPERIENCE, 0);
        int level = ExperienceUtil.getLevel(exp);
        double levelBonusPct = level * 0.01;   // +1% к максимуму за каждый уровень
        double levelBonusAbs = level * 0.1;

        List<ModifierEntry> modifiers = new ArrayList<>();
        float totalQuality = 0;

        // Копируем список и выбираем count атрибутов по весу
        List<AttributeDef> weightedList = new ArrayList<>();
        for (AttributeDef def : availableAttrs) {
            for (int i = 0; i < def.weight; i++) weightedList.add(def);
        }

        // Перемешиваем и берём первые count уникальных
        Collections.shuffle(weightedList, new Random(random.nextLong()));
        Set<String> usedIds = new HashSet<>();
        int attempts = 0;
        while (modifiers.size() < count && attempts < weightedList.size() * 2) {
            AttributeDef def = weightedList.get(attempts % weightedList.size());
            if (usedIds.add(def.id)) {
                double min = def.min;
                double max = def.max;

                // Увеличиваем максимум за счёт уровня
                if (ABSOLUTE_ATTRIBUTES.contains(def.id)) {
                    max += levelBonusAbs;
                } else {
                    max += levelBonusPct;
                }

                double value = min + random.nextDouble() * (max - min);
                value = Math.round(value * 100.0) / 100.0;

                if (ABSOLUTE_ATTRIBUTES.contains(def.id)) {
                    value = Math.round(value); // округляем до целого
                }
                if (value == 0.0) {
                    attempts++;
                    continue;
                }
                String target = def.id.equals("durability") ? "durability" : "attribute";
                Optional<String> attr = target.equals("attribute") ? Optional.of(def.id) : Optional.empty();
                modifiers.add(new ModifierEntry(target, attr, def.operation, value));

                double normalized = (value - def.min) / (def.max - def.min);
                totalQuality += Math.max(0, Math.min(1, normalized));
            }
            attempts++;
        }

        if (modifiers.isEmpty()) {
            AttributeDef def = availableAttrs.get(0);
            double value = def.min;
            if (value == 0.0) value = 0.01;
            String target = def.id.equals("durability") ? "durability" : "attribute";
            Optional<String> attr = target.equals("attribute") ? Optional.of(def.id) : Optional.empty();
            modifiers.add(new ModifierEntry(target, attr, def.operation, value));
            totalQuality = 0;
        }

        float avgQuality = modifiers.isEmpty() ? 0.5f : totalQuality / modifiers.size();
        float quantityFactor = (float) modifiers.size() / maxAttrs;
        float finalQuality = avgQuality * quantityFactor;

        return new TierData(rarity, modifiers, finalQuality);
    }

    private static int[] getMinMaxAttrs(String type, Rarity rarity) {
        // Повторно читаем JSON, чтобы получить min_attrs и max_attrs
        try (InputStream stream = TierModifierLoader.class.getClassLoader()
                .getResourceAsStream("data/legendarytiers/tier_modifiers/" + type + "_modifiers.json")) {
            if (stream == null) return new int[]{1, 1};
            JsonObject json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonObject rarityObj = json.getAsJsonObject(rarity.name().toLowerCase());
            int min = rarityObj.get("min_attrs").getAsInt();
            int max = rarityObj.get("max_attrs").getAsInt();
            return new int[]{min, max};
        } catch (Exception e) {
            return new int[]{1, 2};
        }
    }

    private static String getItemType(ItemStack stack) {
        if (stack.is(ModTags.WEAPON)) return "weapon";
        if (stack.is(ModTags.RANGED_WEAPON)) return "ranged";
        if (stack.is(ModTags.ARMOR)) return "armor";
        if (stack.is(ModTags.TOOL)) return "tool";
        if (stack.is(ModTags.SHIELD)) return "shield";
        return "tool"; // fallback
    }

    private record AttributeDef(String id, int weight, double min, double max, String operation) {}
}