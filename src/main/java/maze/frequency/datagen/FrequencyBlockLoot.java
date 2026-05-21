package maze.frequency.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import maze.frequency.FrequencyMod;

import java.util.concurrent.CompletableFuture;

public class FrequencyBlockLoot implements DataProvider {
    private final PackOutput output;

    public FrequencyBlockLoot(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject lootTable = new JsonObject();
        lootTable.addProperty("type", "minecraft:block");

        JsonArray pools = new JsonArray();
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1);

        JsonArray entries = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", "frequency:symbol_frame");
        entries.add(entry);
        pool.add("entries", entries);

        JsonArray conditions = new JsonArray();
        JsonObject condition = new JsonObject();
        condition.addProperty("condition", "minecraft:survives_explosion");
        conditions.add(condition);
        pool.add("conditions", conditions);

        pools.add(pool);
        lootTable.add("pools", pools);

        ResourceLocation outPath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "loot_table/blocks/symbol_frame");
        var path = output.getOutputFolder().resolve("data/" + outPath.getNamespace() + "/" + outPath.getPath() + ".json");
        return DataProvider.saveStable(cache, lootTable, path);
    }

    @Override
    public String getName() {
        return "Frequency Block Loot Tables";
    }
}
