package maze.frequency.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;

import maze.frequency.init.FrequencyModBlocks;

public class SymbolFrameBlockEntity extends BlockEntity implements ISymbolFrameData {
    private static final String TAG_SYMBOL = "symbol";
    private String symbolName = "symbol_empty";
    /** Client-only: stores ModelData; null on server to avoid class loading issues. */
    private Object modelData;

    /**
     * Client-only model data factory. Set during client initialization.
     * Null on server to avoid class loading issues.
     */
    @javax.annotation.Nullable
    private static java.util.function.Function<String, Object> modelDataFactory = null;

    /**
     * Client-only section dirty handler. Set during client initialization.
     * Null on server to avoid class loading issues.
     */
    @javax.annotation.Nullable
    private static java.util.function.Consumer<net.minecraft.core.BlockPos> sectionDirtyHandler = null;

    /**
     * Registers the client-side model data factory.
     * Must only be called from client initialization (e.g. SymbolFrameClientHelper.init()).
     */
    @net.neoforged.api.distmarker.OnlyIn(net.neoforged.api.distmarker.Dist.CLIENT)
    public static void setModelDataFactory(java.util.function.Function<String, Object> factory) {
        modelDataFactory = factory;
    }

    /**
     * Registers the client-side section dirty handler.
     * Must only be called from client initialization (e.g. SymbolFrameClientHelper.init()).
     */
    @net.neoforged.api.distmarker.OnlyIn(net.neoforged.api.distmarker.Dist.CLIENT)
    public static void setSectionDirtyHandler(java.util.function.Consumer<net.minecraft.core.BlockPos> handler) {
        sectionDirtyHandler = handler;
    }

    public SymbolFrameBlockEntity(BlockPos pos, BlockState state) {
        super(FrequencyModBlocks.SYMBOL_FRAME_BE.get(), pos, state);
        // modelData is lazily created on client via getModelData()
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String name) {
        setSymbolName(name, null);
    }

    public void setSymbolName(String name, Player excludePlayer) {
        this.symbolName = name;
        // Only update model data on the client side (server doesn't need it)
        if (level != null && level.isClientSide && modelDataFactory != null) {
            this.modelData = modelDataFactory.apply(symbolName);
        }
        setChanged();
        if (level != null && !level.isClientSide && level instanceof ServerLevel serverLevel) {
            BlockState state = getBlockState();
            serverLevel.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
            Packet<ClientGamePacketListener> packet = getUpdatePacket();
            if (packet != null) {
                serverLevel.getServer().getPlayerList()
                    .broadcast(excludePlayer, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 64.0, serverLevel.dimension(), packet);
            }
        }
    }

    // Note: getModelData() returns a client-only type (ModelData). 
    // It is only called from the client rendering pipeline, so it's safe.
    @Override
    public net.neoforged.neoforge.client.model.data.ModelData getModelData() {
        if (modelData == null && modelDataFactory != null) {
            modelData = modelDataFactory.apply(symbolName);
        }
        return (net.neoforged.neoforge.client.model.data.ModelData) modelData;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString(TAG_SYMBOL, symbolName);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(TAG_SYMBOL)) {
            symbolName = tag.getString(TAG_SYMBOL);
            // Only recreate model data on the client side
            if (level != null && level.isClientSide && modelDataFactory != null) {
                modelData = modelDataFactory.apply(symbolName);
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putString(TAG_SYMBOL, symbolName);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
        CompoundTag tag = packet.getTag();
        if (tag != null && tag.contains(TAG_SYMBOL)) {
            String oldName = symbolName;
            symbolName = tag.getString(TAG_SYMBOL);
            if (!symbolName.equals(oldName)) {
                // This callback only fires on the client, but guard to be safe
                if (level != null && level.isClientSide) {
                    if (modelDataFactory != null) {
                        modelData = modelDataFactory.apply(symbolName);
                    }
                    requestModelDataUpdate();
                    if (sectionDirtyHandler != null) {
                        sectionDirtyHandler.accept(worldPosition);
                    }
                }
            }
        }
    }
}
