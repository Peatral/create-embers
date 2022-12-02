package xyz.peatral.createembers;

import com.jozufozu.flywheel.core.PartialModel;

public class CEBlockPartials {
    public static PartialModel

        STAMPER_HEAD = block("stamper/head");

    private static PartialModel block(String path) {
        return new PartialModel(CreateEmbers.asResource("block/" + path));
    }

    public static void init() {
    }
}
