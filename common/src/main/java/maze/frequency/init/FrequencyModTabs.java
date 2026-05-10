package maze.frequency.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import java.util.function.Supplier;

public class FrequencyModTabs {
	public static Supplier<CreativeModeTab> FREQUENCY_CREATE_TAB;

	public static void register(TabRegistrar registrar) {
		FREQUENCY_CREATE_TAB = registrar.register("frequency_create_tab", builder ->
			builder.title(Component.translatable("item_group.frequency.frequency_create_tab"))
				.icon(() -> new ItemStack(FrequencyModItems.SYMBOL_C.get()))
				.displayItems((parameters, tabData) -> {
					tabData.accept(FrequencyModItems.SYMBOL_1.get());
					tabData.accept(FrequencyModItems.SYMBOL_2.get());
					tabData.accept(FrequencyModItems.SYMBOL_3.get());
					tabData.accept(FrequencyModItems.SYMBOL_4.get());
					tabData.accept(FrequencyModItems.SYMBOL_5.get());
					tabData.accept(FrequencyModItems.SYMBOL_6.get());
					tabData.accept(FrequencyModItems.SYMBOL_7.get());
					tabData.accept(FrequencyModItems.SYMBOL_8.get());
					tabData.accept(FrequencyModItems.SYMBOL_9.get());
					tabData.accept(FrequencyModItems.SYMBOL_0.get());
					tabData.accept(FrequencyModItems.SYMBOL_A.get());
					tabData.accept(FrequencyModItems.SYMBOL_B.get());
					tabData.accept(FrequencyModItems.SYMBOL_C.get());
					tabData.accept(FrequencyModItems.SYMBOL_D.get());
					tabData.accept(FrequencyModItems.SYMBOL_E.get());
					tabData.accept(FrequencyModItems.SYMBOL_F.get());
					tabData.accept(FrequencyModItems.SYMBOL_G.get());
					tabData.accept(FrequencyModItems.SYMBOL_H.get());
					tabData.accept(FrequencyModItems.SYMBOL_I.get());
					tabData.accept(FrequencyModItems.SYMBOL_J.get());
					tabData.accept(FrequencyModItems.SYMBOL_K.get());
					tabData.accept(FrequencyModItems.SYMBOL_L.get());
					tabData.accept(FrequencyModItems.SYMBOL_M.get());
					tabData.accept(FrequencyModItems.SYMBOL_N.get());
					tabData.accept(FrequencyModItems.SYMBOL_O.get());
					tabData.accept(FrequencyModItems.SYMBOL_P.get());
					tabData.accept(FrequencyModItems.SYMBOL_Q.get());
					tabData.accept(FrequencyModItems.SYMBOL_R.get());
					tabData.accept(FrequencyModItems.SYMBOL_S.get());
					tabData.accept(FrequencyModItems.SYMBOL_T.get());
					tabData.accept(FrequencyModItems.SYMBOL_U.get());
					tabData.accept(FrequencyModItems.SYMBOL_V.get());
					tabData.accept(FrequencyModItems.SYMBOL_W.get());
					tabData.accept(FrequencyModItems.SYMBOL_X.get());
					tabData.accept(FrequencyModItems.SYMBOL_Y.get());
					tabData.accept(FrequencyModItems.SYMBOL_Z.get());
					tabData.accept(FrequencyModItems.SYMBOL_UP_ARROW.get());
					tabData.accept(FrequencyModItems.SYMBOL_DOWN_ARROW.get());
					tabData.accept(FrequencyModItems.SYMBOL_LEFT_ARROW.get());
					tabData.accept(FrequencyModItems.SYMBOL_RIGHT_ARROW.get());
					tabData.accept(FrequencyModItems.SYMBOL_DOUBLE_ARROW_UP.get());
					tabData.accept(FrequencyModItems.SYMBOL_DOUBLE_ARROW_DOWN.get());
					tabData.accept(FrequencyModItems.SYMBOL_DOUBLE_ARROW_LEFT.get());
					tabData.accept(FrequencyModItems.SYMBOL_DOUBLE_ARROW_RIGHT.get());
					tabData.accept(FrequencyModItems.SYMBOL_EMPTY.get());
				})
		);
	}

	public interface TabRegistrar {
		Supplier<CreativeModeTab> register(String name, java.util.function.Function<CreativeModeTab.Builder, CreativeModeTab.Builder> builder);
	}
}
