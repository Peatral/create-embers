package xyz.peatral.createembers.ponder;

import com.simibubi.create.foundation.ponder.PonderTag;
import xyz.peatral.createembers.CEBlocks;
import xyz.peatral.createembers.CreateEmbers;

public class CEPonderTags {
    public static final PonderTag

    ADVANCED_FLUID_MANIPULATION = create("advanced_fluid_manipulation")
            .item(CEBlocks.FLUID_VESSEL.get(), true, false)
            .defaultLang("Advanced Fluid Manipulation", "How to modify and process more interesting fluids")
            .addToIndex();

    public static PonderTag create(String id) {
        return new PonderTag(CreateEmbers.asResource(id));
    }

    public static void register() {}
}
