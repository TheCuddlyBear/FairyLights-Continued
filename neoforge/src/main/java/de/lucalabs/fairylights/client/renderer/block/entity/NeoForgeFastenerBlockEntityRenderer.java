package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.main.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.main.fastener.RegularBlockView;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * NeoForge frustum-culls block entities by {@link #getRenderBoundingBox}, whose default is the
 * single block. A fastener renders its connections (light strings) far beyond its own block, so the
 * common renderer's {@code shouldRenderOffScreen} alone is not enough on NeoForge — the string
 * disappears (and flickers while moving) whenever the fastener's block leaves the view frustum.
 *
 * This thin wrapper delegates the actual rendering to the common {@link FastenerBlockEntityRenderer}
 * and additionally reports a render box that spans the whole connection, the same way the fence
 * fastener entity does via {@code getBoundingBoxForCulling}. The render box hook only exists on
 * NeoForge, so it cannot live in the common renderer.
 */
public final class NeoForgeFastenerBlockEntityRenderer
        implements BlockEntityRenderer<FastenerBlockEntity, FastenerBlockEntityRenderer.FastenerRenderState> {

    private final FastenerBlockEntityRenderer delegate;

    public NeoForgeFastenerBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        this.delegate = new FastenerBlockEntityRenderer(context, new RegularBlockView());
    }

    @Override
    public FastenerBlockEntityRenderer.FastenerRenderState createRenderState() {
        return this.delegate.createRenderState();
    }

    @Override
    public void extractRenderState(
            final FastenerBlockEntity fastener,
            final FastenerBlockEntityRenderer.FastenerRenderState state,
            final float partialTicks,
            final Vec3 cameraPosition,
            final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        this.delegate.extractRenderState(fastener, state, partialTicks, cameraPosition, breakProgress);
    }

    @Override
    public void submit(
            final FastenerBlockEntityRenderer.FastenerRenderState state,
            final PoseStack matrix,
            final SubmitNodeCollector submitNodeCollector,
            final CameraRenderState camera) {
        this.delegate.submit(state, matrix, submitNodeCollector, camera);
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return this.delegate.shouldRenderOffScreen();
    }

    @Override
    public AABB getRenderBoundingBox(final FastenerBlockEntity fastener) {
        return Services.COMPONENTS.maybeGet(fastener, Services.KEYS.FASTENER())
                .map(f -> f.getBounds().inflate(1.0D))
                .orElseGet(() -> new AABB(fastener.getBlockPos()));
    }
}
