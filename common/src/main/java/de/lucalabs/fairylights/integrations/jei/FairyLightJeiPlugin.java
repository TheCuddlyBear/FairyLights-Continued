package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.Common;
import de.lucalabs.fairylights.main.items.FairyLightItems;
import de.lucalabs.fairylights.main.items.crafting.GenericRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class FairyLightJeiPlugin implements IModPlugin {
    @Override
    public @NotNull Identifier getPluginUid() {
        return Common.id("fairylights_jei");
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(GenericRecipe.class, new GenericRecipeExtension());
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registry) {
        registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, FairyLightItems.TRIANGLE_PENNANT, new ColorSubtypeInterpreter());
        registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, FairyLightItems.SQUARE_PENNANT, new ColorSubtypeInterpreter());
        FairyLightItems.lights().forEach(i -> registry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, i, new ColorSubtypeInterpreter()));
    }
}
