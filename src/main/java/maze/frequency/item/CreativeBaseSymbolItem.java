package maze.frequency.item;

import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModMenus;
import net.minecraft.world.item.Item;

public class CreativeBaseSymbolItem extends BaseSymbolItem {

    public CreativeBaseSymbolItem(Item.Properties properties, String symbolName) {
        super(properties, symbolName, FrequencyModMenus.CREATIVE_SYMBOL_SWAP::get, FrequencyModItems::getAllCreativeSymbolStacks);
    }
}
