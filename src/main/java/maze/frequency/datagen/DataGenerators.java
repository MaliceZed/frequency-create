package maze.frequency.datagen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.common.data.LanguageProvider;

import net.minecraft.data.PackOutput;

import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.function.BiConsumer;
import java.util.Set;
import java.util.HashSet;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

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
				loadDefaultLang(consumer, "en_us");
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
				loadDefaultLang(consumer, "ru_ru");
				autoGenerateItemTranslations(consumer, added, false);
			}
		});

		generator.addProvider(true, new FrequencyItemModels(output));
		generator.addProvider(true, new FrequencyBlockStates(output));
		generator.addProvider(true, new FrequencyBlockLoot(output));
	}

	public static void loadDefaultLang(BiConsumer<String, String> consumer, String locale) {
		loadCategory(consumer, locale, "interface");
	}

	private static void loadCategory(BiConsumer<String, String> consumer, String locale, String category) {
		String path = "/assets/" + FrequencyMod.MODID + "/lang/default/" + locale + "/" + category + ".json";
		try (InputStream in = DataGenerators.class.getResourceAsStream(path)) {
			if (in == null) {
				return;
			}
			JsonObject json = JsonParser.parseReader(new InputStreamReader(in)).getAsJsonObject();
			for (var entry : json.entrySet()) {
				consumer.accept(entry.getKey(), entry.getValue().getAsString());
			}
		} catch (IOException e) {
		}
	}

	private static void autoGenerateItemTranslations(BiConsumer<String, String> consumer, Set<String> added, boolean english) {
		String prefix = english ? "Symbol " : "Символ ";
		for (String name : FrequencyModItems.SYMBOL_NAMES) {
			String key = "item.frequency." + name;
			if (added.contains(key)) continue;
			consumer.accept(key, prefix + FrequencyModItems.displayChar(name));
		}
		String incompleteKey = "item.frequency.incomplete_symbol";
		if (!added.contains(incompleteKey)) {
			consumer.accept(incompleteKey, english ? "Incomplete Symbol" : "Незаконченный символ");
		}
	}
}
