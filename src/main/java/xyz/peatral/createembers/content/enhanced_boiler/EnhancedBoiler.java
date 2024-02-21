package xyz.peatral.createembers.content.enhanced_boiler;

import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.peatral.createembers.CEFluids;
import xyz.peatral.createembers.content.steam_valve.IBoilerDataFluidHandler;

public class EnhancedBoiler {
    public BoilerData.BoilerFluidHandler tank;


    public double pressure = 1.0d; // Defaults to atmospheric pressure


    public double waterTemperature = 0.0d;
    public double steamTemperature = 100.0d;
    public double boilerTemperature = 0.0d;


    public double boilingPoint = 100.0d;


    public int lastWaterAmount = 0;
    public int lastSteamAmount = 0;


    public int[] conversionOverTime = new int[40];
    public int currentIndex = 0;

    public EnhancedBoiler(BoilerData.BoilerFluidHandler tank) {
        this.tank = tank;
    }

    public void tick(FluidTankBlockEntity controller, int activeHeat) {
        if (controller == null || tank == null) {
            return;
        }

        int tankVolume = getTotalVolume(controller);
        ((IBoilerDataFluidHandler) tank).create_embers$setCapacity(tankVolume);

        int condensedAmount = 0;
        int evaporatedAmount = 0;
        do {
            int waterAmount = tank.getFluidInTank(0).getAmount();
            int steamAmount = tank.getFluidInTank(1).getAmount();

            pressure = getBoilerPressure(tankVolume, waterAmount, steamAmount, steamTemperature);
            boilingPoint = getWaterBoilingPoint(pressure);

            boilerTemperature = activeHeat > 0 ? getWaterBoilingPoint(1.0d) + Math.sqrt(activeHeat - 1) * 50.0d : 0.0d;
            if (condensedAmount + evaporatedAmount <= 0) {
                recalculateTemperature(lastWaterAmount, waterAmount, lastSteamAmount, steamAmount);
            }

            boolean noCondensation = false;
            if (steamTemperature <= boilingPoint && boilerTemperature < steamTemperature) {
                if (convertFluids(1, CEFluids.STEAM.get().getSource(), Fluids.WATER) > 0) {
                    condensedAmount += 1;
                } else {
                    noCondensation = true;
                }
            } else {
                noCondensation = true;
            }

            boolean noEvaporation = false;
            if (waterTemperature >= boilingPoint && boilerTemperature > waterTemperature) {
                if (convertFluids(1, Fluids.WATER, CEFluids.STEAM.get().getSource()) > 0) {
                    evaporatedAmount += 1;
                } else {
                    noEvaporation = true;
                }
            } else {
                noEvaporation = true;
            }

            if (noEvaporation && noCondensation) {
                break;
            }
        } while(condensedAmount + evaporatedAmount < 512);

        lastWaterAmount = tank.getFluidInTank(0).getAmount();
        lastSteamAmount = tank.getFluidInTank(1).getAmount();

        conversionOverTime[currentIndex] = evaporatedAmount - condensedAmount;
        currentIndex = (currentIndex + 1) % conversionOverTime.length;
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

    private void recalculateTemperature(int lastWaterAmount, int currentWaterAmount, int lastSteamAmount, int currentSteamAmount) {
        // If no fluid is present, the temperature just equals the minimum temperature it can have
        if (currentWaterAmount + currentSteamAmount <= 0) {
            waterTemperature = 0.0d;
            steamTemperature = boilingPoint;
            return;
        }

        // heat up
        if (boilerTemperature > waterTemperature) {
            if (currentWaterAmount > 0) {
                waterTemperature = Math.min(waterTemperature + boilerTemperature / currentWaterAmount, boilingPoint);

                // Additional energy of hot steam will be used
                if (currentSteamAmount > 0 && waterTemperature < boilingPoint) {
                    // todo: exchange 5 through something that makes more sense
                    double heatingThroughSteam = Math.min(boilingPoint - waterTemperature, Math.min(steamTemperature - boilingPoint, 5));
                    steamTemperature -= heatingThroughSteam;
                    waterTemperature += heatingThroughSteam;
                }
            } else {
                steamTemperature = Math.min(steamTemperature + boilerTemperature / currentSteamAmount * 2.0d, 375.0d);
                waterTemperature = 0;
            }
        }

        // cool down
        if (boilerTemperature < steamTemperature) {
            if (currentSteamAmount > 0) {
                steamTemperature = Math.max(steamTemperature - steamTemperature / currentSteamAmount, boilingPoint);
            } else {
                waterTemperature = Math.max(0, waterTemperature - waterTemperature / currentWaterAmount);
                steamTemperature = boilingPoint;
            }
        }

        // If it was not hotter, cool down
        waterTemperature = Math.min(Math.max(0, waterTemperature), boilingPoint);
        steamTemperature = Math.min(Math.max(boilingPoint, steamTemperature), 375.0d);
    }

    private int convertFluids(int conversionAmount, Fluid from, Fluid to) {
        if (conversionAmount == 0) {
            return 0;
        }

        FluidStack convertedFrom = new FluidStack(from, conversionAmount);
        FluidStack convertedTo = new FluidStack(to, conversionAmount);

        convertedFrom = tank.drain(convertedFrom, IFluidHandler.FluidAction.SIMULATE);
        convertedTo.setAmount(tank.fill(convertedTo, IFluidHandler.FluidAction.SIMULATE));

        conversionAmount = Math.min(convertedFrom.getAmount(), convertedTo.getAmount());
        if (conversionAmount > 0) {
            convertedFrom.setAmount(conversionAmount);
            convertedTo.setAmount(conversionAmount);
            tank.fill(convertedTo, IFluidHandler.FluidAction.EXECUTE);
            tank.drain(convertedFrom, IFluidHandler.FluidAction.EXECUTE);
        }
        return conversionAmount;
    }

    private static double getBoilerPressure(int tankVolume, int waterAmount, int steamAmount, double temperature) {
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

    private double getWaterBoilingPoint(double pressure) {
        return pressure > 0.0d ? 234.175d / (17.08085d / Math.log(pressure / 0.0061d) - 1.0d) : 0.0d; // derived from magnus formula
    }

    private int getTotalVolume(FluidTankBlockEntity controller) {
        return controller.getTotalTankSize() * FluidTankBlockEntity.getCapacityMultiplier();
    }

    public void evaluate(FluidTankBlockEntity controller) {
        ((IBoilerDataFluidHandler) tank).create_embers$setCapacity(controller.getTotalTankSize() * FluidTankBlockEntity.getCapacityMultiplier());
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();

        if (tank != null) {
            tag.put("Content", ((IBoilerDataFluidHandler) tank).create_embers$writeToNBT(new CompoundTag()));
        }
        tag.putDouble("Pressure", pressure);
        tag.putDouble("WaterTemperature", waterTemperature);
        tag.putDouble("SteamTemperature", steamTemperature);

        return tag;
    }

    public void read(CompoundTag nbt) {
        if (tank != null && nbt.contains("Content")) {
            ((IBoilerDataFluidHandler) tank).create_embers$readFromNBT(nbt.getCompound("Content"));
            lastWaterAmount = tank.getFluidInTank(0).getAmount();
        }
        pressure = nbt.getDouble("Pressure");
        waterTemperature = nbt.getDouble("WaterTemperature");
        steamTemperature = nbt.getDouble("SteamTemperature");
    }
}
