package maze.frequency;

import com.tterrag.registrate.Registrate;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

import maze.frequency.init.FrequencyModBlocks;
import maze.frequency.init.FrequencyModComponents;
import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModTabs;
import maze.frequency.init.FrequencyModMenus;
import maze.frequency.network.SymbolSwapPacket;
import maze.frequency.network.FrameUpdatePacket;

import maze.frequency.datagen.DataGenerators;
import maze.frequency.compat.FrameInteractionHandler;

@Mod("frequency")
public class FrequencyMod {
    public static final String MODID = "frequency";
    public static final Registrate REGISTRATE = Registrate.create(MODID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public FrequencyMod(IEventBus modEventBus) {
        modEventBus.addListener(this::registerNetworking);
        modEventBus.addListener(DataGenerators::gatherData);

        FrequencyModItems.ITEMS.register(modEventBus);
        FrequencyModTabs.TABS.register(modEventBus);
        FrequencyModBlocks.BLOCKS.register(modEventBus);
        FrequencyModBlocks.BLOCK_ENTITIES.register(modEventBus);
        FrequencyModBlocks.ITEMS.register(modEventBus);
        FrequencyModComponents.COMPONENTS.register(modEventBus);

        // Force-load all registration classes before GatherDataEvent fires.
        FrequencyModTabs.TABS.getClass();
        FrequencyModItems.ITEMS.getClass();
        FrequencyModMenus.BRASS_SYMBOL_SWAP.getClass();
        FrequencyModMenus.ANDESITE_SYMBOL_SWAP.getClass();
        FrequencyModBlocks.BLOCKS.getClass();

        // Register game event handlers (Create wrench, symbol frame interaction)
        FrameInteractionHandler.register();
    }

    private void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID);

        registrar.playBidirectional(
            SymbolSwapPacket.TYPE,
            SymbolSwapPacket.STREAM_CODEC,
            SymbolSwapPacket::handle
        );

        registrar.playBidirectional(
            FrameUpdatePacket.TYPE,
            FrameUpdatePacket.STREAM_CODEC,
            FrameUpdatePacket::handle
        );

    }
}
