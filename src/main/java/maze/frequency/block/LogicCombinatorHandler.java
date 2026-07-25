package maze.frequency.block;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.RaycastHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import maze.frequency.world.inventory.LogicCombinatorMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import maze.frequency.FrequencyMod;

@EventBusSubscriber(modid = FrequencyMod.MODID)
public class LogicCombinatorHandler {

	@SubscribeEvent
	public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
		Level world = event.getLevel();
		BlockPos pos = event.getPos();
		Player player = event.getEntity();
		InteractionHand hand = event.getHand();

		if (player.isSpectator())
			return;

		LogicCombinatorBehaviour behaviour = BlockEntityBehaviour.get(world, pos, LogicCombinatorBehaviour.TYPE);
		if (behaviour == null)
			return;

		if (player.isShiftKeyDown()) {
			if (event.getSide() != LogicalSide.CLIENT) {
				player.openMenu(
					new net.minecraft.world.MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.translatable("gui.frequency.logic_combinator.title");
						}

						@Override
						public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player p) {
							return new LogicCombinatorMenu(
								maze.frequency.init.FrequencyModMenus.LOGIC_COMBINATOR.get(),
								windowId, playerInv, pos);
						}
					},
					pos
				);
			}
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.SUCCESS);
			return;
		}

		ItemStack heldItem = player.getItemInHand(hand);
		BlockHitResult ray = RaycastHelper.rayTraceRange(world, player, 10);
		if (ray == null)
			return;

		for (int i = 0; i < 6; i++) {
			if (behaviour.testHit(i, ray.getLocation())) {
				if (event.getSide() != LogicalSide.CLIENT)
					behaviour.setFrequency(i, heldItem);
				event.setCanceled(true);
				event.setCancellationResult(InteractionResult.SUCCESS);
				world.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .25f, .1f);
				return;
			}
		}
	}

}
