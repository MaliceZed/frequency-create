package maze.frequency.platform;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import java.util.function.Supplier;

public interface IPlatformHelper {
    <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);

    void openSymbolSwapMenu(ServerPlayer player, ItemStack itemStack, int slot);
}
