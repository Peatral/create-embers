package xyz.peatral.createembers.crafting;

import com.google.gson.JsonObject;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class StampingRecipeBuilder implements RecipeBuilder {

    private ProcessingOutput result;
    private Ingredient input;
    private Ingredient stamp;
    private FluidIngredient fluid;
    private Advancement.Builder advancement = Advancement.Builder.advancement();
    @javax.annotation.Nullable
    private String group;
    private final StampingRecipe.Serializer serializer;

    public StampingRecipeBuilder(ProcessingOutput result, Ingredient input, Ingredient stamp, FluidIngredient fluid, StampingRecipe.Serializer serializer) {
        this.result = result;
        this.input = input;
        this.stamp = stamp;
        this.fluid = fluid;
        this.serializer = serializer;
    }

    public static StampingRecipeBuilder simple(ItemStack result) {
        return new StampingRecipeBuilder(new ProcessingOutput(result, 1), Ingredient.EMPTY, Ingredient.EMPTY, FluidIngredient.EMPTY, (StampingRecipe.Serializer) CERecipes.STAMPING_SERIAZLIZER.get());
    }

    public static StampingRecipeBuilder normal(ProcessingOutput result, Ingredient input, Ingredient stamp, FluidIngredient fluid) {
        return new StampingRecipeBuilder(result, input, stamp, fluid, (StampingRecipe.Serializer) CERecipes.STAMPING_SERIAZLIZER.get());
    }

    public StampingRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public StampingRecipeBuilder stamp(Ingredient stamp) {
        this.stamp = stamp;
        return this;
    }

    public StampingRecipeBuilder fluid(FluidIngredient fluid) {
        this.fluid = fluid;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String key, CriterionTriggerInstance trigger) {
        this.advancement.addCriterion(key, trigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getStack().getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(id, this.group == null ? "" : this.group, this.result, this.input, this.stamp, this.fluid, this.advancement, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getStack().getItem().getItemCategory().getRecipeFolderName() + "/" + id.getPath()), this.serializer));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ProcessingOutput result;
        private final Ingredient input;
        private final Ingredient stamp;
        private final FluidIngredient fluid;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final String group;
        private final StampingRecipe.Serializer serializer;

        public Result(ResourceLocation id, String group, ProcessingOutput result, Ingredient input, Ingredient stamp, FluidIngredient fluid, Advancement.Builder advancement, ResourceLocation advancementId, StampingRecipe.Serializer serializer) {
            this.id = id;
            this.result = result;
            this.input = input;
            this.stamp = stamp;
            this.fluid = fluid;
            this.group = group;
            this.serializer = serializer;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        public StampingRecipe toRecipe() {
            return new StampingRecipe(input, stamp, fluid, result, id);
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            serializer.toJson(toRecipe(), json);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}
