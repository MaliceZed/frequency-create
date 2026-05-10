package maze.frequency.forge.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;
import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.function.Supplier;

public class FrequencyModItemsForge {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, FrequencyMod.MODID);

    public static void register() {
        FrequencyModItems.register(new FrequencyModItems.ItemRegistrar() {
            @Override
            public <T extends Item> Supplier<T> register(String name, Supplier<T> supplier) {
                RegistryObject<T> reg = REGISTRY.register(name, supplier);
                return reg;
            }
        });
    }
}
