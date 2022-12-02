package xyz.peatral.createembers.datagen.recipe;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import xyz.peatral.createembers.CEItems;
import xyz.peatral.createembers.CETags;
import xyz.peatral.createembers.CreateEmbers;
import xyz.peatral.createembers.crafting.StampingRecipeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StampingRecipeGen extends RecipeProvider {
    protected final List<GeneratedRecipe> all = new ArrayList<>();
    public StampingRecipeGen(DataGenerator generator) {
        super(generator);
    }

    @FunctionalInterface
    public interface GeneratedRecipe {
        void register(Consumer<FinishedRecipe> consumer);
    }


    GeneratedRecipe
    MOLTEN_IRON_TO_INGOT = simpleBarStamping(Items.IRON_INGOT, "molten_iron"),
    MOLTEN_GOLD_TO_INGOT = simpleBarStamping(Items.GOLD_INGOT, "molten_gold"),
    MOLTEN_COPPER_TO_INGOT = simpleBarStamping(Items.COPPER_INGOT, "molten_copper"),
    MOLTEN_ALUMINUM_TO_INGOT = simpleBarStamping(CEItems.ALUMINUM_INGOT.get(), "molten_aluminum"),
    MOLTEN_BRONZE_TO_INGOT = simpleBarStamping(CEItems.BRONZE_INGOT.get(), "molten_bronze"),
    MOLTEN_DAWNSTONE_TO_INGOT = simpleBarStamping(CEItems.DAWNSTONE_INGOT.get(), "molten_dawnstone"),
    MOLTEN_ELECTRUM_TO_INGOT = simpleBarStamping(CEItems.ELECTRUM_INGOT.get(), "molten_electrum"),
    MOLTEN_LEAD_TO_INGOT = simpleBarStamping(CEItems.LEAD_INGOT.get(), "molten_lead"),
    MOLTEN_NICKEL_TO_INGOT = simpleBarStamping(CEItems.NICKEL_INGOT.get(), "molten_nickel"),
    MOLTEN_SILVER_TO_INGOT = simpleBarStamping(CEItems.SILVER_INGOT.get(), "molten_silver"),
    MOLTEN_TIN_TO_INGOT = simpleBarStamping(CEItems.TIN_INGOT.get(), "molten_tin"),

    MOLTEN_ALUMINUM_TO_PLATE = simplePlateStamping(CEItems.ALUMINUM_PLATE.get(), "molten_aluminum"),
    MOLTEN_BRONZE_TO_PLATE = simplePlateStamping(CEItems.BRONZE_PLATE.get(), "molten_bronze"),
    MOLTEN_DAWNSTONE_TO_PLATE = simplePlateStamping(CEItems.DAWNSTONE_PLATE.get(), "molten_dawnstone"),
    MOLTEN_ELECTRUM_TO_PLATE = simplePlateStamping(CEItems.ELECTRUM_PLATE.get(), "molten_electrum"),
    MOLTEN_LEAD_TO_PLATE = simplePlateStamping(CEItems.LEAD_PLATE.get(), "molten_lead"),
    MOLTEN_NICKEL_TO_PLATE = simplePlateStamping(CEItems.NICKEL_PLATE.get(), "molten_nickel"),
    MOLTEN_SILVER_TO_PLATE = simplePlateStamping(CEItems.SILVER_PLATE.get(), "molten_silver"),
    MOLTEN_TIN_TO_PLATE = simplePlateStamping(CEItems.TIN_PLATE.get(), "molten_tin"),

    RAW_INGOT_STAMP = simpleRawStampStamping(CEItems.STAMP_INGOT_RAW.get(), "ingots"),
    RAW_PLATE_STAMP = simpleRawStampStamping(CEItems.STAMP_PLATE_RAW.get(), "plates"),
    RAW_GEAR_STAMP = simpleRawStampStamping(CEItems.STAMP_GEAR_RAW.get(), "gears");

    protected GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    GeneratedRecipe simpleRawStampStamping(Item stamp, String stamptype) {
        return simpleStamping(StampingRecipeBuilder.simple(new ItemStack(stamp))
                .input(Ingredient.of(CEItems.STAMP_FLAT_RAW.get()))
                .stamp(Ingredient.of(AllTags.forgeItemTag(stamptype))), "raw_" + stamptype + "_stamp");
    }

    GeneratedRecipe simplePlateStamping(Item ingot, String fluid_name) {
        return simpleStamping(StampingRecipeBuilder.simple(new ItemStack(ingot))
                .stamp(Ingredient.of(CEItems.STAMP_PLATE.get()))
                .fluid(FluidIngredient.fromTag(AllTags.forgeFluidTag(fluid_name), 144)), fluid_name + "_to_plate");
    }

    GeneratedRecipe simpleBarStamping(Item ingot, String fluid_name) {
        return simpleStamping(StampingRecipeBuilder.simple(new ItemStack(ingot))
                .stamp(Ingredient.of(CEItems.STAMP_INGOT.get()))
                .fluid(FluidIngredient.fromTag(AllTags.forgeFluidTag(fluid_name), 144)), fluid_name + "_to_ingot");
    }

    GeneratedRecipe simpleStamping(StampingRecipeBuilder builder, String id) {
        return register(consumer -> builder.save(consumer, CreateEmbers.asResource("stamping/" + id)));
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        all.forEach(c -> c.register(consumer));
    }
}
