package xyz.peatral.createembers;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import xyz.peatral.createembers.ponder.CEPonderIndex;

public class CreateEmbersClient {
    public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        CEBlockPartials.init();
        modEventBus.addListener(CreateEmbersClient::onClientSetup);
    }

    public static void onClientSetup(final FMLClientSetupEvent event) {
        CEPonderIndex.register();
        CEPonderIndex.registerTags();
    }
}
