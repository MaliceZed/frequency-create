package maze.frequency.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

/**
 * EMI recipe category for the Frequency Symbol Swapping mechanic.
 * <p>
 * Shows a scrollable gallery of all available symbols,
 * visually identical to the JEI counterpart.
 * <p>
 * Implemented as a singleton — {@link #INSTANCE} is the only instance.
 */
public class FrequencyEmiRecipeCategory extends EmiRecipeCategory {

    public static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "symbol_swap");

    /** The single shared instance of this category. */
    public static final FrequencyEmiRecipeCategory INSTANCE = new FrequencyEmiRecipeCategory();

    private FrequencyEmiRecipeCategory() {
        super(UID, EmiStack.of(new ItemStack(FrequencyModItems.getSymbol("brass_symbol_empty").get())));
    }

    @Override
    public Component getName() {
        return Component.translatable("jei.category.frequency.symbol_swap");
    }
}
