package de.lucalabs.fairylights.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.client.renderer.block.entity.FastenerRenderer;
import de.lucalabs.fairylights.main.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public final class FenceFastenerRenderer extends EntityRenderer<FenceFastenerEntity, FenceFastenerRenderer.FenceFastenerRenderState> {
    public static final Identifier MODEL = Common.id("block/fence_fastener");

    private final FastenerRenderer renderer;

    public FenceFastenerRenderer(final EntityRendererProvider.Context context) {
        super(context);
        this.renderer = new FastenerRenderer(context::bakeLayer, context.getSprites());
    }

    @Override
    public FenceFastenerRenderState createRenderState() {
        return new FenceFastenerRenderState();
    }

    @Override
    public void extractRenderState(final FenceFastenerEntity entity, final FenceFastenerRenderState state, final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.fastener = Services.COMPONENTS.maybeGet(entity, Services.KEYS.FASTENER()).orElse(null);
        state.partialTicks = partialTicks;
    }

    @Override
    protected AABB getBoundingBoxForCulling(final FenceFastenerEntity entity) {
        return entity.getBoundingBoxForCulling();
    }

    @Override
    protected int getBlockLightLevel(final FenceFastenerEntity entity, final BlockPos delta) {
        return entity.level().getBrightness(LightLayer.BLOCK, entity.blockPosition());
    }

    @Override
    public void submit(
            final FenceFastenerRenderState state,
            final PoseStack matrix,
            final SubmitNodeCollector submitNodeCollector,
            final CameraRenderState camera) {

        submitNodeCollector.submitCustomGeometry(matrix, Sheets.cutoutBlockSheet(), (pose, buf) -> {
            final PoseStack local = new PoseStack();
            local.last().set(pose);
            FastenerRenderer.renderBakedModel(MODEL, local, buf, 1.0F, 1.0F, 1.0F, state.lightCoords, OverlayTexture.NO_OVERLAY);
        });
        if (state.fastener != null) {
            this.renderer.render(state.fastener, state.partialTicks, matrix, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY);
        }
        super.submit(state, matrix, submitNodeCollector, camera);
    }

    public static class FenceFastenerRenderState extends EntityRenderState {
        @Nullable
        public Fastener<?> fastener;
        public float partialTicks;
    }
}
