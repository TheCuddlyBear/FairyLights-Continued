package de.lucalabs.fairylights.client.renderer;

import de.lucalabs.fairylights.Common;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteId;

public class RenderConstants {

    public static final float HIGHLIGHT_ALPHA = 0.4F;

    public static final SpriteId SOLID_TEXTURE = new SpriteId(
            TextureAtlas.LOCATION_BLOCKS,
            Common.id("entity/connections"));

    public static final SpriteId TRANSLUCENT_TEXTURE = new SpriteId(
            TextureAtlas.LOCATION_BLOCKS,
            Common.id("entity/connections"));

}
