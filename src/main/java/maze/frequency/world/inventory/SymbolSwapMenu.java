package maze.frequency.world.inventory;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModMenus;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;

public class SymbolSwapMenu extends AbstractContainerMenu {
	private final Player player;
	private final ItemStack heldItem;
	private final int heldSlot;
	private final List<ItemStack> availableSymbols = new ArrayList<>();

	// Основной конструктор (для открытия меню)
	public SymbolSwapMenu(MenuType<?> menuType, int id, Inventory playerInventory, ItemStack stack, int heldSlot, Supplier<List<ItemStack>> symbolLoader) {
		super(menuType, id);
		this.player = playerInventory.player;
		this.heldItem = stack;
		this.heldSlot = heldSlot;
		this.availableSymbols.addAll(symbolLoader.get());
	}

	// Сетевой конструктор (вызывается MenuType фабрикой)
	public SymbolSwapMenu(MenuType<?> menuType, int id, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		this(menuType, id, playerInventory,
			ItemStack.STREAM_CODEC.decode(buf),
			buf.readInt(),
			menuType == FrequencyModMenus.BRASS_SYMBOL_SWAP.get()
				? FrequencyModItems::getAllBrassSymbolStacks
				: menuType == FrequencyModMenus.ANDESITE_SYMBOL_SWAP.get()
					? FrequencyModItems::getAllAndesiteSymbolStacks
					: FrequencyModItems::getAllCopperSymbolStacks);
	}

	public List<ItemStack> getAvailableSymbols() {
		return availableSymbols;
	}

	public ItemStack getHeldItem() {
		return heldItem;
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
