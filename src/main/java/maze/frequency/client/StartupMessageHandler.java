package maze.frequency.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import maze.frequency.config.FrequencyConfig;

@EventBusSubscriber(modid = "frequency", value = Dist.CLIENT)
public class StartupMessageHandler {
    
    private static boolean messageShown = false;
    
    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        if (messageShown) return;
        messageShown = true;
        
        if (!FrequencyConfig.ENABLE_STARTUP_MESSAGE.get()) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        
        String githubUrl = "https://github.com/MaliceZed/frequency-create/issues";
        
        Component message = Component.literal("")
            .append(Component.translatable("message.frequency.startup.title"))
            .append(Component.literal("\n"))
            .append(Component.translatable("message.frequency.startup.description"))
            .append(Component.literal("\n"))
            // Button 1: Open GitHub
            .append(Component.translatable("message.frequency.startup.open_github")
                .withStyle(style -> style
                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, githubUrl))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                        Component.translatable("message.frequency.startup.github_hover")))
                )
            )
            .append(Component.literal("  "))
            // Button 2: Disable this message
            .append(Component.translatable("message.frequency.startup.disable_message")
                .withStyle(style -> style
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/frequency toggleStartup"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        Component.translatable("message.frequency.startup.disable_hover")))
                )
            );
        
        mc.player.sendSystemMessage(message);
    }
}
