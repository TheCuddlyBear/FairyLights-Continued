package de.lucalabs.fairylights.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.client.events.ClientEventHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "renderBlockOutline",
            at = @At("HEAD")
    )
    private void onDrawEntityHighlight(
            MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean onlyTranslucentBlocks, LevelRenderState levelRenderState, CallbackInfo ci) {
        if (onlyTranslucentBlocks) return;

        HitResult target = this.minecraft.hitResult;
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            if (target instanceof EntityHitResult entityTarget) {
                Camera camera = this.minecraft.gameRenderer.getMainCamera();
                DeltaTracker tickCounter = this.minecraft.getDeltaTracker();
                ClientEventHandler.onDrawEntityHighlight(
                        entityTarget.getEntity(),
                        camera,
                        tickCounter.getGameTimeDeltaPartialTick(true),
                        poseStack,
                        bufferSource
                );
            }
        }
    }
}