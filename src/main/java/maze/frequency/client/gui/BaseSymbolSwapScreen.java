package maze.frequency.client.gui;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import maze.frequency.world.inventory.SymbolSwapMenu;
import maze.frequency.network.SymbolSwapPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.ArrayList;

public abstract class BaseSymbolSwapScreen extends AbstractContainerScreen<SymbolSwapMenu> {
    private final ResourceLocation atlas;
    private final int titleColor;
    private final String prefix;
    private static final int BUTTON_SIZE = 20;
    private static final int SPACING = 2;
    private static final int MAX_ITEMS_PER_ROW = 10;
    private static final String[] CATEGORY_KEYS = {
        "gui.frequency.category.digits",
        "gui.frequency.category.letters",
        "gui.frequency.category.special",
        "gui.frequency.category.math"
    };
    private static final boolean[] collapsed = new boolean[CATEGORY_KEYS.length];
    static {
        collapsed[2] = true; // symbols collapsed by default
        collapsed[3] = true; // math collapsed by default
    }
    private static final int CHECKBOX_SIZE = 8;
    private static final int CHECKBOX_ROW_HEIGHT = 14;
    private static final int CONTENT_PADDING = 8;
    private static boolean lettersUppercase = true;
    private static final int GRID_PADDING = 4;
    private static final int SLOT_SIZE = 22;
    private static final int SLOT_SPACING = 12;
    private static final int LABEL_Y_OFFSET = 8;
    private static final int GRID_COLUMNS = 6;
    private static final int GRID_ROWS = 5;
    private static final int SCROLL_HEIGHT = 46;
    private static final int CATEGORY_HEIGHT = 51;
    private static final int CATEGORY_LABEL_WIDTH = 54;
    private static final int CATEGORY_GAP = 9;
    private static final int ITEM_X_OFFSET = 2;
    private static final int ITEM_Y_OFFSET = 2;
    private int digitRows;
    private int letterRows;
    private int specialRows;
    private int mathRows;
    private int checkboxY;
    private int[] categoryLabelY = new int[CATEGORY_KEYS.length];
    private String[] categoryLabels;
    private List<CategoryRow> categoryRows;
    private List<ItemStack> allDigits;
    private List<ItemStack> allUppercase;
    private List<ItemStack> allLowercase;
    private List<ItemStack> allSpecials;
    private List<ItemStack> allMath;
    private List<ItemStack> allLetters;
    private ItemStack currentSymbol;
    private ItemStack glowingSymbolStack = ItemStack.EMPTY;

    private static class CategoryRow {
        List<ItemStack> items = new ArrayList<>();
        int y;
    }

    protected BaseSymbolSwapScreen(SymbolSwapMenu menu, Inventory playerInventory, Component title,
                            ResourceLocation atlas, int titleColor, String prefix) {
        super(menu, playerInventory, title);
        this.atlas = atlas;
        this.titleColor = titleColor;
        this.prefix = prefix;
        this.inventoryLabelY = 10000;
    }

    private int getRowStartX(CategoryRow row) {
        int rowWidth = row.items.size() * (BUTTON_SIZE + SPACING) - SPACING;
        return this.leftPos + (this.imageWidth - rowWidth) / 2;
    }

    @Override
    protected void init() {
        super.init();
        this.allDigits = new ArrayList<>();
        this.allUppercase = new ArrayList<>();
        this.allLowercase = new ArrayList<>();
        this.allSpecials = new ArrayList<>();
        this.allMath = new ArrayList<>();

        for (ItemStack stack : this.menu.getAvailableSymbols()) {
            String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
            if (id.matches(prefix + "[0-9]")) {
                allDigits.add(stack);
            } else if (id.matches(prefix + "[a-z]")) {
                allUppercase.add(stack);
            } else if (id.matches(prefix + "[a-z]_small")) {
                allLowercase.add(stack);
            } else if (id.startsWith(prefix + "math_")) {
                allMath.add(stack);
            } else {
                allSpecials.add(stack);
            }
        }

        this.allLetters = lettersUppercase ? allUppercase : allLowercase;
        digitRows = calcRows(allDigits.size());
        letterRows = calcRows(allLetters.size());
        specialRows = calcRows(allSpecials.size());
        mathRows = calcRows(allMath.size());
        this.currentSymbol = this.menu.getHeldItem();

        this.categoryLabels = new String[CATEGORY_KEYS.length];
        for (int i = 0; i < CATEGORY_KEYS.length; i++) {
            this.categoryLabels[i] = Component.translatable(CATEGORY_KEYS[i]).getString();
        }

        if (!this.currentSymbol.isEmpty()) {
            this.glowingSymbolStack = this.currentSymbol.copy();
            this.glowingSymbolStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            this.glowingSymbolStack = ItemStack.EMPTY;
        }

        int fixedContentWidth = MAX_ITEMS_PER_ROW * (BUTTON_SIZE + SPACING) - SPACING + 28;
        this.imageWidth = DynamicGuiRenderer.calculateWidth(fixedContentWidth);
        this.leftPos = (this.width - this.imageWidth) / 2;
        rebuildLayout();
    }

    private void splitRows(List<ItemStack> items, List<CategoryRow> rows) {
        for (int i = 0; i < items.size(); i += MAX_ITEMS_PER_ROW) {
            CategoryRow row = new CategoryRow();
            int end = Math.min(i + MAX_ITEMS_PER_ROW, items.size());
            row.items = new ArrayList<>(items.subList(i, end));
            rows.add(row);
        }
    }

    private int calcRows(int count) {
        return (count + MAX_ITEMS_PER_ROW - 1) / MAX_ITEMS_PER_ROW;
    }

    private void rebuildLayout() {
        this.categoryRows = new ArrayList<>();
        int[] rowsPerCat = {digitRows, letterRows, specialRows, mathRows};

        if (!collapsed[0]) splitRows(allDigits, categoryRows);
        if (!collapsed[1]) splitRows(allLetters, categoryRows);
        if (!collapsed[2]) splitRows(allSpecials, categoryRows);
        if (!collapsed[3]) splitRows(allMath, categoryRows);

        int categorySpacing = GRID_PADDING;
        int rowSpacing = SLOT_SIZE;
        int labelOffset = SLOT_SPACING;

        int contentHeight = CONTENT_PADDING;
        for (int i = 0; i < CATEGORY_KEYS.length; i++) {
            contentHeight += labelOffset;
            if (!collapsed[i]) {
                contentHeight += rowsPerCat[i] * rowSpacing;
                if (i == 1) contentHeight += CHECKBOX_ROW_HEIGHT;
            }
            if (i < CATEGORY_KEYS.length - 1) contentHeight += categorySpacing;
        }
        contentHeight += CONTENT_PADDING;

        this.imageHeight = Math.max(DynamicGuiRenderer.calculateHeight(contentHeight), 200);
        this.topPos = (this.height - this.imageHeight) / 2;

        int startY = this.topPos + DynamicGuiRenderer.HEADER_HEIGHT + CONTENT_PADDING + labelOffset;
        int rowIdx = 0;
        int currentY = startY;

        for (int cat = 0; cat < CATEGORY_KEYS.length; cat++) {
            categoryLabelY[cat] = currentY - labelOffset;
            int catRows = rowsPerCat[cat];
            if (!collapsed[cat]) {
                if (cat == 1) {
                    checkboxY = currentY;
                    currentY += CHECKBOX_ROW_HEIGHT;
                }
                for (int i = 0; i < catRows; i++) {
                    categoryRows.get(rowIdx + i).y = currentY + i * rowSpacing;
                }
                rowIdx += catRows;
                currentY += catRows * rowSpacing;
            }
            currentY += categorySpacing + labelOffset;
        }

        this.titleLabelX = this.leftPos + CATEGORY_GAP;
        this.titleLabelY = this.topPos + GRID_PADDING;


    }

    private String categoryLabel(int cat) {
        return categoryLabels[cat];
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int labelX = this.leftPos + CATEGORY_GAP;

            for (int cat = 0; cat < CATEGORY_KEYS.length; cat++) {
                String label = categoryLabel(cat);
                int textWidth = this.font.width(label);
                if (mouseX >= labelX - GRID_PADDING && mouseX < labelX + LABEL_Y_OFFSET + textWidth + GRID_PADDING
                    && mouseY >= categoryLabelY[cat] - 2
                    && mouseY < categoryLabelY[cat] + 10) {
                    collapsed[cat] = !collapsed[cat];
                    rebuildLayout();
                    return true;
                }
            }

            if (!collapsed[1]) {
                if (mouseX >= labelX + 10 && mouseX < labelX + 10 + CHECKBOX_SIZE
                    && mouseY >= checkboxY + 1 && mouseY < checkboxY + 1 + CHECKBOX_SIZE) {
                    lettersUppercase = !lettersUppercase;
                    this.allLetters = lettersUppercase ? allUppercase : allLowercase;
                    letterRows = calcRows(allLetters.size());
                    rebuildLayout();
                    return true;
                }
            }

            for (CategoryRow row : categoryRows) {
                if (row.items.isEmpty()) continue;

                int startX = getRowStartX(row);

                for (int i = 0; i < row.items.size(); i++) {
                    int x = startX + i * (BUTTON_SIZE + SPACING);

                    if (mouseX >= x && mouseX < x + BUTTON_SIZE && mouseY >= row.y && mouseY < row.y + BUTTON_SIZE) {
                        int index = this.menu.getAvailableSymbols().indexOf(row.items.get(i));
                        SymbolSwapPacket.send(index);
                        this.onClose();
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        DynamicGuiRenderer.renderGui(guiGraphics, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, atlas);

        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, this.titleColor, false);

        int labelX = this.leftPos + 9;
        int iconYoff = (this.font.lineHeight - GRID_COLUMNS) / 2;

        for (int cat = 0; cat < CATEGORY_KEYS.length; cat++) {
            int iconU = collapsed[cat] ? SCROLL_HEIGHT : CATEGORY_HEIGHT;
            int iconW = collapsed[cat] ? GRID_ROWS : GRID_COLUMNS;
            int iconX = collapsed[cat] ? labelX + 1 : labelX;
            guiGraphics.blit(atlas, iconX, categoryLabelY[cat] + iconYoff, iconU, 0, iconW, GRID_COLUMNS, 64, 64);
            guiGraphics.drawString(this.font, categoryLabel(cat), labelX + LABEL_Y_OFFSET, categoryLabelY[cat], 0xF8F8EC, true);
        }

        if (!collapsed[1]) {
            int cbU = lettersUppercase ? CATEGORY_LABEL_WIDTH : SCROLL_HEIGHT;
            guiGraphics.blit(atlas, labelX + 10, checkboxY + 1, cbU, 8, CHECKBOX_SIZE, CHECKBOX_SIZE, 64, 64);
            guiGraphics.drawString(this.font,
                Component.translatable(lettersUppercase ? "gui.frequency.capital" : "gui.frequency.small").getString(),
                labelX + SLOT_SIZE, checkboxY + 1, 0xF8F8EC, true);
        }

        for (CategoryRow row : categoryRows) {
            if (row.items.isEmpty()) continue;

            int startX = getRowStartX(row);

            for (int i = 0; i < row.items.size(); i++) {
                int x = startX + i * (BUTTON_SIZE + SPACING);

                DynamicGuiRenderer.renderSlot(guiGraphics, x, row.y, atlas);

                ItemStack stack = row.items.get(i);
                if (!glowingSymbolStack.isEmpty() && ItemStack.isSameItem(stack, this.currentSymbol)) {
                    guiGraphics.renderItem(glowingSymbolStack, x + ITEM_X_OFFSET, row.y + ITEM_Y_OFFSET);
                } else {
                    guiGraphics.renderItem(stack, x + ITEM_X_OFFSET, row.y + ITEM_Y_OFFSET);
                }
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        for (CategoryRow row : categoryRows) {
            if (row.items.isEmpty()) continue;

            int startX = getRowStartX(row);

            for (int i = 0; i < row.items.size(); i++) {
                int x = startX + i * (BUTTON_SIZE + SPACING);

                if (mouseX >= x && mouseX < x + BUTTON_SIZE && mouseY >= row.y && mouseY < row.y + BUTTON_SIZE) {

                    DynamicGuiRenderer.renderSlotHover(guiGraphics, x, row.y, atlas);

                    guiGraphics.renderTooltip(this.font, row.items.get(i), mouseX, mouseY);
                }
            }
        }
    }
}
