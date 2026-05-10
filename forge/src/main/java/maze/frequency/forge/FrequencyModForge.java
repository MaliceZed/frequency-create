package maze.frequency.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.api.distmarker.OnlyIn;

import maze.frequency.FrequencyMod;
import maze.frequency.forge.init.FrequencyModItemsForge;
import maze.frequency.forge.init.FrequencyModMenusForge;
import maze.frequency.forge.init.FrequencyModTabsForge;
import maze.frequency.forge.network.PacketHandler;

@Mod(FrequencyMod.MODID)
public class FrequencyModForge {
    public FrequencyModForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        FrequencyModItemsForge.REGISTRY.register(modEventBus);
        FrequencyModItemsForge.register();

        FrequencyModMenusForge.REGISTRY.register(modEventBus);
        FrequencyModMenusForge.register();

        FrequencyModTabsForge.REGISTRY.register(modEventBus);
        FrequencyModTabsForge.register();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
        FrequencyMod.init();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(maze.frequency.forge.client.ClientSetup::init);
    }
}
