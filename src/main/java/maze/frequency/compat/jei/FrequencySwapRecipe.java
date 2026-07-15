package maze.frequency.compat.jei;

import net.minecraft.world.item.ItemStack;
import java.util.List;

/**
 * JEI recipe representing the Frequency symbol-swapping mechanic.
 * <p>
 * A single recipe shows one representative input symbol and ALL available
 * symbols as outputs in a scrollable grid (9 columns, 4 visible rows).
 * <p>
 * In the actual game, any held symbol can be swapped for any other symbol
 * by right-clicking → opening the swapper → selecting a target symbol.
 *
 * @param input   a representative input symbol (single ItemStack)
 * @param outputs every symbol that can be swapped to (full catalogue)
 */
public record FrequencySwapRecipe(ItemStack input, List<ItemStack> outputs) {

    public FrequencySwapRecipe {
        outputs = List.copyOf(outputs);
    }

    /** Convenience: number of available target symbols. */
    public int getOutputCount() {
        return outputs.size();
    }
}
