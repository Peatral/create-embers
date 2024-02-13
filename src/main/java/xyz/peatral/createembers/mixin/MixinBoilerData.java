package xyz.peatral.createembers.mixin;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.peatral.createembers.CEFluids;
import xyz.peatral.createembers.CELang;
import xyz.peatral.createembers.content.steam_valve.IBoilerDataFluidHandler;

import java.util.List;

@Mixin(BoilerData.class)
public abstract class MixinBoilerData implements IHaveGoggleInformation {
    @Unique
    public BoilerData.BoilerFluidHandler create_embers$tank = createHandler();

    @Shadow(remap = false)
    public abstract boolean isActive();

    @Shadow(remap = false)
    public abstract BoilerData.BoilerFluidHandler createHandler();

    @Shadow(remap = false)
    public int activeHeat;

    @Inject(method = "createHandler", at = @At("RETURN"), cancellable = true, remap = false)
    public void createHandler(CallbackInfoReturnable<BoilerData.BoilerFluidHandler> cir) {
        if (create_embers$tank == null) {
            create_embers$tank = cir.getReturnValue();
        } else {
            cir.setReturnValue(create_embers$tank);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"), remap = false)
    public void tick(FluidTankBlockEntity controller, CallbackInfo ci) {
        int maximumCapacity = controller.getTotalTankSize() * FluidTankBlockEntity.getCapacityMultiplier();
        ((IBoilerDataFluidHandler) create_embers$tank).create_embers$setCapacity(maximumCapacity);
        int conversionAmount = activeHeat;
        FluidStack evaporatedWater = new FluidStack(Fluids.WATER, conversionAmount);
        evaporatedWater = create_embers$tank.drain(evaporatedWater, IFluidHandler.FluidAction.SIMULATE);
        conversionAmount = evaporatedWater.getAmount();
        if (!evaporatedWater.isEmpty()) {
            create_embers$tank.drain(evaporatedWater, IFluidHandler.FluidAction.EXECUTE);
            int steamAmount = create_embers$tank.fill(new FluidStack(CEFluids.STEAM.get().getSource(), conversionAmount), IFluidHandler.FluidAction.EXECUTE);

            int fluidInTank = ((IBoilerDataFluidHandler) create_embers$tank).create_embers$getFluidAmount();
            if ((fluidInTank > maximumCapacity || steamAmount < conversionAmount) && !controller.getLevel().isClientSide) {
                BlockPos pos = controller.getLastKnownPos();
                controller.getLevel().playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.6F, 1.0F);
                Explosion explosion = controller.getLevel().explode(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, 3.0f + 7.0f * ((float) activeHeat) / 18.0f, Level.ExplosionInteraction.BLOCK);
            }
        }
    }

    @Inject(method = "evaluate", at = @At("RETURN"), remap = false)
    public void evaluate(FluidTankBlockEntity controller, CallbackInfoReturnable<Boolean> cir) {
        ((IBoilerDataFluidHandler) create_embers$tank).create_embers$setCapacity(controller.getTotalTankSize() * FluidTankBlockEntity.getCapacityMultiplier());
    }

    @Inject(method = "write", at = @At("RETURN"), remap = false)
    public void write(CallbackInfoReturnable<CompoundTag> cir) {
        if (create_embers$tank != null) {
            cir.getReturnValue().put("BoilerSteamTankContent", ((IBoilerDataFluidHandler) create_embers$tank).create_embers$writeToNBT(new CompoundTag()));
        }
    }

    @Inject(method = "read", at = @At("RETURN"), remap = false)
    public void read(CompoundTag nbt, int boilerSize, CallbackInfo ci) {
        if (create_embers$tank != null && nbt.contains("BoilerSteamTankContent")) {
            ((IBoilerDataFluidHandler) create_embers$tank).create_embers$readFromNBT(nbt.getCompound("BoilerSteamTankContent"));
        }
    }

    @Inject(method = "addToGoggleTooltip", at = @At("RETURN"), remap = false)
    public void addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, int boilerSize, CallbackInfoReturnable<Boolean> cir) {
        if (isActive() && create_embers$tank != null) {
            tooltip.add(Components.immutableEmpty());

            CELang.translate("gui.goggles.boiler.steam_info")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 0);

            CELang.builder()
                    .add(Lang.translate("boiler.per_tick", Lang.number(activeHeat)
                            .style(ChatFormatting.AQUA)
                            .add(Lang.translate("generic.unit.millibuckets")))
                    .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

            tooltip.add(Components.immutableEmpty());
            containedFluidTooltip(tooltip, isPlayerSneaking, LazyOptional.of(() -> create_embers$tank));
        }
    }
}
