package maze.frequency.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.world.item.ItemStack;
import maze.frequency.init.FrequencyModItems;

import java.util.ArrayList;
import java.util.List;

@REIPluginClient
public class FrequencyReiPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FrequencyReiCategory());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        // ── Brass ─────────────────────────────────────────────────────
        var brassEmpty = FrequencyModItems.getSymbol("brass_symbol_empty");
        if (brassEmpty != null) {
            List<EntryStack<ItemStack>> brassOutputs = new ArrayList<>();
            FrequencyModItems.ALL_BRASS_SYMBOLS.forEach(h -> brassOutputs.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(h.get()))));
            registry.add(new FrequencyReiDisplay(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(brassEmpty.get())), brassOutputs));
        }

        // ── Andesite ──────────────────────────────────────────────────
        var andesiteEmpty = FrequencyModItems.getAndesiteSymbol("andesite_symbol_empty");
        if (andesiteEmpty != null) {
            List<EntryStack<ItemStack>> andesiteOutputs = new ArrayList<>();
            FrequencyModItems.ALL_ANDESITE_SYMBOLS.forEach(h -> andesiteOutputs.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(h.get()))));
            registry.add(new FrequencyReiDisplay(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(andesiteEmpty.get())), andesiteOutputs));
        }

        // ── Copper ────────────────────────────────────────────────────
        var copperEmpty = FrequencyModItems.getCopperSymbol("copper_symbol_empty");
        if (copperEmpty != null) {
            List<EntryStack<ItemStack>> copperOutputs = new ArrayList<>();
            FrequencyModItems.ALL_COPPER_SYMBOLS.forEach(h -> copperOutputs.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(h.get()))));
            registry.add(new FrequencyReiDisplay(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(copperEmpty.get())), copperOutputs));
        }
    }
}
