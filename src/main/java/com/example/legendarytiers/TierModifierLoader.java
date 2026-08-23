package com.example.legendarytiers;

import com.example.legendarytiers.util.ExperienceUtil;
import com.example.legendarytiers.util.TierHelper;
import com.google.gson.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TierModifierLoader {

    // Структура для хранения данных о редкости (кэшируется один раз при старте)
    private record RarityModifierData(
            int minAttrs,
            int maxAttrs,
            List<AttributeDef> attributes,
            int totalWeight
    ) {}

    private record AttributeDef(String id, int weight, double min, double max, String operation) {
        // Автоматически определяем, является ли оператор добавлением абсолютного значения (ADD_VALUE / addition)
        public boolean isInt() {
            return id.contains("armor") || id.contains("health") || id.contains("luck") || id.contains("toughness");
        }
    }

    // Кэш: Тип предмета (weapon, armor...) -> Редкость -> Данные модификаторов
    private static final Map<String, Map<Rarity, RarityModifierData>> attributeCache = new HashMap<>();
    private static boolean loaded = false;

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
            Map<Rarity, RarityModifierData> rarityMap = new EnumMap<>(Rarity.class);

            for (Rarity rarity : Rarity.values()) {
                String key = rarity.name().toLowerCase();
                if (json.has(key)) {
                    JsonObject rarityObj = json.getAsJsonObject(key);
                    int minAttrs = rarityObj.has("min_attrs") ? rarityObj.get("min_attrs").getAsInt() : 1;
                    int maxAttrs = rarityObj.has("max_attrs") ? rarityObj.get("max_attrs").getAsInt() : 1;

                    JsonArray attrs = rarityObj.getAsJsonArray("attributes");
                    List<AttributeDef> list = new ArrayList<>();
                    int totalWeight = 0;

                    for (JsonElement elem : attrs) {
                        JsonObject attrObj = elem.getAsJsonObject();
                        String id = attrObj.get("id").getAsString();
                        int weight = attrObj.get("weight").getAsInt();
                        double min = attrObj.get("min").getAsDouble();
                        double max = attrObj.get("max").getAsDouble();
                        String operation = attrObj.get("operation").getAsString();

                        list.add(new AttributeDef(id, weight, min, max, operation));
                        totalWeight += weight;
                    }

                    rarityMap.put(rarity, new RarityModifierData(minAttrs, maxAttrs, list, totalWeight));
                }
            }
            attributeCache.put(type, rarityMap);
        } catch (Exception e) {
            System.err.println("Failed to load tier modifiers for type: " + type);
            e.printStackTrace();
        }
    }

    /**
     * Генерирует TierData для предмета с учётом его типа, редкости и уровня.
     */
    public static TierData generate(ItemStack stack, Rarity rarity, RandomSource random) {
        loadBuiltin(); // Загружаем ресурсы при первом вызове

        String type = TierHelper.getItemType(stack);
        Map<Rarity, RarityModifierData> rarityMap = attributeCache.get(type);
        if (rarityMap == null) {
            return new TierData(rarity, List.of(), -1f);
        }

        RarityModifierData rarityData = rarityMap.get(rarity);
        if (rarityData == null || rarityData.attributes().isEmpty()) {
            return new TierData(rarity, List.of(), -1f);
        }

        int minAttrs = rarityData.minAttrs();
        int maxAttrs = rarityData.maxAttrs();
        int count = minAttrs == maxAttrs ? minAttrs : minAttrs + random.nextInt(maxAttrs - minAttrs + 1);

        int exp = stack.getOrDefault(ModDataComponents.EXPERIENCE, 0);

        List<ModifierEntry> modifiers = new ArrayList<>();
        float totalQuality = 0;

        List<AttributeDef> availablePool = new ArrayList<>(rarityData.attributes());
        Set<String> usedIds = new HashSet<>();

        while (modifiers.size() < count && !availablePool.isEmpty()) {
            AttributeDef def = getRandomAttributeByWeight(availablePool, random);
            if (def == null) break;

            availablePool.remove(def);

            if (usedIds.add(def.id())) {
                double min = def.min();
                double max = def.max();

                // 1. Выбиваем БАЗОВОЕ значение строго в диапазоне из JSON [min, max]
                double baseValue = min == max ? min : min + random.nextDouble() * (max - min);

                boolean isGravity = isGravityAttribute(def.id());

                // 2. Рассчитываем итоговое значение с учётом уровня предмета (+1% к значению за уровень)
                double levelMultiplier = ExperienceUtil.getMultiplier(exp);
                double value = baseValue * levelMultiplier;

                // 3. Форматируем результат в зависимости от типа атрибута
                if (def.isInt()) {
                    value = Math.round(value);
                } else {
                    double precision = isGravity ? 10000.0 : 100.0;
                    value = Math.round(value * precision) / precision;
                }

                if (max < 0 && value > 0) {
                    value = -value;
                }

                if (value == 0.0) {
                    continue;
                }

                String target = "durability".equals(def.id()) ? "durability" : "attribute";
                Optional<String> attr = "attribute".equals(target) ? Optional.of(def.id()) : Optional.empty();
                modifiers.add(new ModifierEntry(target, attr, def.operation(), value));

                // 4. Качество рассчитывается относительно исходного барьера из JSON
                double range = max - min;
                double normalized;
                if (range <= 0) {
                    normalized = 1.0;
                } else if (isGravity) {
                    normalized = (max - baseValue) / range; // Инвертированное качество
                } else {
                    normalized = (baseValue - min) / range;
                }
                totalQuality += (float) Math.clamp(normalized, 0.0, 1.0);
            }
        }

        // Фолбэк, если ничего не выбилось
        if (modifiers.isEmpty()) {
            AttributeDef def = rarityData.attributes().get(0);
            double value = def.min() == 0.0 ? 0.01 : def.min();
            if (def.isInt()) {
                value = Math.round(value);
            } else {
                value = Math.round(value * 100.0) / 100.0;
            }

            String target = "durability".equals(def.id()) ? "durability" : "attribute";
            Optional<String> attr = "attribute".equals(target) ? Optional.of(def.id()) : Optional.empty();
            modifiers.add(new ModifierEntry(target, attr, def.operation(), value));
            totalQuality = 0;
        }

        float avgQuality = modifiers.isEmpty() ? 0.5f : totalQuality / modifiers.size();
        float quantityFactor = maxAttrs <= 0 ? 1.0f : (float) modifiers.size() / maxAttrs;
        float finalQuality = avgQuality * quantityFactor;

        return new TierData(rarity, modifiers, finalQuality);
    }

    /**
     * Быстрый и экономный алгоритм взвешенного случайного выбора
     */
    private static AttributeDef getRandomAttributeByWeight(List<AttributeDef> pool, RandomSource random) {
        int totalWeight = 0;
        for (AttributeDef def : pool) {
            totalWeight += def.weight();
        }

        if (totalWeight <= 0) return null;

        int randomWeight = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (AttributeDef def : pool) {
            currentWeight += def.weight();
            if (randomWeight < currentWeight) {
                return def;
            }
        }

        return pool.get(0);
    }

    private static boolean isGravityAttribute(String attributeId) {
        if (attributeId == null) return false;
        return attributeId.equals("generic.gravity")
                || attributeId.equals("minecraft:generic.gravity")
                || attributeId.endsWith(":gravity");
    }
}