package xyz.peatral.createembers.datagen.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import xyz.peatral.createembers.CEItems;
import xyz.peatral.createembers.CETags;
import xyz.peatral.createembers.CreateEmbers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CookingRecipeGen extends RecipeProvider {
    protected final List<CookingRecipeGen.GeneratedRecipe> all = new ArrayList<>();
    public CookingRecipeGen(DataGenerator generator) {
        super(generator);
    }

    GeneratedRecipe

    STAMP_INGOT = simpleStamps(CEItems.STAMP_INGOT_RAW.get(), CEItems.STAMP_INGOT.get(), "baked_ingot_stamp"),
    STAMP_FLAT = simpleStamps(CEItems.STAMP_FLAT_RAW.get(), CEItems.STAMP_FLAT.get(), "baked_flat_stamp"),
    STAMP_PLATE = simpleStamps(CEItems.STAMP_PLATE_RAW.get(), CEItems.STAMP_PLATE.get(), "baked_plate_stamp"),
    STAMP_GEAR = simpleStamps(CEItems.STAMP_GEAR_RAW.get(), CEItems.STAMP_GEAR.get(), "baked_gear_stamp");

    CookingRecipeGen.GeneratedRecipe simpleStamps(ItemLike input, ItemLike result, String id) {
        return simple(SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), result, 0.2F, 100)
                .unlockedBy("has_stamp", has(CETags.ceItemTag("stamps"))), id);
    }

    CookingRecipeGen.GeneratedRecipe simple(SimpleCookingRecipeBuilder builder, String id) {
        return register(consumer -> builder.save(consumer, CreateEmbers.asResource("cooking/" + id)));
    }

    protected CookingRecipeGen.GeneratedRecipe register(CookingRecipeGen.GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(Consumer<FinishedRecipe> consumer);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        all.forEach(c -> c.register(consumer));
    }
}
