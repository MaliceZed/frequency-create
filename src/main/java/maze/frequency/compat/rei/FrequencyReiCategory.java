package maze.frequency.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import maze.frequency.init.FrequencyModItems;

import java.util.ArrayList;
import java.util.List;

public class FrequencyReiCategory implements DisplayCategory<FrequencyReiDisplay> {

    public static final CategoryIdentifier<FrequencyReiDisplay> ID =
        CategoryIdentifier.of("frequency", "symbol_swapping");

    private static final int COLS = 6;
    private static final int ROWS = 3;
    private static final int SLOT_SIZE = 18;
    private static final int PADDING = 2;
    private static final int PADDING_RECIPE = 10;

    private static final int GRID_X = 38 + PADDING_RECIPE; // 48
    private static final int GRID_Y = 0 + PADDING_RECIPE;  // 10

    private static final int INPUT_X = 0 + PADDING_RECIPE;  // 10
    private static final int INPUT_Y = GRID_Y + (SLOT_SIZE + PADDING) + 1; // 10 + 20 + 1 = 31
    private static final int ARROW_X = 19 + PADDING_RECIPE; // 29
    private static final int ARROW_Y = INPUT_Y - 4;         // 30 - 4 = 26
    private static final int SCROLLBAR_X = GRID_X + COLS * (SLOT_SIZE + PADDING) + 4;
    private static final int SCROLLBAR_W = 8;

    @Override
    public CategoryIdentifier<? extends FrequencyReiDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("category.frequency.symbol_swapping");
    }

    @Override
    public Renderer getIcon() {
        var emptyHolder = FrequencyModItems.getSymbol("brass_symbol_empty");
        if (emptyHolder != null) {
            return EntryStacks.of(new ItemStack(emptyHolder.get()));
        }
        return EntryStacks.of(ItemStack.EMPTY);
    }

    @Override
    public int getDisplayWidth(FrequencyReiDisplay display) {
        return SCROLLBAR_X + SCROLLBAR_W + PADDING_RECIPE; // 170 + 8 + 10 = 188
    }

    @Override
    public int getDisplayHeight() {
        return ROWS * (SLOT_SIZE + PADDING) - PADDING + 2 * PADDING_RECIPE; // 58 + 20 = 78
    }

    @Override
    public List<Widget> setupDisplay(FrequencyReiDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point startPoint = new Point(bounds.getX(), bounds.getY());

        int w = getDisplayWidth(display);
        int h = getDisplayHeight();

        // Рамка рецепта через 9-slice текстуру recipecontainer.png
        ResourceLocation REI_CONTAINER = ResourceLocation.fromNamespaceAndPath("roughlyenoughitems", "textures/gui/recipecontainer.png");
        widgets.add(Widgets.createDrawableWidget((draw, mouseX, mouseY, delta) -> {
            int x = startPoint.x;
            int y = startPoint.y;
            int corner = 8;

            // Верхний левый угол
            draw.blit(REI_CONTAINER, x, y, corner, corner, 106, 124, corner, corner, 256, 256);
            // Верхний правый угол
            draw.blit(REI_CONTAINER, x + w - corner, y, corner, corner, 248, 124, corner, corner, 256, 256);
            // Нижний левый угол
            draw.blit(REI_CONTAINER, x, y + h - corner, corner, corner, 106, 182, corner, corner, 256, 256);
            // Нижний правый угол
            draw.blit(REI_CONTAINER, x + w - corner, y + h - corner, corner, corner, 248, 182, corner, corner, 256, 256);

            // Верхняя граница
            draw.blit(REI_CONTAINER, x + corner, y, w - 2 * corner, corner, 114, 124, 134, corner, 256, 256);
            // Нижняя граница
            draw.blit(REI_CONTAINER, x + corner, y + h - corner, w - 2 * corner, corner, 114, 182, 134, corner, 256, 256);
            // Левая граница
            draw.blit(REI_CONTAINER, x, y + corner, corner, h - 2 * corner, 106, 132, corner, 50, 256, 256);
            // Правая граница
            draw.blit(REI_CONTAINER, x + w - corner, y + corner, corner, h - 2 * corner, 248, 132, corner, 50, 256, 256);
            // Центр
            draw.blit(REI_CONTAINER, x + corner, y + corner, w - 2 * corner, h - 2 * corner, 114, 132, 134, 50, 256, 256);
        }));

        // Input слот
        widgets.add(Widgets.createSlot(new Point(startPoint.x + INPUT_X, startPoint.y + INPUT_Y))
            .entries(List.of(display.getInputEntry()))
            .markInput()
        );

        // Стрелка
        widgets.add(Widgets.createLabel(new Point(startPoint.x + ARROW_X + 9, startPoint.y + ARROW_Y + 9),
                Component.literal("\u21D4"))
            .noShadow()
            .color(0xFF404040, 0xFF404040)
        );

        // Кастомный виджет для сетки со скроллбаром
        widgets.add(new ScrollableGridWidget(
            startPoint.x + GRID_X,
            startPoint.y + GRID_Y,
            display.getOutputStacks(),
            display.getScrollOffset(),
            display.getMaxScroll()
        ));

        return widgets;
    }
}
