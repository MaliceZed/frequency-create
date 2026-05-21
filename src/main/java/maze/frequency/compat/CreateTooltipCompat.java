package maze.frequency.compat;

import maze.frequency.init.FrequencyModItems;
import maze.frequency.init.FrequencyModBlocks;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CreateTooltipCompat {
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
            for (var holder : FrequencyModItems.ALL_SYMBOLS) {
                Item item = holder.get();
                Object modifier = ctor.newInstance(item, standardCreate);
                regMethod.invoke(registry, item, modifier);
                useKeyMethod.invoke(null, item, sharedKey);
            }

            // Symbol Frame — uses its own description ID (block.frequency.symbol_frame.tooltip.*)
            Item frameItem = FrequencyModBlocks.SYMBOL_FRAME_ITEM.get();
            Object frameModifier = ctor.newInstance(frameItem, standardCreate);
            regMethod.invoke(registry, frameItem, frameModifier);
        } catch (Throwable t) {
            // Create not fully loaded — skip tooltip integration
        }
    }
}
