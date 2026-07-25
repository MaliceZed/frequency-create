package maze.frequency.init;

import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import maze.frequency.FrequencyMod;
import maze.frequency.block.SymbolFrameBlockEntity;
import maze.frequency.block.LogicCombinatorBlockEntity;

public class FrequencyModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, FrequencyMod.MODID);

    // ═══ Symbol Frame ═══

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SymbolFrameBlockEntity>> SYMBOL_FRAME =
        BLOCK_ENTITY_TYPES.register("symbol_frame", () ->
            BlockEntityType.Builder.of(
                SymbolFrameBlockEntity::new,
                FrequencyModBlocks.SYMBOL_FRAME.get()
            ).build(null)
        );

    // ═══ Logic Combinator ═══

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LogicCombinatorBlockEntity>> LOGIC_COMBINATOR =
        BLOCK_ENTITY_TYPES.register("logic_combinator", () ->
            BlockEntityType.Builder.of(
                LogicCombinatorBlockEntity::new,
                FrequencyModBlocks.LOGIC_COMBINATOR.get()
            ).build(null)
        );

    public static void register() {
        // Empty method — called from FrequencyMod for classloading
    }
}
