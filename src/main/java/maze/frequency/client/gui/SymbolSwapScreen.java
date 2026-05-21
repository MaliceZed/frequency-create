package maze.frequency.client.gui;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.registries.BuiltInRegistries;
import maze.frequency.world.inventory.SymbolSwapMenu;
import maze.frequency.network.SymbolSwapPacket;

import java.util.List;
import java.util.ArrayList;

public class SymbolSwapScreen extends AbstractContainerScreen<SymbolSwapMenu> {
	private static final int BUTTON_SIZE = 20;
	private static final int SPACING = 2;
	private static final int MAX_ITEMS_PER_ROW = 10;
	private static final String[] CATEGORY_KEYS = {
		"gui.frequency.category.digits",
		"gui.frequency.category.letters",
		"gui.frequency.category.symbols"
	};
	private static final boolean[] collapsed = new boolean[CATEGORY_KEYS.length];
	static {
		collapsed[2] = true; // symbols collapsed by default
	}
	private static final int CHECKBOX_SIZE = 8;
	private static final int CHECKBOX_ROW_HEIGHT = 14;
	private static final int CONTENT_PADDING = 8;
	private static boolean lettersUppercase = true;
	private int digitRows;
	private int letterRows;
	private int specialRows;
	private int checkboxY;
	private int[] categoryLabelY = new int[CATEGORY_KEYS.length];
	private List<CategoryRow> categoryRows;
	private List<net.minecraft.world.item.ItemStack> allDigits;
	private List<net.minecraft.world.item.ItemStack> allUppercase;
	private List<net.minecraft.world.item.ItemStack> allLowercase;
	private List<net.minecraft.world.item.ItemStack> allSpecials;
	private List<net.minecraft.world.item.ItemStack> allLetters;

	private static class CategoryRow {
		List<net.minecraft.world.item.ItemStack> items = new ArrayList<>();
		int y;
	}

	public SymbolSwapScreen(SymbolSwapMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
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

		for (net.minecraft.world.item.ItemStack stack : this.menu.getAvailableSymbols()) {
			String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
			if (id.matches("symbol_[0-9]")) {
				allDigits.add(stack);
			} else if (id.matches("symbol_[a-z]")) {
				allUppercase.add(stack);
			} else if (id.matches("symbol_[a-z]_small")) {
				allLowercase.add(stack);
			} else {
				allSpecials.add(stack);
			}
		}

		this.allLetters = lettersUppercase ? allUppercase : allLowercase;
		digitRows = calcRows(allDigits.size());
		letterRows = calcRows(allLetters.size());
		specialRows = calcRows(allSpecials.size());

		int fixedContentWidth = MAX_ITEMS_PER_ROW * (BUTTON_SIZE + SPACING) - SPACING + 28;
		this.imageWidth = DynamicGuiRenderer.calculateWidth(fixedContentWidth);
		this.leftPos = (this.width - this.imageWidth) / 2;
		rebuildLayout();
	}

	private void splitRows(List<net.minecraft.world.item.ItemStack> items, List<CategoryRow> rows) {
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
		int[] rowsPerCat = {digitRows, letterRows, specialRows};

		if (!collapsed[0]) splitRows(allDigits, categoryRows);
		if (!collapsed[1]) splitRows(allLetters, categoryRows);
		if (!collapsed[2]) splitRows(allSpecials, categoryRows);

		int categorySpacing = 4;
		int rowSpacing = 22;
		int labelOffset = 12;

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

		this.titleLabelX = this.leftPos + 9;
		this.titleLabelY = this.topPos + 4;


	}

	private String categoryLabel(int cat) {
		return Component.translatable(CATEGORY_KEYS[cat]).getString();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			int labelX = this.leftPos + 9;

			for (int cat = 0; cat < CATEGORY_KEYS.length; cat++) {
				String label = categoryLabel(cat);
				int textWidth = this.font.width(label);
				if (mouseX >= labelX - 4 && mouseX < labelX + 8 + textWidth + 4
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

		DynamicGuiRenderer.renderGui(guiGraphics, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);

		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x582424, false);

		int labelX = this.leftPos + 9;
		int iconYoff = (this.font.lineHeight - 6) / 2;

		for (int cat = 0; cat < CATEGORY_KEYS.length; cat++) {
			int iconU = collapsed[cat] ? 46 : 51;
			int iconW = collapsed[cat] ? 5 : 6;
			int iconX = collapsed[cat] ? labelX + 1 : labelX;
			guiGraphics.blit(DynamicGuiRenderer.ATLAS, iconX, categoryLabelY[cat] + iconYoff, iconU, 0, iconW, 6, 64, 64);
			guiGraphics.drawString(this.font, categoryLabel(cat), labelX + 8, categoryLabelY[cat], 0xF8F8EC, true);
		}

		if (!collapsed[1]) {
			int cbU = lettersUppercase ? 54 : 46;
			guiGraphics.blit(DynamicGuiRenderer.ATLAS, labelX + 10, checkboxY + 1, cbU, 8, CHECKBOX_SIZE, CHECKBOX_SIZE, 64, 64);
			guiGraphics.drawString(this.font,
				Component.translatable(lettersUppercase ? "gui.frequency.capital" : "gui.frequency.small").getString(),
				labelX + 22, checkboxY + 1, 0xF8F8EC, true);
		}

		for (CategoryRow row : categoryRows) {
			if (row.items.isEmpty()) continue;

			int startX = getRowStartX(row);

			for (int i = 0; i < row.items.size(); i++) {
				int x = startX + i * (BUTTON_SIZE + SPACING);

				DynamicGuiRenderer.renderSlot(guiGraphics, x, row.y);

				guiGraphics.renderItem(row.items.get(i), x + 2, row.y + 2);
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

					DynamicGuiRenderer.renderSlotHover(guiGraphics, x, row.y);

					guiGraphics.renderTooltip(this.font, row.items.get(i), mouseX, mouseY);
				}
			}
		}
	}
}
