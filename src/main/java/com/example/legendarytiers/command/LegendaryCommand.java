package com.example.legendarytiers.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class LegendaryCommand {

    private LegendaryCommand() {
    }

    public static void register(RegisterCommandsEvent event) {

        CommandDispatcher<CommandSourceStack> dispatcher =
                event.getDispatcher();

        LegendarySetCommand.register(dispatcher);

    }

}