package xyz.peatral.createembers.content.stamp_base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import xyz.peatral.createembers.util.RenderUtil;

import java.util.Random;

public class StampBaseRenderer extends SafeBlockEntityRenderer<StampBaseBlockEntity> {
    public StampBaseRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(StampBaseBlockEntity stampBase, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        // Render item / fluid

        float fluidLevel = renderFluid(stampBase, partialTicks, ms, buffer, light, overlay);
        float level = Mth.clamp(fluidLevel, 12 / 16f, 15 / 16f);

        ms.pushPose();
        ms.translate(.5f, level, .5f);

        IItemHandlerModifiable inv = stampBase.itemCapability.orElse(new ItemStackHandler());

        ItemStack stack = inv.getStackInSlot(0);
        if (!stack.isEmpty()) {
            RenderUtil.renderItem(stampBase.getLevel(), ms, buffer, light, overlay, stack, 0, new Random(1), new Vec3(0, 0, 0));
        }

        ms.popPose();
    }

    protected float renderFluid(StampBaseBlockEntity stampBase, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                int light, int overlay) {

        SmartFluidTankBehaviour fluids = stampBase.getBehaviour(SmartFluidTankBehaviour.TYPE);

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
        final float yMin = 12f / 16f;
        final float yMax = yMin + 3f / 16f * fluidLevel;
        final float zMin = 4f / 16f;
        final float zMax = 12f / 16f;

        FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light,
                false);

        return yMax;
    }
}
