package maze.frequency.world.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;

import maze.frequency.init.FrequencyModBlockEntities;
import maze.frequency.block.LogicCombinatorBlockEntity;

public class LogicCombinatorMenu extends AbstractContainerMenu {
    private final BlockPos blockPos;

    // Server constructor (for opening via openMenu)
    public LogicCombinatorMenu(MenuType<?> type, int id, Inventory playerInventory, BlockPos blockPos) {
        super(type, id);
        this.blockPos = blockPos;
    }

    // Network constructor (client-side — reads BlockPos from buffer)
    public LogicCombinatorMenu(MenuType<?> type, int id, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(type, id, playerInventory, buf.readBlockPos());
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
