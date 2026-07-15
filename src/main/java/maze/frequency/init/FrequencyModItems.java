package maze.frequency.init;

import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import maze.frequency.item.AndesiteBaseSymbolItem;
import maze.frequency.item.BrassBaseSymbolItem;
import maze.frequency.item.LiquidSymbolItem;
import maze.frequency.item.CopperBaseSymbolItem;
import maze.frequency.init.FrequencyModMenus;
import maze.frequency.FrequencyMod;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class FrequencyModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(Registries.ITEM, FrequencyMod.MODID);

    public static final List<String> SYMBOL_NAMES = List.of(
        "brass_symbol_1", "brass_symbol_2", "brass_symbol_3", "brass_symbol_4", "brass_symbol_5",
        "brass_symbol_6", "brass_symbol_7", "brass_symbol_8", "brass_symbol_9", "brass_symbol_0",
        "brass_symbol_a", "brass_symbol_b", "brass_symbol_c", "brass_symbol_d", "brass_symbol_e",
        "brass_symbol_f", "brass_symbol_g", "brass_symbol_h", "brass_symbol_i", "brass_symbol_j",
        "brass_symbol_k", "brass_symbol_l", "brass_symbol_m", "brass_symbol_n", "brass_symbol_o",
        "brass_symbol_p", "brass_symbol_q", "brass_symbol_r", "brass_symbol_s", "brass_symbol_t",
        "brass_symbol_u", "brass_symbol_v", "brass_symbol_w", "brass_symbol_x", "brass_symbol_y",
        "brass_symbol_z",
        "brass_symbol_a_small", "brass_symbol_b_small", "brass_symbol_c_small", "brass_symbol_d_small", "brass_symbol_e_small",
        "brass_symbol_f_small", "brass_symbol_g_small", "brass_symbol_h_small", "brass_symbol_i_small", "brass_symbol_j_small",
        "brass_symbol_k_small", "brass_symbol_l_small", "brass_symbol_m_small", "brass_symbol_n_small", "brass_symbol_o_small",
        "brass_symbol_p_small", "brass_symbol_q_small", "brass_symbol_r_small", "brass_symbol_s_small", "brass_symbol_t_small",
        "brass_symbol_u_small", "brass_symbol_v_small", "brass_symbol_w_small", "brass_symbol_x_small", "brass_symbol_y_small",
        "brass_symbol_z_small",
        "brass_symbol_up_arrow", "brass_symbol_down_arrow", "brass_symbol_left_arrow", "brass_symbol_right_arrow",
        "brass_symbol_darrow_up", "brass_symbol_darrow_down", "brass_symbol_darrow_left", "brass_symbol_darrow_right",
        "brass_symbol_skull",
        "brass_symbol_creeperhead",
        "brass_symbol_empty",
        "brass_symbol_math_add",
        "brass_symbol_math_subtract",
        "brass_symbol_math_divide",
        "brass_symbol_math_multiply",
        "brass_symbol_math_equal",
        "brass_symbol_math_percent"
    );

    public static final List<String> ANDESITE_SYMBOL_NAMES = List.of(
        "andesite_symbol_1", "andesite_symbol_2", "andesite_symbol_3", "andesite_symbol_4", "andesite_symbol_5",
        "andesite_symbol_6", "andesite_symbol_7", "andesite_symbol_8", "andesite_symbol_9", "andesite_symbol_0",
        "andesite_symbol_a", "andesite_symbol_b", "andesite_symbol_c", "andesite_symbol_d", "andesite_symbol_e",
        "andesite_symbol_f", "andesite_symbol_g", "andesite_symbol_h", "andesite_symbol_i", "andesite_symbol_j",
        "andesite_symbol_k", "andesite_symbol_l", "andesite_symbol_m", "andesite_symbol_n", "andesite_symbol_o",
        "andesite_symbol_p", "andesite_symbol_q", "andesite_symbol_r", "andesite_symbol_s", "andesite_symbol_t",
        "andesite_symbol_u", "andesite_symbol_v", "andesite_symbol_w", "andesite_symbol_x", "andesite_symbol_y",
        "andesite_symbol_z",
        "andesite_symbol_a_small", "andesite_symbol_b_small", "andesite_symbol_c_small", "andesite_symbol_d_small", "andesite_symbol_e_small",
        "andesite_symbol_f_small", "andesite_symbol_g_small", "andesite_symbol_h_small", "andesite_symbol_i_small", "andesite_symbol_j_small",
        "andesite_symbol_k_small", "andesite_symbol_l_small", "andesite_symbol_m_small", "andesite_symbol_n_small", "andesite_symbol_o_small",
        "andesite_symbol_p_small", "andesite_symbol_q_small", "andesite_symbol_r_small", "andesite_symbol_s_small", "andesite_symbol_t_small",
        "andesite_symbol_u_small", "andesite_symbol_v_small", "andesite_symbol_w_small", "andesite_symbol_x_small", "andesite_symbol_y_small",
        "andesite_symbol_z_small",
        "andesite_symbol_up_arrow", "andesite_symbol_down_arrow", "andesite_symbol_left_arrow", "andesite_symbol_right_arrow",
        "andesite_symbol_darrow_up", "andesite_symbol_darrow_down", "andesite_symbol_darrow_left", "andesite_symbol_darrow_right",
        "andesite_symbol_skull",
        "andesite_symbol_creeperhead",
        "andesite_symbol_empty",
        "andesite_symbol_math_add",
        "andesite_symbol_math_subtract",
        "andesite_symbol_math_divide",
        "andesite_symbol_math_multiply",
        "andesite_symbol_math_equal",
        "andesite_symbol_math_percent"
    );

    public static final List<String> COPPER_SYMBOL_NAMES = List.of(
        "copper_symbol_1", "copper_symbol_2", "copper_symbol_3", "copper_symbol_4", "copper_symbol_5",
        "copper_symbol_6", "copper_symbol_7", "copper_symbol_8", "copper_symbol_9", "copper_symbol_0",
        "copper_symbol_a", "copper_symbol_b", "copper_symbol_c", "copper_symbol_d", "copper_symbol_e",
        "copper_symbol_f", "copper_symbol_g", "copper_symbol_h", "copper_symbol_i", "copper_symbol_j",
        "copper_symbol_k", "copper_symbol_l", "copper_symbol_m", "copper_symbol_n", "copper_symbol_o",
        "copper_symbol_p", "copper_symbol_q", "copper_symbol_r", "copper_symbol_s", "copper_symbol_t",
        "copper_symbol_u", "copper_symbol_v", "copper_symbol_w", "copper_symbol_x", "copper_symbol_y",
        "copper_symbol_z",
        "copper_symbol_a_small", "copper_symbol_b_small", "copper_symbol_c_small", "copper_symbol_d_small", "copper_symbol_e_small",
        "copper_symbol_f_small", "copper_symbol_g_small", "copper_symbol_h_small", "copper_symbol_i_small", "copper_symbol_j_small",
        "copper_symbol_k_small", "copper_symbol_l_small", "copper_symbol_m_small", "copper_symbol_n_small", "copper_symbol_o_small",
        "copper_symbol_p_small", "copper_symbol_q_small", "copper_symbol_r_small", "copper_symbol_s_small", "copper_symbol_t_small",
        "copper_symbol_u_small", "copper_symbol_v_small", "copper_symbol_w_small", "copper_symbol_x_small", "copper_symbol_y_small",
        "copper_symbol_z_small",
        "copper_symbol_up_arrow", "copper_symbol_down_arrow", "copper_symbol_left_arrow", "copper_symbol_right_arrow",
        "copper_symbol_darrow_up", "copper_symbol_darrow_down", "copper_symbol_darrow_left", "copper_symbol_darrow_right",
        "copper_symbol_skull",
        "copper_symbol_creeperhead",
        "copper_symbol_empty",
        "copper_symbol_math_add",
        "copper_symbol_math_subtract",
        "copper_symbol_math_divide",
        "copper_symbol_math_multiply",
        "copper_symbol_math_equal",
        "copper_symbol_math_percent"
    );

    private static final Map<String, DeferredHolder<Item, ? extends Item>> BRASS_SYMBOLS = new HashMap<>();
    public static final List<DeferredHolder<Item, ? extends Item>> ALL_BRASS_SYMBOLS = new ArrayList<>();
    private static final Map<String, DeferredHolder<Item, ? extends Item>> ANDESITE_SYMBOLS = new HashMap<>();
    public static final List<DeferredHolder<Item, ? extends Item>> ALL_ANDESITE_SYMBOLS = new ArrayList<>();
    private static final Map<String, DeferredHolder<Item, ? extends Item>> COPPER_SYMBOLS = new HashMap<>();
    public static final List<DeferredHolder<Item, ? extends Item>> ALL_COPPER_SYMBOLS = new ArrayList<>();
    private static List<ItemStack> allBrassSymbolStacks = null;
    private static List<ItemStack> allAndesiteSymbolStacks = null;
    private static List<ItemStack> allCopperSymbolStacks = null;

    public static final DeferredHolder<Item, LiquidSymbolItem> BRASS_SYMBOL_LIQUID = ITEMS.register("brass_symbol_liquid",
        () -> new LiquidSymbolItem(new Item.Properties().stacksTo(8), FrequencyModMenus.BRASS_SYMBOL_SWAP::get, FrequencyModItems::getAllBrassSymbolStacks));

    public static final DeferredHolder<Item, LiquidSymbolItem> ANDESITE_SYMBOL_LIQUID = ITEMS.register("andesite_symbol_liquid",
        () -> new LiquidSymbolItem(new Item.Properties().stacksTo(8), FrequencyModMenus.ANDESITE_SYMBOL_SWAP::get, FrequencyModItems::getAllAndesiteSymbolStacks));

    public static final DeferredHolder<Item, LiquidSymbolItem> COPPER_SYMBOL_LIQUID = ITEMS.register("copper_symbol_liquid",
        () -> new LiquidSymbolItem(new Item.Properties().stacksTo(8), FrequencyModMenus.COPPER_SYMBOL_SWAP::get, FrequencyModItems::getAllCopperSymbolStacks));

    static {
        Item.Properties props = new Item.Properties().stacksTo(8);
        for (String name : SYMBOL_NAMES) {
            DeferredHolder<Item, ? extends Item> entry = ITEMS.register(name,
                () -> new BrassBaseSymbolItem(props, name));
            BRASS_SYMBOLS.put(name, entry);
            ALL_BRASS_SYMBOLS.add(entry);
        }
        for (String name : ANDESITE_SYMBOL_NAMES) {
            DeferredHolder<Item, ? extends Item> entry = ITEMS.register(name,
                () -> new AndesiteBaseSymbolItem(props, name));
            ANDESITE_SYMBOLS.put(name, entry);
            ALL_ANDESITE_SYMBOLS.add(entry);
        }
        for (String name : COPPER_SYMBOL_NAMES) {
            DeferredHolder<Item, ? extends Item> entry = ITEMS.register(name,
                () -> new CopperBaseSymbolItem(props, name));
            COPPER_SYMBOLS.put(name, entry);
            ALL_COPPER_SYMBOLS.add(entry);
        }
        // Register aliases for old symbol_* names -> new brass_symbol_* names
        for (String name : SYMBOL_NAMES) {
            if (name.startsWith("brass_")) {
                String oldName = name.substring(6); // "brass_symbol_1" -> "symbol_1"
                ITEMS.addAlias(
                    ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, oldName),
                    ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, name)
                );
            }
        }

        // ── Liquid symbols in ALL lists (before empty for correct GUI position) ──
        for (int i = 0; i < ALL_BRASS_SYMBOLS.size(); i++) {
            if (ALL_BRASS_SYMBOLS.get(i).getKey().location().getPath().equals("brass_symbol_empty")) {
                ALL_BRASS_SYMBOLS.add(i, BRASS_SYMBOL_LIQUID);
                break;
            }
        }
        for (int i = 0; i < ALL_ANDESITE_SYMBOLS.size(); i++) {
            if (ALL_ANDESITE_SYMBOLS.get(i).getKey().location().getPath().equals("andesite_symbol_empty")) {
                ALL_ANDESITE_SYMBOLS.add(i, ANDESITE_SYMBOL_LIQUID);
                break;
            }
        }
        for (int i = 0; i < ALL_COPPER_SYMBOLS.size(); i++) {
            if (ALL_COPPER_SYMBOLS.get(i).getKey().location().getPath().equals("copper_symbol_empty")) {
                ALL_COPPER_SYMBOLS.add(i, COPPER_SYMBOL_LIQUID);
                break;
            }
        }
    }

    public static List<ItemStack> getAllBrassSymbolStacks() {
        if (allBrassSymbolStacks == null) {
            allBrassSymbolStacks = ALL_BRASS_SYMBOLS.stream()
                .map(holder -> new ItemStack(holder.get()))
                .toList();
        }
        return allBrassSymbolStacks;
    }

    public static DeferredHolder<Item, ? extends Item> getSymbol(String name) {
        return BRASS_SYMBOLS.get(name);
    }

    public static List<ItemStack> getAllAndesiteSymbolStacks() {
        if (allAndesiteSymbolStacks == null) {
            allAndesiteSymbolStacks = ALL_ANDESITE_SYMBOLS.stream()
                .map(holder -> new ItemStack(holder.get()))
                .toList();
        }
        return allAndesiteSymbolStacks;
    }

    public static DeferredHolder<Item, ? extends Item> getAndesiteSymbol(String name) {
        return ANDESITE_SYMBOLS.get(name);
    }

    public static List<ItemStack> getAllCopperSymbolStacks() {
        if (allCopperSymbolStacks == null) {
            allCopperSymbolStacks = ALL_COPPER_SYMBOLS.stream()
                .map(holder -> new ItemStack(holder.get()))
                .toList();
        }
        return allCopperSymbolStacks;
    }

    public static DeferredHolder<Item, ? extends Item> getCopperSymbol(String name) {
        return COPPER_SYMBOLS.get(name);
    }

    private static final Pattern SMALL_SYMBOL_PATTERN = Pattern.compile("[a-z]_small");

    public static String displayChar(String name) {
        String suffix;
        if (name.startsWith("copper_symbol_")) {
            suffix = name.substring("copper_symbol_".length());
        } else if (name.startsWith("andesite_symbol_")) {
            suffix = name.substring("andesite_symbol_".length());
        } else if (name.startsWith("brass_symbol_")) {
            suffix = name.substring("brass_symbol_".length());
        } else if (name.startsWith("symbol_")) {
            suffix = name.substring("symbol_".length());
        } else {
            suffix = name;
        }
        if (suffix.length() == 1) return suffix.toUpperCase();
        if (SMALL_SYMBOL_PATTERN.matcher(suffix).matches()) {
            return suffix.substring(0, 1);
        }
        if (suffix.equals("empty")) return "Пустой";
        return switch (suffix) {
            case "up_arrow" -> "↑";
            case "down_arrow" -> "↓";
            case "left_arrow" -> "←";
            case "right_arrow" -> "→";
            case "darrow_up" -> "\uE001";
            case "darrow_down" -> "\uE002";
            case "darrow_left" -> "\uE003";
            case "darrow_right" -> "\uE004";
            case "skull" -> "☠";
            case "creeperhead" -> "\uE000";
            case "math_add" -> "+";
            case "math_subtract" -> "-";
            case "math_divide" -> "/";
            case "math_multiply" -> "*";
            case "math_equal" -> "=";
            case "math_percent" -> "%";
            default -> suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
        };
    }
}
