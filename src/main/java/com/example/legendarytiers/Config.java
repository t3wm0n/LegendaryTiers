package com.example.legendarytiers;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Config {
    public static final ModConfigSpec SPEC;
    public static final Config INSTANCE;

    private final ModConfigSpec.ConfigValue<List<? extends String>> oreXpEntries;
    private final ModConfigSpec.IntValue defaultOreXp;
    private final ModConfigSpec.IntValue stoneXp;
    private final ModConfigSpec.IntValue shovelXp;
    private final ModConfigSpec.IntValue axeXp;
    private final ModConfigSpec.IntValue hoeXp;
    private final ModConfigSpec.DoubleValue mobXpMultiplier;
    private final ModConfigSpec.IntValue mobXpMin;
    private final ModConfigSpec.IntValue armorXpPerDamage;

    public Config(ModConfigSpec.Builder builder) {
        builder.push("Experience (Опыт)");

        oreXpEntries = builder
                .comment(
                        "List of ores and XP for them. Format: \"block_id@xp\" (tags are supported, e.g. #minecraft:coal_ores@3)",
                        "Список руд и опыта за них. Формат: \"block_id@xp\" (можно использовать теги, например #minecraft:coal_ores@3)"
                )
                .defineListAllowEmpty("pickaxe.ores", List.of(
                        "#minecraft:coal_ores@3",
                        "#minecraft:copper_ores@3",
                        "#minecraft:iron_ores@4",
                        "#minecraft:gold_ores@5",
                        "#minecraft:diamond_ores@7",
                        "#minecraft:emerald_ores@7",
                        "#minecraft:redstone_ores@4",
                        "#minecraft:lapis_ores@4",
                        "#minecraft:ancient_debris@10"
                ), obj -> obj instanceof String && ((String) obj).contains("@"));

        defaultOreXp = builder
                .comment(
                        "XP for ores not listed above but having tag forge:ores or c:ores",
                        "Опыт за руды, не указанные в списке, но имеющие тег forge:ores или c:ores"
                )
                .defineInRange("pickaxe.default_ore_xp", 3, 0, 100);

        stoneXp = builder
                .comment(
                        "XP for regular blocks mined with a pickaxe (stone, deepslate, etc.)",
                        "Опыт за обычные блоки, добываемые киркой (камень, глубинный сланец и т.п.)"
                )
                .defineInRange("pickaxe.stone_xp", 1, 0, 100);

        shovelXp = builder
                .comment(
                        "XP for blocks mined with a shovel",
                        "Опыт за блоки, добываемые лопатой"
                )
                .defineInRange("shovel.xp", 1, 0, 100);

        axeXp = builder
                .comment(
                        "XP for logs mined with an axe",
                        "Опыт за дерево, добываемое топором"
                )
                .defineInRange("axe.xp", 2, 0, 100);

        hoeXp = builder
                .comment(
                        "XP for tilling soil with a hoe",
                        "Опыт за вспахивание земли мотыгой"
                )
                .defineInRange("hoe.xp", 1, 0, 100);

        mobXpMultiplier = builder
                .comment(
                        "Multiplier for XP from killing mobs (from their max health)",
                        "Множитель опыта за убийство мобов (от их максимального здоровья)"
                )
                .defineInRange("mob.multiplier", 0.5, 0.0, 100.0);
        mobXpMin = builder
                .comment(
                        "Minimum XP for killing a mob",
                        "Минимальный опыт за убийство моба"
                )
                .defineInRange("mob.min", 1, 0, 100);

        armorXpPerDamage = builder
                .comment(
                        "XP for each point of damage absorbed (armor)",
                        "Опыт за полученный урон (для брони)"
                )
                .defineInRange("armor.xp_per_damage", 1, 0, 100);

        builder.pop();
    }

    static {
        Pair<Config, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Config::new);
        SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }

    public List<? extends String> getOreXpEntries() { return oreXpEntries.get(); }
    public int getDefaultOreXp() { return defaultOreXp.get(); }
    public int getStoneXp() { return stoneXp.get(); }
    public int getShovelXp() { return shovelXp.get(); }
    public int getAxeXp() { return axeXp.get(); }
    public int getHoeXp() { return hoeXp.get(); }
    public double getMobXpMultiplier() { return mobXpMultiplier.get(); }
    public int getMobXpMin() { return mobXpMin.get(); }
    public int getArmorXpPerDamage() { return armorXpPerDamage.get(); }
}