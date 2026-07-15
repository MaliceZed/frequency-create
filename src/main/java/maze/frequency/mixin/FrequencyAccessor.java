package maze.frequency.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler$Frequency")
public interface FrequencyAccessor {

    @Accessor("stack")
    ItemStack frequency$getStack();

    @Accessor("color")
    int frequency$getColor();
}
