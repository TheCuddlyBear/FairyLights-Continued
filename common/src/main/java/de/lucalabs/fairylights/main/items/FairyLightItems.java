package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.main.blocks.LightBlock;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import static de.lucalabs.fairylights.main.items.components.FairyLightItemComponents.LIGHT_VARIANT;

public final class FairyLightItems {

    public static final ConnectionItem HANGING_LIGHTS = register("hanging_lights", HangingLightsConnectionItem::new);
    public static final ConnectionItem PENNANT_BUNTING = register("pennant_bunting", PennantBuntingConnectionItem::new);

    public static final ConnectionItem TINSEL = register("tinsel", TinselConnectionItem::new);
    public static final ConnectionItem GARLAND = register("garland", GarlandConnectionItem::new);

    public static final LightItem FAIRY_LIGHT = register("fairy_light", createColorLight(FairyLightBlocks.FAIRY_LIGHT));
    public static final LightItem PAPER_LANTERN = register("paper_lantern", createColorLight(FairyLightBlocks.PAPER_LANTERN));
    public static final LightItem ORB_LANTERN = register("orb_lantern", createColorLight(FairyLightBlocks.ORB_LANTERN));
    public static final LightItem FLOWER_LIGHT = register("flower_light", createColorLight(FairyLightBlocks.FLOWER_LIGHT));
    public static final LightItem CANDLE_LANTERN_LIGHT = register("candle_lantern_light", createColorLight(FairyLightBlocks.CANDLE_LANTERN_LIGHT));
    public static final LightItem JACK_O_LANTERN = register("jack_o_lantern", createColorLight(FairyLightBlocks.JACK_O_LANTERN));
    public static final LightItem SKULL_LIGHT = register("skull_light", createColorLight(FairyLightBlocks.SKULL_LIGHT));
    public static final LightItem GHOST_LIGHT = register("ghost_light", createColorLight(FairyLightBlocks.GHOST_LIGHT));
    public static final LightItem SPIDER_LIGHT = register("spider_light", createColorLight(FairyLightBlocks.SPIDER_LIGHT));
    public static final LightItem WITCH_LIGHT = register("witch_light", createColorLight(FairyLightBlocks.WITCH_LIGHT));
    public static final LightItem SNOWFLAKE_LIGHT = register("snowflake_light", createColorLight(FairyLightBlocks.SNOWFLAKE_LIGHT));
    public static final LightItem HEART_LIGHT = register("heart_light", createColorLight(FairyLightBlocks.HEART_LIGHT));
    public static final LightItem MOON_LIGHT = register("moon_light", createColorLight(FairyLightBlocks.MOON_LIGHT));
    public static final LightItem STAR_LIGHT = register("star_light", createColorLight(FairyLightBlocks.STAR_LIGHT));
    public static final LightItem ICICLE_LIGHTS = register("icicle_lights", createColorLight(FairyLightBlocks.ICICLE_LIGHTS));
    public static final LightItem METEOR_LIGHT = register("meteor_light", createColorLight(FairyLightBlocks.METEOR_LIGHT));
    public static final LightItem CANDLE_LANTERN = register("candle_lantern", createLight(FairyLightBlocks.CANDLE_LANTERN, LightItem::new));
    public static final LightItem OIL_LANTERN = register("oil_lantern", createLight(FairyLightBlocks.OIL_LANTERN, LightItem::new));
    public static final LightItem INCANDESCENT_LIGHT = register("incandescent_light", createLight(FairyLightBlocks.INCANDESCENT_LIGHT, LightItem::new));

    public static final Item TRIANGLE_PENNANT = register("triangle_pennant", PennantItem::new);
    public static final Item SQUARE_PENNANT = register("square_pennant", PennantItem::new);

    private FairyLightItems() {
    }

    private static Function<Item.Properties, LightItem> createLight(
            final LightBlock block,
            final BiFunction<LightBlock, Item.Properties, LightItem> factory) {
        return properties -> factory.apply(
                block,
                properties.useBlockDescriptionPrefix().stacksTo(16).component(LIGHT_VARIANT, block.getVariant().getId())
        );
    }

    private static Function<Item.Properties, LightItem> createColorLight(final LightBlock block) {
        return createLight(block, ColorLightItem::new);
    }

    private static <T extends Item> T register(final String name, Function<Item.Properties, T> factory) {
        Identifier identifier = Common.id(name);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(new Item.Properties().setId(key)));
    }

    public static void initialize() {
        Constants.LOG.info("Registering items");
    }

    public static Stream<LightItem> lights() {
        return BuiltInRegistries.ITEM.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(LightItem.class::isInstance)
                .map(LightItem.class::cast);
    }
}
