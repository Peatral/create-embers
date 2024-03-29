package xyz.peatral.createembers.content.stamper;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import xyz.peatral.createembers.CEBlockEntityTypes;

public class StamperBlock extends Block implements IBE<StamperBlockEntity> {
    public StamperBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<StamperBlockEntity> getBlockEntityClass() {
        return StamperBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StamperBlockEntity> getBlockEntityType() {
        return CEBlockEntityTypes.STAMPER.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);

        return onBlockEntityUse(worldIn, pos, be -> {
            IItemHandlerModifiable inv = be.itemCapability.orElse(new ItemStackHandler(1));
            if (inv.getStackInSlot(0).isEmpty()) {
                ItemStack stack = heldItem.copy();
                stack.setCount(1);
                ItemHandlerHelper.insertItem(be.inventory, stack, false);
                heldItem.setCount(heldItem.getCount() - 1);
                return InteractionResult.SUCCESS;
            }

            ItemStack stackInSlot = inv.getStackInSlot(0);
            player.getInventory().placeItemBackInInventory(stackInSlot);
            inv.setStackInSlot(0, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        withBlockEntityDo(worldIn, pos, be -> {
            ItemHelper.dropContents(worldIn, pos, be.inventory);
        });
        worldIn.removeBlockEntity(pos);
    }
}
