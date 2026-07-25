package maze.frequency.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import maze.frequency.FrequencyMod;
import maze.frequency.client.renderer.LogicCombinatorRenderer;
import maze.frequency.init.FrequencyModBlockEntities;
import maze.frequency.compat.CreateTooltipCompat;

@EventBusSubscriber(modid = FrequencyMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FrequencyClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CreateTooltipCompat.init();
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
            FrequencyModBlockEntities.LOGIC_COMBINATOR.get(),
            LogicCombinatorRenderer::new
        );
    }
}
