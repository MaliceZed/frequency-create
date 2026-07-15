package maze.frequency.client;

import maze.frequency.block.ISectionDirtyHandler;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

/**
 * Client-side implementation of ISectionDirtyHandler.
 * Delegates to FrameClientHandler.markSectionDirty.
 */
@OnlyIn(Dist.CLIENT)
public class ClientSectionDirtyHandler implements ISectionDirtyHandler {
    public static final ClientSectionDirtyHandler INSTANCE = new ClientSectionDirtyHandler();

    @Override
    public void markSectionDirty(BlockPos pos) {
        FrameClientHandler.markSectionDirty(pos);
    }
}
