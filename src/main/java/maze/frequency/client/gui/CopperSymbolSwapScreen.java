package maze.frequency.client.gui;

import maze.frequency.world.inventory.SymbolSwapMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CopperSymbolSwapScreen extends BaseSymbolSwapScreen {
    public CopperSymbolSwapScreen(SymbolSwapMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, DynamicGuiRenderer.COPPER_ATLAS, 0x6B2A1A, "copper_symbol_");
    }
}
