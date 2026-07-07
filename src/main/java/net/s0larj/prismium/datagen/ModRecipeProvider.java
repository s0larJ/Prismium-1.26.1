package net.s0larj.prismium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.s0larj.prismium.block.ModBlocks;
import net.s0larj.prismium.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider,recipeOutput) {
            @Override
            public void buildRecipes() {

                nineBlockStorageRecipes(RecipeCategory.MISC, ModItems.PRISMIUM, RecipeCategory.BUILDING_BLOCKS, ModBlocks.PRISMIUM_BLOCK);

                shaped(RecipeCategory.MISC, ModItems.PRISMIUM)
                        .pattern("SGC")
                        .pattern("GNG")
                        .pattern("CGS")
                        .define('S', Items.PRISMARINE_SHARD)
                        .unlockedBy(getHasName(Items.PRISMARINE_SHARD), has(Items.PRISMARINE_SHARD))
                        .define('N', Items.NETHERITE_SCRAP)
                        .unlockedBy(getHasName(Items.NETHERITE_SCRAP), has(Items.NETHERITE_SCRAP))
                        .define('C', Items.PRISMARINE_CRYSTALS)
                        .unlockedBy(getHasName(Items.PRISMARINE_CRYSTALS), has(Items.PRISMARINE_CRYSTALS))
                        .define('G', Items.GOLD_INGOT)
                        .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                        .group("prismium")
                        .save(recipeOutput, "prismium_base_recipe");

            }
        };
    }

    @Override
    public String getName() {
        return "Prismium Recipes";
    }
}
