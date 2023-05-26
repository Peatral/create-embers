package xyz.peatral.createembers.content.stamp_base;

import com.simibubi.create.foundation.item.SmartInventory;

public class StampBaseInventory extends SmartInventory {
    private StampBaseBlockEntity be;

    public StampBaseInventory(int slots, StampBaseBlockEntity be) {
        super(slots, be);
        this.be = be;
    }
}
