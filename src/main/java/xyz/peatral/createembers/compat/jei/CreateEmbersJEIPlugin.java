package xyz.peatral.createembers.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.peatral.createembers.CEBlocks;
import xyz.peatral.createembers.CreateEmbers;
import xyz.peatral.createembers.compat.jei.category.StampingRecipeCategory;
import xyz.peatral.createembers.crafting.CERecipes;

import java.util.Objects;

@JeiPlugin
public class CreateEmbersJEIPlugin implements IModPlugin {
    private static final ResourceLocation ID = CreateEmbers.asResource("jei_plugin");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new StampingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IIngredientManager ingredientManager = registration.getIngredientManager();
        registration.addRecipes(StampingRecipeCategory.RECIPE_TYPE,
                Objects.requireNonNull(Minecraft.getInstance().getConnection())
                        .getRecipeManager()
                        .getAllRecipesFor(CERecipes.STAMPING.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CEBlocks.STAMPER.get()), StampingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(CEBlocks.STAMP_BASE.get()), StampingRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
