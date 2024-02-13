package xyz.peatral.createembers;

import com.rekindled.embers.RegistryManager;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

public class CEItems {
    static {
        CreateEmbers.registrate()
                .setCreativeTab(CECreativeTabs.BASE_CREATIVE_TAB);
    }

    // ------------- Stamps -------------

    public static final ItemEntry<Item> STAMP_INGOT = ingredient("stamp_ingot", "Bar Stamp");
    public static final ItemEntry<Item> STAMP_FLAT = ingredient("stamp_flat", "Flat Stamp");
    public static final ItemEntry<Item> STAMP_PLATE = ingredient("stamp_plate", "Plate Stamp");
    public static final ItemEntry<Item> STAMP_GEAR = ingredient("stamp_gear", "Gear Stamp");

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

    static {
       GogglesItem.addIsWearingPredicate(player -> RegistryManager.ASHEN_GOGGLES.get().equals(player.getItemBySlot(EquipmentSlot.HEAD).getItem()));
    }

    private static ItemEntry<Item> ingredient(String name) {
        return CreateEmbers.registrate().item(name, Item::new)
                .register();
    }

    private static ItemEntry<Item> ingredient(String id, String name) {
        return CreateEmbers.registrate().item(id, Item::new)
                .lang(name)
                .register();
    }

    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return CreateEmbers.registrate().item(name, Item::new)
                .tag(tags)
                .register();
    }

    // Load this class

    public static void register() {}
}
