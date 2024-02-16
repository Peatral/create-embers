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
import net.minecraft.world.level.material.Fluid;
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

    @Unique
    public double create_embers$pressure = 1.0d; // Defaults to atmospheric pressure

    @Unique
    public double create_embers$temperature = 0.0d;

    @Unique
    public double create_embers$boilingPoint = 100.0d;

    @Unique
    public int create_embers$lastWaterAmount = 0;

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
        if (!isActive())
            return;

        int tankVolume = create_embers$getTotalVolume(controller);
        ((IBoilerDataFluidHandler) create_embers$tank).create_embers$setCapacity(tankVolume);

        FluidStack water = create_embers$tank.getFluidInTank(0);
        FluidStack steam = create_embers$tank.getFluidInTank(1);

        int waterAmount = water.getAmount();
        int steamAmount = steam.getAmount();

        create_embers$pressure = create_embers$getBoilerPressure(tankVolume, waterAmount, steamAmount, create_embers$temperature);
        create_embers$boilingPoint = create_embers$getWaterBoilingPoint(create_embers$pressure);

        double boilerHeat = activeHeat > 0 ? 100.0d + Math.sqrt(activeHeat - 1) * 50.0d : 0.0d;
        if (waterAmount > 0) {
            if (create_embers$lastWaterAmount < waterAmount && create_embers$temperature > 0.0d) {
                create_embers$temperature = (double) create_embers$lastWaterAmount / (waterAmount + steamAmount) * create_embers$temperature;
            } else {
                create_embers$temperature = Math.min(create_embers$temperature + 1.0d / waterAmount * (boilerHeat > 0.0d ? boilerHeat : -create_embers$temperature), create_embers$boilingPoint);
            }
        } else if (steamAmount == 0) {
            create_embers$temperature -= Math.min((boilerHeat > 0.0d ? 1 / boilerHeat : 1.0d) * 0.01d, create_embers$temperature);
        }

        double temperatureDifference = create_embers$temperature - create_embers$boilingPoint;
        if (temperatureDifference >= 0.0d) {
            double conversionPercentage = Math.min(0.05d + (boilerHeat - create_embers$temperature) / create_embers$boilingPoint * 0.5d, 1.0d);
            create_embers$convertFluids(/*(int) (waterAmount * conversionPercentage)*/ 1, Fluids.WATER, CEFluids.STEAM.get().getSource());
        } else if (temperatureDifference < -3.0d) {
            double conversionPercentage = Math.min(0.05d + temperatureDifference / create_embers$boilingPoint * -0.95d, 1.0d);
            create_embers$convertFluids(/*(int) (steamAmount * conversionPercentage)*/ 1, CEFluids.STEAM.get().getSource(), Fluids.WATER);
        }

        create_embers$lastWaterAmount = create_embers$tank.getFluidInTank(0).getAmount();

        /*
        if (create_embers$pressure > 50.0d || create_embers$temperature > 200.0d && (double) waterAmount / tankVolume < 0.05d) {
            if (!controller.getLevel().isClientSide) {
                BlockPos pos = controller.getLastKnownPos();
                controller.getLevel().playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.6F, 1.0F);
                Explosion explosion = controller.getLevel().explode(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, (float) Math.min(2.0f + 10.0f * create_embers$pressure / 50.0f, 12.0f), Level.ExplosionInteraction.BLOCK);
                return;
            }
        }
         */
    }

    @Unique
    private void create_embers$convertFluids(int conversionAmount, Fluid from, Fluid to) {
        if (conversionAmount == 0) {
            return;
        }

        FluidStack convertedFrom = new FluidStack(from, conversionAmount);
        FluidStack convertedTo = new FluidStack(to, conversionAmount);

        convertedFrom = create_embers$tank.drain(convertedFrom, IFluidHandler.FluidAction.SIMULATE);
        convertedTo.setAmount(create_embers$tank.fill(convertedTo, IFluidHandler.FluidAction.SIMULATE));

        conversionAmount = Math.min(convertedFrom.getAmount(), convertedTo.getAmount());
        if (conversionAmount > 0) {
            convertedFrom.setAmount(conversionAmount);
            convertedTo.setAmount(conversionAmount);
            create_embers$tank.fill(convertedTo, IFluidHandler.FluidAction.EXECUTE);
            create_embers$tank.drain(convertedFrom, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Unique
    private double create_embers$getBoilerPressure(int tankVolume, int waterAmount, int steamAmount, double temperature) {
        int fluidAmount = /*waterAmount +*/ steamAmount;
        if (fluidAmount == 0) {
            return 1.0d;
        }

        // this code treats the volume of water and steam differently than the volume of a tank
        // the amount of water and steam gets treated like mass while the capacity of the tank gets treated as volume
        // also how the volume of the water influences the pressure in the real world is a mystery to me
        double WATER_DENSITY = 998.0d; // kg/m^3
        double STEAM_DENSITY = 0.589d; // kg/m^3
        double IDEAL_GAS_CONSTANT = 8.31446d; // J / (K * mol)
        double WATER_MOLES_PER_KG = 55.51d; // in mol

        double mixtureVolume = Math.min(/*waterAmount / WATER_DENSITY +*/ steamAmount / STEAM_DENSITY, tankVolume * 0.001d - waterAmount / WATER_DENSITY); // m^3
        // ideal gas law: pressure of the gas = amount of substance in moles * ideal gas constant * temperature of gas / volume of the gas
        double moleAmount = fluidAmount * WATER_MOLES_PER_KG; // moles
        double temperatureKelvin = temperature + 273.15; // K
        double pressureInPa =  moleAmount * IDEAL_GAS_CONSTANT * temperatureKelvin / mixtureVolume; // Pa
        return pressureInPa * 0.00001d;
    }

    @Unique
    private double create_embers$getWaterBoilingPoint(double pressure) {
        return pressure > 0.0d ? 234.175d / (17.08085d / Math.log(pressure / 0.0061d) - 1.0d) : 0.0d; // derived from magnus formula
    }

    @Unique
    private int create_embers$getTotalVolume(FluidTankBlockEntity controller) {
        return controller.getTotalTankSize() * FluidTankBlockEntity.getCapacityMultiplier();
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
        cir.getReturnValue().putDouble("BoilerTankPressure", create_embers$pressure);
        cir.getReturnValue().putDouble("BoilerTankTemperature", create_embers$temperature);
    }

    @Inject(method = "read", at = @At("RETURN"), remap = false)
    public void read(CompoundTag nbt, int boilerSize, CallbackInfo ci) {
        if (create_embers$tank != null && nbt.contains("BoilerSteamTankContent")) {
            ((IBoilerDataFluidHandler) create_embers$tank).create_embers$readFromNBT(nbt.getCompound("BoilerSteamTankContent"));
            create_embers$lastWaterAmount = create_embers$tank.getFluidInTank(0).getAmount();
        }
        create_embers$pressure = nbt.getDouble("BoilerTankPressure");
        create_embers$temperature = nbt.getDouble("BoilerTankTemperature");
    }

    @Inject(method = "addToGoggleTooltip", at = @At("RETURN"), remap = false)
    public void addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, int boilerSize, CallbackInfoReturnable<Boolean> cir) {
        if (isActive() && create_embers$tank != null) {
            tooltip.add(Components.immutableEmpty());

            CELang.translate("gui.goggles.boiler.steam_info")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 0);
            CELang.builder()
                    .translate("gui.goggles.boiler.pressure", Lang.number(create_embers$pressure)
                            .style(ChatFormatting.GOLD)
                            .add(CELang.translate("generic.unit.pressure")))
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            CELang.translate("gui.goggles.boiler.temperature", Lang.number(create_embers$temperature)
                            .style(ChatFormatting.GOLD)
                            .add(CELang.translate("generic.unit.temperature")))
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            CELang.translate("gui.goggles.boiler.boiling_point", Lang.number(create_embers$boilingPoint)
                            .style(ChatFormatting.GOLD)
                            .add(CELang.translate("generic.unit.temperature")))
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);

            tooltip.add(Components.immutableEmpty());

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
