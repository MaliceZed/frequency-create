package maze.frequency.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

@OnlyIn(Dist.CLIENT)
public class FrameClientHandler {
    public static void markSectionDirty(BlockPos pos) {
        int sx = pos.getX() >> 4, sy = pos.getY() >> 4, sz = pos.getZ() >> 4;
        Minecraft.getInstance().levelRenderer.setSectionDirty(sx, sy, sz);
    }
}
