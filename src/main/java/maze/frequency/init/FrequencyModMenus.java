package maze.frequency.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import maze.frequency.FrequencyMod;
import maze.frequency.world.inventory.SymbolSwapMenu;

public class FrequencyModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, FrequencyMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<SymbolSwapMenu>> SYMBOL_SWAP = REGISTRY.register("symbol_swap", () -> IMenuTypeExtension.create(SymbolSwapMenu::new));
}
