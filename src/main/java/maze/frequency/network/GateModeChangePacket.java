package maze.frequency.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import maze.frequency.FrequencyMod;
import maze.frequency.block.GateMode;
import maze.frequency.block.LogicCombinatorBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

public record GateModeChangePacket(BlockPos pos, int mode) implements CustomPacketPayload {
    public static final Type<GateModeChangePacket> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "gate_mode_change"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GateModeChangePacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC,
        GateModeChangePacket::pos,
        net.minecraft.network.codec.ByteBufCodecs.VAR_INT,
        GateModeChangePacket::mode,
        GateModeChangePacket::new
    );

    @Override
    public Type<GateModeChangePacket> type() {
        return TYPE;
    }

    public static void send(BlockPos pos, int mode) {
        net.neoforged.neoforge.network.PacketDistributor.sendToServer(new GateModeChangePacket(pos, mode));
    }

    public static void handle(GateModeChangePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                LogicCombinatorBehaviour behaviour = BlockEntityBehaviour.get(
                    serverPlayer.level(), packet.pos(), LogicCombinatorBehaviour.TYPE);
                if (behaviour != null) {
                    behaviour.setGateMode(GateMode.byOrdinal(packet.mode()));
                    behaviour.blockEntity.notifyUpdate();
                }
            }
        });
    }
}
