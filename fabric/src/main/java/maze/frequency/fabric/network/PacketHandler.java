package maze.frequency.fabric.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import io.netty.buffer.Unpooled;
import maze.frequency.FrequencyMod;
import maze.frequency.network.SymbolSwapPacket;

public class PacketHandler {
    public static final ResourceLocation SYMBOL_SWAP_PACKET = new ResourceLocation(FrequencyMod.MODID, "symbol_swap");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(SYMBOL_SWAP_PACKET, (server, player, handler, buf, responseSender) -> {
            SymbolSwapPacket packet = new SymbolSwapPacket(buf);
            server.execute(() -> packet.handle(player));
        });
    }

    public static void sendToServer(SymbolSwapPacket packet) {
        FriendlyByteBuf buf = packet.toBuffer();
        ClientPlayNetworking.send(SYMBOL_SWAP_PACKET, buf);
    }
}
