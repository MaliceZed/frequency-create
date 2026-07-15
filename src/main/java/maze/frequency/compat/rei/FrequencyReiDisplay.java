package maze.frequency.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class FrequencyReiDisplay extends BasicDisplay {

    private static final int COLS = 6;
    private static final int ROWS = 3;

    private final int scrollOffset;
    private final int maxScroll;

    public FrequencyReiDisplay(EntryStack<ItemStack> input, List<EntryStack<ItemStack>> outputs) {
        super(
            List.of(EntryIngredient.of(input)),
            Collections.singletonList(EntryIngredient.of(outputs))
        );

        int totalRows = (outputs.size() + COLS - 1) / COLS;
        this.maxScroll = Math.max(0, totalRows - ROWS);
        this.scrollOffset = 0;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FrequencyReiCategory.ID;
    }

    public EntryStack<ItemStack> getInputEntry() {
        return inputs.get(0).get(0).cast();
    }

    /** @return output EntryStacks as a flat list (not the raw ingredient list) */
    public List<EntryStack<ItemStack>> getOutputStacks() {
        return outputs.get(0).stream()
            .map(entry -> entry.<ItemStack>cast())
            .toList();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public int getMaxScroll() {
        return maxScroll;
    }
}
