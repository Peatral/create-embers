package xyz.peatral.createembers.content.stamp_base;

import com.simibubi.create.foundation.item.SmartInventory;

public class StampBaseInventory extends SmartInventory {
    private StampBaseTileEntity te;

    public StampBaseInventory(int slots, StampBaseTileEntity te) {
        super(slots, te);
        this.te = te;
    }
}
