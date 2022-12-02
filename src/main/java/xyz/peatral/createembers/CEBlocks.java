package xyz.peatral.createembers;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import xyz.peatral.createembers.content.fluid_vessel.FluidVesselBlock;
import xyz.peatral.createembers.content.stamp_base.StampBaseBlock;
import xyz.peatral.createembers.content.stamper.StamperBlock;
import xyz.peatral.createembers.content.stamper.StamperItem;

public class CEBlocks {
    private static final CreateRegistrate REGISTRATE = CreateEmbers.registrate()
            .creativeModeTab(() -> CreateEmbers.BASE_CREATIVE_TAB, "Create Embers");

    static {
        REGISTRATE.startSection(AllSections.UNASSIGNED);
    }

    public static final BlockEntry<StampBaseBlock> STAMP_BASE = REGISTRATE.block("stamp_base", StampBaseBlock::new)
        .initialProperties(SharedProperties::stone)
        .properties(BlockBehaviour.Properties::noOcclusion)
        .properties(p -> p.color(MaterialColor.COLOR_GRAY))
        .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.standardModel(c, p)))
        .simpleItem()
        .register();

    public static final BlockEntry<StamperBlock> STAMPER = REGISTRATE.block("stamper", StamperBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .item(StamperItem::new)
            .transform(ModelGen.customItemModel())
            .register();

    public static final BlockEntry<FluidVesselBlock> FLUID_VESSEL = REGISTRATE.block("fluid_vessel", FluidVesselBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    // Load this class

    public static void register() {}
}
