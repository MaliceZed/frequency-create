package maze.frequency.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.ItemStack;
import maze.frequency.init.FrequencyModItems;
import java.util.ArrayList;
import java.util.List;

@EmiEntrypoint
public class FrequencyEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(FrequencyEmiRecipeCategory.INSTANCE);

        // ── Brass ─────────────────────────────────────────────────────
        var brassEmpty = FrequencyModItems.getSymbol("brass_symbol_empty");
        if (brassEmpty != null) {
            EmiStack brassInput = EmiStack.of(new ItemStack(brassEmpty.get()));
            List<EmiStack> brassStacks = new ArrayList<>();
            FrequencyModItems.ALL_BRASS_SYMBOLS.forEach(h -> brassStacks.add(EmiStack.of(new ItemStack(h.get()))));
            registry.addRecipe(new FrequencyEmiRecipe(brassInput, brassStacks));
        }

        // ── Andesite ──────────────────────────────────────────────────
        var andesiteEmpty = FrequencyModItems.getAndesiteSymbol("andesite_symbol_empty");
        if (andesiteEmpty != null) {
            EmiStack andesiteInput = EmiStack.of(new ItemStack(andesiteEmpty.get()));
            List<EmiStack> andesiteStacks = new ArrayList<>();
            FrequencyModItems.ALL_ANDESITE_SYMBOLS.forEach(h -> andesiteStacks.add(EmiStack.of(new ItemStack(h.get()))));
            registry.addRecipe(new FrequencyEmiRecipe(andesiteInput, andesiteStacks));
        }

        // ── Copper ────────────────────────────────────────────────────
        var copperEmpty = FrequencyModItems.getCopperSymbol("copper_symbol_empty");
        if (copperEmpty != null) {
            EmiStack copperInput = EmiStack.of(new ItemStack(copperEmpty.get()));
            List<EmiStack> copperStacks = new ArrayList<>();
            FrequencyModItems.ALL_COPPER_SYMBOLS.forEach(h -> copperStacks.add(EmiStack.of(new ItemStack(h.get()))));
            registry.addRecipe(new FrequencyEmiRecipe(copperInput, copperStacks));
        }
    }
}
