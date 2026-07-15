package maze.frequency.compat.emi;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = "frequency", value = Dist.CLIENT)
public class FrequencyEmiScrollHandler {
    @SubscribeEvent
    public static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
        if (!event.getScreen().getClass().getName().equals("dev.emi.emi.screen.RecipeScreen")) return;
        
        double delta = event.getScrollDeltaY();
        int d = delta > 0 ? -1 : (delta < 0 ? 1 : 0);
        if (d != 0 && FrequencyEmiRecipe.ACTIVE_RECIPE != null && FrequencyEmiRecipe.ACTIVE_RECIPE.scrollBy(d)) {
            event.setCanceled(true);
        }
    }
}
