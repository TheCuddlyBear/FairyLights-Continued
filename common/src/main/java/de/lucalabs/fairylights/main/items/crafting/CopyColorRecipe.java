package de.lucalabs.fairylights.main.items.crafting;

import de.lucalabs.fairylights.main.items.DyeableItem;
import de.lucalabs.fairylights.main.util.Tags;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CopyColorRecipe extends CustomRecipe {
    public CopyColorRecipe() {
        super();
    }

    @Override
    public boolean matches(final CraftingInput inv, final Level world) {
        int count = 0;
        for (int i = 0; i < inv.size(); i++) {
            final ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (!stack.is(Tags.DYEABLE) || ++count > 2) {
                    return false;
                }
            }
        }
        return count == 2;
    }

    @Override
    public @NotNull ItemStack assemble(final CraftingInput inv) {
        ItemStack original = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); i++) {
            final ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(Tags.DYEABLE)) {
                    if (original.isEmpty()) {
                        original = stack;
                    } else {
                        final ItemStack copy = stack.copyWithCount(1);
                        DyeableItem.setColor(copy, DyeableItem.getColor(original));
                        return copy;
                    }
                } else {
                    break;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(final CraftingInput inv) {
        final NonNullList<ItemStack> remaining = super.getRemainingItems(inv);
        for (int i = 0; i < inv.size(); i++) {
            final ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(Tags.DYEABLE)) {
                if (remaining.get(i).isEmpty()) {
                    remaining.set(i, stack.copyWithCount(1));
                }
                break;
            }
        }
        return remaining;
    }

    @Override
    public @NotNull RecipeSerializer<CopyColorRecipe> getSerializer() {
        return FairyLightCraftingRecipes.COPY_COLOR;
    }
}