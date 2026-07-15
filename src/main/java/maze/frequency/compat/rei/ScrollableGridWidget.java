package maze.frequency.compat.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ScrollableGridWidget extends Widget {

    private static final int COLS = 6;
    private static final int ROWS = 3;
    private static final int SLOT_SIZE = 18;
    private static final int PADDING = 2;
    private static final int SCROLLBAR_W = 8;

    private static final ResourceLocation SLOT_BACKGROUND =
        ResourceLocation.withDefaultNamespace("container/slot");

    private final int x;
    private final int y;
    private final List<EntryStack<ItemStack>> allSymbols;
    private final int totalRows;
    private final int maxScrollOffset;

    private int scrollOffset;

    // Drag состояние
    private boolean dragging = false;
    private int dragStartY = 0;
    private int dragStartScroll = 0;

    public ScrollableGridWidget(int x, int y, List<EntryStack<ItemStack>> allSymbols, int scrollOffset, int maxScroll) {
        this.x = x;
        this.y = y;
        this.allSymbols = allSymbols;
        this.totalRows = (allSymbols.size() + COLS - 1) / COLS;
        this.maxScrollOffset = maxScroll;
        this.scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
    }

    @Override
    public List<? extends Widget> children() {
        return List.of();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        // Fallback implementation from Renderable: delegate to bounds-based render
        int gridWidth = COLS * (SLOT_SIZE + PADDING) - PADDING; // 6*20-2 = 118
        int w = gridWidth + SCROLLBAR_W + 4; // 118 + 8 + 4 = 130
        int h = ROWS * (SLOT_SIZE + PADDING) - PADDING; // 58
        render(graphics, new Rectangle(x, y, w, h), mouseX, mouseY, delta);
    }

    @Override
    public void render(GuiGraphics draw, Rectangle bounds, int mouseX, int mouseY, float delta) {
        // Рисуем слоты
        for (int row = 0; row < ROWS; row++) {
            int symbolRow = scrollOffset + row;
            for (int col = 0; col < COLS; col++) {
                int index = symbolRow * COLS + col;
                if (index >= allSymbols.size()) continue;

                int slotX = x + col * (SLOT_SIZE + PADDING);
                int slotY = y + row * (SLOT_SIZE + PADDING);

                // Фон слота
                draw.blitSprite(SLOT_BACKGROUND, slotX, slotY, SLOT_SIZE, SLOT_SIZE);

                // Предмет с отступом 1px
                EntryStack<ItemStack> entry = allSymbols.get(index);
                entry.render(draw, new Rectangle(slotX + 1, slotY + 1, 16, 16), 0, 0, delta);
            }
        }

        // Рисуем скроллбар, если нужно
        if (maxScrollOffset > 0) {
            int gridWidth = COLS * (SLOT_SIZE + PADDING) - PADDING; // 6*20-2 = 118
            int scrollTrackX = x + gridWidth + 2; // x + 120
            int scrollTrackY = y;
            int scrollTrackH = ROWS * (SLOT_SIZE + PADDING) - PADDING;

            // Рамка скроллбара
            draw.fill(scrollTrackX - 1, scrollTrackY - 1, scrollTrackX + SCROLLBAR_W + 1, scrollTrackY, 0xFF555555);
            draw.fill(scrollTrackX - 1, scrollTrackY + scrollTrackH, scrollTrackX + SCROLLBAR_W + 1, scrollTrackY + scrollTrackH + 1, 0xFF555555);
            draw.fill(scrollTrackX - 1, scrollTrackY, scrollTrackX, scrollTrackY + scrollTrackH, 0xFF555555);
            draw.fill(scrollTrackX + SCROLLBAR_W, scrollTrackY, scrollTrackX + SCROLLBAR_W + 1, scrollTrackY + scrollTrackH, 0xFF555555);

            // Трек
            draw.fill(scrollTrackX, scrollTrackY, scrollTrackX + SCROLLBAR_W, scrollTrackY + scrollTrackH, 0xFF333333);

            // Ползунок
            float scrollFraction = maxScrollOffset > 0 ? (float) scrollOffset / maxScrollOffset : 0;
            int thumbHeight = Math.max(10, scrollTrackH / (totalRows - ROWS + 1));
            int thumbY = scrollTrackY + (int) (scrollFraction * (scrollTrackH - thumbHeight));

            draw.fill(scrollTrackX, thumbY, scrollTrackX + SCROLLBAR_W, thumbY + thumbHeight, 0xFF888888);

            // Grips
            if (thumbHeight >= 8) {
                int gripCenter = thumbY + thumbHeight / 2;
                for (int gripOffset = -3; gripOffset <= 3; gripOffset += 3) {
                    int gripY = gripCenter + gripOffset;
                    draw.fill(scrollTrackX + 2, gripY, scrollTrackX + SCROLLBAR_W - 2, gripY + 1, 0xFFAAAAAA);
                }
            }

            // Drag обработка
            if (dragging) {
                long window = Minecraft.getInstance().getWindow().getWindow();
                boolean leftDown = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
                if (leftDown) {
                    int deltaY = mouseY - dragStartY;
                    int movableRange = scrollTrackH - thumbHeight;
                    if (movableRange > 0) {
                        float pctDelta = (float) deltaY / movableRange;
                        int newScroll = Math.max(0, Math.min(maxScrollOffset,
                            dragStartScroll + Math.round(pctDelta * maxScrollOffset)));
                        if (newScroll != scrollOffset) {
                            scrollOffset = newScroll;
                        }
                    }
                } else {
                    dragging = false;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && maxScrollOffset > 0) {
            int gridWidth = COLS * (SLOT_SIZE + PADDING) - PADDING; // 6*20-2 = 118
            int scrollTrackX = x + gridWidth + 2; // x + 120
            int scrollTrackY = y;
            int scrollTrackH = ROWS * (SLOT_SIZE + PADDING) - PADDING;

            // Проверяем, что клик на скроллбаре
            if (mouseX >= scrollTrackX && mouseX <= scrollTrackX + SCROLLBAR_W &&
                mouseY >= scrollTrackY && mouseY <= scrollTrackY + scrollTrackH) {

                float scrollFraction = maxScrollOffset > 0 ? (float) scrollOffset / maxScrollOffset : 0;
                int thumbHeight = Math.max(10, scrollTrackH / (totalRows - ROWS + 1));
                int thumbY = scrollTrackY + (int) (scrollFraction * (scrollTrackH - thumbHeight));

                if (mouseY < thumbY) {
                    // Выше ползунка — скролл вверх на страницу
                    scrollOffset = Math.max(0, scrollOffset - ROWS);
                } else if (mouseY > thumbY + thumbHeight) {
                    // Ниже ползунка — скролл вниз на страницу
                    scrollOffset = Math.min(maxScrollOffset, scrollOffset + ROWS);
                } else {
                    // На ползунке — начинаем drag
                    dragging = true;
                    dragStartY = (int) mouseY;
                    dragStartScroll = scrollOffset;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (maxScrollOffset > 0) {
            int d = scrollY > 0 ? -1 : (scrollY < 0 ? 1 : 0);
            if (d != 0) {
                int newOffset = scrollOffset + d;
                if (newOffset >= 0 && newOffset <= maxScrollOffset) {
                    scrollOffset = newOffset;
                    return true;
                }
            }
        }
        return false;
    }
}
