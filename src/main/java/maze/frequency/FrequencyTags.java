package maze.frequency;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FrequencyTags {
    public static class Items {
        public static final TagKey<Item> SYMBOLS = tag("symbols");
        public static final TagKey<Item> ANDESITE_SYMBOLS = tag("andesite_symbols");
        public static final TagKey<Item> BRASS_SYMBOLS = tag("brass_symbols");
        public static final TagKey<Item> COPPER_SYMBOLS = tag("copper_symbols");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, name));
        }
    }
}
