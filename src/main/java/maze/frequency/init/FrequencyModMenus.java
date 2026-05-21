package maze.frequency.init;

import com.tterrag.registrate.util.entry.MenuEntry;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.RegistryFriendlyByteBuf;

import maze.frequency.FrequencyMod;
import maze.frequency.world.inventory.SymbolSwapMenu;
import maze.frequency.client.gui.SymbolSwapScreen;

public class FrequencyModMenus {
	public static final MenuEntry<SymbolSwapMenu> SYMBOL_SWAP = FrequencyMod.REGISTRATE
		.menu("symbol_swap",
			(MenuType<SymbolSwapMenu> type, int id, Inventory inv, RegistryFriendlyByteBuf buf) ->
				new SymbolSwapMenu(id, inv, buf),
			() -> SymbolSwapScreen::new)
		.register();
}
