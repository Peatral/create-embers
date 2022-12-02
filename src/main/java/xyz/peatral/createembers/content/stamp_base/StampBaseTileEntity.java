package xyz.peatral.createembers.content.stamp_base;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.processing.BasinOperatingTileEntity;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import xyz.peatral.createembers.content.stamper.StamperTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class StampBaseTileEntity extends SmartTileEntity implements IHaveGoggleInformation {

    public StampBaseInventory inputInventory;
    public SmartFluidTankBehaviour tank;
    private boolean contentsChanged;
    protected LazyOptional<IItemHandlerModifiable> itemCapability;
    protected LazyOptional<IFluidHandler> fluidCapability;

    int recipeBackupCheck;

    public StampBaseTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        inputInventory = new StampBaseInventory(1, this);
        inputInventory.whenContentsChanged($ -> contentsChanged = true);
        itemCapability = LazyOptional.of(() -> inputInventory);
        contentsChanged = true;
        recipeBackupCheck = 20;
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 1000)
                .whenFluidUpdates(() -> contentsChanged = true);
        behaviours.add(tank);

        fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper(tank.getCapability().orElse(null)));
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        inputInventory.deserializeNBT(compound.getCompound("InputItems"));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("InputItems", inputInventory.serializeNBT());
    }

    public void onEmptied() {
        getOperator().ifPresent(te -> te.stampBaseRemoved = true);
    }

    @Override
    public void setRemoved() {
        itemCapability.invalidate();
        fluidCapability.invalidate();
        super.setRemoved();
    }

    @Override
    protected void setRemovedNotDueToChunkUnload() {
        onEmptied();
        super.setRemovedNotDueToChunkUnload();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if (!level.isClientSide) {
            if (recipeBackupCheck-- > 0)
                return;
            recipeBackupCheck = 20;
            if (isEmpty())
                return;
            notifyChangeOfContents();
        }
    }

    public boolean isEmpty() {
        return inputInventory.isEmpty() && tank.isEmpty();
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide)
            sendData();

        if (!contentsChanged)
            return;

        contentsChanged = false;
        getOperator().ifPresent(te -> te.stampBaseChecker.scheduleUpdate());
    }

    private Optional<StamperTileEntity> getOperator() {
        if (level == null)
            return Optional.empty();
        BlockEntity te = level.getBlockEntity(worldPosition.above(2));
        if (te instanceof StamperTileEntity)
            return Optional.of((StamperTileEntity) te);
        return Optional.empty();
    }

    public void notifyChangeOfContents() {
        contentsChanged = true;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }
}
