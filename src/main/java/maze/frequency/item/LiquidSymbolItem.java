package maze.frequency.item;

import maze.frequency.data.component.FluidData;
import maze.frequency.init.FrequencyModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class LiquidSymbolItem extends BaseSymbolItem {

    public LiquidSymbolItem(Properties properties, Supplier<MenuType<?>> menuType, Supplier<List<ItemStack>> symbolLoader) {
        super(properties, "liquid", menuType, symbolLoader);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        // 1. Try to read fluid from a tank (IFluidHandler) — SIMULATE only, don't drain
        BlockEntity be = level.getBlockEntity(clickedPos);
        if (be != null) {
            IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, clickedPos, context.getClickedFace());
            if (fluidHandler != null) {
                FluidStack simulated = fluidHandler.drain(100, IFluidHandler.FluidAction.SIMULATE);
                if (!simulated.isEmpty()) {
                    setFluid(stack, simulated.getFluid());
                    level.playSound(player, clickedPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        // 2. Try to read fluid from world — check clicked pos then adjacent
        Fluid fluid = tryReadWorldFluid(level, clickedPos);
        if (fluid == null) {
            fluid = tryReadWorldFluid(level, clickedPos.relative(context.getClickedFace()));
        }
        if (fluid != null) {
            setFluid(stack, fluid);
            level.playSound(player, clickedPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Nullable
    private static Fluid tryReadWorldFluid(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        FluidState fluidState = state.getFluidState();
        if (!fluidState.isEmpty()) {
            return fluidState.getType();
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        FluidData fluidData = stack.get(FrequencyModComponents.FLUID_DATA.get());
        if (fluidData != null) {
            // Blank line to separate Create tooltip from fluid content
            tooltipComponents.add(Component.empty());

            Fluid fluid = net.minecraft.core.registries.BuiltInRegistries.FLUID.get(fluidData.fluid());
            if (fluid != null) {
                Component fluidName = fluid.defaultFluidState().getFluidType().getDescription();
                tooltipComponents.add(Component.translatable(stack.getDescriptionId() + ".tooltip.contains", fluidName)
                    .withStyle(ChatFormatting.GRAY));
            }


        }
    }

    public static void setFluid(ItemStack stack, Fluid fluid) {
        ResourceLocation fluidId = net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(fluid);
        stack.set(FrequencyModComponents.FLUID_DATA.get(), new FluidData(fluidId));
    }

}
