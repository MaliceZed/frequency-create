package maze.frequency.item;

import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModMenus;
import net.minecraft.world.item.Item;

public class CopperBaseSymbolItem extends BaseSymbolItem {

    public CopperBaseSymbolItem(Item.Properties properties, String symbolName) {
        super(properties, symbolName, FrequencyModMenus.COPPER_SYMBOL_SWAP::get, FrequencyModItems::getAllCopperSymbolStacks);
    }
}
