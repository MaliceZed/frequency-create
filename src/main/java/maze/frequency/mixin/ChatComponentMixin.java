package maze.frequency.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import maze.frequency.config.FrequencyConfig;

/**
 * Intercepts clicks on the "[Disable this message]" buttons of the version
 * check notification and the startup message. Each button uses a RUN_COMMAND
 * click event as a marker; this mixin cancels the standard command execution
 * and disables the corresponding feature directly, so no commands need to be
 * registered. Targeted at {@link net.minecraft.client.gui.screens.Screen#handleComponentClicked(Style)},
 * which is the single entry point for processing clickable chat styles (chat
 * messages go through ChatScreen.mouseClicked -> getComponentStyleAt ->
 * Screen.handleComponentClicked).
 */
@Mixin(net.minecraft.client.gui.screens.Screen.class)
@OnlyIn(Dist.CLIENT)
public abstract class ChatComponentMixin {

    @Unique
    private static final String FREQUENCY$DISABLE_VERSION_CHECK = "/frequency toggleVersionCheck";

    @Unique
    private static final String FREQUENCY$DISABLE_STARTUP_MESSAGE = "/frequency toggleStartup";

    @Inject(
        method = "handleComponentClicked",
        at = @At("HEAD"),
        cancellable = true
    )
    private void frequency$handleDisableButtons(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style == null) {
            return;
        }
        ClickEvent clickEvent = style.getClickEvent();
        if (clickEvent == null || clickEvent.getAction() != ClickEvent.Action.RUN_COMMAND) {
            return;
        }
        String value = clickEvent.getValue();
        if (FREQUENCY$DISABLE_VERSION_CHECK.equals(value)) {
            disableVersionCheck(cir);
        } else if (FREQUENCY$DISABLE_STARTUP_MESSAGE.equals(value)) {
            disableStartupMessage(cir);
        }
    }

    @Unique
    private static void disableVersionCheck(CallbackInfoReturnable<Boolean> cir) {
        FrequencyConfig.ENABLE_VERSION_CHECK.set(false);
        FrequencyConfig.SPEC.save();
        sendMessage("message.frequency.version_check.disabled");
        cir.setReturnValue(true);
    }

    @Unique
    private static void disableStartupMessage(CallbackInfoReturnable<Boolean> cir) {
        FrequencyConfig.ENABLE_STARTUP_MESSAGE.set(false);
        FrequencyConfig.SPEC.save();
        sendMessage("message.frequency.startup.disabled");
        cir.setReturnValue(true);
    }

    @Unique
    private static void sendMessage(String key) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendSystemMessage(Component.translatable(key));
        }
    }
}
