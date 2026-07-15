package maze.frequency.init;

import com.tterrag.registrate.util.entry.MenuEntry;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import maze.frequency.FrequencyMod;
import maze.frequency.world.inventory.SymbolSwapMenu;
import maze.frequency.client.gui.SymbolSwapScreen;


public class FrequencyModMenus {
	public static final MenuEntry<SymbolSwapMenu> BRASS_SYMBOL_SWAP = FrequencyMod.REGISTRATE
		.menu("symbol_swap",
			(MenuType<SymbolSwapMenu> type, int id, Inventory inv, RegistryFriendlyByteBuf buf) ->
				new SymbolSwapMenu(type, id, inv, buf),
			() -> (SymbolSwapMenu menu, Inventory inv, Component title) ->
				new SymbolSwapScreen(menu, inv, title))
		.register();

	public static final MenuEntry<SymbolSwapMenu> ANDESITE_SYMBOL_SWAP = FrequencyMod.REGISTRATE
		.menu("andesite_symbol_swap",
			(MenuType<SymbolSwapMenu> type, int id, Inventory inv, RegistryFriendlyByteBuf buf) ->
				new SymbolSwapMenu(type, id, inv, buf),
			() -> (SymbolSwapMenu menu, Inventory inv, Component title) ->
				new SymbolSwapScreen(menu, inv, title, 
					ResourceLocation.fromNamespaceAndPath("frequency", "textures/gui/andesite_symbol_swap.png"),
					0x333333, "andesite_symbol_"))
		.register();

	public static final MenuEntry<SymbolSwapMenu> COPPER_SYMBOL_SWAP = FrequencyMod.REGISTRATE
		.menu("copper_symbol_swap",
			(MenuType<SymbolSwapMenu> type, int id, Inventory inv, RegistryFriendlyByteBuf buf) ->
				new SymbolSwapMenu(type, id, inv, buf),
			() -> (SymbolSwapMenu menu, Inventory inv, Component title) ->
				new SymbolSwapScreen(menu, inv, title, 
					ResourceLocation.fromNamespaceAndPath("frequency", "textures/gui/copper_symbol_swap.png"),
					0x6B2A1A, "copper_symbol_"))
		.register();
}
