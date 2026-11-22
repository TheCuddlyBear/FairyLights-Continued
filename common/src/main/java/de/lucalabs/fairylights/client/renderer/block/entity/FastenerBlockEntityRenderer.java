package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.main.fastener.BlockView;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class FastenerBlockEntityRenderer implements BlockEntityRenderer<FastenerBlockEntity, FastenerBlockEntityRenderer.FastenerRenderState> {

    private final BlockView view;
    private final FastenerRenderer renderer;

    public FastenerBlockEntityRenderer(final BlockEntityRendererProvider.Context context, final BlockView view) {
        this.view = view;
        this.renderer = new FastenerRenderer(context::bakeLayer, context.sprites());
    }

    @Override
    public FastenerRenderState createRenderState() {
        return new FastenerRenderState();
    }

    @Override
    public void extractRenderState(
            final FastenerBlockEntity fastener,
            final FastenerRenderState state,
            final float partialTicks,
            final Vec3 cameraPosition,
            final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(fastener, state, breakProgress);
        state.fastener = Services.COMPONENTS.maybeGet(fastener, Services.KEYS.FASTENER()).orElse(null);
        state.offset = fastener.getOffset();
        state.partialTicks = partialTicks;
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public void submit(
            final FastenerRenderState state,
            final PoseStack matrix,
            final SubmitNodeCollector submitNodeCollector,
            final CameraRenderState camera) {

        if (state.fastener != null) {
            matrix.pushPose();
            matrix.translate(state.offset.x, state.offset.y, state.offset.z);
            this.renderer.render(state.fastener, state.partialTicks, matrix, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY);
            matrix.popPose();
        }
    }

    public static class FastenerRenderState extends BlockEntityRenderState {
        @Nullable
        public Fastener<?> fastener;
        public Vec3 offset = Vec3.ZERO;
        public float partialTicks;
    }
}
