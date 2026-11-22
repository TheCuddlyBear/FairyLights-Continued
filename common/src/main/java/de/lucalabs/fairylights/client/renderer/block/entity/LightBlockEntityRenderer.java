package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.lucalabs.fairylights.client.model.light.LightModel;
import de.lucalabs.fairylights.main.blocks.LightBlock;
import de.lucalabs.fairylights.main.blocks.entity.LightBlockEntity;
import de.lucalabs.fairylights.main.feature.light.Light;
import de.lucalabs.fairylights.main.feature.light.LightBehavior;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LightBlockEntityRenderer implements BlockEntityRenderer<LightBlockEntity, LightBlockEntityRenderer.LightRenderState> {
    private final LightRenderer lights;

    public LightBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        this.lights = new LightRenderer(context::bakeLayer, context.sprites());
    }

    @Override
    public LightRenderState createRenderState() {
        return new LightRenderState();
    }

    @Override
    public void extractRenderState(
            final LightBlockEntity entity,
            final LightRenderState state,
            final float partialTicks,
            final Vec3 cameraPosition,
            final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(entity, state, breakProgress);
        state.light = entity.getLight();
        final BlockState blockState = entity.getBlockState();
        state.face = blockState.getValue(LightBlock.FACE);
        state.rotation = blockState.getValue(LightBlock.FACING).toYRot();
        state.partialTicks = partialTicks;
    }

    @Override
    public void submit(
            final LightRenderState state,
            final PoseStack matrix,
            final SubmitNodeCollector submitNodeCollector,
            final CameraRenderState camera) {

        if (state.light != null) {
            this.submitLight(state, matrix, submitNodeCollector, state.light);
        }
    }

    private <T extends LightBehavior> void submitLight(
            final LightRenderState state,
            final PoseStack matrix,
            final SubmitNodeCollector submitNodeCollector,
            final Light<T> light) {

        final LightModel<T> model = this.lights.getModel(light, -1);
        final AABB box = model.getBounds();
        matrix.pushPose();
        matrix.translate(0.5D, 0.5D, 0.5D);
        matrix.mulPose(Axis.YP.rotationDegrees(180.0F - state.rotation));
        if (light.getVariant().isOrientable()) {
            if (state.face == AttachFace.WALL) {
                matrix.mulPose(Axis.XP.rotationDegrees(90.0F));
            } else if (state.face == AttachFace.FLOOR) {
                matrix.mulPose(Axis.XP.rotationDegrees(-180.0F));
            }
            matrix.translate(0.0D, 0.5D, 0.0D);
        } else {
            if (state.face == AttachFace.CEILING) {
                matrix.translate(0.0D, 0.25D, 0.0D);
            } else if (state.face == AttachFace.WALL) {
                matrix.translate(0.0D, 3.0D / 16.0D, 0.125D);
            } else {
                matrix.translate(0.0D, -box.minY - model.getFloorOffset() - 0.5D, 0.0D);
            }
        }
        this.lights.submit(matrix, submitNodeCollector, light, model, state.partialTicks, state.lightCoords, OverlayTexture.NO_OVERLAY);
        matrix.popPose();
    }

    public static class LightRenderState extends BlockEntityRenderState {
        @Nullable
        public Light<?> light;
        public AttachFace face = AttachFace.FLOOR;
        public float rotation;
        public float partialTicks;
    }
}
