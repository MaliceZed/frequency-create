package maze.frequency.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.Registries;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import maze.frequency.FrequencyMod;
import maze.frequency.block.SymbolFrameBlock;
import maze.frequency.block.SymbolFrameBlockEntity;

public class FrequencyModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(Registries.BLOCK, FrequencyMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FrequencyMod.MODID);
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(Registries.ITEM, FrequencyMod.MODID);

    public static final DeferredHolder<Block, SymbolFrameBlock> SYMBOL_FRAME =
        BLOCKS.register("symbol_frame", () -> new SymbolFrameBlock(
            Block.Properties.of()
                .strength(0.5f)
                .noOcclusion()
                .dynamicShape()
        ));

    public static final DeferredHolder<Item, BlockItem> SYMBOL_FRAME_ITEM =
        ITEMS.register("symbol_frame", () -> new BlockItem(
            SYMBOL_FRAME.get(), new Item.Properties()
        ));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SymbolFrameBlockEntity>> SYMBOL_FRAME_BE =
        BLOCK_ENTITIES.register("symbol_frame", () ->
            BlockEntityType.Builder.of(
                SymbolFrameBlockEntity::new,
                SYMBOL_FRAME.get()
            ).build(null)
        );
}
