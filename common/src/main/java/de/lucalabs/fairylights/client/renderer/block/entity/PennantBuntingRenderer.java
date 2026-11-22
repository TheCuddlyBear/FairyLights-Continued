package de.lucalabs.fairylights.client.renderer.block.entity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.client.model.ModelLoader;
import de.lucalabs.fairylights.client.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.main.connection.PennantBuntingConnection;
import de.lucalabs.fairylights.main.feature.Pennant;
import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.util.Curve;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public class PennantBuntingRenderer extends ConnectionRenderer<PennantBuntingConnection> {
    private static final Identifier TRIANGLE_MODEL = Common.id("entity/triangle_pennant");
    private static final Identifier SQUARE_MODEL = Common.id("entity/square_pennant");

    public static final ImmutableSet<Identifier> MODELS = ImmutableSet.of(TRIANGLE_MODEL, SQUARE_MODEL);

    private final ImmutableMap<Item, Identifier> models = ImmutableMap.of(
            FairyLightItems.TRIANGLE_PENNANT, TRIANGLE_MODEL,
            FairyLightItems.SQUARE_PENNANT, SQUARE_MODEL
    );

    public PennantBuntingRenderer(final Function<ModelLayerLocation, ModelPart> baker, final SpriteGetter sprites) {
        super(baker, sprites, FairyLightModelLayers.PENNANT_WIRE, 0.25F);
    }

    @Override
    protected void render(
            final PennantBuntingConnection conn,
            final Curve catenary,
            final float delta,
            final PoseStack matrix,
            final SubmitNodeCollector collector,
            final int packedLight,
            final int packedOverlay) {

        super.render(conn, catenary, delta, matrix, collector, packedLight, packedOverlay);
        final Pennant[] currLights = conn.getFeatures();
        if (currLights != null && currLights.length > 0) {
            final int count = currLights.length;
            collector.submitCustomGeometry(matrix, Sheets.cutoutBlockSheet(), (pose, buf) -> {
                final PoseStack local = new PoseStack();
                local.last().set(pose);
                for (int i = 0; i < count; i++) {
                    final Pennant currPennant = currLights[i];
                    final int color = currPennant.getColor();
                    final float r = ((color >> 16) & 0xFF) / 255.0F;
                    final float g = ((color >> 8) & 0xFF) / 255.0F;
                    final float b = (color & 0xFF) / 255.0F;

                    final ModelLoader.BakedStandaloneModel model = Services.MODELS
                            .getModel(this.models.getOrDefault(currPennant.getItem(), TRIANGLE_MODEL));

                    final Vec3 pos = currPennant.getPoint(delta);
                    local.pushPose();
                    local.translate(pos.x, pos.y, pos.z);
                    local.mulPose(Axis.YP.rotation(-currPennant.getYaw(delta)));
                    local.mulPose(Axis.ZP.rotation(currPennant.getPitch(delta)));
                    local.mulPose(Axis.XP.rotation(currPennant.getRoll(delta)));
                    local.pushPose();
                    FastenerRenderer.renderBakedModel(model, local, buf, r, g, b, packedLight, packedOverlay);
                    local.popPose();
                    local.popPose();
                }
            });
        }
    }

    public static float diffuseLight(float p_144949_, float p_144950_, float p_144951_) {
        return Math.min(p_144949_ * p_144949_ * 0.6F + p_144950_ * p_144950_ * ((3.0F + p_144950_) / 4.0F) + p_144951_ * p_144951_ * 0.8F, 1.0F);
    }


    public static LayerDefinition wireLayer() {
        return WireModel.createLayer(0, 17, 1);
    }
}
