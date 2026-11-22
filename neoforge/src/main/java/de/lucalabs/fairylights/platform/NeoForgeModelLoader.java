package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.client.model.ModelLoader;
import de.lucalabs.fairylights.client.renderer.block.entity.PennantBuntingRenderer;
import de.lucalabs.fairylights.client.renderer.entity.FenceFastenerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeModelLoader implements ModelLoader {

    private static final Map<Identifier, StandaloneModelKey<BakedStandaloneModel>> KEYS = new HashMap<>();

    static {
        registerKey(FenceFastenerRenderer.MODEL);
        for (final Identifier model : PennantBuntingRenderer.MODELS) {
            registerKey(model);
        }
    }

    private static void registerKey(final Identifier id) {
        KEYS.put(id, new StandaloneModelKey<>(id::toString));
    }

    public static void registerStandaloneModels(final ModelEvent.RegisterStandalone event) {
        for (final Map.Entry<Identifier, StandaloneModelKey<BakedStandaloneModel>> entry : KEYS.entrySet()) {
            final Identifier id = entry.getKey();
            event.register(
                    entry.getValue(),
                    new SimpleUnbakedStandaloneModel<>(id, (model, baker, _) -> ModelLoader.bakeStandalone(model, baker))
            );
        }
    }

    @Override
    public @Nullable BakedStandaloneModel getModel(final Identifier path) {
        final StandaloneModelKey<BakedStandaloneModel> key = KEYS.get(path);
        if (key == null) {
            return null;
        }
        return Minecraft.getInstance().getModelManager().getStandaloneModel(key);
    }
}
