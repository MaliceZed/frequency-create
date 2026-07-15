package maze.frequency.data.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record FluidData(ResourceLocation fluid) {
    
    public static final DataComponentType<FluidData> TYPE = DataComponentType.<FluidData>builder()
        .persistent(ResourceLocation.CODEC.xmap(FluidData::new, FluidData::fluid))
        .networkSynchronized(ResourceLocation.STREAM_CODEC.map(FluidData::new, FluidData::fluid))
        .build();
}
