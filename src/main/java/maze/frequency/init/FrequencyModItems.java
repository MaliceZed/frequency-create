package maze.frequency.init;

import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import maze.frequency.item.*;
import maze.frequency.FrequencyMod;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class FrequencyModItems {
	public static final DeferredRegister<Item> ITEMS =
		DeferredRegister.create(Registries.ITEM, FrequencyMod.MODID);

	public static final List<String> SYMBOL_NAMES = List.of(
		"symbol_1", "symbol_2", "symbol_3", "symbol_4", "symbol_5",
		"symbol_6", "symbol_7", "symbol_8", "symbol_9", "symbol_0",
		"symbol_a", "symbol_b", "symbol_c", "symbol_d", "symbol_e",
		"symbol_f", "symbol_g", "symbol_h", "symbol_i", "symbol_j",
		"symbol_k", "symbol_l", "symbol_m", "symbol_n", "symbol_o",
		"symbol_p", "symbol_q", "symbol_r", "symbol_s", "symbol_t",
		"symbol_u", "symbol_v", "symbol_w", "symbol_x", "symbol_y",
		"symbol_z",
		"symbol_a_small", "symbol_b_small", "symbol_c_small", "symbol_d_small", "symbol_e_small",
		"symbol_f_small", "symbol_g_small", "symbol_h_small", "symbol_i_small", "symbol_j_small",
		"symbol_k_small", "symbol_l_small", "symbol_m_small", "symbol_n_small", "symbol_o_small",
		"symbol_p_small", "symbol_q_small", "symbol_r_small", "symbol_s_small", "symbol_t_small",
		"symbol_u_small", "symbol_v_small", "symbol_w_small", "symbol_x_small", "symbol_y_small",
		"symbol_z_small",
		"symbol_up_arrow", "symbol_down_arrow", "symbol_left_arrow", "symbol_right_arrow",
		"symbol_darrow_up", "symbol_darrow_down", "symbol_darrow_left", "symbol_darrow_right",
		"symbol_skull",
		"symbol_creeperhead",
		"symbol_empty"
	);

	private static final Map<String, DeferredHolder<Item, ? extends Item>> SYMBOLS = new HashMap<>();
	public static final List<DeferredHolder<Item, ? extends Item>> ALL_SYMBOLS = new ArrayList<>();
	public static final DeferredHolder<Item, IncompleteSymbolItem> INCOMPLETE_SYMBOL;

	static {
		Item.Properties props = new Item.Properties().stacksTo(8);
		for (String name : SYMBOL_NAMES) {
			DeferredHolder<Item, ? extends Item> entry = ITEMS.register(name,
				() -> new BaseSymbolItem(props, name));
			SYMBOLS.put(name, entry);
			ALL_SYMBOLS.add(entry);
		}
		INCOMPLETE_SYMBOL = ITEMS.register("incomplete_symbol",
			() -> new IncompleteSymbolItem(props));
	}

	public static DeferredHolder<Item, ? extends Item> getSymbol(String name) {
		return SYMBOLS.get(name);
	}

	public static String displayChar(String name) {
		String suffix = name.substring("symbol_".length());
		if (suffix.length() == 1) return suffix.toUpperCase();
		if (suffix.matches("[a-z]_small")) {
			return suffix.substring(0, 1);
		}
		return switch (suffix) {
			case "up_arrow" -> "↑";
			case "down_arrow" -> "↓";
			case "left_arrow" -> "←";
			case "right_arrow" -> "→";
			case "darrow_up" -> "⇑";
			case "darrow_down" -> "⇓";
			case "darrow_left" -> "⇐";
			case "darrow_right" -> "⇒";
			case "skull" -> "☠";
			case "creeperhead" -> "Creeper";
			default -> suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
		};
	}
}
