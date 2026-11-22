package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.lucalabs.fairylights.client.renderer.RenderConstants;
import de.lucalabs.fairylights.main.connection.Connection;
import de.lucalabs.fairylights.main.util.Catenary;
import de.lucalabs.fairylights.main.util.Curve;
import de.lucalabs.fairylights.main.util.MathHelper;
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
import net.minecraft.util.ARGB;

import java.util.function.Function;

public abstract class ConnectionRenderer<C extends Connection> {
    protected final SpriteGetter sprites;
    private final WireModel model;
    private final float wireInflate;

    protected ConnectionRenderer(final Function<ModelLayerLocation, ModelPart> baker, final SpriteGetter sprites, final ModelLayerLocation wireModelLocation) {
        this(baker, sprites, wireModelLocation, 0.0F);
    }

    protected ConnectionRenderer(final Function<ModelLayerLocation, ModelPart> baker, final SpriteGetter sprites, final ModelLayerLocation wireModelLocation, final float wireInflate) {
        this.sprites = sprites;
        this.model = new WireModel(baker.apply(wireModelLocation));
        this.wireInflate = wireInflate;
    }

    public void render(
            final C conn,
            final float delta,
            final PoseStack matrix,
            final SubmitNodeCollector collector,
            final int packedLight,
            final int packedOverlay) {

        final Curve currCat = conn.getCatenary();
        final Curve prevCat = conn.getPrevCatenary();

        if (currCat != null && prevCat != null) {
            final Curve cat = prevCat.lerp(currCat, delta);
            final TextureAtlasSprite sprite = this.sprites.get(RenderConstants.SOLID_TEXTURE);
            final int wireColor = ARGB.opaque(this.getWireColor(conn));

            collector.submitCustomGeometry(
                    matrix,
                    RenderConstants.SOLID_TEXTURE.renderType(RenderTypes::entityCutout),
                    (pose, consumer) -> {
                        final VertexConsumer buf = sprite.wrap(consumer);
                        final PoseStack local = new PoseStack();
                        local.last().set(pose);
                        final Curve.SegmentIterator it = cat.iterator();
                        while (it.next()) {
                            local.pushPose();
                            local.translate(it.getX(0.0F), it.getY(0.0F), it.getZ(0.0F));
                            local.mulPose(Axis.YP.rotation(MathHelper.PI / 2.0F - it.getYaw()));
                            local.mulPose(Axis.XP.rotation(-it.getPitch()));
                            local.scale(1.0F + this.wireInflate, 1.0F, it.getLength() * 16.0F);
                            this.model.renderToBuffer(local, buf, packedLight, packedOverlay, wireColor);
                            local.popPose();
                            this.renderSegment(conn, it, delta, local, buf, packedLight, packedOverlay);
                        }
                    });
            this.render(conn, cat, delta, matrix, collector, packedLight, packedOverlay);
        }
    }

    protected int getWireColor(final C conn) {
        return 0xFFFFFF;
    }

    protected void render(
            final C conn,
            final Curve catenary,
            final float delta,
            final PoseStack matrix,
            final SubmitNodeCollector collector,
            final int packedLight,
            final int packedOverlay) {}

    protected void renderSegment(final C connection, final Catenary.SegmentView it, final float delta, final PoseStack matrix, final VertexConsumer buf, final int packedLight, final int packedOverlay) {}

    public static class WireModel extends Model.Simple {
        WireModel(final ModelPart root) {
            super(root, RenderTypes::entityCutout);
        }

        public static LayerDefinition createLayer(final int u, final int v, final int size) {
            MeshDefinition mesh = new MeshDefinition();
            mesh.getRoot().addOrReplaceChild("root", CubeListBuilder.create()
                    .texOffs(u, v)
                    .addBox(-size * 0.5F, -size * 0.5F, 0.0F, size, size, 1.0F), PartPose.ZERO);
            return LayerDefinition.create(mesh, 128, 128);
        }
    }
}
