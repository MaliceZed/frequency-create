package maze.frequency.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.ArrayList;
import java.util.List;

/**
 * JEI plugin entry point for Frequency mod.
 * <p>
 * Registers the {@link FrequencyRecipeCategory} and populates it with
 * a single representative recipe: one input symbol → all available symbols
 * in a scrollable output grid.
 */
@JeiPlugin
public class FrequencyJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_UID =
            ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new FrequencyRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<FrequencySwapRecipe> recipes = buildSwapRecipes();
        registration.addRecipes(FrequencyRecipeCategory.RECIPE_TYPE, recipes);
    }

    // ── Recipe generator ─────────────────────────────────────────────────────

    /**
     * Builds three separate symbol-swap recipes — one per tier.
     * <p>
     * Each recipe uses the tier's empty symbol as input and lists
     * every symbol of that tier as a scrollable output.
     */
    private static List<FrequencySwapRecipe> buildSwapRecipes() {
        List<FrequencySwapRecipe> recipes = new ArrayList<>();

        // ── Brass ─────────────────────────────────────────────────────
        var brassEmpty = FrequencyModItems.getSymbol("brass_symbol_empty");
        if (brassEmpty != null) {
            List<ItemStack> brassOutputs = new ArrayList<>();
            FrequencyModItems.ALL_BRASS_SYMBOLS.forEach(h -> brassOutputs.add(new ItemStack(h.get())));
            recipes.add(new FrequencySwapRecipe(new ItemStack(brassEmpty.get()), List.copyOf(brassOutputs)));
        }

        // ── Andesite ──────────────────────────────────────────────────
        var andesiteEmpty = FrequencyModItems.getAndesiteSymbol("andesite_symbol_empty");
        if (andesiteEmpty != null) {
            List<ItemStack> andesiteOutputs = new ArrayList<>();
            FrequencyModItems.ALL_ANDESITE_SYMBOLS.forEach(h -> andesiteOutputs.add(new ItemStack(h.get())));
            recipes.add(new FrequencySwapRecipe(new ItemStack(andesiteEmpty.get()), List.copyOf(andesiteOutputs)));
        }

        // ── Copper ────────────────────────────────────────────────────
        var copperEmpty = FrequencyModItems.getCopperSymbol("copper_symbol_empty");
        if (copperEmpty != null) {
            List<ItemStack> copperOutputs = new ArrayList<>();
            FrequencyModItems.ALL_COPPER_SYMBOLS.forEach(h -> copperOutputs.add(new ItemStack(h.get())));
            recipes.add(new FrequencySwapRecipe(new ItemStack(copperEmpty.get()), List.copyOf(copperOutputs)));
        }

        return recipes;
    }
}
