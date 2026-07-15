package maze.frequency.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import maze.frequency.client.model.FrequencyDisplayContexts;
import maze.frequency.item.ISymbolItem;
import maze.frequency.client.SymbolRenderContext;

@Mixin(targets = "com.simibubi.create.content.logistics.depot.DepotRenderer", remap = false)
@OnlyIn(Dist.CLIENT)
public abstract class DepotRendererMixin {

    @Unique
    private static final ThreadLocal<Boolean> frequency$isSymbolItem = ThreadLocal.withInitial(() -> false);

    @ModifyArg(
        method = "renderItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"
        ),
        index = 0
    )
    private static ItemStack frequency$checkStack(ItemStack stack) {
        frequency$isSymbolItem.set(stack.getItem() instanceof ISymbolItem);
        return stack;
    }

    @ModifyArg(
        method = "renderItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"
        ),
        index = 1
    )
    private static ItemDisplayContext frequency$modifyContext(ItemDisplayContext context) {
        if (frequency$isSymbolItem.get()) {
            return SymbolRenderContext.TABLE_CLOTH.get() ? FrequencyDisplayContexts.TABLE_CLOTH : FrequencyDisplayContexts.SURFACE;
        }
        return context;
    }
}
