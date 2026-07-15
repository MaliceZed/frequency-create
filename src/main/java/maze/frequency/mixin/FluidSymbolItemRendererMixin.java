package maze.frequency.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import maze.frequency.data.component.FluidData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class FluidSymbolItemRendererMixin {

    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.BEFORE))
    private void frequency$onBeforePopPose(ItemStack stack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        // Check if this is our liquid symbol item
        FluidData fluidData = stack.get(FluidData.TYPE);
        if (fluidData == null) return;

        Fluid fluid = BuiltInRegistries.FLUID.get(fluidData.fluid());
        if (fluid == null) return;

        var ext = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation stillTex = ext.getStillTexture();
        if (stillTex == null) return;

        int tintColor = ext.getTintColor();
        int r = (tintColor >> 16) & 0xFF;
        int g = (tintColor >> 8) & 0xFF;
        int b = tintColor & 0xFF;

        var atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        var fluidSprite = atlas.getSprite(stillTex);
        if (fluidSprite == null) return;

        // Render fluid overlay quad at the liquid element position
        // Element from model: from (5, 4, 7.55) to (11, 12, 8.25), south face at z=8.25
        poseStack.pushPose();

        RenderType renderType = RenderType.cutout();
        VertexConsumer consumer = buffer.getBuffer(renderType);

        float x1 = 5.0f / 16.0f;
        float y1 = 4.0f / 16.0f;
        float x2 = 11.0f / 16.0f;
        float y2 = 12.0f / 16.0f;
        float z = 8.25f / 16.0f;

        // Map UV: element UV [5,4,11,12] in 16x16 texture space to fluid sprite on atlas
        float u0 = fluidSprite.getU0();
        float u1 = fluidSprite.getU1();
        float v0 = fluidSprite.getV0();
        float v1 = fluidSprite.getV1();

        float relU0 = 5.0f / 16.0f;
        float relU1 = 11.0f / 16.0f;
        float relV0 = 4.0f / 16.0f;
        float relV1 = 12.0f / 16.0f;

        float fu0 = u0 + (u1 - u0) * relU0;
        float fu1 = u0 + (u1 - u0) * relU1;
        float fv0 = v0 + (v1 - v0) * relV0;
        float fv1 = v0 + (v1 - v0) * relV1;

        Matrix4f pose = poseStack.last().pose();
        Vector3f normal = new Vector3f(0.0f, 0.0f, 1.0f);

        // South face overlay quad
        consumer.addVertex(pose, x1, y1, z)
            .setColor(r, g, b, 255)
            .setUv(fu0, fv0)
            .setUv2(combinedLight >> 16 & 0xFFFF, combinedLight & 0xFFFF)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setOverlay(combinedOverlay);

        consumer.addVertex(pose, x2, y1, z)
            .setColor(r, g, b, 255)
            .setUv(fu1, fv0)
            .setUv2(combinedLight >> 16 & 0xFFFF, combinedLight & 0xFFFF)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setOverlay(combinedOverlay);

        consumer.addVertex(pose, x2, y2, z)
            .setColor(r, g, b, 255)
            .setUv(fu1, fv1)
            .setUv2(combinedLight >> 16 & 0xFFFF, combinedLight & 0xFFFF)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setOverlay(combinedOverlay);

        consumer.addVertex(pose, x1, y2, z)
            .setColor(r, g, b, 255)
            .setUv(fu0, fv1)
            .setUv2(combinedLight >> 16 & 0xFFFF, combinedLight & 0xFFFF)
            .setNormal(normal.x(), normal.y(), normal.z())
            .setOverlay(combinedOverlay);

        poseStack.popPose();
    }
}
