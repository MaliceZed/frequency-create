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
import maze.frequency.item.BaseSymbolItem;

@Mixin(targets = "com.simibubi.create.content.kinetics.belt.BeltRenderer")
@OnlyIn(Dist.CLIENT)
public abstract class BeltRendererMixin {

    @Unique
    private static Boolean frequency$isSymbolItem = false;

    @ModifyArg(
        method = "renderItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"
        ),
        index = 0
    )
    private static ItemStack frequency$checkStack(ItemStack stack) {
        frequency$isSymbolItem = stack.getItem() instanceof BaseSymbolItem;
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
        return frequency$isSymbolItem ? FrequencyDisplayContexts.SURFACE : context;
    }
}
