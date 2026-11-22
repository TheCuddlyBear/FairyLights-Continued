package de.lucalabs.fairylights.client.model;

import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public interface ModelLoader {
    @Nullable
    BakedStandaloneModel getModel(Identifier path);

    static BakedStandaloneModel bakeStandalone(final ResolvedModel model, final ModelBaker baker) {
        return new BakedStandaloneModel(
                model.bakeTopGeometry(model.getTopTextureSlots(), baker, BlockModelRotation.IDENTITY),
                model.getTopTransforms()
        );
    }

    record BakedStandaloneModel(QuadCollection quads, ItemTransforms transforms) {
        public static final BakedStandaloneModel EMPTY = new BakedStandaloneModel(QuadCollection.EMPTY, ItemTransforms.NO_TRANSFORMS);
    }
}
