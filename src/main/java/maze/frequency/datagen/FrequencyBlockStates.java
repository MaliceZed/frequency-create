package maze.frequency.datagen;

import com.google.gson.JsonObject;

import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import maze.frequency.FrequencyMod;
import maze.frequency.block.SymbolFrameBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FrequencyBlockStates implements DataProvider {
    private final PackOutput output;

    public FrequencyBlockStates(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject variants = new JsonObject();
        ResourceLocation model = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "block/symbol_frame");

        List<Direction> facings = List.of(
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST,
            Direction.UP, Direction.DOWN
        );
        List<Direction> rotations = List.of(
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
        );

        for (Direction facing : facings) {
            for (Direction rotation : rotations) {
                String key = "facing=" + facing.getName() + ",rotation=" + rotation.getName();

                JsonObject variant = new JsonObject();
                variant.addProperty("model", model.toString());

                int xRot = 0;
                int yRot = 0;

                if (facing == Direction.UP) {
                    xRot = 270;
                    yRot = (rotationToY(rotation) + 180) % 360;
                } else if (facing == Direction.DOWN) {
                    xRot = 90;
                    yRot = (rotationToY(rotation) + 180) % 360;
                } else {
                    yRot = rotationToY(facing);
                }

                if (xRot != 0) variant.addProperty("x", xRot);
                if (yRot != 0) variant.addProperty("y", yRot);

                variants.add(key, variant);
            }
        }

        // Blockstate JSON with 24 variants
        JsonObject blockstate = new JsonObject();
        blockstate.add("variants", variants);

        ResourceLocation outPath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "blockstates/symbol_frame");
        var path = output.getOutputFolder().resolve("assets/" + outPath.getNamespace() + "/" + outPath.getPath() + ".json");
        return DataProvider.saveStable(cache, blockstate, path);
    }

    private static int rotationToY(Direction dir) {
        return switch (dir) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    @Override
    public String getName() {
        return "Frequency Block States";
    }
}
