package de.lucalabs.fairylights.main.items.crafting.ingredient;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class LazyTagIngredient {
    private LazyTagIngredient() {
    }

    public static @NotNull Ingredient of(final @NotNull TagKey<Item> tag) {
        return Ingredient.of(new LazyTagHolderSet(Objects.requireNonNull(tag, "tag")));
    }

    private static final class LazyTagHolderSet extends HolderSet.ListBacked<Item> {
        private final TagKey<Item> tag;

        private LazyTagHolderSet(final TagKey<Item> tag) {
            this.tag = tag;
        }

        @Override
        protected List<Holder<Item>> contents() {
            return BuiltInRegistries.ITEM.get(this.tag)
                    .map(named -> named.stream().toList())
                    .orElse(List.of());
        }

        @Override
        public boolean isBound() {
            return BuiltInRegistries.ITEM.get(this.tag)
                    .map(HolderSet::isBound)
                    .orElse(false);
        }

        @Override
        public Either<TagKey<Item>, List<Holder<Item>>> unwrap() {
            return Either.left(this.tag);
        }

        @Override
        public Optional<TagKey<Item>> unwrapKey() {
            return Optional.of(this.tag);
        }

        @Override
        public boolean contains(final Holder<Item> value) {
            return value.is(this.tag);
        }

        @Override
        public boolean canSerializeIn(final HolderOwner<Item> owner) {
            return BuiltInRegistries.ITEM.canSerializeIn(owner);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || (obj instanceof LazyTagHolderSet other && this.tag.equals(other.tag));
        }

        @Override
        public int hashCode() {
            return this.tag.hashCode();
        }

        @Override
        public String toString() {
            return "LazyTagHolderSet(" + this.tag + ")";
        }
    }
}