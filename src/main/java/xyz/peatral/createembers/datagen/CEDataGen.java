package xyz.peatral.createembers.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.peatral.createembers.CreateEmbers;
import xyz.peatral.createembers.datagen.recipe.CookingRecipeGen;
import xyz.peatral.createembers.datagen.recipe.MixingRecipeGen;
import xyz.peatral.createembers.datagen.recipe.PressingRecipeGen;
import xyz.peatral.createembers.datagen.recipe.StampingRecipeGen;
import xyz.peatral.createembers.ponder.CEPonderIndex;
import xyz.peatral.createembers.ponder.CEPonderTags;

@Mod.EventBusSubscriber(modid = CreateEmbers.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CEDataGen {
    @SubscribeEvent
    public static void onDatagen(GatherDataEvent event) {
        CETagGen.datagen();

        DataGenerator gen = event.getGenerator();
        if (event.includeServer()) {
            gen.addProvider(true, new StampingRecipeGen(gen));
            gen.addProvider(true, new PressingRecipeGen(gen));
            gen.addProvider(true, new MixingRecipeGen(gen));
            gen.addProvider(true, new CookingRecipeGen(gen));
        }
        if (event.includeClient()) {
            CELangGen.prepare();

            CEPonderTags.register();
            CEPonderIndex.register();
            CEPonderIndex.registerLang();
        }
    }
}
