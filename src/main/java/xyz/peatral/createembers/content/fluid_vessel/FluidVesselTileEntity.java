package xyz.peatral.createembers.content.fluid_vessel;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class FluidVesselTileEntity extends SmartTileEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour tank;
    private boolean contentsChanged;
    protected LazyOptional<IFluidHandler> fluidCapability;

    public FluidVesselTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        contentsChanged = true;
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 16000)
                .whenFluidUpdates(() -> contentsChanged = true);
        behaviours.add(tank);

        fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper(tank.getCapability().orElse(null)));
    }

    @Override
    public void setRemoved() {
        fluidCapability.invalidate();
        super.setRemoved();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide)
            sendData();

        if (!contentsChanged)
            return;

        contentsChanged = false;
        //getOperator().ifPresent(te -> te.basinChecker.scheduleUpdate());
    }

    public void notifyChangeOfContents() {
        contentsChanged = true;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }


    public SmartFluidTankBehaviour getTankBehaviour() {
        return tank;
    }
}
