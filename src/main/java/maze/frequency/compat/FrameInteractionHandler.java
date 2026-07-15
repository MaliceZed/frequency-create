package maze.frequency.compat;

import net.minecraft.core.BlockPos;
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

import maze.frequency.block.ISectionDirtyHandler;
import maze.frequency.block.ServerSectionDirtyHandler;
import maze.frequency.block.SymbolFrameBlockEntity;
import maze.frequency.init.FrequencyModBlocks;
import maze.frequency.init.FrequencyModItems;
import maze.frequency.item.BaseSymbolItem;
import maze.frequency.network.FrameUpdatePacket;

public class FrameInteractionHandler {

    /**
     * Section dirty handler. Defaults to server no-op; replaced with client
     * implementation during client setup (see ClientSectionDirtyHandler).
     */
    private static ISectionDirtyHandler dirtyHandler = ServerSectionDirtyHandler.INSTANCE;

    /**
     * Sets the section dirty handler (used on the client side).
     * Called during client initialization.
     */
    public static void setDirtyHandler(ISectionDirtyHandler handler) {
        dirtyHandler = handler;
    }

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

        Player player = event.getEntity();

        if (isWrench(stack)) {
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
                // Use the abstract handler instead of reflection
                dirtyHandler.markSectionDirty(pos);
                PacketDistributor.sendToServer(new FrameUpdatePacket(pos, symbolName));
            }
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    private static String getSymbolFromStack(ItemStack stack) {
        if (stack.isEmpty()) return null;
        if (!(stack.getItem() instanceof BaseSymbolItem symbolItem)) return null;
        String originalName = symbolItem.getSymbolName();
        String name;

        if (originalName.startsWith("brass_")) {
            name = originalName.substring(6); // "brass_symbol_a" -> "symbol_a"
            if (FrequencyModItems.getSymbol(originalName) == null) return null;
        } else if (originalName.startsWith("andesite_")) {
            name = originalName.substring(9); // "andesite_symbol_a" -> "symbol_a"
            if (FrequencyModItems.getAndesiteSymbol(originalName) == null) return null;
        } else if (originalName.startsWith("copper_")) {
            name = originalName.substring(7); // "copper_symbol_a" -> "symbol_a"
            if (FrequencyModItems.getCopperSymbol(originalName) == null) return null;
        } else {
            return null;
        }
        return name;
    }

    private static boolean isWrench(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItemHolder().unwrapKey()
                .map(key -> key.location().equals(WRENCH_ID))
                .orElse(false);
    }
}
