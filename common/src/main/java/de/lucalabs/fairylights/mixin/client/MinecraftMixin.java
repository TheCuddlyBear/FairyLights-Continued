package de.lucalabs.fairylights.mixin.client;

import de.lucalabs.fairylights.client.events.ClientEventHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(at = @At("RETURN"), method = "pick")
    public void updateTargetedEntity(CallbackInfo ci) {
        ClientEventHandler.updateHitConnection();
    }
}