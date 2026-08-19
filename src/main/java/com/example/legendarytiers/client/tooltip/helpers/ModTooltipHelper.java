package com.example.legendarytiers.client.tooltip.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class ModTooltipHelper {

    public static List<Component> getExtraTooltipLines(ItemStack stack) {
        if (stack.isEmpty()) return List.of();

        Player player = Minecraft.getInstance().player;
        var level = Minecraft.getInstance().level;

        Item.TooltipContext context = (level != null)
                ? Item.TooltipContext.of(level)
                : Item.TooltipContext.EMPTY;

        TooltipFlag flag = Minecraft.getInstance().options.advancedItemTooltips
                ? TooltipFlag.Default.ADVANCED
                : TooltipFlag.Default.NORMAL;

        List<Component> allLines = stack.getTooltipLines(context, player, flag);

        // Если строк 1 или меньше — это только название предмета, доп. информации нет
        if (allLines.size() <= 1) return List.of();

        List<Component> extraLines = new ArrayList<>();

        // Пропускаем 0-ю строку (название предмета)
        for (int i = 1; i < allLines.size(); i++) {
            Component line = allLines.get(i);

            // Фильтруем пустые строки или строки, которые вы рендерите вручную
            String rawText = line.getString().trim();
            if (!rawText.isEmpty()) {
                extraLines.add(line);
            }
        }

        return extraLines;
    }
}
