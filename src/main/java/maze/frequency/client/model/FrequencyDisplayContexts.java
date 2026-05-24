package maze.frequency.client.model;

import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class FrequencyDisplayContexts {
    public static final ItemDisplayContext REDSTONE_LINK = ItemDisplayContext.valueOf("FREQUENCY_REDSTONE_LINK");
    public static final ItemDisplayContext SURFACE = ItemDisplayContext.valueOf("FREQUENCY_SURFACE");

    private FrequencyDisplayContexts() {}
}
