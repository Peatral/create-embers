package xyz.peatral.createembers.content.steam_valve;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IBoilerDataFluidHandler extends IFluidHandler {
    IBoilerDataFluidHandler create_embers$readFromNBT(CompoundTag nbt);

    CompoundTag create_embers$writeToNBT(CompoundTag nbt);

    void create_embers$setCapacity(int capacity);

    int create_embers$getFluidAmount();

}
