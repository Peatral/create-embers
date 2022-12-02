package xyz.peatral.createembers;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CEItems {
    private static final CreateRegistrate REGISTRATE = CreateEmbers.registrate()
            .creativeModeTab(() -> CreateEmbers.BASE_CREATIVE_TAB);

    static {
        REGISTRATE.startSection(AllSections.UNASSIGNED);
    }

    // ------------- Stamps -------------

    public static final ItemEntry<Item> STAMP_INGOT_RAW = ingredient("stamp_ingot_raw", "Raw Bar Stamp");
    public static final ItemEntry<Item> STAMP_FLAT_RAW = ingredient("stamp_flat_raw", "Raw Flat Stamp");
    public static final ItemEntry<Item> STAMP_PLATE_RAW = ingredient("stamp_plate_raw", "Raw Plate Stamp");
    public static final ItemEntry<Item> STAMP_GEAR_RAW = ingredient("stamp_gear_raw", "Raw Gear Stamp");
    public static final ItemEntry<Item> STAMP_INGOT = taggedIngredient("stamp_ingot", "Bar Stamp", CETags.ceItemTag("stamps/ingots"), CETags.CEItemTags.CREATEEMBERS_STAMPS.tag);
    public static final ItemEntry<Item> STAMP_FLAT = taggedIngredient("stamp_flat", "Flat Stamp", CETags.ceItemTag("stamps/flat"), CETags.CEItemTags.CREATEEMBERS_STAMPS.tag);
    public static final ItemEntry<Item> STAMP_PLATE = taggedIngredient("stamp_plate", "Plate Stamp", CETags.ceItemTag("stamps/plates"), CETags.CEItemTags.CREATEEMBERS_STAMPS.tag);
    public static final ItemEntry<Item> STAMP_GEAR = taggedIngredient("stamp_gear", "Gear Stamp", CETags.ceItemTag("stamps/gears"), CETags.CEItemTags.CREATEEMBERS_STAMPS.tag);

    // ------------- Ingots -------------

    public static final ItemEntry<Item> ALUMINUM_INGOT = taggedIngredient("aluminum_ingot", AllTags.forgeItemTag("ingots/aluminum"), CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);
    public static final ItemEntry<Item> BRONZE_INGOT = taggedIngredient("bronze_ingot", AllTags.forgeItemTag("ingots/bronze"), CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);
    public static final ItemEntry<Item> DAWNSTONE_INGOT = taggedIngredient("dawnstone_ingot", AllTags.forgeItemTag("ingots/dawnstone"), CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);
    public static final ItemEntry<Item> ELECTRUM_INGOT = taggedIngredient("electrum_ingot", AllTags.forgeItemTag("ingots/electrum"), CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);
    public static final ItemEntry<Item> LEAD_INGOT = taggedIngredient("lead_ingot", AllTags.forgeItemTag("ingots/lead"), CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);
    public static final ItemEntry<Item> NICKEL_INGOT = taggedIngredient("nickel_ingot", AllTags.forgeItemTag("ingots/nickel"), CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);
    public static final ItemEntry<Item> SILVER_INGOT = taggedIngredient("silver_ingot", AllTags.forgeItemTag("ingots/silver"), CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);
    public static final ItemEntry<Item> TIN_INGOT = taggedIngredient("tin_ingot", AllTags.forgeItemTag("ingots/tin"), CETags.CEItemTags.CREATEEMBERS_INGOTS.tag);

    // ------------- Plates -------------

    public static final ItemEntry<Item> ALUMINUM_PLATE = taggedIngredient("aluminum_plate", AllTags.forgeItemTag("plates/aluminum"), CETags.CEItemTags.PLATES.tag);
    public static final ItemEntry<Item> BRONZE_PLATE = taggedIngredient("bronze_plate", AllTags.forgeItemTag("plates/bronze"), CETags.CEItemTags.PLATES.tag);
    public static final ItemEntry<Item> DAWNSTONE_PLATE = taggedIngredient("dawnstone_plate", AllTags.forgeItemTag("plates/dawnstone"), CETags.CEItemTags.PLATES.tag);
    public static final ItemEntry<Item> ELECTRUM_PLATE = taggedIngredient("electrum_plate", AllTags.forgeItemTag("plates/electrum"), CETags.CEItemTags.PLATES.tag);
    public static final ItemEntry<Item> LEAD_PLATE = taggedIngredient("lead_plate", AllTags.forgeItemTag("plates/lead"), CETags.CEItemTags.PLATES.tag);
    public static final ItemEntry<Item> NICKEL_PLATE = taggedIngredient("nickel_plate", AllTags.forgeItemTag("plates/nickel"), CETags.CEItemTags.PLATES.tag);
    public static final ItemEntry<Item> SILVER_PLATE = taggedIngredient("silver_plate", AllTags.forgeItemTag("plates/silver"), CETags.CEItemTags.PLATES.tag);
    public static final ItemEntry<Item> TIN_PLATE = taggedIngredient("tin_plate", AllTags.forgeItemTag("plates/tin"), CETags.CEItemTags.PLATES.tag);

    // ------------- Nuggets -------------

    public static final ItemEntry<Item> ALUMINUM_NUGGET = taggedIngredient("aluminum_nugget", AllTags.forgeItemTag("nuggets/aluminum"), CETags.CEItemTags.NUGGETS.tag);
    public static final ItemEntry<Item> BRONZE_NUGGET = taggedIngredient("bronze_nugget", AllTags.forgeItemTag("nuggets/bronze"), CETags.CEItemTags.NUGGETS.tag);
    public static final ItemEntry<Item> DAWNSTONE_NUGGET = taggedIngredient("dawnstone_nugget", AllTags.forgeItemTag("nuggets/dawnstone"), CETags.CEItemTags.NUGGETS.tag);
    public static final ItemEntry<Item> ELECTRUM_NUGGET = taggedIngredient("electrum_nugget", AllTags.forgeItemTag("nuggets/electrum"), CETags.CEItemTags.NUGGETS.tag);
    public static final ItemEntry<Item> LEAD_NUGGET = taggedIngredient("lead_nugget", AllTags.forgeItemTag("nuggets/lead"), CETags.CEItemTags.NUGGETS.tag);
    public static final ItemEntry<Item> NICKEL_NUGGET = taggedIngredient("nickel_nugget", AllTags.forgeItemTag("nuggets/nickel"), CETags.CEItemTags.NUGGETS.tag);
    public static final ItemEntry<Item> SILVER_NUGGET = taggedIngredient("silver_nugget", AllTags.forgeItemTag("nuggets/silver"), CETags.CEItemTags.NUGGETS.tag);
    public static final ItemEntry<Item> TIN_NUGGET = taggedIngredient("tin_nugget", AllTags.forgeItemTag("nuggets/tin"), CETags.CEItemTags.NUGGETS.tag);

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }

    private static ItemEntry<Item> ingredient(String id, String name) {
        return REGISTRATE.item(id, Item::new)
                .lang(name)
                .register();
    }

    private static ItemEntry<Item> taggedIngredient(String id, String name, TagKey<Item>... tags) {
        return REGISTRATE.item(id, Item::new)
                .lang(name)
                .tag(tags)
                .register();
    }

    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }

    // Load this class

    public static void register() {}
}
