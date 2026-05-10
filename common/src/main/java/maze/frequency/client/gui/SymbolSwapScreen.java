package maze.frequency.client.gui;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.registries.BuiltInRegistries;
import maze.frequency.world.inventory.SymbolSwapMenu;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class SymbolSwapScreen extends AbstractContainerScreen<SymbolSwapMenu> {
	private List<CategoryRow> categoryRows;
	private final Consumer<Integer> packetSender;

	private static class CategoryRow {
		List<net.minecraft.world.item.ItemStack> items = new ArrayList<>();
		int startIndex;
		int y;
	}

	public SymbolSwapScreen(SymbolSwapMenu menu, Inventory playerInventory, Component title, Consumer<Integer> packetSender) {
		super(menu, playerInventory, title);
		this.inventoryLabelY = 10000;
		this.packetSender = packetSender;

	}

	@Override
	protected void init() {
		super.init();
		this.categoryRows = new ArrayList<>();

		List<net.minecraft.world.item.ItemStack> symbols = this.menu.getAvailableSymbols();

		CategoryRow digits1 = new CategoryRow();
		CategoryRow digits2 = new CategoryRow();
		CategoryRow letters1 = new CategoryRow();
		CategoryRow letters2 = new CategoryRow();
		CategoryRow letters3 = new CategoryRow();
		CategoryRow arrowsAndEmpty = new CategoryRow();

		List<net.minecraft.world.item.ItemStack> allDigits = new ArrayList<>();
		List<net.minecraft.world.item.ItemStack> allLetters = new ArrayList<>();

		for (int i = 0; i < symbols.size(); i++) {
			net.minecraft.world.item.ItemStack stack = symbols.get(i);
			String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();

			if (id.matches("symbol_[0-9]")) {
				allDigits.add(stack);
			} else if (id.matches("symbol_[a-z]")) {
				allLetters.add(stack);
			} else if (id.contains("arrow") || id.contains("darrow") || id.equals("symbol_empty")) {
				arrowsAndEmpty.items.add(stack);
				if (arrowsAndEmpty.startIndex == 0 && !arrowsAndEmpty.items.isEmpty()) arrowsAndEmpty.startIndex = i;
			}
		}

		for (int i = 0; i < allDigits.size(); i++) {
			if (i < 5) {
				digits1.items.add(allDigits.get(i));
			} else {
				digits2.items.add(allDigits.get(i));
			}
		}

		for (int i = 0; i < allLetters.size(); i++) {
			if (i < 9) {
				letters1.items.add(allLetters.get(i));
			} else if (i < 18) {
				letters2.items.add(allLetters.get(i));
			} else {
				letters3.items.add(allLetters.get(i));
			}
		}

		categoryRows.add(digits1);
		categoryRows.add(digits2);
		categoryRows.add(letters1);
		categoryRows.add(letters2);
		categoryRows.add(letters3);
		categoryRows.add(arrowsAndEmpty);

		int maxItemsInRow = 0;
		for (CategoryRow row : categoryRows) {
			if (row.items.size() > maxItemsInRow) {
				maxItemsInRow = row.items.size();
			}
		}

		int buttonSize = 20;
		int spacing = 2;
		int contentWidth = maxItemsInRow * (buttonSize + spacing) - spacing + 28;
		int categorySpacing = 4;
		int rowSpacing = 22;
		int labelOffset = 10;

		int contentHeight = 0;
		contentHeight += 8;
		contentHeight += labelOffset;
		contentHeight += rowSpacing * 2;
		contentHeight += categorySpacing;
		contentHeight += labelOffset;
		contentHeight += rowSpacing * 3;
		contentHeight += categorySpacing;
		contentHeight += labelOffset;
		contentHeight += rowSpacing;
		contentHeight += 8;

		this.imageWidth = DynamicGuiRenderer.calculateWidth(contentWidth);
		this.imageHeight = DynamicGuiRenderer.calculateHeight(contentHeight);

		int startY = this.topPos + 16 + 8 + labelOffset;

		digits1.y = startY;
		digits2.y = startY + rowSpacing;
		letters1.y = startY + rowSpacing * 2 + categorySpacing + labelOffset;
		letters2.y = startY + rowSpacing * 3 + categorySpacing + labelOffset;
		letters3.y = startY + rowSpacing * 4 + categorySpacing + labelOffset;
		arrowsAndEmpty.y = startY + rowSpacing * 5 + categorySpacing * 2 + labelOffset * 2;

		this.titleLabelX = this.leftPos + 9;
		this.titleLabelY = this.topPos + 4;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			int buttonSize = 20;
			int spacing = 2;
			int globalIndex = 0;

			for (CategoryRow row : categoryRows) {
				if (row.items.isEmpty()) continue;

				int rowWidth = row.items.size() * (buttonSize + spacing) - spacing;
				int startX = this.leftPos + (this.imageWidth - rowWidth) / 2;

				for (int i = 0; i < row.items.size(); i++) {
					int x = startX + i * (buttonSize + spacing);

					if (mouseX >= x && mouseX < x + 20 && mouseY >= row.y && mouseY < row.y + 20) {
						packetSender.accept(globalIndex);
						this.onClose();
						return true;
					}
					globalIndex++;
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

		DynamicGuiRenderer.renderGui(guiGraphics, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);

		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x582424, false);

		int labelX = this.leftPos + 9;
		int labelOffset = 12;

		if (!categoryRows.isEmpty() && categoryRows.get(0).items.size() > 0) {
			guiGraphics.drawString(this.font, Component.translatable("gui.frequency.category.digits"), labelX, categoryRows.get(0).y - labelOffset, 0xE2E2E2, true);
		}

		if (categoryRows.size() > 2 && categoryRows.get(2).items.size() > 0) {
			guiGraphics.drawString(this.font, Component.translatable("gui.frequency.category.letters"), labelX, categoryRows.get(2).y - labelOffset, 0xE2E2E2, true);
		}

		if (categoryRows.size() > 5 && categoryRows.get(5).items.size() > 0) {
			guiGraphics.drawString(this.font, Component.translatable("gui.frequency.category.symbols"), labelX, categoryRows.get(5).y - labelOffset, 0xE2E2E2, true);
		}

		int buttonSize = 20;
		int spacing = 2;
		int globalIndex = 0;

		for (CategoryRow row : categoryRows) {
			if (row.items.isEmpty()) continue;

			int rowWidth = row.items.size() * (buttonSize + spacing) - spacing;
			int startX = this.leftPos + (this.imageWidth - rowWidth) / 2;

			for (int i = 0; i < row.items.size(); i++) {
				int x = startX + i * (buttonSize + spacing);

				DynamicGuiRenderer.renderSlot(guiGraphics, x, row.y);

				guiGraphics.renderItem(row.items.get(i), x + 2, row.y + 2);
				globalIndex++;
			}
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);

		int buttonSize = 20;
		int spacing = 2;
		int globalIndex = 0;

		for (CategoryRow row : categoryRows) {
			if (row.items.isEmpty()) continue;

			int rowWidth = row.items.size() * (buttonSize + spacing) - spacing;
			int startX = this.leftPos + (this.imageWidth - rowWidth) / 2;

			for (int i = 0; i < row.items.size(); i++) {
				int x = startX + i * (buttonSize + spacing);

				if (mouseX >= x && mouseX < x + 20 && mouseY >= row.y && mouseY < row.y + 20) {

					DynamicGuiRenderer.renderSlotHover(guiGraphics, x, row.y);

					guiGraphics.renderTooltip(this.font, row.items.get(i), mouseX, mouseY);
				}
				globalIndex++;
			}
		}
	}
}
