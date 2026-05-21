package maze.frequency.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class DynamicGuiRenderer {
	private static final int MIN_WIDTH = 190;

	private static final int CORNER_WIDTH = 3;
	static final int HEADER_HEIGHT = 16;
	private static final int BOTTOM_HEIGHT = 2;
	private static final int EDGE_WIDTH = 3;
	private static final int EDGE_HEIGHT = 16;
	private static final int HEADER_CENTER_WIDTH = 16;
	private static final int HEADER_CENTER_HEIGHT = 16;
	private static final int BOTTOM_CENTER_WIDTH = 16;
	private static final int BOTTOM_CENTER_HEIGHT = 2;
	private static final int BACKGROUND_TILE_SIZE = 16;
	private static final int SLOT_SIZE = 20;

	static final ResourceLocation ATLAS = ResourceLocation.fromNamespaceAndPath("frequency", "textures/gui/symbol_swap.png");
	private static final int ATLAS_WIDTH = 64;
	private static final int ATLAS_HEIGHT = 64;

	private static final int CORNER_TOP_LEFT_U = 0;
	private static final int CORNER_TOP_LEFT_V = 0;
	private static final int HEADER_CENTER_U = 4;
	private static final int HEADER_CENTER_V = 0;
	private static final int CORNER_TOP_RIGHT_U = 21;
	private static final int CORNER_TOP_RIGHT_V = 0;
	private static final int SLOT_U = 25;
	private static final int SLOT_V = 0;

	private static final int EDGE_LEFT_U = 0;
	private static final int EDGE_LEFT_V = 17;
	private static final int BACKGROUND_U = 4;
	private static final int BACKGROUND_V = 17;
	private static final int EDGE_RIGHT_U = 21;
	private static final int EDGE_RIGHT_V = 17;
	private static final int SLOT_HOVER_U = 25;
	private static final int SLOT_HOVER_V = 21;

	private static final int CORNER_BOTTOM_LEFT_U = 0;
	private static final int CORNER_BOTTOM_LEFT_V = 34;
	private static final int BOTTOM_CENTER_U = 4;
	private static final int BOTTOM_CENTER_V = 34;
	private static final int CORNER_BOTTOM_RIGHT_U = 21;
	private static final int CORNER_BOTTOM_RIGHT_V = 34;

	public static int calculateWidth(int contentWidth) {
		int totalWidth = CORNER_WIDTH * 2 + contentWidth;
		return Math.max(totalWidth, MIN_WIDTH);
	}

	public static int calculateHeight(int contentHeight) {
		return HEADER_HEIGHT + contentHeight + BOTTOM_HEIGHT;
	}

	public static void renderGui(GuiGraphics guiGraphics, int x, int y, int width, int height) {
		int contentWidth = width - CORNER_WIDTH * 2;
		int contentHeight = height - HEADER_HEIGHT - BOTTOM_HEIGHT;

		renderHeader(guiGraphics, x, y, width);

		renderContent(guiGraphics, x, y + HEADER_HEIGHT, width, contentHeight);

		renderBottom(guiGraphics, x, y + HEADER_HEIGHT + contentHeight, width);
	}

	private static void renderHeader(GuiGraphics guiGraphics, int x, int y, int width) {

		guiGraphics.blit(ATLAS, x, y, CORNER_TOP_LEFT_U, CORNER_TOP_LEFT_V, CORNER_WIDTH, HEADER_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);

		int centerWidth = width - CORNER_WIDTH * 2;
		int centerX = x + CORNER_WIDTH;
		blitStretched(guiGraphics, ATLAS, centerX, y, centerWidth, HEADER_HEIGHT, HEADER_CENTER_U, HEADER_CENTER_V, HEADER_CENTER_WIDTH, HEADER_CENTER_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);

		guiGraphics.blit(ATLAS, x + width - CORNER_WIDTH, y, CORNER_TOP_RIGHT_U, CORNER_TOP_RIGHT_V, CORNER_WIDTH, HEADER_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);
	}

	private static void renderContent(GuiGraphics guiGraphics, int x, int y, int width, int height) {

		blitStretched(guiGraphics, ATLAS, x, y, EDGE_WIDTH, height, EDGE_LEFT_U, EDGE_LEFT_V, EDGE_WIDTH, EDGE_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);

		blitStretched(guiGraphics, ATLAS, x + width - EDGE_WIDTH, y, EDGE_WIDTH, height, EDGE_RIGHT_U, EDGE_RIGHT_V, EDGE_WIDTH, EDGE_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);

		int contentWidth = width - EDGE_WIDTH * 2;
		int contentX = x + EDGE_WIDTH;
		for (int j = 0; j < height; j += BACKGROUND_TILE_SIZE) {
			int drawHeight = Math.min(BACKGROUND_TILE_SIZE, height - j);
			for (int i = 0; i < contentWidth; i += BACKGROUND_TILE_SIZE) {
				int drawWidth = Math.min(BACKGROUND_TILE_SIZE, contentWidth - i);
				guiGraphics.blit(ATLAS, contentX + i, y + j, BACKGROUND_U, BACKGROUND_V, drawWidth, drawHeight, ATLAS_WIDTH, ATLAS_HEIGHT);
			}
		}
	}

	private static void renderBottom(GuiGraphics guiGraphics, int x, int y, int width) {

		guiGraphics.blit(ATLAS, x, y, CORNER_BOTTOM_LEFT_U, CORNER_BOTTOM_LEFT_V, CORNER_WIDTH, BOTTOM_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);

		int centerWidth = width - CORNER_WIDTH * 2;
		int centerX = x + CORNER_WIDTH;
		blitStretched(guiGraphics, ATLAS, centerX, y, centerWidth, BOTTOM_HEIGHT, BOTTOM_CENTER_U, BOTTOM_CENTER_V, BOTTOM_CENTER_WIDTH, BOTTOM_CENTER_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);

		guiGraphics.blit(ATLAS, x + width - CORNER_WIDTH, y, CORNER_BOTTOM_RIGHT_U, CORNER_BOTTOM_RIGHT_V, CORNER_WIDTH, BOTTOM_HEIGHT, ATLAS_WIDTH, ATLAS_HEIGHT);
	}

	private static void blitStretched(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int atlasWidth, int atlasHeight) {
		guiGraphics.blit(texture, x, y, width, height, u, v, textureWidth, textureHeight, atlasWidth, atlasHeight);
	}

	public static void renderSlot(GuiGraphics guiGraphics, int x, int y) {
		guiGraphics.blit(ATLAS, x, y, SLOT_U, SLOT_V, SLOT_SIZE, SLOT_SIZE, ATLAS_WIDTH, ATLAS_HEIGHT);
	}

	public static void renderSlotHover(GuiGraphics guiGraphics, int x, int y) {
		guiGraphics.blit(ATLAS, x, y, SLOT_HOVER_U, SLOT_HOVER_V, SLOT_SIZE, SLOT_SIZE, ATLAS_WIDTH, ATLAS_HEIGHT);
	}
}
