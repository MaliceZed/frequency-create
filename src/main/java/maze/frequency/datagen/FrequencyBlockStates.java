package maze.frequency.datagen;

import com.google.gson.JsonObject;

import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import maze.frequency.FrequencyMod;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FrequencyBlockStates implements DataProvider {
    private final PackOutput output;

    public FrequencyBlockStates(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        // === SYMBOL FRAME ===
        JsonObject symbolFrameVariants = new JsonObject();
        ResourceLocation sfModel = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "block/symbol_frame");

        List<Direction> sfFacings = List.of(
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST,
            Direction.UP, Direction.DOWN
        );
        List<Direction> rotations = List.of(
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
        );

        for (Direction facing : sfFacings) {
            for (Direction rotation : rotations) {
                String key = "facing=" + facing.getName() + ",rotation=" + rotation.getName();
                JsonObject variant = new JsonObject();
                variant.addProperty("model", sfModel.toString());

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

                symbolFrameVariants.add(key, variant);
            }
        }

        JsonObject symbolFrameBlockstate = new JsonObject();
        symbolFrameBlockstate.add("variants", symbolFrameVariants);
        ResourceLocation sfPath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "blockstates/symbol_frame");
        var sfOut = output.getOutputFolder().resolve("assets/" + sfPath.getNamespace() + "/" + sfPath.getPath() + ".json");
        CompletableFuture<?> sfFuture = DataProvider.saveStable(cache, symbolFrameBlockstate, sfOut);

        // === LOGIC COMBINATOR ===
        JsonObject lcVariants = new JsonObject();
        List<Direction> hFacings = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
        boolean[] bools = {false, true};

        for (Direction facing : hFacings) {
            for (boolean input1 : bools) {
                for (boolean input2 : bools) {
                    for (boolean output : bools) {
                        String key = "facing=" + facing.getName()
                            + ",input1=" + input1
                            + ",input2=" + input2
                            + ",output=" + output
                            + ",single=false";

                        String suffix = (input1 ? "1" : "0") + (output ? "1" : "0") + (input2 ? "1" : "0");
                        ResourceLocation model = ResourceLocation.fromNamespaceAndPath(
                            FrequencyMod.MODID, "block/logic_combinator/logic_combinator_" + suffix);

                        JsonObject variant = new JsonObject();
                        variant.addProperty("model", model.toString());
                        int yRot = rotationToY(facing);
                        if (yRot != 0) variant.addProperty("y", yRot);

                        lcVariants.add(key, variant);
                    }
                }
            }
        }

        for (Direction facing : hFacings) {
            for (boolean input1 : bools) {
                for (boolean output : bools) {
                    String key = "facing=" + facing.getName()
                        + ",input1=" + input1
                        + ",input2=false"
                        + ",output=" + output
                        + ",single=true";

                    String suffix = (input1 ? "1" : "0") + (output ? "1" : "0");
                    ResourceLocation model = ResourceLocation.fromNamespaceAndPath(
                        FrequencyMod.MODID, "block/logic_combinator/logic_combinator_s_" + suffix);

                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", model.toString());
                    int yRot = rotationToY(facing);
                    if (yRot != 0) variant.addProperty("y", yRot);

                    lcVariants.add(key, variant);
                }
            }
        }

        for (Direction facing : hFacings) {
            for (boolean input1 : bools) {
                for (boolean output : bools) {
                    String key = "facing=" + facing.getName()
                        + ",input1=" + input1
                        + ",input2=true"
                        + ",output=" + output
                        + ",single=true";

                    String suffix = (input1 ? "1" : "0") + (output ? "1" : "0");
                    ResourceLocation model = ResourceLocation.fromNamespaceAndPath(
                        FrequencyMod.MODID, "block/logic_combinator/logic_combinator_s_" + suffix);

                    JsonObject variant = new JsonObject();
                    variant.addProperty("model", model.toString());
                    int yRot = rotationToY(facing);
                    if (yRot != 0) variant.addProperty("y", yRot);

                    lcVariants.add(key, variant);
                }
            }
        }

        JsonObject lcBlockstate = new JsonObject();
        lcBlockstate.add("variants", lcVariants);
        ResourceLocation lcPath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "blockstates/logic_combinator");
        var lcOut = output.getOutputFolder().resolve("assets/" + lcPath.getNamespace() + "/" + lcPath.getPath() + ".json");
        CompletableFuture<?> lcFuture = DataProvider.saveStable(cache, lcBlockstate, lcOut);

        return CompletableFuture.allOf(sfFuture, lcFuture);
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
