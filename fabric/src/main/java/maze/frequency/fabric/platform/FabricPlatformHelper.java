package maze.frequency.fabric.platform;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import maze.frequency.platform.IPlatformHelper;
import maze.frequency.world.inventory.SymbolSwapMenu;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        throw new UnsupportedOperationException("Use platform-specific registration");
    }

    @Override
    public void openSymbolSwapMenu(ServerPlayer player, ItemStack itemStack, int slot) {
        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer serverPlayer, FriendlyByteBuf buf) {
                buf.writeInt(slot);
            }

            @Override
            public Component getDisplayName() {
                return Component.translatable("gui.frequency.symbol_swap.title");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player p) {
                return new SymbolSwapMenu(id, playerInventory, itemStack, slot);
            }
        });
    }
}
