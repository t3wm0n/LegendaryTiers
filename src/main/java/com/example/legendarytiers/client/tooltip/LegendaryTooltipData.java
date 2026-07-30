package com.example.legendarytiers.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record LegendaryTooltipData(
        LegendaryTooltipContext context
) implements TooltipComponent { }