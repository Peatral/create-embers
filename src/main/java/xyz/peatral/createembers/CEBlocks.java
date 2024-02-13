package xyz.peatral.createembers;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import xyz.peatral.createembers.content.fluid_vessel.FluidVesselBlock;
import xyz.peatral.createembers.content.stamp_base.StampBaseBlock;
import xyz.peatral.createembers.content.stamper.StamperBlock;
import xyz.peatral.createembers.content.stamper.StamperItem;
import xyz.peatral.createembers.content.steam_valve.SteamValveBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CEBlocks {
    public static final BlockEntry<StampBaseBlock> STAMP_BASE = CreateEmbers.registrate().block("stamp_base", StampBaseBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    static {
        CreateEmbers.registrate()
                .setCreativeTab(CECreativeTabs.BASE_CREATIVE_TAB);
    }

    public static final BlockEntry<StamperBlock> STAMPER = CreateEmbers.registrate().block("stamper", StamperBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .item(StamperItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<FluidVesselBlock> FLUID_VESSEL = CreateEmbers.registrate().block("fluid_vessel", FluidVesselBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.standardModel(c, p)))
            .simpleItem()
            .register();

    public static final BlockEntry<SteamValveBlock> STEAM_VALVE = CreateEmbers.registrate().block("steam_valve", SteamValveBlock::new)
            .initialProperties(SharedProperties::copperMetal)
			.transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .transform(BlockStressDefaults.setCapacity(1024.0))
            .transform(BlockStressDefaults.setGeneratorSpeed(SteamEngineBlock::getSpeedRange))
            .item()
			.transform(customItemModel())
            .register();

    // Load this class

    public static void register() {}
}
