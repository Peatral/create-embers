package xyz.peatral.createembers;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import xyz.peatral.createembers.crafting.CERecipes;

@Mod(CreateEmbers.ID)
public class CreateEmbers {
    public static final String ID = "createembers";
    public static final String NAME = "Create Embers";
    public static final String VERSION = "0.0.1";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab BASE_CREATIVE_TAB = new CreateEmbersItemGroup();

    private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(ID);

    public CreateEmbers() {
        onCtor();
    }

    public static void onCtor() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        CETags.init();
        CEBlocks.register();
        CEItems.register();
        CEFluids.register();
        CETileEntities.register();
        CERecipes.register(modEventBus);

        modEventBus.addListener(CreateEmbers::init);
        modEventBus.addListener(EventPriority.LOWEST, CreateEmbers::gatherData);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CreateEmbersClient.prepareClient(modEventBus, forgeEventBus));
    }

    public static void init(final FMLCommonSetupEvent event) {
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
        }
        if (event.includeServer()) {
        }
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
}
