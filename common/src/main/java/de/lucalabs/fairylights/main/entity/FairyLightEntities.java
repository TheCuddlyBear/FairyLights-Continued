package de.lucalabs.fairylights.main.entity;

import de.lucalabs.fairylights.Common;
import java.util.function.Function;

import de.lucalabs.fairylights.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class FairyLightEntities {

    public static final EntityType<FenceFastenerEntity> FASTENER = register("fastener", key ->
            EntityType.Builder.<FenceFastenerEntity>of(FenceFastenerEntity::new, MobCategory.MISC)
                    .sized(1.15F, 2.8F)
                    .eyeHeight(1)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build(key)
    );

    private FairyLightEntities() {
    }

    private static <T extends Entity> EntityType<T> register(final String name, Function<ResourceKey<EntityType<?>>, EntityType<T>> factory) {
        Identifier identifier = Common.id(name);
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, identifier);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, factory.apply(key));
    }

    public static void initialize() {
        Constants.LOG.info("Registering entities");
    }
}
