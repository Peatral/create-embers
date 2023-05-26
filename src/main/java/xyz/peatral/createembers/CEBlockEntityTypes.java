package xyz.peatral.createembers;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import xyz.peatral.createembers.content.fluid_vessel.FluidVesselBlockEntity;
import xyz.peatral.createembers.content.fluid_vessel.FluidVesselRenderer;
import xyz.peatral.createembers.content.stamp_base.StampBaseBlockEntity;
import xyz.peatral.createembers.content.stamp_base.StampBaseRenderer;
import xyz.peatral.createembers.content.stamper.StamperBlockEntity;
import xyz.peatral.createembers.content.stamper.StamperRenderer;

public class CEBlockEntityTypes {
    public static final BlockEntityEntry<StampBaseBlockEntity> STAMP_BASE = CreateEmbers.registrate()
            .blockEntity("stamp_base", StampBaseBlockEntity::new)
            .validBlocks(CEBlocks.STAMP_BASE)
            .renderer(() -> StampBaseRenderer::new)
            .register();

    public static final BlockEntityEntry<StamperBlockEntity> STAMPER = CreateEmbers.registrate()
            .blockEntity("stamper", StamperBlockEntity::new)
            .validBlocks(CEBlocks.STAMPER)
            .renderer(() -> StamperRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidVesselBlockEntity> FLUID_VESSEL = CreateEmbers.registrate()
            .blockEntity("fluid_vessel", FluidVesselBlockEntity::new)
            .validBlocks(CEBlocks.FLUID_VESSEL)
            .renderer(() -> FluidVesselRenderer::new)
            .register();

    // Load this class

    public static void register() {}
}
