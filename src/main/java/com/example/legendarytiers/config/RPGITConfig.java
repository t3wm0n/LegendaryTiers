package com.example.legendarytiers.config;

import com.example.legendarytiers.LegendaryTiers;
import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class RPGITConfig extends Config {

    public static final RPGITConfig INSTANCE =
            ConfigApiJava.registerAndLoadConfig(RPGITConfig::new);

    public RPGITConfig() {
        super(ResourceLocation.fromNamespaceAndPath(LegendaryTiers.MOD_ID, "config"));
    }

    // --- Tiers System ---
    public TiersSystem tiers_system = new TiersSystem();

    public static class TiersSystem extends ConfigSection {
        @Comment("""
                ДОПОЛНИТЕЛЬНЫЕ предметы и теги, которые могут получать тиры.
                Базовые теги заложены в моде и работают всегда.
                Формат: '#namespace:tag' или 'namespace:item_id'
                
                ADDITIONAL items and tags that can receive tiers.
                Base tags are built into the mod's files and always work.
                Format: '#namespace:tag' or 'namespace:item_id'""")
        public List<String> additional_tierable_items = List.of();
    }

    // --- Item Types Mapping ---
    public ItemTypes item_types = new ItemTypes();

    public static class ItemTypes extends ConfigSection {
        @Comment("""
                Дополнительные теги/предметы для категории WEAPON
                Additional tags/items for the category WEAPON""")
        public List<String> additional_weapons = List.of();

        @Comment("""
                Дополнительные теги/предметы для категории RANGED
                Additional tags/items for the category RANGED""")
        public List<String> additional_ranged = List.of();

        @Comment("""
                Дополнительные теги/предметы для категории ARMOR
                Additional tags/items for the category ARMOR""")
        public List<String> additional_armor = List.of();

        @Comment("""
                Дополнительные теги/предметы для категории TOOL
                Additional tags/items for the category TOOL""")
        public List<String> additional_tools = List.of();

        @Comment("""
                Дополнительные теги/предметы для категории SHIELD
                Additional tags/items for the category SHIELD""")
        public List<String> additional_shields = List.of();
    }

    // --- Experience Settings ---
    public Experience experience = new Experience();

    public static class Experience extends ConfigSection {
        @Comment("""
            List of ores and XP for them. Format: "block_id@xp" (tags are supported, e.g. #minecraft:coal_ores@3)
            Список руд и опыта за них. Формат: "block_id@xp" (можно использовать теги, например #minecraft:coal_ores@3)""")
        public List<String> pickaxe_ores = List.of(
                "#minecraft:coal_ores@3",
                "#minecraft:copper_ores@3",
                "#minecraft:iron_ores@4",
                "#minecraft:gold_ores@5",
                "#minecraft:diamond_ores@7",
                "#minecraft:emerald_ores@7",
                "#minecraft:redstone_ores@4",
                "#minecraft:lapis_ores@4",
                "#minecraft:ancient_debris@10"
        );

        @Comment("""
            XP for ores not listed above but having tag forge:ores or c:ores
            Опыт за руды, не указанные в списке, но имеющие тег forge:ores или c:ores""")
        public ValidatedInt default_ore_xp = new ValidatedInt(3, 100, 1);

        @Comment("""
            XP for regular blocks mined with a pickaxe (stone, deepslate, etc.)
            Опыт за обычные блоки, добываемые киркой (камень, глубинный сланец и т.п.)""")
        public ValidatedInt stone_xp = new ValidatedInt(1, 100, 1);

        @Comment("""
            XP for blocks mined with a shovel
            Опыт за блоки, добываемые лопатой""")
        public ValidatedInt shovel_xp = new ValidatedInt(1, 100, 1);

        @Comment("""
            XP for logs mined with an axe
            Опыт за дерево, добываемое топором""")
        public ValidatedInt axe_xp = new ValidatedInt(2, 100, 1);

        @Comment("""
            XP for tilling soil with a hoe
            Опыт за вспахивание земли мотыгой""")
        public ValidatedInt hoe_xp = new ValidatedInt(1, 100, 1);

        @Comment("""
            Multiplier for XP from killing mobs (from their max health)
            Множитель опыта за убийство мобов (от их максимального здоровья)""")
        public ValidatedDouble mob_multiplier = new ValidatedDouble(0.5, 100.0, 0.1);

        @Comment("""
            Minimum XP for killing a mob
            Минимальный опыт за убийство моба""")
        public ValidatedInt mob_min = new ValidatedInt(1, 100, 1);

        @Comment("""
            XP for each point of damage absorbed (armor)
            Опыт за полученный урон (для брони)""")
        public ValidatedInt armor_xp_per_damage = new ValidatedInt(1, 100, 1);
    }

    // --- Client / UI Settings ---
    public Client client = new Client();

    public static class Client extends ConfigSection {
        @Comment("""
                Enable custom animated tooltips for legendary items (true = custom, false = vanilla)
                Включить кастомные анимированные тултипы (true = кастомные, false = ванильные)""")
        public boolean enable_custom_tooltips = true;
    }
}