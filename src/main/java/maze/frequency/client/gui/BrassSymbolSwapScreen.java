package maze.frequency.client.gui;

import maze.frequency.world.inventory.SymbolSwapMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BrassSymbolSwapScreen extends BaseSymbolSwapScreen {
    public BrassSymbolSwapScreen(SymbolSwapMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, DynamicGuiRenderer.BRASS_ATLAS, 0x582424, "brass_symbol_");
    }
}
