package maze.frequency.fabric.init;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModMenus;

import java.util.function.Supplier;

public class FrequencyModMenusFabric {
    public static void register() {
        FrequencyModMenus.register(new FrequencyModMenus.MenuRegistrar() {
            @Override
            public <T extends net.minecraft.world.inventory.AbstractContainerMenu> Supplier<MenuType<T>> register(String name, FrequencyModMenus.MenuFactory<T> factory) {
                MenuType<T> menuType = new ExtendedScreenHandlerType<>(factory::create);
                Registry.register(BuiltInRegistries.MENU, new ResourceLocation(FrequencyMod.MODID, name), menuType);
                return () -> menuType;
            }
        });
    }
}
