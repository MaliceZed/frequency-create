package maze.frequency.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class FrequencyConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_STARTUP_MESSAGE = BUILDER
            .comment("Set to false to disable the startup welcome message")
            .define("enableStartupMessage", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
