package maze.frequency.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import maze.frequency.block.LogicCombinatorBehaviour;
import maze.frequency.block.LogicCombinatorBlockEntity;

public class LogicCombinatorRenderer implements BlockEntityRenderer<LogicCombinatorBlockEntity> {

    public LogicCombinatorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(LogicCombinatorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getLevel() == null) return;
        LogicCombinatorBehaviour behaviour = blockEntity.getLogicBehaviour();
        if (behaviour == null) return;

        for (int i = 0; i < 6; i++) {
            ItemStack stack = behaviour.getFrequency(i);
            if (stack.isEmpty()) continue;

            ValueBoxTransform transform = behaviour.getSlotTransform(i);
            poseStack.pushPose();
            transform.transform(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), poseStack);
            ValueBoxRenderer.renderItemIntoValueBox(stack, poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
        }
    }
}
