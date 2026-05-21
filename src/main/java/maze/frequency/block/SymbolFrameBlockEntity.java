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

import net.neoforged.neoforge.client.model.data.ModelData;

import maze.frequency.client.model.SymbolFrameModelData;
import maze.frequency.init.FrequencyModBlocks;
import maze.frequency.client.FrameClientHandler;

import net.minecraft.world.entity.player.Player;

public class SymbolFrameBlockEntity extends BlockEntity {
    private static final String TAG_SYMBOL = "symbol";
    private String symbolName = "symbol_empty";
    private ModelData modelData;

    public SymbolFrameBlockEntity(BlockPos pos, BlockState state) {
        super(FrequencyModBlocks.SYMBOL_FRAME_BE.get(), pos, state);
        this.modelData = ModelData.builder().with(SymbolFrameModelData.SYMBOL_PROPERTY, symbolName).build();
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String name) {
        setSymbolName(name, null);
    }

    public void setSymbolName(String name, Player excludePlayer) {
        this.symbolName = name;
        this.modelData = ModelData.builder().with(SymbolFrameModelData.SYMBOL_PROPERTY, symbolName).build();
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

    @Override
    public ModelData getModelData() {
        return modelData;
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
            modelData = ModelData.builder().with(SymbolFrameModelData.SYMBOL_PROPERTY, symbolName).build();
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
                modelData = ModelData.builder().with(SymbolFrameModelData.SYMBOL_PROPERTY, symbolName).build();
                requestModelDataUpdate();
                if (level != null && level.isClientSide) {
                    FrameClientHandler.markSectionDirty(worldPosition);
                }
            }
        }
    }
}
