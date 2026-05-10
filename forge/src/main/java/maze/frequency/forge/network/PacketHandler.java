package maze.frequency.forge.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import maze.frequency.FrequencyMod;
import maze.frequency.network.SymbolSwapPacket;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(FrequencyMod.MODID, "main"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	public static void register() {
		int id = 0;
		INSTANCE.messageBuilder(SymbolSwapPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
			.decoder(SymbolSwapPacket::new)
			.encoder(SymbolSwapPacket::write)
			.consumerMainThread((packet, ctx) -> {
				ServerPlayer player = ctx.get().getSender();
				if (player != null) {
					packet.handle(player);
				}
				ctx.get().setPacketHandled(true);
			})
			.add();
	}

	public static void sendToServer(SymbolSwapPacket packet) {
		INSTANCE.sendToServer(packet);
	}
}
