package xyz.peatral.createembers.content.fluid_vessel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;

public class FluidVesselRenderer extends SafeBlockEntityRenderer<FluidVesselBlockEntity> {
    public FluidVesselRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(FluidVesselBlockEntity fluidVessel, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        // Render fluid

        float fluidLevel = renderFluid(fluidVessel, partialTicks, ms, buffer, light, overlay);
    }

    protected float renderFluid(FluidVesselBlockEntity fluidVessel, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                int light, int overlay) {
        SmartFluidTankBehaviour fluids = fluidVessel.getTankBehaviour();

        if (fluids == null)
            return 0;

        SmartFluidTankBehaviour.TankSegment tankSegment = fluids.getPrimaryTank();
        FluidStack renderedFluid = tankSegment.getRenderedFluid();
        if (renderedFluid.isEmpty())
            return 0;

        float fluidLevel = Mth.clamp(tankSegment.getFluidLevel().getValue(partialTicks), 0, 1);
        if (fluidLevel <= 0)
            return 0;

        fluidLevel = 1 - ((1 - fluidLevel) * (1 - fluidLevel));

        final float xMin = 4f / 16f;
        final float xMax = 12f / 16f;
        final float yMin = 2f / 16f;
        final float yMax = yMin + 13f / 16f * fluidLevel;
        final float zMin = 4f / 16f;
        final float zMax = 12f / 16f;

        FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light,
                false);

        return yMax;
    }
}
