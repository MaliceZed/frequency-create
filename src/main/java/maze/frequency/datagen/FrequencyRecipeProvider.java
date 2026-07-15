package maze.frequency.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModBlocks;
import maze.frequency.init.FrequencyModItems;
import com.simibubi.create.AllItems;

import java.util.concurrent.CompletableFuture;

public class FrequencyRecipeProvider extends RecipeProvider {
    public FrequencyRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        TagKey<Item> symbolsTag = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "brass_symbols"));

        // Symbol Frame — N N / S / N N
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FrequencyModBlocks.SYMBOL_FRAME_ITEM.get())
            .pattern("N N")
            .pattern(" S ")
            .pattern("N N")
            .define('N', Ingredient.of(Tags.Items.NUGGETS_IRON))
            .define('S', Ingredient.of(symbolsTag))
            .unlockedBy("has_symbol", has(Tags.Items.NUGGETS_IRON))
            .save(consumer);

        // Empty Symbol — N / B
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FrequencyModItems.getSymbol("brass_symbol_empty").get())
            .pattern("N")
            .pattern("B")
            .define('N', Ingredient.of(net.minecraft.tags.ItemTags.create(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "nuggets/copper"))))
            .define('B', Ingredient.of(AllItems.BRASS_SHEET.get()))
            .unlockedBy("has_brass", has(net.minecraft.tags.ItemTags.create(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("c", "nuggets/copper"))))
            .save(consumer);

        // Empty Andesite Symbol — N / A
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FrequencyModItems.getAndesiteSymbol("andesite_symbol_empty").get())
            .pattern("N")
            .pattern("A")
            .define('N', Ingredient.of(Tags.Items.NUGGETS_IRON))
            .define('A', Ingredient.of(AllItems.ANDESITE_ALLOY.get()))
            .unlockedBy("has_andesite", has(AllItems.ANDESITE_ALLOY.get()))
            .save(consumer);

        // Empty Copper Symbol — N / C
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FrequencyModItems.getCopperSymbol("copper_symbol_empty").get())
            .pattern("N")
            .pattern("C")
            .define('N', Ingredient.of(Tags.Items.NUGGETS_IRON))
            .define('C', Ingredient.of(AllItems.COPPER_SHEET.get()))
            .unlockedBy("has_copper", has(AllItems.COPPER_SHEET.get()))
            .save(consumer);
    }
}
