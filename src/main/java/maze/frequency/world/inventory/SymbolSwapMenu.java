package maze.frequency.world.inventory;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModMenus;

import java.util.List;
import java.util.ArrayList;

public class SymbolSwapMenu extends AbstractContainerMenu {
	private final Player player;
	private final ItemStack heldItem;
	private final int heldSlot;
	private final List<ItemStack> availableSymbols;

	public SymbolSwapMenu(int id, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
		this(id, playerInventory,
			ItemStack.STREAM_CODEC.decode(extraData),
			extraData.readInt());
	}

	public SymbolSwapMenu(int id, Inventory playerInventory, ItemStack heldItem, int heldSlot) {
		super(FrequencyModMenus.SYMBOL_SWAP.get(), id);
		this.player = playerInventory.player;
		this.heldItem = heldItem;
		this.heldSlot = heldSlot;
		this.availableSymbols = new ArrayList<>();

		loadAvailableSymbols();
	}

	private void loadAvailableSymbols() {
		for (var symbol : FrequencyModItems.ALL_SYMBOLS) {
			availableSymbols.add(new ItemStack(symbol.get()));
		}
	}

	public List<ItemStack> getAvailableSymbols() {
		return availableSymbols;
	}

	public void swapSymbol(int symbolIndex) {
		if (symbolIndex >= 0 && symbolIndex < availableSymbols.size()) {
			ItemStack newSymbol = availableSymbols.get(symbolIndex).copy();
			newSymbol.setCount(heldItem.getCount());
			player.getInventory().setItem(heldSlot, newSymbol);
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
