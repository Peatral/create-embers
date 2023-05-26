package xyz.peatral.createembers;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;

public class CEFluids {

    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_IRON = simpleFluid("molten_iron", "Molten Iron");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_GOLD = simpleFluid("molten_gold", "Molten Gold");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_COPPER = simpleFluid("molten_copper", "Molten Copper");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_ALUMINUM = simpleFluid("molten_aluminum", "Molten Aluminum");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_BRONZE = simpleFluid("molten_bronze", "Molten Bronze");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_DAWNSTONE = simpleFluid("molten_dawnstone", "Molten Dawnstone");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_ELECTRUM = simpleFluid("molten_electrum", "Molten Electrum");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_LEAD = simpleFluid("molten_lead", "Molten Lead");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_NICKEL = simpleFluid("molten_nickel", "Molten Nickel");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_SILVER = simpleFluid("molten_silver", "Molten Silver");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_TIN = simpleFluid("molten_tin", "Molten Tin");


    public static FluidEntry<ForgeFlowingFluid.Flowing> simpleFluid(String id, String name) {
        return standardFluid(id, NoColorFluidAttributes::new)
                .lang(name)
                .properties(b -> b.viscosity(6000)
                        .density(2000)
                        .lightLevel(15)
                        .temperature(900))
                .fluidProperties(p -> p.levelDecreasePerBlock(2)
                        .tickRate(25)
                        .slopeFindDistance(3)
                        .explosionResistance(100f))
                .tag(AllTags.forgeFluidTag(id))
                .source(ForgeFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
                .bucket()
                .defaultModel()
                .tag(AllTags.forgeItemTag("buckets/" + id))
                .transform(bucketModel(id))
                .register();
    }

    public static FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> standardFluid(String name,
                                                                                   FluidBuilder.FluidTypeFactory typeFactory) {
        return CreateEmbers.registrate().fluid(name, CreateEmbers.asResource("fluid/" + name + "_still"), CreateEmbers.asResource("fluid/" + name + "_flow"),
                typeFactory);
    }

    public static <I extends BucketItem, P> NonNullFunction<ItemBuilder<I, P>, P> bucketModel(String fluid) {
        return b -> b.model((ctx, prov) -> prov.getBuilder(prov.name(() -> ctx.getEntry().asItem()))
                        .parent(new ModelFile.UncheckedModelFile("forge:item/bucket"))
                        .customLoader(DynamicFluidContainerModelBuilder::begin)
                        .fluid(ForgeRegistries.FLUIDS.getValue(CreateEmbers.asResource(fluid)))
                        .end()
                        .texture("base", "minecraft:item/bucket")
                        .texture("fluid", CreateEmbers.asResource("item/drip_bucket")))
                .build();
    }

    // Load this class

    public static void register() {}

    /**
     * Removing alpha from tint prevents optifine from forcibly applying biome
     * colors to modded fluids (Makes translucent fluids disappear)
     */
    private static class NoColorFluidAttributes extends com.simibubi.create.AllFluids.TintedFluidType {

        public NoColorFluidAttributes(Properties properties, ResourceLocation stillTexture,
                                      ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

    }
}
