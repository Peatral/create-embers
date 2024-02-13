package xyz.peatral.createembers.mixin;

import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.blockentity.CopperCellBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import xyz.peatral.createembers.util.IHaveExtendedGoggleInformation;

import java.util.List;

@Mixin(CopperCellBlockEntity.class)
public abstract class MixinCopperCell extends BlockEntity implements IHaveExtendedGoggleInformation {
    public MixinCopperCell(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedEmbersTooltip(tooltip, isPlayerSneaking, getCapability(EmbersCapabilities.EMBER_CAPABILITY));
    }
}
