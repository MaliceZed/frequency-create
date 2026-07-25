package maze.frequency.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuType;

import maze.frequency.world.inventory.SymbolSwapMenu;

import java.util.List;
import java.util.function.Supplier;

import maze.frequency.item.ISymbolItem;

public class BaseSymbolItem extends Item implements ISymbolItem {
	private final String symbolName;
	private final Supplier<MenuType<?>> menuType;
	private final Supplier<List<ItemStack>> symbolLoader;

	public BaseSymbolItem(Item.Properties properties, String symbolName, Supplier<MenuType<?>> menuType, Supplier<List<ItemStack>> symbolLoader) {
		super(properties);
		this.symbolName = symbolName;
		this.menuType = menuType;
		this.symbolLoader = symbolLoader;
	}

	public String getSymbolName() {
		return symbolName;
	}

	@Override
	public String getDescriptionId() {
		if (symbolName != null && symbolName.endsWith("empty")) {
			return "item.frequency.symbol_empty";
		}
		return super.getDescriptionId();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);

		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;

			serverPlayer.openMenu(new SimpleMenuProvider(
				(id, playerInventory, p) -> new SymbolSwapMenu(menuType.get(), id, playerInventory, itemStack, slot, symbolLoader),
				Component.translatable("gui.frequency.symbol_swap.title")
			), buf -> {
				ItemStack.STREAM_CODEC.encode(buf, itemStack);
				buf.writeInt(slot);
			});
		}

		return InteractionResultHolder.success(itemStack);
	}
}
