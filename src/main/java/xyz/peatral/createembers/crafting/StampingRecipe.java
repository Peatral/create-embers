package xyz.peatral.createembers.crafting;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import xyz.peatral.createembers.content.stamp_base.StampBaseBlockEntity;
import xyz.peatral.createembers.content.stamper.StamperBlockEntity;

public class StampingRecipe implements Recipe<SmartInventory> {


    final Ingredient input;
    final Ingredient stamp;
    final FluidIngredient fluid;
    final ProcessingOutput output;

    private final ResourceLocation id;

    public StampingRecipe(Ingredient input, Ingredient stamp, FluidIngredient fluid, ProcessingOutput output, ResourceLocation id) {
        this.input = input;
        this.stamp = stamp;
        this.fluid = fluid;
        this.output = output;
        this.id = id;
    }

    public static boolean match(StampBaseBlockEntity stampBase, StamperBlockEntity stampBaseOperatingTileEntity, Recipe<?> recipe) {
        return apply(stampBase, stampBaseOperatingTileEntity, recipe, true);
    }

    public static boolean apply(StampBaseBlockEntity stampBase, StamperBlockEntity stampBaseOperatingTileEntity, Recipe<?> recipe) {
        return apply(stampBase, stampBaseOperatingTileEntity, recipe, false);
    }

    public static boolean apply(StampBaseBlockEntity stampBase, StamperBlockEntity stampBaseOperatingTileEntity, Recipe<?> recipe, boolean test) {
        if (!(recipe instanceof StampingRecipe)) {
            return false;
        }
        StampingRecipe stampingRecipe = (StampingRecipe) recipe;

        IItemHandler stampBaseItems = stampBase.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElse(null);
        IItemHandler stamperItems = stampBaseOperatingTileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElse(null);
        IFluidHandler stampBaseFluids = stampBase.getCapability(ForgeCapabilities.FLUID_HANDLER)
                .orElse(null);

        if (stamperItems == null || stampBaseFluids == null)
            return false;

        Ingredient input = stampingRecipe.getInput();
        Ingredient stamp = stampingRecipe.getStamp();
        FluidIngredient fluid =
                stampingRecipe.getFluidIngredient();

        boolean recipeValid = input.test(stampBaseItems.getStackInSlot(0)) && fluid.test(stampBaseFluids.getFluidInTank(0)) && fluid.getRequiredAmount() <= stampBaseFluids.getFluidInTank(0).getAmount() && stamp.test(stamperItems.getStackInSlot(0));
        if (recipeValid && !test) {
            stampBaseFluids.drain(fluid.getRequiredAmount(), IFluidHandler.FluidAction.EXECUTE);
            if (!input.isEmpty()) {
                stampBaseItems.extractItem(0, 1, false);
            }

            if (stampBase.getLevel() != null) {
                ItemStack result = stampingRecipe.getOutput().rollOutput();
                dropItemStack(stampBase.getLevel(), stampBase.getBlockPos().getX(), stampBase.getBlockPos().above().getY(), stampBase.getBlockPos().getZ(), result);
            }
        }
        return recipeValid;
    }

    public static void dropItemStack(Level level, double x, double y, double z, ItemStack stack) {
        double d0 = (double) EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = Math.floor(x) + level.random.nextDouble() * d1 + d2;
        double d4 = Math.floor(y) + level.random.nextDouble() * d1;
        double d5 = Math.floor(z) + level.random.nextDouble() * d1 + d2;

        while(!stack.isEmpty()) {
            ItemEntity itementity = new ItemEntity(level, d3, d4, d5, stack.split(level.random.nextInt(21) + 10));
            float f = 0.05F;
            itementity.setDeltaMovement(level.random.triangle(0.0D, 0.11485000171139836D), level.random.triangle(0.2D, 0.11485000171139836D), level.random.triangle(0.0D, 0.11485000171139836D));
            level.addFreshEntity(itementity);
        }

    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(input, stamp);
    }

    public FluidIngredient getFluidIngredient() {
        return fluid;
    }

    public Ingredient getStamp() {
        return stamp;
    }

    public Ingredient getInput() {
        return input;
    }

    public ProcessingOutput getOutput() {
        return output;
    }

    @Override
    public boolean matches(SmartInventory inv, Level level) {
        if (level.isClientSide)
            return false;

        return false;
    }

    @Override
    public ItemStack assemble(SmartInventory inv) {
        return output.getStack();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.getStack().copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CERecipes.STAMPING_SERIAZLIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CERecipes.STAMPING.get();
    }

    public static class Serializer implements RecipeSerializer<StampingRecipe> {
        public void toJson(StampingRecipe recipe, JsonObject json) {
            if (recipe.input != Ingredient.EMPTY)
                json.add("input", recipe.input.toJson());
            json.add("stamp", recipe.stamp.toJson());
            json.add("fluid", recipe.fluid.serialize());
            json.add("result", recipe.output.serialize());
        }

        @Override
        public StampingRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new StampingRecipe(
                    json.has("input") ? Ingredient.fromJson(json.get("input")) : Ingredient.EMPTY,
                    Ingredient.fromJson(json.get("stamp")),
                    FluidIngredient.deserialize(json.get("fluid")),
                    ProcessingOutput.deserialize(json.get("result")),
                    id);
        }

        @Override
        public @Nullable StampingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            Ingredient stampInput = Ingredient.fromNetwork(buf);
            FluidIngredient fluid = FluidIngredient.read(buf);
            ProcessingOutput output = ProcessingOutput.read(buf);
            return new StampingRecipe(input, stampInput, fluid, output, id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, StampingRecipe recipe) {
            recipe.input.toNetwork(buf);
            recipe.stamp.toNetwork(buf);
            recipe.fluid.write(buf);
            recipe.output.write(buf);
        }
    }


}
