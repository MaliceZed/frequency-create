package maze.frequency.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class LogicCombinatorSlotTransform extends ValueBoxTransform.Dual {

    private final int pair;

    private static final double[][] FIRST_POSITIONS = {
        {0.8125, 0.251, 0.65625},
        {0.5, 0.251, 0.65625},
        {0.1875, 0.251, 0.65625}
    };
    private static final double[][] SECOND_POSITIONS = {
        {0.8125, 0.251, 0.34375},
        {0.5, 0.251, 0.34375},
        {0.1875, 0.251, 0.34375}
    };

    public LogicCombinatorSlotTransform(boolean first, int pair) {
        super(first);
        this.pair = pair;
    }

    @Override
    public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
        double[] base = first ? FIRST_POSITIONS[pair] : SECOND_POSITIONS[pair];
        Vec3 vec = new Vec3(base[0], base[1], base[2]);
        return rotateHorizontally(state, vec);
    }

    @Override
    public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
        if (!state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) return;
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        float yRot = AngleHelper.horizontalAngle(facing);
        ms.mulPose(Axis.YP.rotationDegrees(yRot));
        ms.mulPose(Axis.XP.rotationDegrees(90));
    }

    @Override
    public float getScale() {
        return 0.4975f;
    }
}
