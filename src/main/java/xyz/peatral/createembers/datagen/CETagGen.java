package xyz.peatral.createembers.datagen;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import xyz.peatral.createembers.CETags;
import xyz.peatral.createembers.CreateEmbers;

public class CETagGen {
    public static final CreateRegistrate REGISTRATE = CreateEmbers.registrate();
    private static void genItemTags(RegistrateTagsProvider<Item> prov) {
        prov.tag(ItemTags.BEACON_PAYMENT_ITEMS)
                .addTag(CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);

        prov.tag(Tags.Items.INGOTS)
                .addTag(CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);

        prov.tag(CETags.CEItemTags.STAMPS.tag)
                .addTag(CETags.CEItemTags.CREATEEMBERS_STAMPS.tag);
    }

    public static void datagen() {
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, CETagGen::genItemTags);
    }
}
