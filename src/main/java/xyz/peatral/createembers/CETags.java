package xyz.peatral.createembers;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import static xyz.peatral.createembers.CETags.NameSpace.FORGE;
import static xyz.peatral.createembers.CETags.NameSpace.MOD;

public class CETags {
    public enum NameSpace {
        MOD(CreateEmbers.ID, false, true),
        FORGE("forge")
        ;

        public final String id;
        public final boolean optionalDefault;
        public final boolean alwaysDatagenDefault;

        NameSpace(String id) {
            this(id, true, false);
        }

        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }

    public enum CEItemTags {
        CREATEEMBERS_INGOTS,
        NUGGETS(FORGE),
        PLATES(FORGE)
        ;

        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;

        CEItemTags() {
            this(MOD);
        }

        CEItemTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CEItemTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        CEItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        CEItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = AllTags.optionalTag(ForgeRegistries.ITEMS, id);
            } else {
                tag = ItemTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Item item) {
            return item.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }

        private static void init() {
        }
    }

    public static void init() {
        CEItemTags.init();
    }
}
