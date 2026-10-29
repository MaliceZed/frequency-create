package maze.frequency.client.gui;

import maze.frequency.world.inventory.SymbolSwapMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CreativeSymbolSwapScreen extends BaseSymbolSwapScreen {
    public CreativeSymbolSwapScreen(SymbolSwapMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, DynamicGuiRenderer.CREATIVE_ATLAS, 0x321B3C, "creative_symbol_");
    }
}
