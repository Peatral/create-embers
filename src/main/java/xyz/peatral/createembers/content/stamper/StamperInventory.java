package xyz.peatral.createembers.content.stamper;

import com.simibubi.create.foundation.item.SmartInventory;

public class StamperInventory extends SmartInventory {
    private StamperBlockEntity be;

    public StamperInventory(int slots, StamperBlockEntity be) {
        super(slots, be);
        this.be = be;
        withMaxStackSize(1);
    }
}
