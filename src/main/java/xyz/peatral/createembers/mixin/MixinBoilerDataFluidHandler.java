package xyz.peatral.createembers.mixin;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.peatral.createembers.content.steam_valve.IBoilerDataFluidHandler;

@Mixin(value = com.simibubi.create.content.fluids.tank.BoilerData.BoilerFluidHandler.class)
public abstract class MixinBoilerDataFluidHandler implements IFluidHandler, IBoilerDataFluidHandler {
    @Unique
    public int create_embers$capacity = 10000;
    @Unique
    public FluidTank create_embers$steamTank = new FluidTank(create_embers$capacity, MixinBoilerDataFluidHandler::create_embers$isSteam);

    @Unique
    public FluidTank create_embers$waterTank = new FluidTank(create_embers$capacity, stack -> FluidHelper.isWater(stack.getFluid()));

    @Inject(method = "getTanks", at = @At("RETURN"), cancellable = true, remap = false)
    public void getTanks(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(2);
    }

    @Inject(method = "getTankCapacity", at = @At("RETURN"), cancellable = true, remap = false)
    public void getTankCapacity(int tank, CallbackInfoReturnable<Integer> cir) {
        if (0 <= tank && tank < 2) {
            cir.setReturnValue(create_embers$getTank(tank).getCapacity());
        }
    }

    @Inject(method = "getFluidInTank", at = @At("RETURN"), cancellable = true, remap = false)
    public void getFluidInTank(int tank, CallbackInfoReturnable<FluidStack> cir) {
        if (0 <= tank && tank < 2) {
            cir.setReturnValue(create_embers$getTank(tank).getFluid());
        }
    }

    @Inject(method = "isFluidValid", at = @At("RETURN"), cancellable = true, remap = false)
    public void isFluidValid(int tank, FluidStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (0 <= tank && tank < 2) {
            cir.setReturnValue(create_embers$getTank(tank).isFluidValid(stack));
        }
    }

    @Inject(method = "fill", at = @At("RETURN"), remap = false, cancellable = true)
    public void fill(FluidStack resource, FluidAction action, CallbackInfoReturnable<Integer> cir) {
        int amount = cir.getReturnValue();
        if (FluidHelper.isWater(resource.getFluid())) {
            resource.setAmount(amount);
            amount = create_embers$waterTank.fill(resource, action);
        } else if (create_embers$isSteam(resource)) {
            amount = create_embers$steamTank.fill(resource, action);
        }
        cir.setReturnValue(amount);
    }

    @Inject(method = "drain(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;", at = @At("RETURN"), cancellable = true, remap = false)
    public void drain(FluidStack resource, FluidAction action, CallbackInfoReturnable<FluidStack> cir) {
        if (FluidHelper.isWater(resource.getFluid())) {
            cir.setReturnValue(create_embers$waterTank.drain(resource, action));
        } else if (create_embers$isSteam(resource)) {
            cir.setReturnValue(create_embers$steamTank.drain(resource, action));
        }

    }

    @Inject(method = "drain(ILnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;", at = @At("RETURN"), cancellable = true, remap = false)
    public void drain(int maxDrain, FluidAction action, CallbackInfoReturnable<FluidStack> cir) {
        cir.setReturnValue(create_embers$steamTank.drain(maxDrain, action));
    }

    @Override
    public void create_embers$setCapacity(int capacity) {
        create_embers$capacity = capacity;
        create_embers$steamTank.setCapacity(create_embers$capacity);
        create_embers$waterTank.setCapacity(create_embers$capacity);
    }

    @Override
    public int create_embers$getFluidAmount() {
        return create_embers$steamTank.getFluidAmount() + create_embers$waterTank.getFluidAmount();
    }

    @Override
    public IBoilerDataFluidHandler create_embers$readFromNBT(CompoundTag nbt) {
        create_embers$steamTank.readFromNBT(nbt.getCompound("SteamTank"));
        create_embers$waterTank.readFromNBT(nbt.getCompound("WaterTank"));
        return this;
    }

    @Override
    public CompoundTag create_embers$writeToNBT(CompoundTag nbt) {
        CompoundTag steam = new CompoundTag();
        CompoundTag water = new CompoundTag();
        create_embers$steamTank.writeToNBT(steam);
        create_embers$waterTank.writeToNBT(water);
        nbt.put("SteamTank", steam);
        nbt.put("WaterTank", water);
        return nbt;
    }

    @Unique
    private FluidTank create_embers$getTank(int tank) {
        return switch (tank) {
            case 0 -> create_embers$waterTank;
            case 1 -> create_embers$steamTank;
            default -> null;
        };
    }

    @Unique
    private static boolean create_embers$isSteam(FluidStack stack) {
        return FluidHelper.isTag(stack.getFluid(), AllTags.forgeFluidTag("steam"));
    }
}
