package maze.frequency.block;

import net.minecraft.core.BlockPos;

/**
 * Abstraction for marking render sections dirty.
 * Implementations exist for both client and server sides.
 */
public interface ISectionDirtyHandler {
    void markSectionDirty(BlockPos pos);
}
