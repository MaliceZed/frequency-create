package maze.frequency.init;

import net.minecraft.world.item.Item;
import maze.frequency.item.*;
import java.util.function.Supplier;

public class FrequencyModItems {
	public static Supplier<Item> SYMBOL_EMPTY;
	public static Supplier<Item> SYMBOL_0;
	public static Supplier<Item> SYMBOL_1;
	public static Supplier<Item> SYMBOL_2;
	public static Supplier<Item> SYMBOL_3;
	public static Supplier<Item> SYMBOL_4;
	public static Supplier<Item> SYMBOL_5;
	public static Supplier<Item> SYMBOL_6;
	public static Supplier<Item> SYMBOL_7;
	public static Supplier<Item> SYMBOL_8;
	public static Supplier<Item> SYMBOL_9;
	public static Supplier<Item> SYMBOL_A;
	public static Supplier<Item> SYMBOL_B;
	public static Supplier<Item> SYMBOL_C;
	public static Supplier<Item> SYMBOL_D;
	public static Supplier<Item> SYMBOL_E;
	public static Supplier<Item> SYMBOL_F;
	public static Supplier<Item> SYMBOL_G;
	public static Supplier<Item> SYMBOL_H;
	public static Supplier<Item> SYMBOL_I;
	public static Supplier<Item> SYMBOL_J;
	public static Supplier<Item> SYMBOL_K;
	public static Supplier<Item> SYMBOL_L;
	public static Supplier<Item> SYMBOL_M;
	public static Supplier<Item> SYMBOL_N;
	public static Supplier<Item> SYMBOL_O;
	public static Supplier<Item> SYMBOL_P;
	public static Supplier<Item> SYMBOL_Q;
	public static Supplier<Item> SYMBOL_R;
	public static Supplier<Item> SYMBOL_S;
	public static Supplier<Item> SYMBOL_T;
	public static Supplier<Item> SYMBOL_U;
	public static Supplier<Item> SYMBOL_V;
	public static Supplier<Item> SYMBOL_W;
	public static Supplier<Item> SYMBOL_X;
	public static Supplier<Item> SYMBOL_Y;
	public static Supplier<Item> SYMBOL_Z;
	public static Supplier<Item> SYMBOL_UP_ARROW;
	public static Supplier<Item> SYMBOL_DOWN_ARROW;
	public static Supplier<Item> SYMBOL_LEFT_ARROW;
	public static Supplier<Item> SYMBOL_RIGHT_ARROW;
	public static Supplier<Item> SYMBOL_DOUBLE_ARROW_UP;
	public static Supplier<Item> SYMBOL_DOUBLE_ARROW_DOWN;
	public static Supplier<Item> SYMBOL_DOUBLE_ARROW_LEFT;
	public static Supplier<Item> SYMBOL_DOUBLE_ARROW_RIGHT;
	public static Supplier<Item> INCOMPLETE_SYMBOL;

	public static void register(ItemRegistrar registrar) {
		SYMBOL_EMPTY = registrar.register("symbol_empty", SymbolEmptyItem::new);
		SYMBOL_0 = registrar.register("symbol_0", Symbol0Item::new);
		SYMBOL_1 = registrar.register("symbol_1", Symbol1Item::new);
		SYMBOL_2 = registrar.register("symbol_2", Symbol2Item::new);
		SYMBOL_3 = registrar.register("symbol_3", Symbol3Item::new);
		SYMBOL_4 = registrar.register("symbol_4", Symbol4Item::new);
		SYMBOL_5 = registrar.register("symbol_5", Symbol5Item::new);
		SYMBOL_6 = registrar.register("symbol_6", Symbol6Item::new);
		SYMBOL_7 = registrar.register("symbol_7", Symbol7Item::new);
		SYMBOL_8 = registrar.register("symbol_8", Symbol8Item::new);
		SYMBOL_9 = registrar.register("symbol_9", Symbol9Item::new);
		SYMBOL_A = registrar.register("symbol_a", SymbolAItem::new);
		SYMBOL_B = registrar.register("symbol_b", SymbolBItem::new);
		SYMBOL_C = registrar.register("symbol_c", SymbolCItem::new);
		SYMBOL_D = registrar.register("symbol_d", SymbolDItem::new);
		SYMBOL_E = registrar.register("symbol_e", SymbolEItem::new);
		SYMBOL_F = registrar.register("symbol_f", SymbolFItem::new);
		SYMBOL_G = registrar.register("symbol_g", SymbolGItem::new);
		SYMBOL_H = registrar.register("symbol_h", SymbolHItem::new);
		SYMBOL_I = registrar.register("symbol_i", SymbolIItem::new);
		SYMBOL_J = registrar.register("symbol_j", SymbolJItem::new);
		SYMBOL_K = registrar.register("symbol_k", SymbolKItem::new);
		SYMBOL_L = registrar.register("symbol_l", SymbolLItem::new);
		SYMBOL_M = registrar.register("symbol_m", SymbolMItem::new);
		SYMBOL_N = registrar.register("symbol_n", SymbolNItem::new);
		SYMBOL_O = registrar.register("symbol_o", SymbolOItem::new);
		SYMBOL_P = registrar.register("symbol_p", SymbolPItem::new);
		SYMBOL_Q = registrar.register("symbol_q", SymbolQItem::new);
		SYMBOL_R = registrar.register("symbol_r", SymbolRItem::new);
		SYMBOL_S = registrar.register("symbol_s", SymbolSItem::new);
		SYMBOL_T = registrar.register("symbol_t", SymbolTItem::new);
		SYMBOL_U = registrar.register("symbol_u", SymbolUItem::new);
		SYMBOL_V = registrar.register("symbol_v", SymbolVItem::new);
		SYMBOL_W = registrar.register("symbol_w", SymbolWItem::new);
		SYMBOL_X = registrar.register("symbol_x", SymbolXItem::new);
		SYMBOL_Y = registrar.register("symbol_y", SymbolYItem::new);
		SYMBOL_Z = registrar.register("symbol_z", SymbolZItem::new);
		SYMBOL_UP_ARROW = registrar.register("symbol_up_arrow", SymbolUpArrowItem::new);
		SYMBOL_DOWN_ARROW = registrar.register("symbol_down_arrow", SymbolDownArrowItem::new);
		SYMBOL_LEFT_ARROW = registrar.register("symbol_left_arrow", SymbolLeftArrowItem::new);
		SYMBOL_RIGHT_ARROW = registrar.register("symbol_right_arrow", SymbolRightArrowItem::new);
		SYMBOL_DOUBLE_ARROW_UP = registrar.register("symbol_darrow_up", SymbolDoubleArrowUpItem::new);
		SYMBOL_DOUBLE_ARROW_DOWN = registrar.register("symbol_darrow_down", SymbolDoubleArrowDownItem::new);
		SYMBOL_DOUBLE_ARROW_LEFT = registrar.register("symbol_darrow_left", SymbolDoubleArrowLeftItem::new);
		SYMBOL_DOUBLE_ARROW_RIGHT = registrar.register("symbol_darrow_right", SymbolDoubleArrowRightItem::new);
		INCOMPLETE_SYMBOL = registrar.register("incomplete_symbol", IncompleteSymbolItem::new);
	}

	public interface ItemRegistrar {
		<T extends Item> Supplier<T> register(String name, Supplier<T> item);
	}
}
