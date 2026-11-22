package de.lucalabs.fairylights.main.blocks.entity;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.Constants;
import de.lucalabs.fairylights.main.blocks.FairyLightBlocks;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class FairyLightBlockEntities {

    public static final BlockEntityType<FastenerBlockEntity> FASTENER = register(
            "fastener",
            () -> new BlockEntityType<>(FastenerBlockEntity::new, Set.of(FairyLightBlocks.FASTENER)));

    public static final BlockEntityType<LightBlockEntity> LIGHT = register("light", () -> new BlockEntityType<>(LightBlockEntity::new, Set.of(
            FairyLightBlocks.FAIRY_LIGHT,
            FairyLightBlocks.PAPER_LANTERN,
            FairyLightBlocks.ORB_LANTERN,
            FairyLightBlocks.FLOWER_LIGHT,
            FairyLightBlocks.CANDLE_LANTERN_LIGHT,
            FairyLightBlocks.JACK_O_LANTERN,
            FairyLightBlocks.SKULL_LIGHT,
            FairyLightBlocks.GHOST_LIGHT,
            FairyLightBlocks.SPIDER_LIGHT,
            FairyLightBlocks.WITCH_LIGHT,
            FairyLightBlocks.SNOWFLAKE_LIGHT,
            FairyLightBlocks.HEART_LIGHT,
            FairyLightBlocks.MOON_LIGHT,
            FairyLightBlocks.STAR_LIGHT,
            FairyLightBlocks.ICICLE_LIGHTS,
            FairyLightBlocks.METEOR_LIGHT,
            FairyLightBlocks.OIL_LANTERN,
            FairyLightBlocks.CANDLE_LANTERN,
            FairyLightBlocks.INCANDESCENT_LIGHT)));

    private FairyLightBlockEntities() {
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(final String name, Supplier<BlockEntityType<T>> supplier) {
        Identifier identifier = Common.id(name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, identifier, supplier.get());
    }

    public static void initialize() {
        Constants.LOG.info("Registering block entities");
    }
}
