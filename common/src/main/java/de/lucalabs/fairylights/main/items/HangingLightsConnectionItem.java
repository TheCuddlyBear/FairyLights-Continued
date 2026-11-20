package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.main.connection.ConnectionTypes;
import de.lucalabs.fairylights.main.items.components.ComponentRecords;
import de.lucalabs.fairylights.main.registries.FairyLightRegistries;
import de.lucalabs.fairylights.main.string.StringTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.HANGING_LIGHTS);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @NotNull TooltipContext context,
            @NotNull TooltipDisplay display,
            @NotNull Consumer<Component> tooltip,
            @NotNull TooltipFlag type) {
        final ComponentRecords.ConnectionLogic logic = ComponentRecords.ConnectionLogic.fromItemStack(stack);
        if (logic == null) {
            return;
        }

        final Identifier name = FairyLightRegistries.STRING_TYPES.getKey(logic.string().orElse(StringTypes.BLACK_STRING));
        if (name != null) {
            tooltip.accept(Component.translatable("item." + name.getNamespace() + "." + name.getPath()).withStyle(ChatFormatting.GRAY));
        }

        if (!logic.pattern().isEmpty()) {
            tooltip.accept(Component.empty());
        }
        for (ItemStack lightStack : logic.pattern()) {
            tooltip.accept(lightStack.getHoverName());
            lightStack.getItem().appendHoverText(lightStack, context, display, tooltip, type);
        }
    }
}
