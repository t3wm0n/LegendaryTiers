package com.example.legendarytiers.command;

import com.example.legendarytiers.ModDataComponents;
import com.example.legendarytiers.Rarity;
import com.example.legendarytiers.TierData;
import com.example.legendarytiers.TierModifierLoader;
import com.example.legendarytiers.util.ExperienceUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public final class LegendarySetCommand {

    private static final String[] RARITIES = {
            "common",
            "rare",
            "epic",
            "legendary",
            "mythic",
            "divine"
    };

    private LegendarySetCommand() {}

    private static CompletableFuture<Suggestions> suggestRarities(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder
    ) {

        for (String rarity : RARITIES) {
            builder.suggest(rarity);
        }

        return builder.buildFuture();

    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(

                literal("legendary")

                        .requires(source -> source.hasPermission(2))

                        .then(

                                literal("set")

                                        .then(

                                                argument(
                                                        "rarity",
                                                        StringArgumentType.word()
                                                ).suggests(LegendarySetCommand::suggestRarities)

                                                        .then(

                                                                argument(
                                                                        "level",
                                                                        IntegerArgumentType.integer(1,100)
                                                                )
                                                                        .executes(context -> setItem(context))
                                                        )

                                        )

                        )

        );

    }

    private static int setItem(CommandContext<CommandSourceStack> context) {

        CommandSourceStack source = context.getSource();

        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (CommandSyntaxException e) {
            source.sendFailure(Component.literal("Эту команду может использовать только игрок."));
            return 0;
        }

        ItemStack stack = player.getMainHandItem();

        if (stack.isEmpty()) {

            source.sendFailure(
                    Component.literal("Возьми предмет в главную руку.")
            );

            return 0;

        }

        String rarityName =
                StringArgumentType.getString(
                        context,
                        "rarity"
                );

        Rarity rarity;

        try {

            rarity = Rarity.valueOf(
                    rarityName.toUpperCase(Locale.ROOT)
            );

        } catch (IllegalArgumentException e) {

            context.getSource().sendFailure(
                    Component.literal("Неизвестная редкость.")
            );

            return 0;

        }

        int level =
                IntegerArgumentType.getInteger(
                        context,
                        "level"
                );

        int exp = ExperienceUtil.getExperienceForLevel(level);

        stack.set(
                ModDataComponents.EXPERIENCE,
                exp
        );

        TierData data =
                TierModifierLoader.generate(
                        stack,
                        rarity,
                        RandomSource.create()
                );

        stack.set(
                ModDataComponents.TIER_DATA,
                data
        );

        player.getInventory().setChanged();

        source.sendSuccess(

                () -> Component.literal(
                        "§aПредмет обновлён!"
                ),

                false

        );

        return 1;

    }

}