package maze.frequency.forge.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import maze.frequency.client.gui.SymbolSwapScreen;
import maze.frequency.forge.network.PacketHandler;
import maze.frequency.init.FrequencyModMenus;
import maze.frequency.network.SymbolSwapPacket;
import maze.frequency.world.inventory.SymbolSwapMenu;

public class ClientSetup {
	public static void init() {
		MenuScreens.register(FrequencyModMenus.SYMBOL_SWAP.get(), new MenuScreens.ScreenConstructor<SymbolSwapMenu, SymbolSwapScreen>() {
			@Override
			public SymbolSwapScreen create(SymbolSwapMenu menu, Inventory playerInventory, Component title) {
				return new SymbolSwapScreen(menu, playerInventory, title, index -> {
					PacketHandler.sendToServer(new SymbolSwapPacket(index));
				});
			}
		});
	}
}
