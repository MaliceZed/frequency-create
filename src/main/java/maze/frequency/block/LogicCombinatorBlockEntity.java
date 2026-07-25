package maze.frequency.block;

import java.util.List;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import maze.frequency.init.FrequencyModBlockEntities;

public class LogicCombinatorBlockEntity extends SmartBlockEntity {

    private LogicCombinatorBehaviour behaviour;

    public LogicCombinatorBlockEntity(BlockPos pos, BlockState state) {
        super(FrequencyModBlockEntities.LOGIC_COMBINATOR.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviour = new LogicCombinatorBehaviour(this);
        behaviours.add(behaviour);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide()) return;
        if (behaviour == null) return;
        behaviour.onTick();
    }

    public LogicCombinatorBehaviour getBehaviour() {
        return behaviour;
    }

    // Backward compatibility — client handler calls getLogicBehaviour()
    public LogicCombinatorBehaviour getLogicBehaviour() {
        return behaviour;
    }
}
