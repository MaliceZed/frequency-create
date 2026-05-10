package maze.frequency.forge.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraft.world.inventory.MenuType;
import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModMenus;

import java.util.function.Supplier;

public class FrequencyModMenusForge {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, FrequencyMod.MODID);

    public static void register() {
        FrequencyModMenus.register(new FrequencyModMenus.MenuRegistrar() {
            @Override
            public <T extends net.minecraft.world.inventory.AbstractContainerMenu> Supplier<MenuType<T>> register(String name, FrequencyModMenus.MenuFactory<T> factory) {
                RegistryObject<MenuType<T>> reg = REGISTRY.register(name, () -> IForgeMenuType.create(factory::create));
                return () -> reg.get();
            }
        });
    }
}
