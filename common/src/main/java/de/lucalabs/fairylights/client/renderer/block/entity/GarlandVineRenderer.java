package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.lucalabs.fairylights.main.connection.GarlandVineConnection;
import de.lucalabs.fairylights.client.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.client.renderer.RenderConstants;
import de.lucalabs.fairylights.main.util.Curve;
import de.lucalabs.fairylights.main.util.MathHelper;
import de.lucalabs.fairylights.main.util.RandomArray;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;

import java.util.function.Function;

public class GarlandVineRenderer extends ConnectionRenderer<GarlandVineConnection> {
    private static final int RING_COUNT = 7;
    private static final RandomArray RAND = new RandomArray(8411, RING_COUNT * 4);

    private final RingsModel rings;

    protected GarlandVineRenderer(final Function<ModelLayerLocation, ModelPart> baker, final SpriteGetter sprites) {
        super(baker, sprites, FairyLightModelLayers.VINE_WIRE);
        this.rings = new RingsModel(baker.apply(FairyLightModelLayers.GARLAND_RINGS));
    }

    @Override
    protected void render(
            final GarlandVineConnection conn,
            final Curve catenary,
            final float delta,
            final PoseStack matrix,
            final SubmitNodeCollector collector,
            final int packedLight,
            final int packedOverlay) {
        super.render(conn, catenary, delta, matrix, collector, packedLight, packedOverlay);
        final int hash = conn.getUUID().hashCode();
        final TextureAtlasSprite sprite = this.sprites.get(RenderConstants.SOLID_TEXTURE);
        collector.submitCustomGeometry(
                matrix,
                RenderConstants.SOLID_TEXTURE.renderType(RenderTypes::entityCutout),
                (pose, consumer) -> {
                    final VertexConsumer buf = sprite.wrap(consumer);
                    final PoseStack local = new PoseStack();
                    local.last().set(pose);
                    catenary.visitPoints(0.25F, false, (index, x, y, z, yaw, pitch) -> {
                        local.pushPose();
                        local.translate(x, y, z);
                        local.mulPose(Axis.YP.rotation(-yaw));
                        local.mulPose(Axis.ZP.rotation(pitch));
                        local.mulPose(Axis.ZP.rotationDegrees(RAND.get(index + hash) * 45.0F));
                        local.mulPose(Axis.YP.rotationDegrees(RAND.get(index + 8 + hash) * 60.F + 90.0F));
                        this.rings.renderRing(index % RING_COUNT, local, buf, packedLight, packedOverlay, 0xFFFFFFFF);
                        local.popPose();
                    });
                });
    }

    public static LayerDefinition wireLayer() {
        return WireModel.createLayer(39, 0, 1);
    }

    public static class RingsModel extends Model.Simple {
        final ModelPart[] roots;

        RingsModel(final ModelPart root) {
            super(root, RenderTypes::entityCutout);
            ModelPart[] roots = new ModelPart[RING_COUNT];
            for (int i = 0; i < RING_COUNT; i++) {
                roots[i] = root.getChild(Integer.toString(i));
            }
            this.roots = roots;
        }

        public static LayerDefinition createLayer() {
            final float size = 4.0F;
            CubeListBuilder root = CubeListBuilder.create()
                    .texOffs(14, 91)
                    .addBox(-size / 2.0F, -size / 2.0F, -size / 2.0F, size, size, size);
            PartPose crossPose = PartPose.rotation(0.0F, 0.0F, MathHelper.HALF_PI);
            MeshDefinition mesh = new MeshDefinition();
            for (int i = 0; i < RING_COUNT; i++) {
                mesh.getRoot().addOrReplaceChild(Integer.toString(i), root, PartPose.ZERO)
                        .addOrReplaceChild("cross_" + i, CubeListBuilder.create()
                                .texOffs(i * 8, 64)
                                .addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F)
                                .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F), crossPose);
            }
            return LayerDefinition.create(mesh, 128, 128);
        }

        public void renderRing(final int which, final PoseStack matrix, final VertexConsumer builder, final int light, final int overlay, final int color) {
            this.roots[which].render(matrix, builder, light, overlay, color);
        }
    }
}
