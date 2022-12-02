package xyz.peatral.createembers.content.stamper;

import com.simibubi.create.content.contraptions.components.AssemblyOperatorBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import xyz.peatral.createembers.CEBlocks;

public class StamperItem extends AssemblyOperatorBlockItem {
    public StamperItem(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    protected boolean operatesOn(BlockState placedOnState) {
        return CEBlocks.STAMP_BASE.has(placedOnState);
    }
}
