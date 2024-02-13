package xyz.peatral.createembers.mixin;

import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.api.EmbersAPI;
import com.rekindled.embers.api.power.IEmberPacketProducer;
import com.rekindled.embers.api.power.IEmberPacketReceiver;
import com.rekindled.embers.api.tile.ITargetable;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WrenchItem.class)
public abstract class MixinWrench extends Item {

    public MixinWrench(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructorReturn(Properties pProperties, CallbackInfo ci) {
        EmbersAPI.registerLinkingHammer(this);
        EmbersAPI.registerHammerTargetGetter(this);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        CompoundTag nbt = stack.getOrCreateTag();
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockEntity tile = world.getBlockEntity(pos);
        if (world != null && nbt.contains("targetWorld") && world.dimension().location().toString().equals(nbt.getString("targetWorld"))) {
            BlockPos targetPos = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
            BlockEntity targetTile = world.getBlockEntity(targetPos);
            if (targetTile instanceof IEmberPacketProducer) {
                if (tile instanceof IEmberPacketReceiver) {
                    ((IEmberPacketProducer)targetTile).setTargetPosition(pos, Direction.byName(nbt.getString("targetFace")));
                    world.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5F, 1.5F + world.random.nextFloat() * 0.1F, false);
                    nbt.remove("targetWorld");
                    return InteractionResult.SUCCESS;
                }
            } else if (targetTile instanceof ITargetable) {
                ((ITargetable)targetTile).setTarget(pos);
                world.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5F, 1.5F + world.random.nextFloat() * 0.1F, false);
                nbt.remove("targetWorld");
                return InteractionResult.SUCCESS;
            }
        }

        if (world != null && (tile instanceof IEmberPacketProducer || tile instanceof ITargetable)) {
            Direction face = context.getClickedFace();
            if (tile instanceof IEmberPacketProducer) {
                face = ((IEmberPacketProducer)tile).getEmittingDirection(face);
                if (face == null) {
                    return super.onItemUseFirst(stack, context);
                }
            }

            nbt.putString("targetWorld", world.dimension().location().toString());
            nbt.putString("targetFace", face.getName());
            nbt.putInt("targetX", pos.getX());
            nbt.putInt("targetY", pos.getY());
            nbt.putInt("targetZ", pos.getZ());
            world.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5F, 1.95F + world.random.nextFloat() * 0.2F, false);
            if (world.isClientSide) {
                EmbersClientEvents.lastTarget = null;
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.onItemUseFirst(stack, context);
        }
    }
}
