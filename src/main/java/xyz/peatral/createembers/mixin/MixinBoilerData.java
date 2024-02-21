package xyz.peatral.createembers.mixin;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.BoilerData;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.peatral.createembers.CELang;
import xyz.peatral.createembers.content.enhanced_boiler.EnhancedBoiler;

import java.util.List;

@Mixin(BoilerData.class)
public abstract class MixinBoilerData implements IHaveGoggleInformation {
    @Unique
    public EnhancedBoiler create_embers$enhancedBoiler = new EnhancedBoiler(createHandler());

    @Shadow(remap = false)
    public abstract boolean isActive();

    @Shadow(remap = false)
    public int activeHeat;

    @Shadow(remap = false)
    public abstract BoilerData.BoilerFluidHandler createHandler();

    @Inject(method = "createHandler", at = @At("RETURN"), cancellable = true, remap = false)
    public void createHandler(CallbackInfoReturnable<BoilerData.BoilerFluidHandler> cir) {
        if (create_embers$enhancedBoiler != null && create_embers$enhancedBoiler.tank != null) {
            cir.setReturnValue(create_embers$enhancedBoiler.tank);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"), remap = false)
    public void tick(FluidTankBlockEntity controller, CallbackInfo ci) {
        if (!isActive())
            return;

        create_embers$enhancedBoiler.tick(controller, activeHeat);

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

    @Inject(method = "evaluate", at = @At("RETURN"), remap = false)
    public void evaluate(FluidTankBlockEntity controller, CallbackInfoReturnable<Boolean> cir) {
        create_embers$enhancedBoiler.evaluate(controller);
    }

    @Inject(method = "write", at = @At("RETURN"), remap = false)
    public void write(CallbackInfoReturnable<CompoundTag> cir) {
        if (create_embers$enhancedBoiler != null) {
            cir.getReturnValue().put("EnhancedBoiler", create_embers$enhancedBoiler.write());
        }
    }

    @Inject(method = "read", at = @At("RETURN"), remap = false)
    public void read(CompoundTag nbt, int boilerSize, CallbackInfo ci) {
        if (create_embers$enhancedBoiler != null && nbt.contains("EnhancedBoiler")) {
            create_embers$enhancedBoiler.read(nbt.getCompound("EnhancedBoiler"));
        }
    }

    @Inject(method = "addToGoggleTooltip", at = @At("RETURN"), remap = false)
    public void addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, int boilerSize, CallbackInfoReturnable<Boolean> cir) {
        if (isActive() && create_embers$enhancedBoiler != null) {
            tooltip.add(Components.immutableEmpty());

            CELang.translate("gui.goggles.boiler.steam_info")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 0);
            CELang.builder()
                    .translate("gui.goggles.boiler.pressure", Lang.number(create_embers$enhancedBoiler.pressure)
                            .style(ChatFormatting.GOLD)
                            .add(CELang.translate("generic.unit.pressure")))
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            CELang.translate("gui.goggles.boiler.water_temperature", Lang.number(create_embers$enhancedBoiler.waterTemperature)
                            .style(ChatFormatting.GOLD)
                            .add(CELang.translate("generic.unit.temperature")))
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            CELang.translate("gui.goggles.boiler.steam_temperature", Lang.number(create_embers$enhancedBoiler.steamTemperature)
                            .style(ChatFormatting.GOLD)
                            .add(CELang.translate("generic.unit.temperature")))
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            CELang.translate("gui.goggles.boiler.boiler_temperature", Lang.number(create_embers$enhancedBoiler.boilerTemperature)
                            .style(ChatFormatting.GOLD)
                            .add(CELang.translate("generic.unit.temperature")))
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);
            CELang.translate("gui.goggles.boiler.boiling_point", Lang.number(create_embers$enhancedBoiler.boilingPoint)
                            .style(ChatFormatting.GOLD)
                            .add(CELang.translate("generic.unit.temperature")))
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip, 1);

            tooltip.add(Components.immutableEmpty());

            double avgConversion = 0.0d;
            for (int conversion : create_embers$enhancedBoiler.conversionOverTime) {
                avgConversion += conversion;
            }
            avgConversion /= create_embers$enhancedBoiler.conversionOverTime.length;

            CELang.builder()
                    .add(Lang.translate("boiler.per_tick",
                                    Lang.number(Math.abs(avgConversion))
                                            .style(ChatFormatting.BLUE)
                                            .add(Lang.translate("generic.unit.millibuckets")))
                                            .space()
                                            .add(avgConversion < 0 ?
                                                    CELang.translate("gui.goggles.boiler.condensation")
                                                            .style(ChatFormatting.AQUA) :
                                                    CELang.translate("gui.goggles.boiler.evaporation")
                                                            .style(ChatFormatting.LIGHT_PURPLE))
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

            tooltip.add(Components.immutableEmpty());
            containedFluidTooltip(tooltip, isPlayerSneaking, LazyOptional.of(() -> create_embers$enhancedBoiler.tank));
        }
    }
}
