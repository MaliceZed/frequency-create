package maze.frequency.fabric.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModTabs;

import java.util.function.Supplier;

public class FrequencyModTabsFabric {
    public static void register() {
        FrequencyModTabs.register((name, builderFunc) -> {
            CreativeModeTab tab = builderFunc.apply(FabricItemGroup.builder()).build();
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(FrequencyMod.MODID, name), tab);
            return () -> tab;
        });
    }
}
