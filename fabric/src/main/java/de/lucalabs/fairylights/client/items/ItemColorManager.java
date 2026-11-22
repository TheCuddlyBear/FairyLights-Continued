package de.lucalabs.fairylights.client.items;

import de.lucalabs.fairylights.client.color.FairyLightItemTints;
import net.minecraft.client.color.item.ItemTintSources;

public final class ItemColorManager {

    private ItemColorManager() {
    }

    public static void setupColors() {
        FairyLightItemTints.registerAll(ItemTintSources.ID_MAPPER::put);
    }
}
