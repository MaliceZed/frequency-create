package maze.frequency.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

import maze.frequency.FrequencyMod;
import maze.frequency.client.renderer.LogicCombinatorRenderer;
import maze.frequency.init.FrequencyModBlockEntities;
import maze.frequency.compat.CreateTooltipCompat;
import maze.frequency.compat.emi.FrequencyEmiScrollHandler;

@EventBusSubscriber(modid = FrequencyMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FrequencyClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CreateTooltipCompat.init();

        // EMI is an optional dependency: register the scroll handler only when
        // it is actually installed. FrequencyEmiScrollHandler carries no direct
        // dev.emi.emi.* imports (EMI types are reached through the recipe class),
        // so importing it here is safe; loading the class itself must never
        // happen without EMI present, which this guard guarantees.
        if (ModList.get().isLoaded("emi")) {
            NeoForge.EVENT_BUS.register(FrequencyEmiScrollHandler.class);
        }
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
            FrequencyModBlockEntities.LOGIC_COMBINATOR.get(),
            LogicCombinatorRenderer::new
        );
    }
}
