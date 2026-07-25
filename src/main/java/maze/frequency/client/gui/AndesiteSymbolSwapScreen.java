package maze.frequency.client.gui;

import maze.frequency.world.inventory.SymbolSwapMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AndesiteSymbolSwapScreen extends BaseSymbolSwapScreen {
    public AndesiteSymbolSwapScreen(SymbolSwapMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, DynamicGuiRenderer.ANDESITE_ATLAS, 0x333333, "andesite_symbol_");
    }
}
