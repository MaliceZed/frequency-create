package maze.frequency.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import maze.frequency.FrequencyMod;
import maze.frequency.block.SymbolFrameBlockEntity;
import maze.frequency.init.FrequencyModItems;

public record FrameUpdatePacket(BlockPos pos, String symbolName) implements CustomPacketPayload {
    public static final Type<FrameUpdatePacket> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "frame_update"));

    public static final StreamCodec<FriendlyByteBuf, FrameUpdatePacket> STREAM_CODEC = StreamCodec.of(
        (buf, packet) -> {
            buf.writeBlockPos(packet.pos);
            buf.writeUtf(packet.symbolName);
        },
        buf -> new FrameUpdatePacket(buf.readBlockPos(), buf.readUtf())
    );

    @Override
    public Type<FrameUpdatePacket> type() {
        return TYPE;
    }

    public static void handle(FrameUpdatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                String name = packet.symbolName();
                if (!name.startsWith("symbol_") || FrequencyModItems.getSymbol(name) == null) return;

                Level level = serverPlayer.serverLevel();
                BlockEntity be = level.getBlockEntity(packet.pos());
                if (be instanceof SymbolFrameBlockEntity frameBE) {
                    frameBE.setSymbolName(name, serverPlayer);
                }
            }
        });
    }
}
