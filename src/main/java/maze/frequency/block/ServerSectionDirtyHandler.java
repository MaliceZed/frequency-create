package maze.frequency.block;

import net.minecraft.core.BlockPos;

/**
 * Server-side no-op implementation of ISectionDirtyHandler.
 * Server doesn't need to mark render sections dirty.
 */
public class ServerSectionDirtyHandler implements ISectionDirtyHandler {
    public static final ServerSectionDirtyHandler INSTANCE = new ServerSectionDirtyHandler();

    @Override
    public void markSectionDirty(BlockPos pos) {
        // No-op on server
    }
}
