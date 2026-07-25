package maze.frequency.block;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import maze.frequency.client.renderer.LogicCombinatorSlotTransform;

public class LogicCombinatorBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<LogicCombinatorBehaviour> TYPE = new BehaviourType<>();

    private LinkBehaviour input1Link;
    private LinkBehaviour outputLink;
    private LinkBehaviour input2Link;

    private final LogicCombinatorSlotTransform[] slotTransforms = new LogicCombinatorSlotTransform[6];

    private int input1Power = 0;
    private int input2Power = 0;
    private int outputPower = 0;

    private GateMode gateMode = GateMode.AND;

    private static final String[] NBT_KEYS = {
        "FreqPair1First", "FreqPair1Second",
        "FreqPair2First", "FreqPair2Second",
        "FreqPair3First", "FreqPair3Second"
    };

    public static final String[] TOOLTIP_KEYS = {
        "frequency.tooltip.pair1_first",
        "frequency.tooltip.pair1_second",
        "frequency.tooltip.pair2_first",
        "frequency.tooltip.pair2_second",
        "frequency.tooltip.pair3_first",
        "frequency.tooltip.pair3_second"
    };

    public LogicCombinatorBehaviour(SmartBlockEntity be) {
        super(be);
        for (int i = 0; i < 6; i++)
            slotTransforms[i] = new LogicCombinatorSlotTransform(i % 2 == 0, i / 2);

        input1Link = LinkBehaviour.receiver(be,
            Pair.of(slotTransforms[0], slotTransforms[1]),
            power -> {
                input1Power = power;
                processLogic();
            });
        outputLink = LinkBehaviour.transmitter(be,
            Pair.of(slotTransforms[2], slotTransforms[3]),
            () -> outputPower);
        input2Link = LinkBehaviour.receiver(be,
            Pair.of(slotTransforms[4], slotTransforms[5]),
            power -> {
                input2Power = power;
                processLogic();
            });
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public void setFrequency(int slot, ItemStack stack) {
        if (slot < 2) {
            input1Link.setFrequency(slot == 0, stack);
        } else if (slot < 4) {
            outputLink.setFrequency(slot == 2, stack);
        } else {
            input2Link.setFrequency(slot == 4, stack);
        }
        blockEntity.notifyUpdate();
    }

    public ItemStack getFrequency(int slot) {
        LinkBehaviour link;
        boolean first;
        if (slot < 2) {
            link = input1Link;
            first = slot == 0;
        } else if (slot < 4) {
            link = outputLink;
            first = slot == 2;
        } else {
            link = input2Link;
            first = slot == 4;
        }
        var key = link.getNetworkKey();
        return first ? key.getFirst().getStack() : key.getSecond().getStack();
    }

    public boolean isSlotEmpty(int slot) {
        return getFrequency(slot).isEmpty();
    }

    public ValueBoxTransform getSlotTransform(int slot) {
        return slotTransforms[slot];
    }

    public boolean testHit(int slot, Vec3 hit) {
        if (getWorld() == null) return false;
        Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(blockEntity.getBlockPos()));
        return slotTransforms[slot].testHit(getWorld(), getPos(), blockEntity.getBlockState(), localHit);
    }

    public Component getSlotLabel(int slot) {
        return Component.translatable(TOOLTIP_KEYS[slot]);
    }

    public void onTick() {
        processLogic();
    }

    public void initialize() {
        input1Link.initialize();
        outputLink.initialize();
        input2Link.initialize();
    }

    public void unload() {
        input1Link.unload();
        outputLink.unload();
        input2Link.unload();
    }

    /**
     * AND-gate logic processing. Called directly from callbacks,
     * without relying on SmartBlockEntity tick().
     */
    private void processLogic() {
        if (blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide()) return;

        boolean input1 = input1Power > 0;
        boolean input2 = input2Power > 0;
        boolean output = gateMode.evaluate(input1, input2);
        int newPower = output ? 15 : 0;

        // Update blockstate
        BlockState state = blockEntity.getBlockState();
        boolean changed = false;
        if (state.hasProperty(LogicCombinatorBlock.INPUT1) && state.getValue(LogicCombinatorBlock.INPUT1) != input1) {
            state = state.setValue(LogicCombinatorBlock.INPUT1, input1);
            changed = true;
        }
        if (state.hasProperty(LogicCombinatorBlock.INPUT2) && state.getValue(LogicCombinatorBlock.INPUT2) != input2) {
            state = state.setValue(LogicCombinatorBlock.INPUT2, input2);
            changed = true;
        }
        if (state.hasProperty(LogicCombinatorBlock.OUTPUT) && state.getValue(LogicCombinatorBlock.OUTPUT) != output) {
            state = state.setValue(LogicCombinatorBlock.OUTPUT, output);
            changed = true;
        }
        if (changed) {
            blockEntity.getLevel().setBlock(blockEntity.getBlockPos(), state, 3);
        }

        // Self-loop prevention: block if output frequencies match input frequencies
        var outputKey = outputLink.getNetworkKey();
        var input1Key = input1Link.getNetworkKey();
        var input2Key = input2Link.getNetworkKey();
        boolean selfLoop = outputKey.equals(input1Key) || outputKey.equals(input2Key);
        
        if (selfLoop && newPower > 0) {
            newPower = 0;
            output = false;
        }

        // Update transmitter
        if (outputPower != newPower) {
            outputPower = newPower;
            outputLink.notifySignalChange();
        }
    }

    public int getInput1Power() { return input1Power; }
    public int getInput2Power() { return input2Power; }
    public int getOutputPower() { return outputPower; }
    public boolean isInput1Active() { return input1Power > 0; }
    public boolean isInput2Active() { return input2Power > 0; }

    public void setOutputPower(int power) {
        outputPower = power;
        outputLink.notifySignalChange();
    }

    public GateMode getGateMode() { return gateMode; }
    public void setGateMode(GateMode mode) {
        gateMode = mode;
        if (blockEntity.getLevel() != null && !blockEntity.getLevel().isClientSide()) {
            BlockState state = blockEntity.getBlockState();
            boolean shouldBeSingle = mode == GateMode.NOT;
            if (state.getValue(LogicCombinatorBlock.SINGLE) != shouldBeSingle) {
                blockEntity.getLevel().setBlock(blockEntity.getBlockPos(),
                    state.setValue(LogicCombinatorBlock.SINGLE, shouldBeSingle), 3);
            }
        }
    }

    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        CompoundTag links = new CompoundTag();
        CompoundTag i1 = new CompoundTag();
        input1Link.write(i1, registries, clientPacket);
        links.put("Input1", i1);
        CompoundTag out = new CompoundTag();
        outputLink.write(out, registries, clientPacket);
        links.put("Output", out);
        CompoundTag i2 = new CompoundTag();
        input2Link.write(i2, registries, clientPacket);
        links.put("Input2", i2);
        tag.put("Links", links);
        tag.putInt("GateMode", gateMode.ordinal());
    }

    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        if (tag.contains("GateMode")) gateMode = GateMode.byOrdinal(tag.getInt("GateMode"));
        if (tag.contains("Links")) {
            CompoundTag links = tag.getCompound("Links");
            if (links.contains("Input1"))
                input1Link.read(links.getCompound("Input1"), registries, clientPacket);
            if (links.contains("Output"))
                outputLink.read(links.getCompound("Output"), registries, clientPacket);
            if (links.contains("Input2"))
                input2Link.read(links.getCompound("Input2"), registries, clientPacket);
        }
    }
}
