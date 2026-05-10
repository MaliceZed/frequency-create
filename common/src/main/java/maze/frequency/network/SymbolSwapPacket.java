package maze.frequency.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import maze.frequency.world.inventory.SymbolSwapMenu;

public class SymbolSwapPacket {
	private final int symbolIndex;

	public SymbolSwapPacket(int symbolIndex) {
		this.symbolIndex = symbolIndex;
	}

	public SymbolSwapPacket(FriendlyByteBuf buf) {
		this.symbolIndex = buf.readInt();
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(symbolIndex);
	}

	public FriendlyByteBuf toBuffer() {
		FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
		write(buf);
		return buf;
	}

	public int getSymbolIndex() {
		return symbolIndex;
	}

	public void handle(ServerPlayer player) {
		if (player.containerMenu instanceof SymbolSwapMenu menu) {
			menu.swapSymbol(symbolIndex);
		}
	}
}
