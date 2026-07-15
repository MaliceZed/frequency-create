package maze.frequency.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import maze.frequency.FrequencyMod;

public class FrequencyModTabs {
	public static final DeferredRegister<CreativeModeTab> TABS =
		DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FrequencyMod.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FREQUENCY_CREATE_TAB =
		TABS.register("frequency_create_tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(FrequencyModItems.getSymbol("brass_symbol_empty").get()))
			.title(Component.translatable("item_group.frequency.frequency_create_tab"))
			.displayItems((params, output) -> {
				output.accept(FrequencyModItems.getSymbol("brass_symbol_empty").get().getDefaultInstance());
				output.accept(FrequencyModItems.getAndesiteSymbol("andesite_symbol_empty").get().getDefaultInstance());
				output.accept(FrequencyModItems.getCopperSymbol("copper_symbol_empty").get().getDefaultInstance());
				output.accept(FrequencyModBlocks.SYMBOL_FRAME_ITEM.get().getDefaultInstance());
			})
			.build());
}
