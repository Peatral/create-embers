package xyz.peatral.createembers.content.stamper;

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

public class StamperBlock extends Block implements ITE<StamperTileEntity> {
    public StamperBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<StamperTileEntity> getTileEntityClass() {
        return StamperTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends StamperTileEntity> getTileEntityType() {
        return CETileEntities.STAMPER.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);

        return onTileEntityUse(worldIn, pos, te -> {
            IItemHandlerModifiable inv = te.itemCapability.orElse(new ItemStackHandler(1));
            if (inv.getStackInSlot(0).isEmpty()) {
                ItemStack stack = heldItem.copy();
                stack.setCount(1);
                ItemHandlerHelper.insertItem(te.inventory, stack, false);
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
        withTileEntityDo(worldIn, pos, te -> {
            ItemHelper.dropContents(worldIn, pos, te.inventory);
        });
        worldIn.removeBlockEntity(pos);
    }
}
