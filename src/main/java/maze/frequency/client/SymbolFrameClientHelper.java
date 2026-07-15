package maze.frequency.client;

import maze.frequency.block.ISymbolFrameData;
import maze.frequency.block.SymbolFrameBlockEntity;
import maze.frequency.client.model.SymbolFrameModelData;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Client-only helper for SymbolFrameBlockEntity.
 * All methods in this class must only be called from the client side.
 */
public class SymbolFrameClientHelper {

    /**
     * Initializes client-side providers in SymbolFrameBlockEntity.
     * Must be called during client setup (e.g. from FMLClientSetupEvent).
     */
    public static void init() {
        SymbolFrameBlockEntity.setModelDataFactory(SymbolFrameClientHelper::createModelData);
        SymbolFrameBlockEntity.setSectionDirtyHandler(pos -> FrameClientHandler.markSectionDirty(pos));
    }

    /**
     * Creates ModelData for the given symbol name.
     * @param symbolName the symbol identifier
     * @return built ModelData
     */
    public static ModelData createModelData(String symbolName) {
        return ModelData.builder()
            .with(SymbolFrameModelData.SYMBOL_PROPERTY, symbolName != null ? symbolName : "symbol_empty")
            .build();
    }

    /**
     * Creates ModelData for the given ISymbolFrameData.
     * @param data symbol frame data
     * @return built ModelData
     */
    public static ModelData createModelData(ISymbolFrameData data) {
        return createModelData(data.getSymbolName());
    }

    /**
     * Marks the render section dirty for the given position.
     * @param level the level
     * @param pos the block position
     */
    public static void markSectionDirty(Level level, BlockPos pos) {
        if (level != null && level.isClientSide) {
            FrameClientHandler.markSectionDirty(pos);
        }
    }
}
