package maze.frequency.client;

import maze.frequency.FrequencyMod;
import maze.frequency.compat.CreateTooltipCompat;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-only setup: registers client lifecycle listeners
 * that reference client-only classes (e.g. Create tooltip integration).
 */
@EventBusSubscriber(modid = FrequencyMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FrequencyClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CreateTooltipCompat.init();
    }
}
