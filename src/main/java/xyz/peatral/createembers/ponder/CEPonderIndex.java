package xyz.peatral.createembers.ponder;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import xyz.peatral.createembers.CEBlocks;
import xyz.peatral.createembers.CreateEmbers;

public class CEPonderIndex {
    private static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateEmbers.ID);
    private static final CreateRegistrate REGISTRATE = CreateEmbers.registrate();

    public static void register() {
        HELPER.forComponents(CEBlocks.FLUID_VESSEL)
                .addStoryBoard("fluid_vessel/fill_and_empty", FluidVesselScenes::fill_and_empty);
    }

    public static void registerTags() {
        PonderRegistry.TAGS.forTag(CEPonderTags.ADVANCED_FLUID_MANIPULATION)
                .add(CEBlocks.FLUID_VESSEL);
    }

    public static void registerLang() {
        PonderLocalization.provideRegistrateLang(REGISTRATE);
    }
}
