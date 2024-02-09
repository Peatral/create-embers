package xyz.peatral.createembers.datagen;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import xyz.peatral.createembers.CETags;
import xyz.peatral.createembers.CreateEmbers;

public class CETagGen {
    private static void genItemTags(RegistrateTagsProvider<Item> prov) {
        prov.addTag(ItemTags.BEACON_PAYMENT_ITEMS)
                .addTag(CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);

        prov.addTag(Tags.Items.INGOTS)
                .addTag(CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);
    }

    public static void datagen() {
        CreateEmbers.registrate().addDataGenerator(ProviderType.ITEM_TAGS, CETagGen::genItemTags);
    }
}
