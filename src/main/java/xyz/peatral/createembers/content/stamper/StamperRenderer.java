package xyz.peatral.createembers.content.stamper;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.relays.belt.BeltHelper;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import xyz.peatral.createembers.CEBlockPartials;
import xyz.peatral.createembers.util.RenderUtil;

import java.util.Random;

public class StamperRenderer extends SafeTileEntityRenderer<StamperTileEntity> {
    public StamperRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public boolean shouldRenderOffScreen(StamperTileEntity te) {
        return true;
    }

    @Override
    protected void renderSafe(StamperTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        BlockState blockState = te.getBlockState();

        float renderedHeadOffset = te.getRenderedHeadOffset(partialTicks);
        //float renderedHeadOffset = 0;

        SuperByteBuffer headRender = CachedBufferer.partial(CEBlockPartials.STAMPER_HEAD, blockState);
        headRender
                .translate(0, -0.0001 - renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        ms.pushPose();
        ms.translate(.5f, -renderedHeadOffset - 1 / 64.f, .5f);
        IItemHandlerModifiable inv = te.itemCapability.orElse(new ItemStackHandler());
        ItemStack stack = inv.getStackInSlot(0);
        if (!stack.isEmpty()) {
            RenderUtil.renderItem(ms, buffer, light, overlay, stack, 0, new Random(1), new Vec3(0, 0, 0));
        }
        ms.popPose();
    }
}
