package de.lucalabs.fairylights.client.items;

import de.lucalabs.fairylights.client.color.FairyLightItemTints;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * Registers Fairy Lights' custom {@link net.minecraft.client.color.item.ItemTintSource} codecs
 * through NeoForge's {@link RegisterColorHandlersEvent.ItemTintSources} event.
 */
public final class ItemColorManager {

    private ItemColorManager() {
    }

    public static void setupColors(final RegisterColorHandlersEvent.ItemTintSources event) {
        FairyLightItemTints.registerAll(event::register);
    }
}
