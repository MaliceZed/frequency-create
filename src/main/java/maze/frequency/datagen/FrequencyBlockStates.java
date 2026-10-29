package maze.frequency.datagen;

import com.google.gson.JsonArray;
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
        JsonArray lcMultipart = new JsonArray();
        List<Direction> hFacings = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
        boolean[] bools = {false, true};

        for (Direction facing : hFacings) {
            // Корпус и шесть блоков частот присутствуют при любом состоянии входов и выхода.
            addLogicCombinatorPart(lcMultipart, facing, "chassi");

            // Антенна входа 1 выбирается по состоянию input1.
            for (boolean input1 : bools) {
                addLogicCombinatorPart(
                    lcMultipart,
                    facing,
                    input1 ? "input1_on" : "input1_off",
                    "input1",
                    Boolean.toString(input1)
                );
            }

            // Антенна выхода выбирается по состоянию output.
            for (boolean output : bools) {
                addLogicCombinatorPart(
                    lcMultipart,
                    facing,
                    output ? "output_on" : "output_off",
                    "output",
                    Boolean.toString(output)
                );
            }

            // В одиночном режиме вход 2 всегда отображается погашенным.
            addLogicCombinatorPart(lcMultipart, facing, "input2_disabled", "single", "true");

            // В обычном режиме вход 2 выбирается по состоянию input2.
            for (boolean input2 : bools) {
                addLogicCombinatorPart(
                    lcMultipart,
                    facing,
                    input2 ? "input2_on" : "input2_off",
                    "single", "false",
                    "input2", Boolean.toString(input2)
                );
            }
        }

        JsonObject lcBlockstate = new JsonObject();
        lcBlockstate.add("multipart", lcMultipart);
        ResourceLocation lcPath = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "blockstates/logic_combinator");
        var lcOut = output.getOutputFolder().resolve("assets/" + lcPath.getNamespace() + "/" + lcPath.getPath() + ".json");
        CompletableFuture<?> lcFuture = DataProvider.saveStable(cache, lcBlockstate, lcOut);

        return CompletableFuture.allOf(sfFuture, lcFuture);
    }

    private static void addLogicCombinatorPart(
        JsonArray multipart,
        Direction facing,
        String modelName,
        String... whenProperties
    ) {
        JsonObject part = new JsonObject();
        JsonObject when = new JsonObject();
        when.addProperty("facing", facing.getName());
        for (int i = 0; i < whenProperties.length; i += 2) {
            when.addProperty(whenProperties[i], whenProperties[i + 1]);
        }
        part.add("when", when);

        JsonObject apply = new JsonObject();
        ResourceLocation model = ResourceLocation.fromNamespaceAndPath(
            FrequencyMod.MODID, "block/logic_combinator/" + modelName);
        apply.addProperty("model", model.toString());

        int yRot = rotationToY(facing);
        if (yRot != 0) apply.addProperty("y", yRot);

        part.add("apply", apply);
        multipart.add(part);
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
