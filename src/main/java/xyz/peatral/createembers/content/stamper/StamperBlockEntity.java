package xyz.peatral.createembers.content.stamper;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.simple.DeferralBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.peatral.createembers.CELang;
import xyz.peatral.createembers.content.stamp_base.StampBaseBlockEntity;
import xyz.peatral.createembers.crafting.StampingRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StamperBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    private static final Object stampingRecipesKey = new Object();

    public DeferralBehaviour stampBaseChecker;
    public boolean stampBaseRemoved;
    protected Recipe<?> currentRecipe;

    public SmartInventory inventory;
    public LazyOptional<IItemHandlerModifiable> itemCapability;

    public boolean running;
    public int runningTicks;
    public int prevRunningTicks;

    public StamperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new StamperInventory(1, this);
        itemCapability = LazyOptional.of(() -> inventory);

        running = false;
        runningTicks = 0;
        prevRunningTicks = 0;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        stampBaseChecker = new DeferralBehaviour(this, this::updateStampBase);
        behaviours.add(stampBaseChecker);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        return super.getCapability(cap, side);
    }


    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        inventory.deserializeNBT(compound.getCompound("InputItems"));
        running = compound.getBoolean("Running");
        prevRunningTicks = runningTicks = compound.getInt("Ticks");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("InputItems", inventory.serializeNBT());
        compound.putBoolean("Running", running);
        compound.putInt("Ticks", runningTicks);
    }

    @Override
    public void tick() {
        if (stampBaseRemoved) {
            stampBaseRemoved = false;
            onStampBaseRemoved();
            sendData();
            return;
        }

        super.tick();

        if (runningTicks >= 40) {
            running = false;
            runningTicks = 0;
            stampBaseChecker.scheduleUpdate();
            return;
        }

        if (running && level != null) {
            if ((!level.isClientSide || isVirtual()) && runningTicks == 20) {
                applyStampBaseRecipe();
                sendData();
            }
            prevRunningTicks = runningTicks++;
        }
    }

    private void applyStampBaseRecipe() {
        if (currentRecipe == null)
            return;

        Optional<StampBaseBlockEntity> optionalStampBase = getStampBase();
        if (!optionalStampBase.isPresent())
            return;
        StampBaseBlockEntity stampBase = optionalStampBase.get();
        if (!StampingRecipe.apply(stampBase, this, currentRecipe))
            return;
        //getProcessedRecipeTrigger().ifPresent(this::award);
        stampBase.tank.sendDataImmediately();

        stampBase.notifyChangeOfContents();
    }

    protected boolean updateStampBase() {
        if (level == null || level.isClientSide)
            return true;

        List<Recipe<?>> recipes = getMatchingRecipes();
        if (recipes.isEmpty())
            return true;
        currentRecipe = recipes.get(0);
        startProcessingStampBase();
        sendData();
        return true;
    }

    public void startProcessingStampBase() {
        if (running && runningTicks <= 40)
            return;
        running = true;
        runningTicks = 0;
    }

    protected <C extends Container> boolean matchStampBaseRecipe(Recipe<C> recipe) {
        if (recipe == null)
            return false;
        Optional<StampBaseBlockEntity> stampBase = getStampBase();
        if (!stampBase.isPresent())
            return false;
        return StampingRecipe.match(stampBase.get(), this, recipe);
    }

    protected List<Recipe<?>> getMatchingRecipes() {
        if (getStampBase().map(StampBaseBlockEntity::isEmpty)
                .orElse(true))
            return new ArrayList<>();

        return RecipeFinder.get(getRecipeCacheKey(), level, recipe -> true).stream()
                .filter(this::matchStampBaseRecipe)
                .sorted((r1, r2) -> r2.getIngredients()
                        .size()
                        - r1.getIngredients()
                        .size())
                .collect(Collectors.toList());
    }

    protected void onStampBaseRemoved() {
        if (!running)
            return;
        runningTicks = 40;
        running = false;
    }

    protected Optional<StampBaseBlockEntity> getStampBase() {
        if (level == null)
            return Optional.empty();
        BlockEntity stampbaseTE = level.getBlockEntity(worldPosition.below(2));
        if (!(stampbaseTE instanceof StampBaseBlockEntity))
            return Optional.empty();
        return Optional.of((StampBaseBlockEntity) stampbaseTE);
    }

    public float getRenderedHeadOffset(float partialTicks) {
        if (!running)
            return 0;
        int runningTicks = Math.abs(this.runningTicks);
        float ticks = Mth.lerp(partialTicks, prevRunningTicks, runningTicks);
        if (runningTicks < 40 / 3f)
            return (float) Mth.clamp(Math.pow(ticks / (40 / 2.3f), 3f), 0, 1);
        return Mth.clamp((30 - ticks) / 10, 0, 1);
    }

    protected Object getRecipeCacheKey() {
        return stampingRecipesKey;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        Component indent = Components.literal(IHaveGoggleInformation.spacing);
        Component indent2 = Components.literal(IHaveGoggleInformation.spacing + " ");

        Optional<StampBaseBlockEntity> stampBaseOptional = getStampBase();
        if (stampBaseOptional.isEmpty())
            return false;
        StampBaseBlockEntity stampBase = stampBaseOptional.get();

        tooltip.add(indent.plainCopy()
                .append(CELang.translateDirect("gui.goggles.stamper")));

        ItemStack stamperStack = inventory.getItem(0);
        tooltip.add(indent2.plainCopy()
                .append(CELang.translateDirect("gui.goggles.stamper.stamp",
                        stamperStack.isEmpty() ? CELang.translateDirect("gui.none") : stamperStack.getHoverName()
                ).withStyle(stamperStack.isEmpty() ? ChatFormatting.DARK_GRAY : ChatFormatting.GRAY)));

        ItemStack stampBaseStack = stampBase.inputInventory.getItem(0);
        tooltip.add(indent2.plainCopy()
                .append(CELang.translateDirect("gui.goggles.stamper.item",
                        stampBaseStack.isEmpty() ? CELang.translateDirect("gui.none") : stampBaseStack.getHoverName()
                ).withStyle(stampBaseStack.isEmpty() ? ChatFormatting.DARK_GRAY : ChatFormatting.GRAY)));

        FluidStack stampBaseFluid = stampBase.tank.getPrimaryTank().getRenderedFluid();
        tooltip.add(indent2.plainCopy()
                .append(CELang.translateDirect("gui.goggles.stamper.fluid",
                        stampBaseFluid.isEmpty() ? CELang.translateDirect("gui.none") : stampBaseFluid.getDisplayName()
                ).withStyle(stampBaseFluid.isEmpty() ? ChatFormatting.DARK_GRAY : ChatFormatting.GRAY)));

        List<Recipe<?>> recipes = getMatchingRecipes();
        if (!recipes.isEmpty()) {
            tooltip.add(indent2.plainCopy()
                    .append(CELang.translateDirect("gui.goggles.stamper.recipes")
                            .withStyle(ChatFormatting.GRAY)));
            for (Recipe<?> recipe : recipes) {
                tooltip.add(indent2.plainCopy()
                        .append(CELang.translateDirect("gui.goggles.stamper.recipe", recipe.getResultItem(level.registryAccess()).getHoverName())
                                .withStyle(ChatFormatting.GRAY)));
            }
        }

        if (running) {
            tooltip.add(indent2.plainCopy()
                    .append(CELang.translateDirect("gui.goggles.stamper.running"
                    ).withStyle(ChatFormatting.GRAY)));
            tooltip.add(indent2.plainCopy()
                    .append(CELang.translateDirect("gui.goggles.stamper.runningticks",
                            runningTicks
                    ).withStyle(ChatFormatting.GRAY)));
        }

        return true;
    }
}

