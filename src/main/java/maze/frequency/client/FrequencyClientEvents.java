package maze.frequency.client;

import maze.frequency.FrequencyMod;
import maze.frequency.client.model.FluidSymbolBakedModel;
import maze.frequency.client.model.SymbolFrameBakedModel;
import maze.frequency.compat.FrameInteractionHandler;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent.ModifyBakingResult;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

import maze.frequency.data.component.FluidData;
import maze.frequency.init.FrequencyModItems;

import java.util.ArrayList;
import java.util.Map.Entry;

@EventBusSubscriber(modid = FrequencyMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FrequencyClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        SymbolFrameClientHelper.init();
        FrameInteractionHandler.setDirtyHandler(ClientSectionDirtyHandler.INSTANCE);
    }

    @SubscribeEvent
    public static void onModelModify(ModifyBakingResult event) {
        var models = event.getModels();
        var toWrap = new ArrayList<Entry<ModelResourceLocation, BakedModel>>();

        for (var entry : models.entrySet()) {
            var id = entry.getKey().id();
            if (!id.getNamespace().equals(FrequencyMod.MODID))
                continue;
            if (id.getPath().equals("symbol_frame")) {
                toWrap.add(entry);
            }
        }

        var toWrapFluid = new ArrayList<Entry<ModelResourceLocation, BakedModel>>();
        for (var entry : models.entrySet()) {
            var id = entry.getKey().id();
            if (!id.getNamespace().equals(FrequencyMod.MODID))
                continue;
            if (id.getPath().equals("brass_symbol_liquid") ||
                id.getPath().equals("andesite_symbol_liquid") ||
                id.getPath().equals("copper_symbol_liquid")) {
                toWrapFluid.add(entry);
            }
        }

        for (var entry : toWrap) {
            BakedModel wrapped = new SymbolFrameBakedModel(entry.getValue());
            models.put(entry.getKey(), wrapped);
        }

        for (var entry : toWrapFluid) {
            BakedModel wrapped = new FluidSymbolBakedModel(entry.getValue());
            models.put(entry.getKey(), wrapped);
        }
    }

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                var fluidData = stack.get(FluidData.TYPE);
                if (fluidData != null) {
                    var fluid = net.minecraft.core.registries.BuiltInRegistries.FLUID.get(fluidData.fluid());
                    if (fluid != null) {
                        var ext = IClientFluidTypeExtensions.of(fluid);
                        return ext.getTintColor();
                    }
                }
                return 0xFFFFFF;
            }
            return 0xFFFFFF;
        }, FrequencyModItems.BRASS_SYMBOL_LIQUID.get(), FrequencyModItems.ANDESITE_SYMBOL_LIQUID.get(), FrequencyModItems.COPPER_SYMBOL_LIQUID.get());
    }
}
