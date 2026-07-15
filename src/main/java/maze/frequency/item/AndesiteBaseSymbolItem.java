package maze.frequency.item;

import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModMenus;
import net.minecraft.world.item.Item;

public class AndesiteBaseSymbolItem extends BaseSymbolItem {

    public AndesiteBaseSymbolItem(Item.Properties properties, String symbolName) {
        super(properties, symbolName, FrequencyModMenus.ANDESITE_SYMBOL_SWAP::get, FrequencyModItems::getAllAndesiteSymbolStacks);
    }
}
