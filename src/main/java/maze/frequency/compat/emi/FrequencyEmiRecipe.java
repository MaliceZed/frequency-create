package maze.frequency.compat.emi;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import org.lwjgl.glfw.GLFW;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.api.widget.WidgetHolder;

import maze.frequency.FrequencyMod;

/**
 * EMI рецепт для отображения каталога всех Frequency символов.
 * <p>
 * Один рецепт, все символы в виде прокручиваемой сетки 6×3.
 * Внутренний виджет {@link ScrollableGridWidget} реализует всю логику
 * рендера, скролла и drag-перетаскивания ползунка.
 */
public class FrequencyEmiRecipe implements EmiRecipe {

    // ── Константы сетки ─────────────────────────────────────────────────────

    private static final int COLS = 6;
    private static final int ROWS = 3;
    private static final int VISIBLE_SLOTS = COLS * ROWS; // 18
    private static final int SLOT_SIZE = 18;
    private static final int PADDING = 2;

    // Сетка output (первый ряд) — отцентрована по вертикали
    private static final int GRID_X = 38;
    private static final int GRID_Y = 0;

    // Input слот (symbol_empty) — на уровне 2 ряда (GRID_Y + 20)
    private static final int INPUT_X = 0;
    private static final int INPUT_Y = GRID_Y + (SLOT_SIZE + PADDING); // 10 + 20 = 30

    // Стрелка — на уровне 2 ряда (напротив input слота)
    private static final int ARROW_X = 19;
    private static final int ARROW_Y = INPUT_Y; // 30

    // Скроллбар
    private static final int SCROLLBAR_X = GRID_X + COLS * (SLOT_SIZE + PADDING) + 2; // 38 + 120 + 2 = 160
    private static final int SCROLLBAR_W = 8;
    private static final int SCROLLBAR_H = ROWS * (SLOT_SIZE + PADDING) - PADDING; // 3 * 20 - 2 = 58

    // ── Размер всего рецепта ────────────────────────────────────────────────

    private static final int DISPLAY_WIDTH = SCROLLBAR_X + SCROLLBAR_W;
    private static final int DISPLAY_HEIGHT = GRID_Y + SCROLLBAR_H;

    public static final int RECIPE_WIDTH = DISPLAY_WIDTH;
    public static final int RECIPE_HEIGHT = DISPLAY_HEIGHT;

    // ── Текстура слота ──────────────────────────────────────────────────────

    private static final ResourceLocation SLOT_BACKGROUND =
            ResourceLocation.withDefaultNamespace("container/slot");

    // ═══════════════════════════════════════════════════════════════════════
    //  Статические поля для drag и внешнего скролла
    // ═══════════════════════════════════════════════════════════════════════

    /** Ссылка на «активный» экземпляр рецепта (последний созданный). */
    public static FrequencyEmiRecipe ACTIVE_RECIPE = null;

    /** Идёт ли перетаскивание ползунка. */
    public static boolean DRAGGING = false;

    /** Y мыши (относительно рецепта) на момент начала drag. */
    public static int DRAG_START_Y = 0;

    /** scrollOffset на момент начала drag. */
    public static int DRAG_START_SCROLL = 0;

    // ═══════════════════════════════════════════════════════════════════════
    //  Поля экземпляра
    // ═══════════════════════════════════════════════════════════════════════

    private final EmiStack inputStack;
    private final List<EmiStack> allSymbols;
    private final int totalRows;
    private final int maxScrollOffset;
    private int scrollOffset;

    // ═══════════════════════════════════════════════════════════════════════
    //  Конструктор
    // ═══════════════════════════════════════════════════════════════════════

    public FrequencyEmiRecipe(EmiStack inputStack, List<EmiStack> allSymbols) {
        this.inputStack = inputStack;
        this.allSymbols = allSymbols;
        this.totalRows = (allSymbols.size() + COLS - 1) / COLS;
        this.maxScrollOffset = Math.max(0, totalRows - ROWS);
        this.scrollOffset = 0;
        ACTIVE_RECIPE = this;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  EmiRecipe
    // ═══════════════════════════════════════════════════════════════════════

    @Override
    public EmiRecipeCategory getCategory() {
        return FrequencyEmiRecipeCategory.INSTANCE;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "symbol_swap");
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of();
    }

    @Override
    public List<EmiStack> getOutputs() {
        return allSymbols;
    }

    @Override
    public int getDisplayWidth() {
        return DISPLAY_WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return DISPLAY_HEIGHT;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        // Input слот (symbol_empty)
        widgets.addSlot(inputStack, INPUT_X, INPUT_Y).drawBack(true);

        // Кастомный виджет сетки со скроллбаром
        widgets.add(new ScrollableGridWidget());
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  Скролл (вызывается из FrequencyEmiScrollHandler)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Прокрутить сетку на {@code delta} строк (отрицательное — вверх).
     *
     * @return true, если позиция изменилась
     */
    public boolean scrollBy(int delta) {
        int newOffset = scrollOffset + delta;
        if (newOffset < 0 || newOffset > maxScrollOffset) return false;
        scrollOffset = newOffset;
        return true;
    }

    /**
     * Проверить, находится ли точка ({@code relX}, {@code relY}) над областью
     * скроллбара. Координаты относительные (относительно левого верхнего угла
     * рецепта).
     */
    public boolean isMouseOverScrollbar(int relX, int relY) {
        return relX >= SCROLLBAR_X
            && relX < SCROLLBAR_X + SCROLLBAR_W
            && relY >= GRID_Y
            && relY < GRID_Y + SCROLLBAR_H;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  Внутренний класс — кастомный виджет сетки со скроллбаром
    // ═══════════════════════════════════════════════════════════════════════

    private class ScrollableGridWidget extends Widget {

        ScrollableGridWidget() {
        }

        @Override
        public Bounds getBounds() {
            return new Bounds(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
        }

        @Override
        public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
            // ── Рисуем фон и слоты ──────────────────────────────────────
            for (int row = 0; row < ROWS; row++) {
                int symbolRow = scrollOffset + row;
                for (int col = 0; col < COLS; col++) {
                    int index = symbolRow * COLS + col;
                    if (index >= allSymbols.size()) continue;

                    int x = GRID_X + col * (SLOT_SIZE + PADDING);
                    int y = GRID_Y + row * (SLOT_SIZE + PADDING);

                    // Фон слота
                    draw.blitSprite(SLOT_BACKGROUND, x, y, SLOT_SIZE, SLOT_SIZE);

                    // Предмет внутри слота (смещение +1px для лучшего позиционирования)
                    EmiStack stack = allSymbols.get(index);
                    stack.render(draw, x + 1, y + 1, delta);
                }
            }

            // Рисуем стрелку преобразования
            String arrow = "⇔";
            int textWidth = Minecraft.getInstance().font.width(arrow);
            int textX = ARROW_X + (18 - textWidth) / 2;
            int textY = ARROW_Y + 1 + (18 - Minecraft.getInstance().font.lineHeight) / 2;
            draw.drawString(Minecraft.getInstance().font, arrow, textX, textY, 0xFF404040, false);

            // ── Скроллбар ───────────────────────────────────────────────
            if (maxScrollOffset > 0) {
                int scrollTrackX = SCROLLBAR_X;
                int scrollTrackY = GRID_Y;
                int scrollTrackH = SCROLLBAR_H;
                
                // Рамка скроллбара
                // Светло-серая рамка 1px
                draw.fill(scrollTrackX - 1, scrollTrackY - 1, scrollTrackX + SCROLLBAR_W + 1, scrollTrackY, 0xFF555555); // top
                draw.fill(scrollTrackX - 1, scrollTrackY + scrollTrackH, scrollTrackX + SCROLLBAR_W + 1, scrollTrackY + scrollTrackH + 1, 0xFF555555); // bottom
                draw.fill(scrollTrackX - 1, scrollTrackY, scrollTrackX, scrollTrackY + scrollTrackH, 0xFF555555); // left
                draw.fill(scrollTrackX + SCROLLBAR_W, scrollTrackY, scrollTrackX + SCROLLBAR_W + 1, scrollTrackY + scrollTrackH, 0xFF555555); // right
                
                // Трек (фон скроллбара) — тёмно-серый
                draw.fill(scrollTrackX, scrollTrackY, scrollTrackX + SCROLLBAR_W, scrollTrackY + scrollTrackH, 0xFF333333);
                
                // Ползунок
                float scrollFraction = maxScrollOffset > 0 ? (float) scrollOffset / maxScrollOffset : 0;
                int thumbHeight = Math.max(10, scrollTrackH / (totalRows - ROWS + 1));
                int thumbY = scrollTrackY + (int) (scrollFraction * (scrollTrackH - thumbHeight));
                
                // Ползунок — светло-серый с более светлой серединой
                draw.fill(scrollTrackX, thumbY, scrollTrackX + SCROLLBAR_W, thumbY + thumbHeight, 0xFF888888);
                
                // Grips — три горизонтальные линии на ползунке
                if (thumbHeight >= 8) {
                    int gripCenter = thumbY + thumbHeight / 2;
                    for (int gripOffset = -3; gripOffset <= 3; gripOffset += 3) {
                        int gripY = gripCenter + gripOffset;
                        draw.fill(scrollTrackX + 2, gripY, scrollTrackX + SCROLLBAR_W - 2, gripY + 1, 0xFFAAAAAA);
                    }
                }
                
                // ── Drag-перетаскивание ─────────────────────────────────
                if (DRAGGING) {
                    long window = Minecraft.getInstance().getWindow().getWindow();
                    boolean leftDown = GLFW.glfwGetMouseButton(window,
                            GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
                    if (leftDown) {
                        // mouseX, mouseY — относительные (относительно рецепта)
                        int deltaY = mouseY - DRAG_START_Y;
                        int movableRange = scrollTrackH - thumbHeight;
                        if (movableRange > 0) {
                            float pctDelta = (float) deltaY / movableRange;
                            int newScroll = Math.max(0,
                                    Math.min(maxScrollOffset,
                                            DRAG_START_SCROLL
                                                    + Math.round(pctDelta * maxScrollOffset)));
                            if (newScroll != scrollOffset) {
                                scrollOffset = newScroll;
                            }
                        }
                    } else {
                        // Кнопка отпущена — завершаем drag
                        DRAGGING = false;
                    }
                }
            }
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (button != 0) return false;

            if (isMouseOverScrollbar(mouseX, mouseY) && maxScrollOffset > 0) {
                // Вычисляем позицию ползунка
                int thumbHeight = Math.max(10,
                        SCROLLBAR_H / (totalRows - ROWS + 1));
                float scrollFraction = (float) scrollOffset / maxScrollOffset;
                int thumbY = GRID_Y
                        + (int) (scrollFraction * (SCROLLBAR_H - thumbHeight));

                if (mouseY >= thumbY && mouseY < thumbY + thumbHeight) {
                    // Клик на ползунке — начинаем drag
                    DRAGGING = true;
                    DRAG_START_Y = mouseY;
                    DRAG_START_SCROLL = scrollOffset;
                } else if (mouseY < thumbY) {
                    // Выше ползунка — страницу вверх
                    scrollOffset = Math.max(0, scrollOffset - ROWS);
                } else {
                    // Ниже ползунка — страницу вниз
                    scrollOffset = Math.min(maxScrollOffset, scrollOffset + ROWS);
                }
                return true;
            }

            // ── Клик на предмет — показать рецепты/использования ──────
            for (int row = 0; row < ROWS; row++) {
                int symbolRow = scrollOffset + row;
                for (int col = 0; col < COLS; col++) {
                    int index = symbolRow * COLS + col;
                    if (index >= allSymbols.size()) continue;

                    int x = GRID_X + col * (SLOT_SIZE + PADDING);
                    int y = GRID_Y + row * (SLOT_SIZE + PADDING);

                    if (mouseX >= x && mouseX < x + SLOT_SIZE
                            && mouseY >= y && mouseY < y + SLOT_SIZE) {
                        EmiStack clicked = allSymbols.get(index);
                        EmiApi.displayRecipes(clicked);
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
            for (int row = 0; row < ROWS; row++) {
                int symbolRow = scrollOffset + row;
                for (int col = 0; col < COLS; col++) {
                    int index = symbolRow * COLS + col;
                    if (index >= allSymbols.size()) continue;

                    int x = GRID_X + col * (SLOT_SIZE + PADDING);
                    int y = GRID_Y + row * (SLOT_SIZE + PADDING);

                    if (mouseX >= x && mouseX < x + SLOT_SIZE
                            && mouseY >= y && mouseY < y + SLOT_SIZE) {
                        EmiStack stack = allSymbols.get(index);
                        ItemStack itemStack = stack.getItemStack();
                        if (!itemStack.isEmpty()) {
                            Minecraft mc = Minecraft.getInstance();
                            boolean advanced = mc.options.advancedItemTooltips;
                            TooltipFlag flag = advanced
                                    ? TooltipFlag.Default.ADVANCED
                                    : TooltipFlag.Default.NORMAL;
                            List<Component> tooltipLines =
                                    itemStack.getTooltipLines(Item.TooltipContext.EMPTY, mc.player, flag);
                            return tooltipLines.stream()
                                    .map(Component::getVisualOrderText)
                                    .map(ClientTooltipComponent::create)
                                    .collect(Collectors.toList());
                        }
                        return List.of();
                    }
                }
            }
            return List.of();
        }
    }
}
