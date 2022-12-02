package xyz.peatral.createembers.content.stamper;

import com.simibubi.create.foundation.item.SmartInventory;

public class StamperInventory extends SmartInventory {
    private StamperTileEntity te;

    public StamperInventory(int slots, StamperTileEntity te) {
        super(slots, te);
        this.te = te;
        withMaxStackSize(1);
    }
}
