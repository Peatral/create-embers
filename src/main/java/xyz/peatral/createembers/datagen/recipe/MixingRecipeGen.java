package xyz.peatral.createembers.datagen.recipe;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.fluids.FluidStack;
import xyz.peatral.createembers.CEFluids;
import xyz.peatral.createembers.CEItems;
import xyz.peatral.createembers.CreateEmbers;

public class MixingRecipeGen extends ProcessingRecipeGen {
    GeneratedRecipe

    MOLTEN_DAWNSTONE = create(CreateEmbers.asResource("molten_dawnstone"), b -> b
            .require(AllTags.forgeFluidTag("molten_copper"), 250)
            .require(AllTags.forgeFluidTag("molten_gold"), 250)
            .requiresHeat(HeatCondition.SUPERHEATED)
            .output(new FluidStack(CEFluids.MOLTEN_DAWNSTONE.get().getSource(), 500))),
    MOLTEN_ELECTRUM = create(CreateEmbers.asResource("molten_electrum"), b -> b
            .require(AllTags.forgeFluidTag("molten_silver"), 250)
            .require(AllTags.forgeFluidTag("molten_gold"), 250)
            .requiresHeat(HeatCondition.SUPERHEATED)
            .output(new FluidStack(CEFluids.MOLTEN_ELECTRUM.get().getSource(), 500))),
    MOLTEN_BRONZE = create(CreateEmbers.asResource("molten_bronze"), b -> b
            .require(AllTags.forgeFluidTag("molten_copper"), 375)
            .require(AllTags.forgeFluidTag("molten_tin"), 125)
            .requiresHeat(HeatCondition.SUPERHEATED)
            .output(new FluidStack(CEFluids.MOLTEN_BRONZE.get().getSource(), 500)))

    ;

    public MixingRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
