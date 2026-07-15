package maze.frequency.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import maze.frequency.client.SymbolRenderContext;

@Mixin(targets = "com.simibubi.create.content.logistics.tableCloth.TableClothRenderer", remap = false)
@OnlyIn(Dist.CLIENT)
public abstract class TableClothRendererMixin {

    @Inject(method = "renderSafe", at = @At("HEAD"))
    private void frequency$setTableClothContext(CallbackInfo ci) {
        SymbolRenderContext.TABLE_CLOTH.set(true);
    }

    @Inject(method = "renderSafe", at = @At("RETURN"))
    private void frequency$clearTableClothContext(CallbackInfo ci) {
        SymbolRenderContext.TABLE_CLOTH.set(false);
    }
}
