package maze.frequency.fabric;

import net.fabricmc.api.ModInitializer;
import maze.frequency.FrequencyMod;
import maze.frequency.fabric.init.FrequencyModItemsFabric;
import maze.frequency.fabric.init.FrequencyModMenusFabric;
import maze.frequency.fabric.init.FrequencyModTabsFabric;
import maze.frequency.fabric.network.PacketHandler;

public class FrequencyModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FrequencyModItemsFabric.register();
        FrequencyModMenusFabric.register();
        FrequencyModTabsFabric.register();
        PacketHandler.register();

        FrequencyMod.init();
    }
}
