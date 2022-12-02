package xyz.peatral.createembers.content.stamp_base;

import com.simibubi.create.content.contraptions.fluids.actors.GenericItemFilling;
import com.simibubi.create.content.contraptions.processing.EmptyingByBasin;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import xyz.peatral.createembers.CEBlocks;
import xyz.peatral.createembers.CETileEntities;

public class StampBaseBlock extends Block implements ITE<StampBaseTileEntity> {

    public StampBaseBlock(Properties properties) {
        super(properties);
    }


    @Override
    public Class<StampBaseTileEntity> getTileEntityClass() {
        return StampBaseTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends StampBaseTileEntity> getTileEntityType() {
        return CETileEntities.STAMP_BASE.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);

        return onTileEntityUse(worldIn, pos, te -> {
            if (!heldItem.isEmpty()) {
                if (FluidHelper.tryEmptyItemIntoTE(worldIn, player, handIn, heldItem, te))
                    return InteractionResult.SUCCESS;
                if (FluidHelper.tryFillItemFromTE(worldIn, player, handIn, heldItem, te))
                    return InteractionResult.SUCCESS;

                if (EmptyingByBasin.canItemBeEmptied(worldIn, heldItem)
                        || GenericItemFilling.canItemBeFilled(worldIn, heldItem))
                    return InteractionResult.SUCCESS;
                if (heldItem.getItem()
                        .equals(Items.SPONGE)
                        && !te.getCapability(ForgeCapabilities.FLUID_HANDLER)
                        .map(iFluidHandler -> iFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE))
                        .orElse(FluidStack.EMPTY)
                        .isEmpty()) {
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.PASS;
            }

            IItemHandlerModifiable inv = te.itemCapability.orElse(new ItemStackHandler(1));
            boolean success = false;
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getStackInSlot(slot);
                if (stackInSlot.isEmpty())
                    continue;
                player.getInventory()
                        .placeItemBackInInventory(stackInSlot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
                success = true;
            }
            if (success)
                worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f,
                        1f + RandomSource.create().nextFloat());
            te.onEmptied();
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);
        if (!CEBlocks.STAMP_BASE.has(worldIn.getBlockState(entityIn.blockPosition().below())))
            return;
        if (!(entityIn instanceof ItemEntity))
            return;
        if (!entityIn.isAlive())
            return;
        ItemEntity itemEntity = (ItemEntity) entityIn;
        withTileEntityDo(worldIn, entityIn.blockPosition().below(), te -> {

            // Tossed items bypass the quarter-stack limit
            te.inputInventory.withMaxStackSize(64);
            ItemStack insertItem = ItemHandlerHelper.insertItem(te.inputInventory, itemEntity.getItem()
                    .copy(), false);
            te.inputInventory.withMaxStackSize(16);

            if (insertItem.isEmpty()) {
                itemEntity.discard();
                return;
            }

            itemEntity.setItem(insertItem);
        });
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        withTileEntityDo(worldIn, pos, te -> {
            ItemHelper.dropContents(worldIn, pos, te.inputInventory);
        });
        worldIn.removeBlockEntity(pos);
    }
}
