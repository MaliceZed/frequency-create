package maze.frequency.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.core.BlockPos;

public class FrameClientHandler {
    @OnlyIn(Dist.CLIENT)
    public static void markSectionDirty(BlockPos pos) {
        int sx = pos.getX() >> 4, sy = pos.getY() >> 4, sz = pos.getZ() >> 4;
        net.minecraft.client.Minecraft.getInstance().levelRenderer.setSectionDirty(sx, sy, sz);
    }
}
