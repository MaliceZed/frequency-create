package maze.frequency.compat.emi;

import net.minecraft.client.gui.screens.Screen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

/**
 * Forwards mouse wheel input to the scrollable symbol grid of Frequency
 * recipes displayed inside EMI's recipe screen.
 * <p>
 * This class is deliberately NOT annotated with {@code @EventBusSubscriber}.
 * FML reflects over every annotated class during mod construction, which
 * would resolve the {@code FrequencyEmiRecipe} signatures below and crash
 * with {@code NoClassDefFoundError} whenever EMI is absent. Instead,
 * registration on the NeoForge game bus happens conditionally from
 * {@code maze.frequency.client.FrequencyClientSetup}, guarded by a
 * {@code ModList.isLoaded("emi")} check that never touches this class
 * unless EMI is actually installed.
 * <p>
 * EMI's widget API has no per-widget scroll callback, so scrolling is handled
 * through NeoForge's {@link ScreenEvent.MouseScrolled.Pre}. To keep EMI's own
 * scrolling intact the handler stays deliberately narrow:
 * <ul>
 * <li>It only runs while EMI's recipe screen ({@code dev.emi.emi.screen.RecipeScreen},
 * checked by name because this mod compiles against EMI's API jar only) is open.</li>
 * <li>While the catalog grid is being rendered, its widget re-registers itself
 * here every frame via {@link #updateVisibleGrid}. A registration older than
 * {@link #FRESH_WINDOW_NANOS} means the grid is no longer on screen (another
 * tab or page is shown), and all wheel input is left to EMI untouched.</li>
 * <li>The scroll position is applied only to the exact instance that was drawn
 * last frame — never to a stale copy — so every wheel click visibly moves the
 * grid by one row immediately, regardless of where over the EMI screen the
 * cursor is.</li>
 * <li>The event is consumed whenever the catalog is on screen, so EMI does not
 * page between recipes at the same time as the grid scrolls.</li>
 * </ul>
 */
public final class FrequencyEmiScrollHandler {

    /**
     * A grid that has not been redrawn within this window is considered gone
     * from the screen. Rendering happens every frame, so one frame period is
     * normally enough; the window just adds safety for frame drops during
     * screen switches.
     */
    private static final long FRESH_WINDOW_NANOS = 250_000_000L; // 0.25 s

    /**
     * The catalog recipe whose grid was rendered most recently, or {@code null}
     * when nothing was rendered yet. Kept as a single slot instead of a map so
     * the wheel can only ever affect the instance that is actually visible.
     */
    private static FrequencyEmiRecipe visibleGrid;

    private static long lastRenderNanos;

    private FrequencyEmiScrollHandler() {
    }

    /**
     * Called by the recipe grid widget every frame it renders, marking that
     * instance as the currently visible one.
     */
    static void updateVisibleGrid(FrequencyEmiRecipe recipe) {
        visibleGrid = recipe;
        lastRenderNanos = System.nanoTime();
    }

    @SubscribeEvent
    public static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
        if (!isEmiRecipeScreen(event.getScreen())) return;
        if (!hasVisibleGrid()) return;

        double deltaY = event.getScrollDeltaY();
        if (deltaY == 0) return;

        // One wheel notch scrolls the catalog by exactly one row, immediately.
        int direction = deltaY > 0 ? -1 : 1;
        visibleGrid.scrollBy(direction);

        // The wheel belongs to the catalog while it is displayed; consuming the
        // event keeps EMI from paging recipes in the same gesture.
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onScreenClosing(ScreenEvent.Closing event) {
        if (isEmiRecipeScreen(event.getScreen())) {
            clearVisibleGrid();
        }
    }

    private static boolean hasVisibleGrid() {
        return visibleGrid != null
                && System.nanoTime() - lastRenderNanos <= FRESH_WINDOW_NANOS;
    }

    private static void clearVisibleGrid() {
        visibleGrid = null;
        lastRenderNanos = 0L;
    }

    private static boolean isEmiRecipeScreen(Screen screen) {
        return screen.getClass().getName().equals("dev.emi.emi.screen.RecipeScreen");
    }
}
