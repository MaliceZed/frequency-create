package maze.frequency.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import maze.frequency.FrequencyMod;
import maze.frequency.world.inventory.SymbolSwapMenu;

public record SymbolSwapPacket(int symbolIndex) implements CustomPacketPayload {
	public static final Type<SymbolSwapPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "symbol_swap"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SymbolSwapPacket> STREAM_CODEC = StreamCodec.composite(
		net.minecraft.network.codec.ByteBufCodecs.VAR_INT,
		SymbolSwapPacket::symbolIndex,
		SymbolSwapPacket::new
	);

	@Override
	public Type<SymbolSwapPacket> type() {
		return TYPE;
	}

	public static void send(int symbolIndex) {
		net.neoforged.neoforge.network.PacketDistributor.sendToServer(new SymbolSwapPacket(symbolIndex));
	}

	public static void handle(SymbolSwapPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer serverPlayer) {
				if (serverPlayer.containerMenu instanceof SymbolSwapMenu menu) {
					menu.swapSymbol(packet.symbolIndex());
				}
			}
		});
	}
}
