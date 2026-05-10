package maze.frequency.init;

import net.minecraft.world.inventory.MenuType;
import maze.frequency.world.inventory.SymbolSwapMenu;
import java.util.function.Supplier;

public class FrequencyModMenus {
	public static Supplier<MenuType<SymbolSwapMenu>> SYMBOL_SWAP;

	public static void register(MenuRegistrar registrar) {
		SYMBOL_SWAP = registrar.register("symbol_swap", SymbolSwapMenu::new);
	}

	public interface MenuRegistrar {
		<T extends net.minecraft.world.inventory.AbstractContainerMenu> Supplier<MenuType<T>> register(
			String name,
			MenuFactory<T> factory
		);
	}

	@FunctionalInterface
	public interface MenuFactory<T extends net.minecraft.world.inventory.AbstractContainerMenu> {
		T create(int id, net.minecraft.world.entity.player.Inventory inventory, net.minecraft.network.FriendlyByteBuf buf);
	}
}
