package xyz.peatral.createembers;

import com.simibubi.create.foundation.data.AssetLookup;
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
    public static final BlockEntry<StampBaseBlock> STAMP_BASE = CreateEmbers.registrate().block("stamp_base", StampBaseBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    static {
        CreateEmbers.registrate()
                .creativeModeTab(() -> CreateEmbers.BASE_CREATIVE_TAB, "Create Embers");
    }

    public static final BlockEntry<StamperBlock> STAMPER = CreateEmbers.registrate().block("stamper", StamperBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .item(StamperItem::new)
            .transform(ModelGen.customItemModel())
            .register();

    public static final BlockEntry<FluidVesselBlock> FLUID_VESSEL = CreateEmbers.registrate().block("fluid_vessel", FluidVesselBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.color(MaterialColor.COLOR_GRAY))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    // Load this class

    public static void register() {}
}
