package com.example.legendarytiers.util;

import java.util.HashMap;
import java.util.Map;

public final class RunicTranslator {

    private static final Map<Character, String> RUNIC_MAP = new HashMap<>();

    static {
        // --- Кириллица ---
        RUNIC_MAP.put('а', "ᛅ"); // Ar
        RUNIC_MAP.put('б', "ᛒ"); // Bjarkan
        RUNIC_MAP.put('в', "ᚢ"); // Ur (v/w)
        RUNIC_MAP.put('г', "ᚴ"); // Kaun (g/k)
        RUNIC_MAP.put('д', "ᛏ"); // Tyr (d/t)
        RUNIC_MAP.put('е', "ᛁ"); // Isa (e/i)
        RUNIC_MAP.put('ё', "ᛁ");
        RUNIC_MAP.put('ж', "ᛋ"); // Sol (z/s)
        RUNIC_MAP.put('з', "ᛋ");
        RUNIC_MAP.put('и', "ᛁ"); // Isa
        RUNIC_MAP.put('й', "ᛁ");
        RUNIC_MAP.put('к', "ᚴ"); // Kaun
        RUNIC_MAP.put('л', "ᛚ"); // Logr
        RUNIC_MAP.put('м', "ᛘ"); // Madr
        RUNIC_MAP.put('н', "ᚾ"); // Naudr
        RUNIC_MAP.put('о', "ᚢ"); // Ur (o/u)
        RUNIC_MAP.put('п', "ᛒ"); // Bjarkan (p/b)
        RUNIC_MAP.put('р', "ᚱ"); // Reid
        RUNIC_MAP.put('с', "ᛋ"); // Sol
        RUNIC_MAP.put('т', "ᛏ"); // Tyr
        RUNIC_MAP.put('у', "ᚢ"); // Ur
        RUNIC_MAP.put('ф', "ᚠ"); // Fe
        RUNIC_MAP.put('х', "ᚼ"); // Hagall
        RUNIC_MAP.put('ц', "ᛋ");
        RUNIC_MAP.put('ч', "ᚴ");
        RUNIC_MAP.put('ш', "ᛋ");
        RUNIC_MAP.put('щ', "ᛋ");
        RUNIC_MAP.put('ъ', "");
        RUNIC_MAP.put('ы', "ᛁ");
        RUNIC_MAP.put('ь', "");
        RUNIC_MAP.put('э', "ᛅ");
        RUNIC_MAP.put('ю', "ᚢ");
        RUNIC_MAP.put('я', "ᛅ");

        // --- English / Latin ---
        RUNIC_MAP.put('a', "ᛅ");
        RUNIC_MAP.put('b', "ᛒ");
        RUNIC_MAP.put('c', "ᚴ");
        RUNIC_MAP.put('d', "ᛏ");
        RUNIC_MAP.put('e', "ᛁ");
        RUNIC_MAP.put('f', "ᚠ");
        RUNIC_MAP.put('g', "ᚴ");
        RUNIC_MAP.put('h', "ᚼ");
        RUNIC_MAP.put('i', "ᛁ");
        RUNIC_MAP.put('j', "ᛁ");
        RUNIC_MAP.put('k', "ᚴ");
        RUNIC_MAP.put('l', "ᛚ");
        RUNIC_MAP.put('m', "ᛘ");
        RUNIC_MAP.put('n', "ᚾ");
        RUNIC_MAP.put('o', "ᚢ");
        RUNIC_MAP.put('p', "ᛒ");
        RUNIC_MAP.put('q', "ᚴ");
        RUNIC_MAP.put('r', "ᚱ");
        RUNIC_MAP.put('s', "ᛋ");
        RUNIC_MAP.put('t', "ᛏ");
        RUNIC_MAP.put('u', "ᚢ");
        RUNIC_MAP.put('v', "ᚢ");
        RUNIC_MAP.put('w', "ᚢ");
        RUNIC_MAP.put('x', "ᛋ");
        RUNIC_MAP.put('y', "ᚢ");
        RUNIC_MAP.put('z', "ᛋ");
    }

    private RunicTranslator() {}

    public static String toYoungerFuthark(String text) {
        if (text == null || text.isEmpty()) return "";

        StringBuilder runes = new StringBuilder();
        for (char ch : text.toLowerCase().toCharArray()) {
            if (RUNIC_MAP.containsKey(ch)) {
                runes.append(RUNIC_MAP.get(ch));
            } else if (ch == ' ') {
                runes.append(" "); // Сохраняем пробелы (или можно ставить двоеточие ":" как у викингов)
            } else {
                runes.append(ch); // Цифры и спецсимволы оставляем как есть
            }
        }
        return runes.toString();
    }
}