package maze.frequency.item;

import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModMenus;
import net.minecraft.world.item.Item;

public class BrassBaseSymbolItem extends BaseSymbolItem {

    public BrassBaseSymbolItem(Item.Properties properties, String symbolName) {
        super(properties, symbolName, FrequencyModMenus.BRASS_SYMBOL_SWAP::get, FrequencyModItems::getAllBrassSymbolStacks);
    }
}
