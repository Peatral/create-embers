package xyz.peatral.createembers.content.steam_valve;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class SteamValveRenderer extends SafeBlockEntityRenderer<SteamValveBlockEntity> {
    public SteamValveRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(SteamValveBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
    }
}
