package de.lucalabs.fairylights.client.models;

import de.lucalabs.fairylights.client.model.ModelLoader;
import de.lucalabs.fairylights.client.renderer.block.entity.PennantBuntingRenderer;
import de.lucalabs.fairylights.client.renderer.entity.FenceFastenerRenderer;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FabricModelLoader implements ModelLoader {

    private static final Map<Identifier, ExtraModelKey<BakedStandaloneModel>> KEYS = new HashMap<>();

    static {
        registerKey(FenceFastenerRenderer.MODEL);
        for (final Identifier model : PennantBuntingRenderer.MODELS) {
            registerKey(model);
        }
    }

    private static void registerKey(final Identifier id) {
        KEYS.put(id, ExtraModelKey.create(id::toString));
    }

    public static void registerExtraModels(final ModelLoadingPlugin.Context context) {
        for (final Map.Entry<Identifier, ExtraModelKey<BakedStandaloneModel>> entry : KEYS.entrySet()) {
            final Identifier id = entry.getKey();
            context.addModel(
                    entry.getValue(),
                    new SimpleUnbakedExtraModel<>(id, ModelLoader::bakeStandalone)
            );
        }
    }

    @Override
    public @Nullable BakedStandaloneModel getModel(final Identifier path) {
        final ExtraModelKey<BakedStandaloneModel> key = KEYS.get(path);
        if (key == null) {
            return null;
        }
        return Minecraft.getInstance().getModelManager().getModel(key);
    }
}
