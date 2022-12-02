package xyz.peatral.createembers.util;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.relays.belt.BeltHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class RenderUtil {
    public static void renderItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack itemStack,
                                  int angle, Random r, Vec3 itemPosition) {
        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        TransformStack msr = TransformStack.cast(ms);
        int count = (int) (Mth.log2((int) (itemStack.getCount()))) / 2;
        boolean renderUpright = BeltHelper.isItemUpright(itemStack);
        boolean blockItem = itemRenderer.getModel(itemStack, null, null, 0)
                .isGui3d();

        ms.pushPose();
        msr.rotateY(angle);

        if (renderUpright) {
            Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
            if (renderViewEntity != null) {
                Vec3 positionVec = renderViewEntity.position();
                Vec3 vectorForOffset = itemPosition;
                Vec3 diff = vectorForOffset.subtract(positionVec);
                float yRot = (float) (Mth.atan2(diff.x, diff.z) + Math.PI);
                ms.mulPose(Vector3f.YP.rotation(yRot));
            }
            ms.translate(0, 3 / 32d, -1 / 16f);
        }

        for (int i = 0; i <= count; i++) {
            ms.pushPose();
            if (blockItem)
                ms.translate(r.nextFloat() * .0625f * i, 0, r.nextFloat() * .0625f * i);
            ms.scale(.5f, .5f, .5f);
            if (!blockItem && !renderUpright) {
                msr.rotateX(90);
            }
            itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED, light, overlay, ms, buffer, 0);
            ms.popPose();

            if (!renderUpright) {
                if (!blockItem)
                    msr.rotateY(10);
                ms.translate(0, blockItem ? 1 / 64d : 1 / 16d, 0);
            } else
                ms.translate(0, 0, -1 / 16f);
        }

        ms.popPose();
    }
}
