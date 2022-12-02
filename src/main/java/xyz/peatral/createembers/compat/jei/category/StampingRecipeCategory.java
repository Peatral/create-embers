package xyz.peatral.createembers.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import xyz.peatral.createembers.CEBlocks;
import xyz.peatral.createembers.CEGuiTextures;
import xyz.peatral.createembers.CreateEmbers;
import xyz.peatral.createembers.crafting.StampingRecipe;

public class StampingRecipeCategory implements IRecipeCategory<StampingRecipe> {
    public static final RecipeType<StampingRecipe> RECIPE_TYPE = new RecipeType<>(CreateEmbers.asResource("stamping"), StampingRecipe.class);

    public static ResourceLocation texture = CreateEmbers.asResource("textures/gui/jei/jei_stamp.png");
    public static IDrawable SLOT = CEGuiTextures.SLOT.asDrawable();

    private final IDrawable background;
    private final Component title;
    private final IDrawable icon;
    public StampingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 108, 71);
        this.title = Component.translatable(CreateEmbers.ID + ".jei.recipe.stamping");
        this.icon = new ItemIcon(() -> new ItemStack(CEBlocks.STAMPER.get()));
    }

    @Override
    public RecipeType<StampingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StampingRecipe recipe, IFocusGroup focuses) {
        Ingredient stamp = recipe.getStamp();
        Ingredient input = recipe.getInput();
        FluidIngredient fluid = recipe.getFluidIngredient();
        ProcessingOutput output = recipe.getOutput();
        if (!input.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 8, 28)
                    .addIngredients(input)
                    .setBackground(SLOT, -1, -1);
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 47, 7)
                .addIngredients(stamp)
                .setBackground(SLOT, -1, -1);

        if (!fluid.equals(FluidIngredient.EMPTY)) {
            builder.addSlot(RecipeIngredientRole.INPUT, 47, 48)
                    .addIngredients(ForgeTypes.FLUID_STACK, CreateRecipeCategory.withImprovedVisibility(recipe.getFluidIngredient().getMatchingFluidStacks()))
                    .addTooltipCallback(CreateRecipeCategory.addFluidTooltip(fluid.getRequiredAmount()))
                    .setBackground(SLOT, -1, -1);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 84, 28)
                .addItemStack(output.getStack())
                .addTooltipCallback(CreateRecipeCategory.addStochasticTooltip(output))
                .setBackground(SLOT, -1, -1);
    }
}
