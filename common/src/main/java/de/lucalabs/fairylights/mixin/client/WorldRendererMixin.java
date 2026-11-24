package de.lucalabs.fairylights.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.client.events.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @Inject(
            method = "submitBlockOutline",
            at = @At("HEAD")
    )
    private void onDrawEntityHighlight(
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState, CallbackInfo ci) {
        HitResult target = Minecraft.getInstance().hitResult;
        if (target instanceof EntityHitResult entityTarget) {
            ClientEventHandler.onDrawEntityHighlight(
                    entityTarget.getEntity(),
                    levelRenderState.cameraRenderState.pos,
                    poseStack,
                    submitNodeCollector
            );
        }
    }
}