package maze.frequency.init;

import com.tterrag.registrate.util.entry.MenuEntry;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;

import maze.frequency.FrequencyMod;
import maze.frequency.world.inventory.SymbolSwapMenu;
import maze.frequency.client.gui.BrassSymbolSwapScreen;
import maze.frequency.client.gui.AndesiteSymbolSwapScreen;
import maze.frequency.client.gui.CopperSymbolSwapScreen;
import maze.frequency.world.inventory.LogicCombinatorMenu;
import maze.frequency.client.gui.LogicCombinatorScreen;


public class FrequencyModMenus {
	public static final MenuEntry<SymbolSwapMenu> BRASS_SYMBOL_SWAP = FrequencyMod.REGISTRATE
		.menu("symbol_swap",
			(MenuType<SymbolSwapMenu> type, int id, Inventory inv, RegistryFriendlyByteBuf buf) ->
				new SymbolSwapMenu(type, id, inv, buf),
			() -> (SymbolSwapMenu menu, Inventory inv, Component title) ->
				new BrassSymbolSwapScreen(menu, inv, title))
		.register();

	public static final MenuEntry<SymbolSwapMenu> ANDESITE_SYMBOL_SWAP = FrequencyMod.REGISTRATE
		.menu("andesite_symbol_swap",
			(MenuType<SymbolSwapMenu> type, int id, Inventory inv, RegistryFriendlyByteBuf buf) ->
				new SymbolSwapMenu(type, id, inv, buf),
			() -> (SymbolSwapMenu menu, Inventory inv, Component title) ->
				new AndesiteSymbolSwapScreen(menu, inv, title))
		.register();

	public static final MenuEntry<SymbolSwapMenu> COPPER_SYMBOL_SWAP = FrequencyMod.REGISTRATE
		.menu("copper_symbol_swap",
			(MenuType<SymbolSwapMenu> type, int id, Inventory inv, RegistryFriendlyByteBuf buf) ->
				new SymbolSwapMenu(type, id, inv, buf),
			() -> (SymbolSwapMenu menu, Inventory inv, Component title) ->
				new CopperSymbolSwapScreen(menu, inv, title))
		.register();

	public static final MenuEntry<LogicCombinatorMenu> LOGIC_COMBINATOR = FrequencyMod.REGISTRATE
		.menu("logic_combinator",
			(MenuType<LogicCombinatorMenu> type, int id, Inventory inv, RegistryFriendlyByteBuf buf) ->
				new LogicCombinatorMenu(type, id, inv, buf),
			() -> (LogicCombinatorMenu menu, Inventory inv, Component title) ->
				new LogicCombinatorScreen(menu, inv, title))
		.register();
}
