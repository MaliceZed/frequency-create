package maze.frequency.compat;

import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModBlocks;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Client-only tooltip integration with Create.
 * Uses reflection to access Create internals; all client-only code is guarded by @OnlyIn.
 */
@OnlyIn(Dist.CLIENT)
public class CreateTooltipCompat {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;

        if (ModList.get() == null || !ModList.get().isLoaded("create"))
            return;

        try {
            Class<?> tmc = Class.forName("com.simibubi.create.foundation.item.TooltipModifier");
            Field regField = tmc.getField("REGISTRY");
            Object registry = regField.get(null);
            Method regMethod = registry.getClass().getMethod("register", Object.class, Object.class);

            Class<?> paletteClass = Class.forName("net.createmod.catnip.lang.FontHelper$Palette");
            Object standardCreate = paletteClass.getField("STANDARD_CREATE").get(null);

            Class<?> modifierClass = Class.forName("com.simibubi.create.foundation.item.ItemDescription$Modifier");
            Constructor<?> ctor = modifierClass.getConstructor(Item.class, paletteClass);

            Class<?> idc = Class.forName("com.simibubi.create.foundation.item.ItemDescription");
            Method useKeyMethod = idc.getMethod("useKey", net.minecraft.world.level.ItemLike.class, String.class);

            String sharedKey = "item.frequency.symbol";
            // Brass (латунные) символы
            for (var holder : FrequencyModItems.ALL_BRASS_SYMBOLS) {
                Item item = holder.get();
                if (item instanceof maze.frequency.item.LiquidSymbolItem) continue;
                Object modifier = ctor.newInstance(item, standardCreate);
                regMethod.invoke(registry, item, modifier);
                useKeyMethod.invoke(null, item, sharedKey);
            }

            // Andesite (андезитовые) символы
            for (var holder : FrequencyModItems.ALL_ANDESITE_SYMBOLS) {
                Item item = holder.get();
                if (item instanceof maze.frequency.item.LiquidSymbolItem) continue;
                Object modifier = ctor.newInstance(item, standardCreate);
                regMethod.invoke(registry, item, modifier);
                useKeyMethod.invoke(null, item, sharedKey);
            }

            // Copper (медные) символы
            for (var holder : FrequencyModItems.ALL_COPPER_SYMBOLS) {
                Item item = holder.get();
                if (item instanceof maze.frequency.item.LiquidSymbolItem) continue;
                Object modifier = ctor.newInstance(item, standardCreate);
                regMethod.invoke(registry, item, modifier);
                useKeyMethod.invoke(null, item, sharedKey);
            }

            // Liquid symbols — own tooltip key with summary + swap + fluid reading
            String liquidKey = "item.frequency.liquid_symbol";
            for (var holder : FrequencyModItems.ALL_BRASS_SYMBOLS) {
                Item item = holder.get();
                if (item instanceof maze.frequency.item.LiquidSymbolItem) {
                    Object modifier = ctor.newInstance(item, standardCreate);
                    regMethod.invoke(registry, item, modifier);
                    useKeyMethod.invoke(null, item, liquidKey);
                }
            }
            for (var holder : FrequencyModItems.ALL_ANDESITE_SYMBOLS) {
                Item item = holder.get();
                if (item instanceof maze.frequency.item.LiquidSymbolItem) {
                    Object modifier = ctor.newInstance(item, standardCreate);
                    regMethod.invoke(registry, item, modifier);
                    useKeyMethod.invoke(null, item, liquidKey);
                }
            }
            for (var holder : FrequencyModItems.ALL_COPPER_SYMBOLS) {
                Item item = holder.get();
                if (item instanceof maze.frequency.item.LiquidSymbolItem) {
                    Object modifier = ctor.newInstance(item, standardCreate);
                    regMethod.invoke(registry, item, modifier);
                    useKeyMethod.invoke(null, item, liquidKey);
                }
            }

            // Symbol Frame — uses its own description ID (block.frequency.symbol_frame.tooltip.*)
            Item frameItem = FrequencyModBlocks.SYMBOL_FRAME_ITEM.get();
            Object frameModifier = ctor.newInstance(frameItem, standardCreate);
            regMethod.invoke(registry, frameItem, frameModifier);
        } catch (Throwable t) {
            LOGGER.error("Failed to initialize Create tooltip compatibility", t);
        }
    }
}
