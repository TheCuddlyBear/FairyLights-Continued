package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.lucalabs.fairylights.client.model.ModelLoader;
import de.lucalabs.fairylights.client.model.light.BowModel;
import de.lucalabs.fairylights.client.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.client.renderer.RenderConstants;
import de.lucalabs.fairylights.main.connection.*;
import de.lucalabs.fairylights.main.fastener.Fastener;
import de.lucalabs.fairylights.main.fastener.FenceFastener;
import de.lucalabs.fairylights.main.util.ColorUtils;
import de.lucalabs.fairylights.main.util.Tags;
import de.lucalabs.fairylights.platform.Services;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FastenerRenderer {
    private final SpriteGetter sprites;
    private final HangingLightsRenderer hangingLights;
    private final PennantBuntingRenderer pennants;
    private final GarlandVineRenderer garland;
    private final GarlandTinselRenderer tinsel;
    private final BowModel bow;

    public FastenerRenderer(final Function<ModelLayerLocation, ModelPart> baker, final SpriteGetter sprites) {
        this.sprites = sprites;
        this.hangingLights = new HangingLightsRenderer(baker, sprites);
        this.pennants = new PennantBuntingRenderer(baker, sprites);
        this.garland = new GarlandVineRenderer(baker, sprites);
        this.tinsel = new GarlandTinselRenderer(baker, sprites);
        this.bow = new BowModel(baker.apply(FairyLightModelLayers.BOW));
    }

    public void render(
            final Fastener<?> fastener,
            final float delta,
            final PoseStack matrix,
            final SubmitNodeCollector collector,
            final int packedLight,
            final int packedOverlay) {

        boolean renderBow = true;
        for (final Connection conn : fastener.getAllConnections()) {
            if (conn.getFastener() == fastener) {
                this.renderConnection(delta, matrix, collector, packedLight, packedOverlay, conn);
            }
            if (renderBow && conn instanceof GarlandVineConnection && this.renderBow(fastener, matrix, collector, packedLight, packedOverlay)) {
                renderBow = false;
            }
        }
    }


    private boolean renderBow(
            Fastener<?> fastener,
            PoseStack matrix,
            SubmitNodeCollector collector,
            int packedLight,
            int packedOverlay) {

        if (fastener instanceof FenceFastener) {
            final Level world = fastener.getWorld();
            if (world == null) {
                return false;
            }
            final BlockState state = world.getBlockState(fastener.getPos());
            if (!state.is(Tags.FENCES)) {
                return false;
            }
            final TextureAtlasSprite sprite = this.sprites.get(RenderConstants.SOLID_TEXTURE);
            final float offset = -1.5F / 16.0F;
            final boolean north = state.getValue(FenceBlock.NORTH);
            final boolean east = state.getValue(FenceBlock.EAST);
            final boolean south = state.getValue(FenceBlock.SOUTH);
            final boolean west = state.getValue(FenceBlock.WEST);
            boolean tryDirX = true;
            boolean bow = false;
            if (!north && (east || west)) {
                this.bow(matrix, Direction.NORTH, offset, collector, sprite, packedLight, packedOverlay);
                tryDirX = false;
                bow = true;
            }
            if (!south && (east || west)) {
                this.bow(matrix, Direction.SOUTH, offset, collector, sprite, packedLight, packedOverlay);
                tryDirX = false;
                bow = true;
            }
            if (tryDirX) {
                if (!east && (north || south)) {
                    this.bow(matrix, Direction.EAST, offset, collector, sprite, packedLight, packedOverlay);
                    bow = true;
                }
                if (!west && (north || south)) {
                    this.bow(matrix, Direction.WEST, offset, collector, sprite, packedLight, packedOverlay);
                    bow = true;
                }
            }
            return bow;
        } else if (fastener.getFacing().getAxis() != Direction.Axis.Y) {
            final TextureAtlasSprite sprite = this.sprites.get(RenderConstants.SOLID_TEXTURE);
            this.bow(matrix, fastener.getFacing(), 0.0F, collector, sprite, packedLight, packedOverlay);
            return true;
        }
        return false;
    }

    private void bow(PoseStack matrix, Direction dir, float offset, SubmitNodeCollector collector, TextureAtlasSprite sprite, int packedLight, int packedOverlay) {
        matrix.pushPose();
        matrix.mulPose(Axis.YP.rotationDegrees(180.0F - dir.toYRot()));
        if (offset != 0.0F) {
            matrix.translate(0.0D, 0.0D, offset);
        }
        collector.submitModel(
                this.bow,
                Unit.INSTANCE,
                matrix,
                RenderConstants.SOLID_TEXTURE.renderType(RenderTypes::entityCutout),
                packedLight,
                packedOverlay,
                ColorUtils.WHITE,
                sprite,
                0,
                null);
        matrix.popPose();
    }

    private void renderConnection(final float delta, final PoseStack matrix, final SubmitNodeCollector collector, final int packedLight, final int packedOverlay, final Connection conn) {
        if (conn instanceof HangingLightsConnection) {
            this.hangingLights.render((HangingLightsConnection) conn, delta, matrix, collector, packedLight, packedOverlay);
        } else if (conn instanceof PennantBuntingConnection) {
            this.pennants.render((PennantBuntingConnection) conn, delta, matrix, collector, packedLight, packedOverlay);
        } else if (conn instanceof GarlandTinselConnection) {
            this.tinsel.render((GarlandTinselConnection) conn, delta, matrix, collector, packedLight, packedOverlay);
        } else if (conn instanceof GarlandVineConnection) {
            this.garland.render((GarlandVineConnection) conn, delta, matrix, collector, packedLight, packedOverlay);
        }
    }

    public static void renderBakedModel(final Identifier path, final PoseStack matrix, final VertexConsumer buf, final float r, final float g, final float b, final int packedLight, final int packedOverlay) {
        renderBakedModel(Services.MODELS.getModel(path), matrix, buf, r, g, b, packedLight, packedOverlay);
    }

    public static void renderBakedModel(@Nullable final ModelLoader.BakedStandaloneModel model, final PoseStack matrix, final VertexConsumer buf, final float r, final float g, final float b, final int packedLight, final int packedOverlay) {
        renderBakedModel(model, ItemDisplayContext.FIXED, matrix, buf, r, g, b, packedLight, packedOverlay);
    }

    public static void renderBakedModel(
            @Nullable final ModelLoader.BakedStandaloneModel model,
            final ItemDisplayContext type,
            final PoseStack matrix,
            final VertexConsumer buf,
            final float r,
            final float g,
            final float b,
            final int packedLight,
            final int packedOverlay) {
        if (model == null) {
            return;
        }

        final PoseStack.Pose lastStack = matrix.last();
        final ItemTransform transform = model.transforms().getTransform(type);
        if (transform != ItemTransform.NO_TRANSFORM) {
            transform.apply(false, lastStack);
            // ItemTransform.apply in 26.1 appends translate(-0.5, -0.5, -0.5) for item centering; undo it to preserve raw display transform semantics.
            lastStack.translate(0.5F, 0.5F, 0.5F);
        }

        final QuadInstance quadInstance = new QuadInstance();
        quadInstance.setColor(ARGB.colorFromFloat(1.0F, r, g, b));
        quadInstance.setLightCoords(packedLight);
        quadInstance.setOverlayCoords(packedOverlay);

        for (final BakedQuad quad : model.quads().getAll()) {
            buf.putBakedQuad(lastStack, quad, quadInstance);
        }
    }
}
