package maze.frequency.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import maze.frequency.config.FrequencyConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class StartupToggleCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("frequency")
            .then(Commands.literal("toggleStartup")
                .executes(StartupToggleCommand::execute)
            )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        // Set the config value to false
        FrequencyConfig.ENABLE_STARTUP_MESSAGE.set(false);

        // Save the config spec directly to the config file
        FrequencyConfig.SPEC.save();

        ctx.getSource().sendSuccess(
            () -> Component.literal("Startup message disabled"),
            false
        );

        return 1;
    }
}
