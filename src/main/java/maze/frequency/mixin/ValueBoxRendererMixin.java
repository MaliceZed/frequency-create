package maze.frequency.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import maze.frequency.client.model.FrequencyDisplayContexts;
import maze.frequency.item.ISymbolItem;

@Mixin(targets = "com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer", remap = false)
@OnlyIn(Dist.CLIENT)
public abstract class ValueBoxRendererMixin {

    @Unique
    private static boolean frequency$isSymbolItem = false;

    @Inject(method = "renderItemIntoValueBox", at = @At("HEAD"))
    private static void frequency$checkItem(ItemStack filter, PoseStack ms, MultiBufferSource buffer, int light, int overlay, CallbackInfo ci) {
        frequency$isSymbolItem = filter.getItem() instanceof ISymbolItem;
    }

    @ModifyArg(
        method = "renderItemIntoValueBox",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"
        ),
        index = 1
    )
    private static ItemDisplayContext frequency$modifyContext(ItemDisplayContext context) {
        return frequency$isSymbolItem ? FrequencyDisplayContexts.REDSTONE_LINK : context;
    }
}
