package xyz.peatral.createembers;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import xyz.peatral.createembers.content.fluid_vessel.FluidVesselRenderer;
import xyz.peatral.createembers.content.fluid_vessel.FluidVesselTileEntity;
import xyz.peatral.createembers.content.stamp_base.StampBaseRenderer;
import xyz.peatral.createembers.content.stamp_base.StampBaseTileEntity;
import xyz.peatral.createembers.content.stamper.StamperRenderer;
import xyz.peatral.createembers.content.stamper.StamperTileEntity;

public class CETileEntities {
    public static final BlockEntityEntry<StampBaseTileEntity> STAMP_BASE = CreateEmbers.registrate()
            .tileEntity("stamp_base", StampBaseTileEntity::new)
            .validBlocks(CEBlocks.STAMP_BASE)
            .renderer(() -> StampBaseRenderer::new)
            .register();

    public static final BlockEntityEntry<StamperTileEntity> STAMPER = CreateEmbers.registrate()
            .tileEntity("stamper", StamperTileEntity::new)
            .validBlocks(CEBlocks.STAMPER)
            .renderer(() -> StamperRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidVesselTileEntity> FLUID_VESSEL = CreateEmbers.registrate()
            .tileEntity("fluid_vessel", FluidVesselTileEntity::new)
            .validBlocks(CEBlocks.FLUID_VESSEL)
            .renderer(() -> FluidVesselRenderer::new)
            .register();

    // Load this class

    public static void register() {}
}
