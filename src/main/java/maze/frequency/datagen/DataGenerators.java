package maze.frequency.datagen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.common.data.LanguageProvider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.world.level.block.Block;

import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class DataGenerators {
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();

        generator.addProvider(true, new LanguageProvider(output, FrequencyMod.MODID, "en_us") {
            @Override
            protected void addTranslations() {
                Set<String> added = new HashSet<>();
                BiConsumer<String, String> consumer = (key, value) -> {
                    add(key, value);
                    added.add(key);
                };
                try {
                    loadDefaultLang(consumer, "en_us");
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load default language en_us", e);
                }
                autoGenerateItemTranslations(consumer, added, true);
            }
        });

        generator.addProvider(true, new LanguageProvider(output, FrequencyMod.MODID, "ru_ru") {
            @Override
            protected void addTranslations() {
                Set<String> added = new HashSet<>();
                BiConsumer<String, String> consumer = (key, value) -> {
                    add(key, value);
                    added.add(key);
                };
                try {
                    loadDefaultLang(consumer, "ru_ru");
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load default language ru_ru", e);
                }
                autoGenerateItemTranslations(consumer, added, false);
            }
        });

        generator.addProvider(true, new FrequencyItemModels(output));
        generator.addProvider(true, new FrequencyBlockStates(output));
        generator.addProvider(true, new FrequencyBlockLoot(output));
        generator.addProvider(true, new FrequencyRecipeProvider(output, event.getLookupProvider()));

        var blockTagProvider = new FrequencyBlockTagProvider(
            output,
            event.getLookupProvider(),
            event.getExistingFileHelper()
        );
        generator.addProvider(true, blockTagProvider);

        generator.addProvider(true, new FrequencyItemTagProvider(
            output,
            event.getLookupProvider(),
            CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()),
            event.getExistingFileHelper()
        ));
    }

    public static void loadDefaultLang(BiConsumer<String, String> consumer, String locale) throws IOException {
        loadCategory(consumer, locale, "interface");
    }

    private static void loadCategory(BiConsumer<String, String> consumer, String locale, String category) throws IOException {
        String path = "/assets/" + FrequencyMod.MODID + "/lang/default/" + locale + "/" + category + ".json";
        try (InputStream in = DataGenerators.class.getResourceAsStream(path);
             Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            if (in == null) {
                return;
            }
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            for (var entry : json.entrySet()) {
                consumer.accept(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }

    private static void autoGenerateItemTranslations(BiConsumer<String, String> consumer, Set<String> added, boolean english) {
        String prefix = english ? "Symbol " : "Символ ";
        for (String name : FrequencyModItems.SYMBOL_NAMES) {
            if (name.endsWith("_symbol_empty")) continue;
            String key = "item.frequency." + name;
            if (added.contains(key)) continue;
            consumer.accept(key, prefix + FrequencyModItems.displayChar(name));
        }
        for (String name : FrequencyModItems.ANDESITE_SYMBOL_NAMES) {
            if (name.endsWith("_symbol_empty")) continue;
            String key = "item.frequency." + name;
            if (added.contains(key)) continue;
            consumer.accept(key, prefix + FrequencyModItems.displayChar(name));
        }
        for (String name : FrequencyModItems.COPPER_SYMBOL_NAMES) {
            if (name.endsWith("_symbol_empty")) continue;
            String key = "item.frequency." + name;
            if (added.contains(key)) continue;
            consumer.accept(key, prefix + FrequencyModItems.displayChar(name));
        }
    }
}
