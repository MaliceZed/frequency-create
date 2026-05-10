package maze.frequency.forge.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;
import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModTabs;

public class FrequencyModTabsForge {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FrequencyMod.MODID);

    public static void register() {
        FrequencyModTabs.register((name, builderFunc) -> {
            RegistryObject<CreativeModeTab> reg = REGISTRY.register(name, () -> builderFunc.apply(CreativeModeTab.builder()).build());
            return reg;
        });
    }
}
