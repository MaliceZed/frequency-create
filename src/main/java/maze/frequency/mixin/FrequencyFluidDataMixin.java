package maze.frequency.mixin;

import maze.frequency.data.component.FluidData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(targets = "com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler$Frequency")
public class FrequencyFluidDataMixin {

    @Shadow
    private ItemStack stack;

    @Shadow
    private int color;

    @Shadow
    private Item item;

    @Inject(method = "hashCode", at = @At("HEAD"), cancellable = true, remap = false)
    private void frequency$hashCode(CallbackInfoReturnable<Integer> cir) {
        FluidData data = stack.get(FluidData.TYPE);
        int fluidHash = data != null ? data.fluid().hashCode() : 0;
        cir.setReturnValue(item.hashCode() * 31 * 31 ^ color * 31 ^ fluidHash);
    }

    @Inject(method = "equals", at = @At("RETURN"), cancellable = true, remap = false)
    private void frequency$equals(Object obj, CallbackInfoReturnable<Boolean> cir) {
        // If original equals returned false, our check doesn't matter
        if (!cir.getReturnValueZ()) return;

        // If obj is the same class, check FluidData
        if (obj != null && obj.getClass() == this.getClass()) {
            FluidData thisFluid = stack.get(FluidData.TYPE);
            FluidData otherFluid = ((FrequencyAccessor) obj).frequency$getStack().get(FluidData.TYPE);
            if (!Objects.equals(thisFluid, otherFluid)) {
                cir.setReturnValue(false);
            }
        } else {
            cir.setReturnValue(false);
        }
    }
}
