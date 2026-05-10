package maze.frequency.world.inventory;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
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

	public SymbolSwapMenu(int id, Inventory playerInventory, FriendlyByteBuf extraData) {
		super(FrequencyModMenus.SYMBOL_SWAP.get(), id);

		int slot = (extraData != null && extraData.readableBytes() >= 4) ? extraData.readInt() : playerInventory.selected;
		this.player = playerInventory.player;
		this.heldSlot = slot;
		this.heldItem = playerInventory.getItem(slot);
		this.availableSymbols = new ArrayList<>();
		loadAvailableSymbols();
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

		String[] symbolIds = {
			"symbol_0", "symbol_1", "symbol_2", "symbol_3", "symbol_4",
			"symbol_5", "symbol_6", "symbol_7", "symbol_8", "symbol_9",
			"symbol_a", "symbol_b", "symbol_c", "symbol_d", "symbol_e",
			"symbol_f", "symbol_g", "symbol_h", "symbol_i", "symbol_j",
			"symbol_k", "symbol_l", "symbol_m", "symbol_n", "symbol_o",
			"symbol_p", "symbol_q", "symbol_r", "symbol_s", "symbol_t",
			"symbol_u", "symbol_v", "symbol_w", "symbol_x", "symbol_y",
			"symbol_z", "symbol_empty", "symbol_up_arrow", "symbol_down_arrow",
			"symbol_left_arrow", "symbol_right_arrow", "symbol_darrow_up",
			"symbol_darrow_down", "symbol_darrow_left", "symbol_darrow_right"
		};

		for (String symbolId : symbolIds) {
			ResourceLocation id = new ResourceLocation("frequency", symbolId);
			if (BuiltInRegistries.ITEM.containsKey(id)) {
				net.minecraft.world.item.Item item = BuiltInRegistries.ITEM.get(id);
				availableSymbols.add(new ItemStack(item));
			}
		}
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
