package maze.frequency.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class IncompleteSymbolItem extends Item {
	public IncompleteSymbolItem() {
		super(new Item.Properties().stacksTo(8).rarity(Rarity.UNCOMMON));
	}
}
