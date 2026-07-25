package maze.frequency.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import maze.frequency.FrequencyMod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FrequencyBlockLoot implements DataProvider {
    private final PackOutput output;

    /** All blocks that use a simple "drop self + survives_explosion" loot table */
    private static final List<String> SIMPLE_DROP_BLOCKS = List.of(
            "symbol_frame",
            "logic_combinator"
    );

    public FrequencyBlockLoot(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (String blockName : SIMPLE_DROP_BLOCKS) {
            futures.add(saveSimpleBlockLoot(cache, blockName));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    /**
     * Generates a standard block loot table: type=minecraft:block, pool with survives_explosion, drop-self.
     */
    private CompletableFuture<?> saveSimpleBlockLoot(CachedOutput cache, String blockName) {
        JsonObject lootTable = new JsonObject();
        lootTable.addProperty("type", "minecraft:block");

        JsonArray pools = new JsonArray();
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1);

        JsonArray entries = new JsonArray();
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", FrequencyMod.MODID + ":" + blockName);
        entries.add(entry);
        pool.add("entries", entries);

        JsonArray conditions = new JsonArray();
        JsonObject condition = new JsonObject();
        condition.addProperty("condition", "minecraft:survives_explosion");
        conditions.add(condition);
        pool.add("conditions", conditions);

        pools.add(pool);
        lootTable.add("pools", pools);

        ResourceLocation outPath = ResourceLocation.fromNamespaceAndPath(
                FrequencyMod.MODID, "loot_table/blocks/" + blockName);
        var path = output.getOutputFolder()
                .resolve("data/" + outPath.getNamespace() + "/" + outPath.getPath() + ".json");
        return DataProvider.saveStable(cache, lootTable, path);
    }

    @Override
    public String getName() {
        return "Frequency Block Loot Tables";
    }
}
