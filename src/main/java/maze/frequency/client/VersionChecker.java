package maze.frequency.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import maze.frequency.FrequencyMod;
import maze.frequency.config.FrequencyConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforgespi.language.IModInfo;

/**
 * Client-side version checker.
 *
 * Fetches build.gradle from the mod's GitHub repository on the NeoForge-1.21.1
 * branch, parses the "version = 'X.Y.Z'" line and shows a chat message if a
 * newer version is available. Runs fully asynchronously and never blocks the
 * game thread.
 */
@EventBusSubscriber(modid = FrequencyMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class VersionChecker {

    private static final String VERSION_URL =
        "https://raw.githubusercontent.com/MaliceZed/frequency-create/NeoForge-1.21.1/build.gradle";

    private static final Pattern VERSION_PATTERN =
        Pattern.compile("(?m)^\\s*version\\s*=\\s*['\"]([^'\"]+)['\"]");

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();

    private static boolean checkedThisSession = false;

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        checkForUpdatesAsync();
    }

    public static void checkForUpdatesAsync() {
        if (checkedThisSession || !FrequencyConfig.ENABLE_VERSION_CHECK.get()) {
            return;
        }
        checkedThisSession = true;

        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder(URI.create(VERSION_URL))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();
                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    return;
                }
                Matcher matcher = VERSION_PATTERN.matcher(response.body());
                if (!matcher.find()) {
                    return;
                }
                String latestVersion = matcher.group(1);
                String currentVersion = ModList.get().getModContainerById(FrequencyMod.MODID)
                    .map(ModContainer::getModInfo)
                    .map(IModInfo::getVersion)
                    .map(v -> v.toString())
                    .orElse("unknown");
                if (isNewer(latestVersion, currentVersion)) {
                    Minecraft.getInstance().execute(() -> {
                        if (Minecraft.getInstance().player != null) {
                            String curseforgeUrl = "https://www.curseforge.com/minecraft/mc-mods/frequency-create";
                            String modrinthUrl = "https://modrinth.com/mod/frequency-create";

                            Component message = Component.literal("")
                                .append(Component.translatable("message.frequency.version_check.title"))
                                .append(Component.literal("\n"))
                                .append(Component.translatable("message.frequency.version_check.curseforge")
                                    .withStyle(style -> style
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, curseforgeUrl))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Component.translatable("message.frequency.version_check.curseforge_hover")))
                                    )
                                )
                                .append(Component.literal(" "))
                                .append(Component.translatable("message.frequency.version_check.modrinth")
                                    .withStyle(style -> style
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, modrinthUrl))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Component.translatable("message.frequency.version_check.modrinth_hover")))
                                    )
                                )
                                .append(Component.literal(" "))
                                .append(Component.translatable("message.frequency.version_check.disable_message")
                                    .withStyle(style -> style
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/frequency toggleVersionCheck"))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Component.translatable("message.frequency.version_check.disable_hover")))
                                    )
                                )
                                .append(Component.literal("\n"));

                            Minecraft.getInstance().player.sendSystemMessage(message);
                        }
                    });
                }
            } catch (Exception e) {
                // Ignore failures silently (offline, network errors, repo renamed, etc.)
            }
        });
    }

    private static boolean isNewer(String latest, String current) {
        String[] latestParts = latest.split("-", 2);
        String[] currentParts = current.split("-", 2);

        int[] latestNumbers = parseNumbers(latestParts[0]);
        int[] currentNumbers = parseNumbers(currentParts[0]);

        int maxLength = Math.max(latestNumbers.length, currentNumbers.length);
        for (int i = 0; i < maxLength; i++) {
            int latestNumber = i < latestNumbers.length ? latestNumbers[i] : 0;
            int currentNumber = i < currentNumbers.length ? currentNumbers[i] : 0;
            if (latestNumber != currentNumber) {
                return latestNumber > currentNumber;
            }
        }

        // Numeric parts are equal. Suffix order: none < -a < -b < -c ... (0.1.6 < 0.1.6-a < 0.1.6-b)
        String latestSuffix = latestParts.length > 1 ? latestParts[1] : "";
        String currentSuffix = currentParts.length > 1 ? currentParts[1] : "";
        if (latestSuffix.isEmpty() && currentSuffix.isEmpty()) {
            return false;
        }
        if (latestSuffix.isEmpty()) {
            return false;
        }
        if (currentSuffix.isEmpty()) {
            return true;
        }
        return latestSuffix.compareTo(currentSuffix) > 0;
    }

    private static int[] parseNumbers(String value) {
        String[] parts = value.split("\\.");
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            numbers[i] = parseInt(parts[i]);
        }
        return numbers;
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
