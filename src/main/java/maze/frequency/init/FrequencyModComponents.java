package maze.frequency.init;

import maze.frequency.FrequencyMod;
import maze.frequency.data.component.FluidData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FrequencyModComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, FrequencyMod.MODID);
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidData>> FLUID_DATA =
        COMPONENTS.register("fluid_data",
            () -> FluidData.TYPE);
}
