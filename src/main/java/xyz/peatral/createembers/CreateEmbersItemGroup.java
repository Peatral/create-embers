package xyz.peatral.createembers;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreateEmbersItemGroup extends CreativeModeTab {

    public CreateEmbersItemGroup() {
        super(CreateEmbers.ID);
    }

    @Override
    public ItemStack makeIcon() {
        return CEBlocks.STAMP_BASE.asStack();
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> list) {
        super.fillItemList(list);
    }
}
