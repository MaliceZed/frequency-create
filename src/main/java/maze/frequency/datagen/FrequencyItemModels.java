package maze.frequency.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.ArrayList;

public class FrequencyItemModels implements DataProvider {
	private final PackOutput output;

	public FrequencyItemModels(PackOutput output) {
		this.output = output;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		List<String> symbols = new ArrayList<>(FrequencyModItems.SYMBOL_NAMES);
		List<String> andesiteSymbols = FrequencyModItems.ANDESITE_SYMBOL_NAMES;
		List<String> copperSymbols = FrequencyModItems.COPPER_SYMBOL_NAMES;

		@SuppressWarnings("unchecked")
		CompletableFuture<?>[] futures = new CompletableFuture[symbols.size() + andesiteSymbols.size() + copperSymbols.size() + 1];
		for (int i = 0; i < symbols.size(); i++) {
			String symbol = symbols.get(i);
			JsonObject model = new JsonObject();
			model.addProperty("parent", FrequencyMod.MODID + ":item/base/brass_symbol_base");
			JsonObject textures = new JsonObject();
			textures.addProperty("symbol", FrequencyMod.MODID + ":item/brass_symbols/" + symbol.substring("brass_".length()));
			model.add("textures", textures);

			ResourceLocation outputPath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "models/item/" + symbol);
			var path = output.getOutputFolder().resolve("assets/" + outputPath.getNamespace() + "/" + outputPath.getPath() + ".json");
			futures[i] = DataProvider.saveStable(cache, model, path);
		}

		for (int i = 0; i < andesiteSymbols.size(); i++) {
			String symbol = andesiteSymbols.get(i);
			JsonObject model = new JsonObject();
			model.addProperty("parent", FrequencyMod.MODID + ":item/base/andesite_symbol_base");
			JsonObject textures = new JsonObject();
			textures.addProperty("symbol", FrequencyMod.MODID + ":item/andesite_symbols/" + symbol.substring("andesite_".length()));
			model.add("textures", textures);

			ResourceLocation outputPath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "models/item/" + symbol);
			var path = output.getOutputFolder().resolve("assets/" + outputPath.getNamespace() + "/" + outputPath.getPath() + ".json");
			futures[symbols.size() + i] = DataProvider.saveStable(cache, model, path);
		}

		for (int i = 0; i < copperSymbols.size(); i++) {
			String symbol = copperSymbols.get(i);
			JsonObject model = new JsonObject();
			model.addProperty("parent", FrequencyMod.MODID + ":item/base/copper_symbol_base");
			JsonObject textures = new JsonObject();
			textures.addProperty("symbol", FrequencyMod.MODID + ":item/copper_symbols/" + symbol.substring("copper_".length()));
			model.add("textures", textures);

			ResourceLocation outputPath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "models/item/" + symbol);
			var path = output.getOutputFolder().resolve("assets/" + outputPath.getNamespace() + "/" + outputPath.getPath() + ".json");
			futures[symbols.size() + andesiteSymbols.size() + i] = DataProvider.saveStable(cache, model, path);
		}

		JsonObject frameModel = new JsonObject();
		frameModel.addProperty("parent", FrequencyMod.MODID + ":block/symbol_frame");
		JsonObject frameTextures = new JsonObject();
		frameTextures.addProperty("s070", FrequencyMod.MODID + ":block/symbols/symbol_creeperhead");
		frameModel.add("textures", frameTextures);
		ResourceLocation framePath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "models/item/symbol_frame");
		var frameFile = output.getOutputFolder().resolve("assets/" + framePath.getNamespace() + "/" + framePath.getPath() + ".json");
		futures[symbols.size() + andesiteSymbols.size() + copperSymbols.size()] = DataProvider.saveStable(cache, frameModel, frameFile);

		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Frequency Item Models";
	}
}
