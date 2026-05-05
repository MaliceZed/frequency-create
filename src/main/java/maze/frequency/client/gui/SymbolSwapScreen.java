package maze.frequency.client.gui;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import maze.frequency.world.inventory.SymbolSwapMenu;
import maze.frequency.network.SymbolSwapPacket;

import java.util.List;
import java.util.ArrayList;

public class SymbolSwapScreen extends AbstractContainerScreen<SymbolSwapMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("frequency", "textures/gui/symbol_swap.png");
	private List<CategoryRow> categoryRows;

	private static class CategoryRow {
		List<net.minecraft.world.item.ItemStack> items = new ArrayList<>();
		int startIndex;
		int y;
	}

	public SymbolSwapScreen(SymbolSwapMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
		this.imageWidth = 198;
		this.imageHeight = 170;
		this.inventoryLabelY = 10000; // Скрываем надпись "Inventory"
		this.titleLabelY = this.titleLabelY - 2; // Заголовок выше на 2 пикселя (было -3, теперь -3+1=-2)
		this.titleLabelX = this.titleLabelX + 6; // Заголовок правее на 6 пикселей
	}

	@Override
	protected void init() {
		super.init();
		this.categoryRows = new ArrayList<>();

		List<net.minecraft.world.item.ItemStack> symbols = this.menu.getAvailableSymbols();

		// Разделяем символы по категориям
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
			} else if (id.contains("arrow") || id.equals("symbol_empty")) {
				arrowsAndEmpty.items.add(stack);
				if (arrowsAndEmpty.startIndex == 0 && !arrowsAndEmpty.items.isEmpty()) arrowsAndEmpty.startIndex = i;
			}
		}

		// Разделяем цифры: 5 + 5
		for (int i = 0; i < allDigits.size(); i++) {
			if (i < 5) {
				digits1.items.add(allDigits.get(i));
			} else {
				digits2.items.add(allDigits.get(i));
			}
		}

		// Разделяем буквы: 9 + 9 + 8
		for (int i = 0; i < allLetters.size(); i++) {
			if (i < 9) {
				letters1.items.add(allLetters.get(i));
			} else if (i < 18) {
				letters2.items.add(allLetters.get(i));
			} else {
				letters3.items.add(allLetters.get(i));
			}
		}

		// Устанавливаем Y координаты для каждой категории
		int startY = this.topPos + 32; // Увеличено на 2 пикселя (было 30)
		int rowSpacing = 20;
		int categorySpacing = 12; // Уменьшенное расстояние перед новой категорией

		digits1.y = startY;
		digits2.y = startY + rowSpacing;
		letters1.y = startY + rowSpacing * 2 + categorySpacing;
		letters2.y = startY + rowSpacing * 3 + categorySpacing;
		letters3.y = startY + rowSpacing * 4 + categorySpacing;
		arrowsAndEmpty.y = startY + rowSpacing * 5 + categorySpacing * 2;

		categoryRows.add(digits1);
		categoryRows.add(digits2);
		categoryRows.add(letters1);
		categoryRows.add(letters2);
		categoryRows.add(letters3);
		categoryRows.add(arrowsAndEmpty);

		// Обновляем высоту окна
		this.imageHeight = Math.max(100, arrowsAndEmpty.y - this.topPos + 50);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			int buttonSize = 20;
			int globalIndex = 0;

			for (CategoryRow row : categoryRows) {
				if (row.items.isEmpty()) continue;

				int rowWidth = row.items.size() * buttonSize;
				int startX = this.leftPos + (this.imageWidth - rowWidth) / 2 + 2;

				for (int i = 0; i < row.items.size(); i++) {
					int x = startX + i * buttonSize;

					if (mouseX >= x && mouseX < x + 16 && mouseY >= row.y && mouseY < row.y + 16) {
						SymbolSwapPacket.send(globalIndex);
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
		guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x582424, false);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		// Рисуем текстуру фона
		guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

		// Рисуем названия категорий
		int labelX = this.leftPos + 8;
		int labelOffset = 12; // Отступ от надписи до предметов

		// Цифры
		if (!categoryRows.isEmpty() && categoryRows.get(0).items.size() > 0) {
			guiGraphics.drawString(this.font, Component.translatable("gui.frequency.category.digits"), labelX, categoryRows.get(0).y - labelOffset, 0xD2D2D2, false);
		}

		// Буквы
		if (categoryRows.size() > 2 && categoryRows.get(2).items.size() > 0) {
			guiGraphics.drawString(this.font, Component.translatable("gui.frequency.category.letters"), labelX, categoryRows.get(2).y - labelOffset, 0xD2D2D2, false);
		}

		// Символы
		if (categoryRows.size() > 5 && categoryRows.get(5).items.size() > 0) {
			guiGraphics.drawString(this.font, Component.translatable("gui.frequency.category.symbols"), labelX, categoryRows.get(5).y - labelOffset, 0xD2D2D2, false);
		}

		// Рисуем символы
		int buttonSize = 20;
		int globalIndex = 0;

		for (CategoryRow row : categoryRows) {
			if (row.items.isEmpty()) continue;

			int rowWidth = row.items.size() * buttonSize;
			int startX = this.leftPos + (this.imageWidth - rowWidth) / 2 + 2;

			for (int i = 0; i < row.items.size(); i++) {
				int x = startX + i * buttonSize;
				guiGraphics.renderItem(row.items.get(i), x, row.y);
				globalIndex++;
			}
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);

		// Рисуем тултипы
		int buttonSize = 20;
		int globalIndex = 0;

		for (CategoryRow row : categoryRows) {
			if (row.items.isEmpty()) continue;

			int rowWidth = row.items.size() * buttonSize;
			int startX = this.leftPos + (this.imageWidth - rowWidth) / 2 + 2;

			for (int i = 0; i < row.items.size(); i++) {
				int x = startX + i * buttonSize;

				if (mouseX >= x && mouseX < x + 16 && mouseY >= row.y && mouseY < row.y + 16) {
					guiGraphics.renderTooltip(this.font, row.items.get(i), mouseX, mouseY);
				}
				globalIndex++;
			}
		}
	}
}
