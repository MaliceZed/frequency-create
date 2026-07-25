package maze.frequency.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModBlocks;

public class FrequencyBlockTagProvider extends BlockTagsProvider {

    public static final TagKey<Block> CREATE_BRITTLE = TagKey.create(
        net.minecraft.core.registries.Registries.BLOCK,
        ResourceLocation.fromNamespaceAndPath("create", "brittle")
    );
    public static final TagKey<Block> CREATE_SAFE_NBT = TagKey.create(
        net.minecraft.core.registries.Registries.BLOCK,
        ResourceLocation.fromNamespaceAndPath("create", "safe_nbt")
    );

    public FrequencyBlockTagProvider(PackOutput output,
                                      CompletableFuture<HolderLookup.Provider> lookupProvider,
                                      ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FrequencyMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        tag(CREATE_BRITTLE)
            .add(FrequencyModBlocks.SYMBOL_FRAME.get())
            .add(FrequencyModBlocks.LOGIC_COMBINATOR.get());

        tag(CREATE_SAFE_NBT)
            .add(FrequencyModBlocks.SYMBOL_FRAME.get())
            .add(FrequencyModBlocks.LOGIC_COMBINATOR.get());
    }

    @Override
    public String getName() {
        return "Frequency Block Tags";
    }
}
