package maze.frequency;

import com.tterrag.registrate.Registrate;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

import maze.frequency.init.FrequencyModBlocks;
import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModTabs;
import maze.frequency.init.FrequencyModMenus;
import maze.frequency.network.SymbolSwapPacket;
import maze.frequency.network.FrameUpdatePacket;
import maze.frequency.datagen.DataGenerators;
import maze.frequency.compat.FrameInteractionHandler;
import maze.frequency.compat.CreateTooltipCompat;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.BakedModel;

import net.neoforged.neoforge.client.event.ModelEvent.ModifyBakingResult;
import net.neoforged.bus.api.SubscribeEvent;

import maze.frequency.client.model.SymbolFrameBakedModel;

@Mod("frequency")
public class FrequencyMod {
	public static final String MODID = "frequency";
	public static final Registrate REGISTRATE = Registrate.create(MODID);

	public FrequencyMod(IEventBus modEventBus) {
		modEventBus.addListener(this::registerNetworking);
		modEventBus.addListener(DataGenerators::gatherData);
		addNetworkMessage(SymbolSwapPacket.TYPE, SymbolSwapPacket.STREAM_CODEC, SymbolSwapPacket::handle);
		addNetworkMessage(FrameUpdatePacket.TYPE, FrameUpdatePacket.STREAM_CODEC, FrameUpdatePacket::handle);

		FrequencyModItems.ITEMS.register(modEventBus);
		FrequencyModTabs.TABS.register(modEventBus);
		FrequencyModBlocks.BLOCKS.register(modEventBus);
		FrequencyModBlocks.BLOCK_ENTITIES.register(modEventBus);
		FrequencyModBlocks.ITEMS.register(modEventBus);

		// Force-load all registration classes before GatherDataEvent fires.
		try {
			Class.forName("maze.frequency.init.FrequencyModTabs");
			Class.forName("maze.frequency.init.FrequencyModItems");
			Class.forName("maze.frequency.init.FrequencyModMenus");
			Class.forName("maze.frequency.init.FrequencyModBlocks");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to force-load registration classes", e);
		}

		// Register game event handlers (Create wrench, symbol frame interaction)
		FrameInteractionHandler.register();
		// Hook into Create's tooltip system after all registries are processed
		modEventBus.addListener((FMLClientSetupEvent event) -> CreateTooltipCompat.init());
	}

	private static boolean networkingRegistered = false;
	private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> MESSAGES = new HashMap<>();

	private record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
	}

	public static <T extends CustomPacketPayload> void addNetworkMessage(CustomPacketPayload.Type<T> id, StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
		if (networkingRegistered)
			throw new IllegalStateException("Cannot register new network messages after networking has been registered");
		MESSAGES.put(id, new NetworkMessage<>(reader, handler));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void registerNetworking(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(MODID);
		MESSAGES.forEach((id, networkMessage) -> registrar.playBidirectional(id, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler()));
		networkingRegistered = true;
	}

	@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
	public static class ClientEvents {
		@SubscribeEvent
		public static void onModifyBakingResult(ModifyBakingResult event) {
			var models = event.getModels();
			var toWrap = new ArrayList<Entry<ModelResourceLocation, BakedModel>>();

			for (var entry : models.entrySet()) {
				if (entry.getKey().id().getNamespace().equals(MODID) && entry.getKey().id().getPath().equals("symbol_frame")) {
					toWrap.add(entry);
				}
			}

			for (var entry : toWrap) {
				SymbolFrameBakedModel wrapped = new SymbolFrameBakedModel(entry.getValue());
				models.put(entry.getKey(), wrapped);
			}
		}
	}
}
