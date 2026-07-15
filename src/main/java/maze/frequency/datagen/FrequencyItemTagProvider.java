package maze.frequency.datagen;

import maze.frequency.FrequencyMod;
import maze.frequency.FrequencyTags;
import maze.frequency.init.FrequencyModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class FrequencyItemTagProvider extends ItemTagsProvider {

    public FrequencyItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                    CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
                                    ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, FrequencyMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // Brass (латунные) символы
        var brassTag = tag(FrequencyTags.Items.BRASS_SYMBOLS);
        FrequencyModItems.ALL_BRASS_SYMBOLS.forEach(holder -> brassTag.add(holder.getKey()));

        // Andesite (андезитовые) символы
        var andesiteTag = tag(FrequencyTags.Items.ANDESITE_SYMBOLS);
        FrequencyModItems.ALL_ANDESITE_SYMBOLS.forEach(holder -> andesiteTag.add(holder.getKey()));

        // Copper (медные) символы
        var copperTag = tag(FrequencyTags.Items.COPPER_SYMBOLS);
        FrequencyModItems.ALL_COPPER_SYMBOLS.forEach(holder -> copperTag.add(holder.getKey()));

        // Общий тег для всех символов
        tag(FrequencyTags.Items.SYMBOLS)
            .addTag(FrequencyTags.Items.BRASS_SYMBOLS)
            .addTag(FrequencyTags.Items.ANDESITE_SYMBOLS)
            .addTag(FrequencyTags.Items.COPPER_SYMBOLS);
    }

    @Override
    public String getName() {
        return "Frequency Item Tags";
    }
}
