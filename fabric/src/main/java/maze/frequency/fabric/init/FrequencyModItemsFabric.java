package maze.frequency.fabric.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.function.Supplier;

public class FrequencyModItemsFabric {
    public static void register() {
        FrequencyModItems.register(new FrequencyModItems.ItemRegistrar() {
            @Override
            public <T extends Item> Supplier<T> register(String name, Supplier<T> supplier) {
                T item = supplier.get();
                Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(FrequencyMod.MODID, name), item);
                return () -> item;
            }
        });
    }
}
