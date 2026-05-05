package maze.frequency.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;

public class BaseSymbolItem extends Item {
	public BaseSymbolItem() {
		super(new Item.Properties().stacksTo(8).rarity(Rarity.UNCOMMON));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);

		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;

			serverPlayer.openMenu(new net.minecraft.world.SimpleMenuProvider(
				(id, playerInventory, p) -> new maze.frequency.world.inventory.SymbolSwapMenu(id, playerInventory, itemStack, slot),
				net.minecraft.network.chat.Component.translatable("gui.frequency.symbol_swap.title")
			), buf -> {
				ItemStack.STREAM_CODEC.encode(buf, itemStack);
				buf.writeInt(slot);
			});
		}

		return InteractionResultHolder.success(itemStack);
	}
}
