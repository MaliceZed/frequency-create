package maze.frequency.world.inventory;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
		ResourceLocation symbolsTag = ResourceLocation.fromNamespaceAndPath("frequency", "symbols");
		BuiltInRegistries.ITEM.getTag(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM, symbolsTag))
			.ifPresent(tag -> {
				tag.forEach(itemHolder -> {
					availableSymbols.add(new ItemStack(itemHolder.value()));
				});
			});

		availableSymbols.sort((a, b) -> {
			String idA = BuiltInRegistries.ITEM.getKey(a.getItem()).getPath();
			String idB = BuiltInRegistries.ITEM.getKey(b.getItem()).getPath();

			int orderA = getSymbolOrder(idA);
			int orderB = getSymbolOrder(idB);

			return Integer.compare(orderA, orderB);
		});
	}

	private int getSymbolOrder(String itemId) {

		if (itemId.matches("symbol_[0-9]")) {
			return itemId.charAt(itemId.length() - 1) - '0';
		}

		if (itemId.matches("symbol_[a-z]")) {
			return 10 + (itemId.charAt(itemId.length() - 1) - 'a');
		}

		if (itemId.equals("symbol_empty")) {
			return 36;
		}

		if (itemId.equals("symbol_up_arrow")) return 37;
		if (itemId.equals("symbol_down_arrow")) return 38;
		if (itemId.equals("symbol_left_arrow")) return 39;
		if (itemId.equals("symbol_right_arrow")) return 40;

		if (itemId.equals("symbol_darrow_up")) return 41;
		if (itemId.equals("symbol_darrow_down")) return 42;
		if (itemId.equals("symbol_darrow_left")) return 43;
		if (itemId.equals("symbol_darrow_right")) return 44;

		return 999;
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
