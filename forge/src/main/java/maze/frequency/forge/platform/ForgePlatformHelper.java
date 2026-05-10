package maze.frequency.forge.platform;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import maze.frequency.platform.IPlatformHelper;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        throw new UnsupportedOperationException("Use platform-specific registration");
    }

    @Override
    public void openSymbolSwapMenu(ServerPlayer player, ItemStack itemStack, int slot) {
        player.openMenu(new net.minecraft.world.MenuProvider() {
            @Override
            public net.minecraft.network.chat.Component getDisplayName() {
                return net.minecraft.network.chat.Component.translatable("gui.frequency.symbol_swap.title");
            }

            @Override
            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.entity.player.Player p) {
                return new maze.frequency.world.inventory.SymbolSwapMenu(id, playerInventory, itemStack, slot);
            }
        });
    }
}
