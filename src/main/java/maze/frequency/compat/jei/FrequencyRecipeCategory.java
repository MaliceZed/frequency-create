package maze.frequency.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.List;

/**
 * JEI category that displays the Frequency Symbol Swapping mechanic.
 * <p>
 * Layout:
 * <pre>
 *   [Input]  →  [Scrollable output grid 6×3 (18 visible slots)]
 * </pre>
 * A single recipe shows one input symbol and ALL output symbols in a
 * scrollable grid.  JEI's built-in {@link mezz.jei.api.gui.widgets.IScrollGridWidget}
 * handles scissor clipping, scrollbar rendering and mouse-wheel input.
 */
public class FrequencyRecipeCategory implements IRecipeCategory<FrequencySwapRecipe> {

    public static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "symbol_swap");
    public static final RecipeType<FrequencySwapRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, FrequencySwapRecipe.class);

    // ── Layout constants ──────────────────────────────────────────────────────

    private static final int SLOT_SIZE = 18;
    // Grid: 6 columns × 3 visible rows → 108 px wide × 54 px tall
    private static final int GRID_COLS = 6;
    private static final int VISIBLE_ROWS = 3;

    private static final int BG_WIDTH  = 170;
    private static final int BG_HEIGHT = 100;

    // Input slot — vertically centred, left side
    private static final int INPUT_X = 8;
    private static final int INPUT_Y = (BG_HEIGHT - SLOT_SIZE) / 2;   // 41

    // Output grid — vertically centred, placed after the arrow
    private static final int GRID_X = 41;
    private static final int GRID_Y = (BG_HEIGHT - VISIBLE_ROWS * SLOT_SIZE) / 2;   // 23

    // ── Drawables ─────────────────────────────────────────────────────────────

    private final IDrawable background;
    private final IDrawable icon;

    public FrequencyRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(BG_WIDTH, BG_HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(
                new ItemStack(FrequencyModItems.getSymbol("brass_symbol_empty").get()));
    }

    // ── IRecipeCategory ───────────────────────────────────────────────────────

    @Override
    public RecipeType<FrequencySwapRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.category.frequency.symbol_swap");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    // ── Recipe layout ─────────────────────────────────────────────────────────

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FrequencySwapRecipe recipe, IFocusGroup focuses) {
        // ── Input slot (fixed position) ───────────────────────────────────
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y)
                .addItemStack(recipe.input());

        // ── Output slots — added WITHOUT position; they will be arranged  ─
        //    by the scroll grid widget created in createRecipeExtras().
        for (ItemStack stack : recipe.outputs()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT)
                    .addItemStack(stack);
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, FrequencySwapRecipe recipe,
                                   IFocusGroup focuses) {
        List<IRecipeSlotDrawable> outputSlots = builder.getRecipeSlots()
                .getSlots(RecipeIngredientRole.OUTPUT);

        if (!outputSlots.isEmpty()) {
            // Create a scrollable grid: 7 columns, 3 visible rows.
            // JEI automatically clips the content, renders a scrollbar,
            // and handles mouse-wheel scrolling.
            builder.addScrollGridWidget(outputSlots, GRID_COLS, VISIBLE_ROWS)
                    .setPosition(GRID_X, GRID_Y);
        }
    }

    // ── Additional rendering (arrow) ─────────────────────────────────────────

    @Override
    public void draw(FrequencySwapRecipe recipe, IRecipeSlotsView slotsView,
                     GuiGraphics graphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        // Arrow between input and output grid
        graphics.drawString(font, "\u21C4",   // ⇄
                INPUT_X + SLOT_SIZE + 2,
                INPUT_Y + 5,
                0x404040, false);
    }
}
