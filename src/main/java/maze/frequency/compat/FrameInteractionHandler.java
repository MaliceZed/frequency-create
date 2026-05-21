package maze.frequency.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import maze.frequency.block.SymbolFrameBlockEntity;
import maze.frequency.init.FrequencyModBlocks;
import maze.frequency.init.FrequencyModItems;
import maze.frequency.network.FrameUpdatePacket;

public class FrameInteractionHandler {

    public static void register() {
        NeoForge.EVENT_BUS.addListener(FrameInteractionHandler::onRightClickBlock);
    }

    private static final ResourceLocation WRENCH_ID =
        ResourceLocation.fromNamespaceAndPath("create", "wrench");

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (!state.is(FrequencyModBlocks.SYMBOL_FRAME.get())) return;

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        Player player = event.getEntity();

        if (itemId.equals(WRENCH_ID)) {
            if (player.isShiftKeyDown()) {
                if (!level.isClientSide) {
                    ItemStack frameStack = new ItemStack(FrequencyModBlocks.SYMBOL_FRAME_ITEM.get());
                    if (!player.addItem(frameStack)) {
                        player.drop(frameStack, false);
                    }
                    level.destroyBlock(pos, false);
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
            return;
        }

        String symbolName = getSymbolFromStack(stack);
        if (symbolName != null) {
            if (level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof SymbolFrameBlockEntity frameBE) {
                    frameBE.setSymbolName(symbolName);
                    frameBE.requestModelDataUpdate();
                }
                maze.frequency.client.FrameClientHandler.markSectionDirty(pos);
                PacketDistributor.sendToServer(new FrameUpdatePacket(pos, symbolName));
            }
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    private static String getSymbolFromStack(ItemStack stack) {
        if (stack.isEmpty()) return null;
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!id.getNamespace().equals("frequency")) return null;
        String path = id.getPath();
        if (!path.startsWith("symbol_") || path.equals("incomplete_symbol")) return null;
        if (FrequencyModItems.getSymbol(path) == null) return null;
        return path;
    }
}
