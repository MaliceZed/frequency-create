package maze.frequency.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.core.NonNullList;

import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REI plugin for Frequency mod.
 * <p>
 * Registers a custom category showing the two crafting recipes
 * and a symbol gallery.
 */
@REIPluginClient
public class FrequencyReiPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<FrequencyReiDisplay> CATEGORY_ID =
            CategoryIdentifier.of(FrequencyMod.MODID, "symbols");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FrequencyReiCategory());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        // ── 1. Symbol Frame recipe ───────────────────────────────────────────
        loadShaped(mc, "symbol_frame", 3, 3).ifPresent(holder -> {
            if (holder.value() instanceof ShapedRecipe shaped) {
                registry.add(new FrequencyReiDisplay(
                        FrequencyReiDisplay.Type.FRAME_CRAFTING,
                        shaped.getResultItem(mc.level.registryAccess()),
                        shaped.getIngredients(),
                        shaped.getWidth(),
                        shaped.getHeight(),
                        List.of()
                ));
            }
        });

        // ── 2. Incomplete Symbol recipe ──────────────────────────────────────
        loadShaped(mc, "symbol_recipe", 1, 2).ifPresent(holder -> {
            if (holder.value() instanceof ShapedRecipe shaped) {
                registry.add(new FrequencyReiDisplay(
                        FrequencyReiDisplay.Type.INCOMPLETE_CRAFTING,
                        shaped.getResultItem(mc.level.registryAccess()),
                        shaped.getIngredients(),
                        shaped.getWidth(),
                        shaped.getHeight(),
                        List.of()
                ));
            }
        });

        // ── 3. Symbol Gallery ────────────────────────────────────────────────
        List<ItemStack> galleryStacks = new ArrayList<>();
        for (var holder : FrequencyModItems.ALL_BRASS_SYMBOLS) {
            galleryStacks.add(new ItemStack(holder.get()));
        }
        registry.add(new FrequencyReiDisplay(
                FrequencyReiDisplay.Type.SYMBOL_GALLERY,
                ItemStack.EMPTY,
                NonNullList.create(),
                0,
                0,
                galleryStacks
        ));
    }

    private Optional<RecipeHolder<?>> loadShaped(Minecraft mc, String id, int w, int h) {
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, id);
        return mc.level.getRecipeManager().byKey(recipeId);
    }

    // ── Inner classes ─────────────────────────────────────────────────────────

    /**
     * Data holder for REI display.
     */
    public record FrequencyReiDisplay(
            Type type,
            ItemStack result,
            NonNullList<Ingredient> ingredients,
            int gridWidth,
            int gridHeight,
            List<ItemStack> galleryItems
    ) implements Display {
        public enum Type {
            FRAME_CRAFTING,
            INCOMPLETE_CRAFTING,
            SYMBOL_GALLERY
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return CATEGORY_ID;
        }

        @Override
        public List<EntryIngredient> getInputEntries() {
            if (type == Type.SYMBOL_GALLERY) {
                return galleryItems.stream()
                        .map(EntryIngredients::of)
                        .toList();
            }
            return ingredients.stream()
                    .map(EntryIngredients::of)
                    .toList();
        }

        @Override
        public List<EntryIngredient> getOutputEntries() {
            if (type == Type.SYMBOL_GALLERY) return List.of();
            return List.of(EntryIngredients.of(result));
        }
    }

    /**
     * The category that renders Frequency displays.
     */
    public static class FrequencyReiCategory implements DisplayCategory<FrequencyReiDisplay> {
        @Override
        public CategoryIdentifier<? extends FrequencyReiDisplay> getCategoryIdentifier() {
            return CATEGORY_ID;
        }

        @Override
        public Component getTitle() {
            return Component.translatable("jei.category.frequency.symbols");
        }

        @Override
        public Renderer getIcon() {
            return EntryStacks.of(FrequencyModItems.getSymbol("symbol_empty").get());
        }

        @Override
        public int getDisplayWidth(FrequencyReiDisplay display) {
            return 176;
        }

        @Override
        public int getDisplayHeight(FrequencyReiDisplay display) {
            if (display.type() == FrequencyReiDisplay.Type.SYMBOL_GALLERY) {
                int rows = (int) Math.ceil(display.galleryItems().size() / 9.0);
                return 20 + rows * 18;
            }
            return 100;
        }

        @Override
        public List<Widget> setupDisplay(FrequencyReiDisplay display, me.shedaniel.rei.api.client.gui.widgets.Bounds bounds) {
            List<Widget> widgets = new ArrayList<>();

            switch (display.type()) {
                case FRAME_CRAFTING -> {
                    widgets.add(Widgets.createLabel(
                            new me.shedaniel.math.Point(10, 6),
                            Component.translatable("jei.category.frequency.symbols.frame_recipe")
                    ).noShadow().color(0x404040, 0x404040));
                    addShapedWidgets(widgets, display, 3, 3);
                }
                case INCOMPLETE_CRAFTING -> {
                    widgets.add(Widgets.createLabel(
                            new me.shedaniel.math.Point(10, 6),
                            Component.translatable("jei.category.frequency.symbols.incomplete_recipe")
                    ).noShadow().color(0x404040, 0x404040));
                    addShapedWidgets(widgets, display, 1, 2);
                }
                case SYMBOL_GALLERY -> {
                    widgets.add(Widgets.createLabel(
                            new me.shedaniel.math.Point(10, 6),
                            Component.translatable("jei.category.frequency.symbols.gallery")
                    ).noShadow().color(0x404040, 0x404040));
                    int cols = 9;
                    int idx = 0;
                    for (ItemStack stack : display.galleryItems()) {
                        int x = 1 + (idx % cols) * 18;
                        int y = 20 + (idx / cols) * 18;
                        widgets.add(Widgets.createSlot(new me.shedaniel.math.Point(x, y))
                                .entry(EntryStacks.of(stack))
                                .notWorkingWithIt()
                                .disableBackground());
                        idx++;
                    }
                }
            }

            return widgets;
        }

        private void addShapedWidgets(List<Widget> widgets, FrequencyReiDisplay display, int w, int h) {
            int startX = 10;
            int startY = 20;
            int slotSize = 18;

            int idx = 0;
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    if (idx < display.ingredients().size()) {
                        Ingredient ing = display.ingredients().get(idx);
                        if (!ing.isEmpty()) {
                            widgets.add(Widgets.createSlot(
                                    new me.shedaniel.math.Point(startX + col * slotSize, startY + row * slotSize)
                            ).entries(EntryIngredients.of(ing)));
                        }
                    }
                    idx++;
                }
            }

            // Result slot
            widgets.add(Widgets.createSlot(
                    new me.shedaniel.math.Point(startX + w * slotSize + 20, startY + (h / 2) * slotSize)
            ).entries(List.of(EntryStacks.of(display.result()))).disableBackground());
        }
    }
}
