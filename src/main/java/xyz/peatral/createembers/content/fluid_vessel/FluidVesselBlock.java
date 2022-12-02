package xyz.peatral.createembers.content.fluid_vessel;

import com.simibubi.create.content.contraptions.fluids.actors.GenericItemFilling;
import com.simibubi.create.content.contraptions.processing.EmptyingByBasin;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.peatral.createembers.CETileEntities;

public class FluidVesselBlock extends Block implements ITE<FluidVesselTileEntity> {
    public FluidVesselBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<FluidVesselTileEntity> getTileEntityClass() {
        return FluidVesselTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidVesselTileEntity> getTileEntityType() {
        return CETileEntities.FLUID_VESSEL.get();
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
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        worldIn.removeBlockEntity(pos);
    }
}
