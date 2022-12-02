package xyz.peatral.createembers.datagen.recipe;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.DataGenerator;
import xyz.peatral.createembers.CEItems;
import xyz.peatral.createembers.CreateEmbers;

public class PressingRecipeGen extends ProcessingRecipeGen {
    CreateRecipeProvider.GeneratedRecipe

    ALUMINUM_PLATE = create(CreateEmbers.asResource("aluminum_ingot"), b -> b.require(AllTags.forgeItemTag("ingots/aluminum"))
            .output(CEItems.ALUMINUM_PLATE.get())),
    BRONZE_PLATE = create(CreateEmbers.asResource("bronze_ingot"), b -> b.require(AllTags.forgeItemTag("ingots/bronze"))
            .output(CEItems.BRONZE_PLATE.get())),
    DAWNSTONE_PLATE = create(CreateEmbers.asResource("dawnstone_ingot"), b -> b.require(AllTags.forgeItemTag("ingots/dawnstone"))
            .output(CEItems.DAWNSTONE_PLATE.get())),
    ELECTRUM_PLATE = create(CreateEmbers.asResource("electrum_ingot"), b -> b.require(AllTags.forgeItemTag("ingots/electrum"))
            .output(CEItems.ELECTRUM_PLATE.get())),
    LEAD_PLATE = create(CreateEmbers.asResource("lead_ingot"), b -> b.require(AllTags.forgeItemTag("ingots/lead"))
            .output(CEItems.LEAD_PLATE.get())),
    NICKEL_PLATE = create(CreateEmbers.asResource("nickel_ingot"), b -> b.require(AllTags.forgeItemTag("ingots/nickel"))
            .output(CEItems.NICKEL_PLATE.get())),
    SILVER_PLATE = create(CreateEmbers.asResource("silver_ingot"), b -> b.require(AllTags.forgeItemTag("ingots/silver"))
            .output(CEItems.SILVER_PLATE.get())),
    TIN_PLATE = create(CreateEmbers.asResource("tin_ingot"), b -> b.require(AllTags.forgeItemTag("ingots/tin"))
            .output(CEItems.TIN_PLATE.get()))
    ;

    public PressingRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}
