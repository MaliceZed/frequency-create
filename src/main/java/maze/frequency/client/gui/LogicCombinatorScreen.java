package maze.frequency.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.BlockState;
import maze.frequency.FrequencyMod;
import maze.frequency.block.GateMode;
import maze.frequency.block.LogicCombinatorBlock;
import maze.frequency.block.LogicCombinatorBlockEntity;
import maze.frequency.network.GateModeChangePacket;
import maze.frequency.world.inventory.LogicCombinatorMenu;

public class LogicCombinatorScreen extends AbstractContainerScreen<LogicCombinatorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        FrequencyMod.MODID, "textures/gui/logic_combinator.png");
    private static final int[][] SLOT_POSITIONS = {
        {16, 49},  // Input1 Freq1
        {16, 76},  // Input1 Freq2
        {50, 49},  // Output Freq1
        {50, 76},  // Output Freq2
        {84, 49},  // Input2 Freq1
        {84, 76},  // Input2 Freq2
    };
    private static final int SLOT_SIZE = 16;

    private static final int[][] BUTTON_POSITIONS = {
        {118, 29},  // AND
        {118, 42},  // OR
        {118, 55},  // NOT
        {118, 68},  // XAND
        {118, 81},  // XOR
        {118, 94},  // IMPL
    };
    private static final int BUTTON_WIDTH = 21;
    private static final int BUTTON_HEIGHT = 11;

    private static final int BUTTON_ON_U = 202;
    private static final int BUTTON_OFF_U = 202;
    private static final int BUTTON_ON_HOVER_U = 223;
    private static final int BUTTON_OFF_HOVER_U = 223;
    private static final int BUTTON_ON_V = 0;
    private static final int BUTTON_OFF_V = 11;

    private static final int CONFIRM_X = 151;
    private static final int CONFIRM_Y = 117;
    private static final int CONFIRM_SIZE = 18;

    public LogicCombinatorScreen(LogicCombinatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.translatable("gui.frequency.logic_combinator.title"));
        this.imageWidth = 184;
        this.imageHeight = 141;
    }

    private GateMode getGateMode() {
        if (minecraft == null || minecraft.level == null) return GateMode.AND;
        var blockEntity = minecraft.level.getBlockEntity(menu.getBlockPos());
        if (!(blockEntity instanceof LogicCombinatorBlockEntity lcb)) return GateMode.AND;
        var behaviour = lcb.getLogicBehaviour();
        if (behaviour == null) return GateMode.AND;
        return behaviour.getGateMode();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (minecraft == null || minecraft.level == null) return;
        var blockEntity = minecraft.level.getBlockEntity(menu.getBlockPos());
        if (!(blockEntity instanceof LogicCombinatorBlockEntity lcb)) return;
        var behaviour = lcb.getLogicBehaviour();
        if (behaviour == null) return;

        for (int i = 0; i < 6; i++) {
            var freq = behaviour.getFrequency(i);
            if (!freq.isEmpty()) {
                guiGraphics.renderItem(freq, x + SLOT_POSITIONS[i][0], y + SLOT_POSITIONS[i][1]);
            }
        }

        // Active indicator (read from blockstate — it replicates to client)
        BlockState bs = blockEntity.getBlockState();
        int[][] indicators = {
            {18, 65},   // Input1
            {52, 65},   // Output
            {86, 65},   // Input2
        };
        boolean input1Active = bs.getValue(LogicCombinatorBlock.INPUT1);
        boolean outputActive = bs.getValue(LogicCombinatorBlock.OUTPUT);
        boolean input2Active = bs.getValue(LogicCombinatorBlock.INPUT2);
        boolean isSingle = bs.getValue(LogicCombinatorBlock.SINGLE);

        // Input1
        if (input1Active) {
            guiGraphics.blit(TEXTURE, x + indicators[0][0], y + indicators[0][1], 0, 141, 12, 11, 256, 256);
        }
        // Output
        if (outputActive) {
            guiGraphics.blit(TEXTURE, x + indicators[1][0], y + indicators[1][1], 0, 141, 12, 11, 256, 256);
        }
        // Input2: drawn when active OR when SINGLE=true
        if (input2Active) {
            int u = isSingle ? 12 : 0;
            guiGraphics.blit(TEXTURE, x + indicators[2][0], y + indicators[2][1], u, 141, 12, 11, 256, 256);
        } else if (isSingle) {
            guiGraphics.blit(TEXTURE, x + indicators[2][0], y + indicators[2][1], 12, 141, 12, 11, 256, 256);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("gui.frequency.logic_combinator.title"), 9, 4, 0x582424, false);

        var modeText = Component.translatable("gui.frequency.logic_combinator.mode");
        int textWidth = this.font.width(modeText);
        int x = 116 + (55 - textWidth) / 2;
        guiGraphics.drawString(this.font, modeText, x, 20, 0xe2e2e2, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int guiX = (width - imageWidth) / 2;
        int guiY = (height - imageHeight) / 2;
        GateMode currentMode = getGateMode();

        // Mode buttons — always drawn
        for (int i = 0; i < 6; i++) {
            int bx = guiX + BUTTON_POSITIONS[i][0];
            int by = guiY + BUTTON_POSITIONS[i][1];

            boolean hovered = mouseX >= bx && mouseX < bx + BUTTON_WIDTH && mouseY >= by && mouseY < by + BUTTON_HEIGHT;
            boolean selected = (i == currentMode.ordinal());

            int u;
            int v;
            if (selected && hovered) {
                u = BUTTON_ON_HOVER_U;
                v = BUTTON_ON_V;
            } else if (selected) {
                u = BUTTON_ON_U;
                v = BUTTON_ON_V;
            } else if (hovered) {
                u = BUTTON_OFF_HOVER_U;
                v = BUTTON_OFF_V;
            } else {
                u = BUTTON_OFF_U;
                v = BUTTON_OFF_V;
            }
            guiGraphics.blit(TEXTURE, bx, by, u, v, BUTTON_WIDTH, BUTTON_HEIGHT, 256, 256);
        }

        // Confirm button hover sprite
        int cx = guiX + CONFIRM_X;
        int cy = guiY + CONFIRM_Y;
        if (mouseX >= cx && mouseX < cx + CONFIRM_SIZE && mouseY >= cy && mouseY < cy + CONFIRM_SIZE) {
            guiGraphics.blit(TEXTURE, cx, cy, 184, 0, CONFIRM_SIZE, CONFIRM_SIZE, 256, 256);
        }

        // Hover highlights and frequency slot tooltips
        if (minecraft == null || minecraft.level == null) return;
        var blockEntity = minecraft.level.getBlockEntity(menu.getBlockPos());
        if (!(blockEntity instanceof LogicCombinatorBlockEntity lcb)) return;
        var behaviour = lcb.getLogicBehaviour();
        if (behaviour == null) return;

        for (int i = 0; i < 6; i++) {
            int sx = guiX + SLOT_POSITIONS[i][0];
            int sy = guiY + SLOT_POSITIONS[i][1];

            if (mouseX >= sx && mouseX < sx + SLOT_SIZE && mouseY >= sy && mouseY < sy + SLOT_SIZE) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0, 0, 200);
                guiGraphics.fill(sx, sy, sx + SLOT_SIZE, sy + SLOT_SIZE, 0x80FFFFFF);
                guiGraphics.pose().popPose();

                var freq = behaviour.getFrequency(i);
                if (!freq.isEmpty()) {
                    guiGraphics.renderTooltip(this.font, freq, mouseX, mouseY);
                }
            }
        }

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int guiX = (width - imageWidth) / 2;
            int guiY = (height - imageHeight) / 2;

            // Mode button clicks — send packet immediately
            for (int i = 0; i < 6; i++) {
                int bx = guiX + BUTTON_POSITIONS[i][0];
                int by = guiY + BUTTON_POSITIONS[i][1];

                if (mouseX >= bx && mouseX < bx + BUTTON_WIDTH && mouseY >= by && mouseY < by + BUTTON_HEIGHT) {
                    GateModeChangePacket.send(menu.getBlockPos(), i);
                    return true;
                }
            }

            // Confirm button — just closes the GUI
            int cx = guiX + CONFIRM_X;
            int cy = guiY + CONFIRM_Y;
            if (mouseX >= cx && mouseX < cx + CONFIRM_SIZE && mouseY >= cy && mouseY < cy + CONFIRM_SIZE) {
                onClose();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
