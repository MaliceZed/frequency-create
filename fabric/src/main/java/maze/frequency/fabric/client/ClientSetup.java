package maze.frequency.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import maze.frequency.client.gui.SymbolSwapScreen;
import maze.frequency.fabric.network.PacketHandler;
import maze.frequency.init.FrequencyModMenus;
import maze.frequency.world.inventory.SymbolSwapMenu;

public class ClientSetup implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        init();
    }

    public static void init() {
        MenuScreens.register(FrequencyModMenus.SYMBOL_SWAP.get(), new MenuScreens.ScreenConstructor<SymbolSwapMenu, SymbolSwapScreen>() {
            @Override
            public SymbolSwapScreen create(SymbolSwapMenu menu, Inventory playerInventory, Component title) {
                return new SymbolSwapScreen(menu, playerInventory, title, index -> {
                    PacketHandler.sendToServer(new maze.frequency.network.SymbolSwapPacket(index));
                });
            }
        });
    }
}
