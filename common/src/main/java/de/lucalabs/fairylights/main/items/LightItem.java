package de.lucalabs.fairylights.main.items;

import de.lucalabs.fairylights.main.blocks.LightBlock;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import static de.lucalabs.fairylights.main.items.components.FairyLightItemComponents.COLORS;
import static de.lucalabs.fairylights.main.items.components.FairyLightItemComponents.TWINKLE;

public class LightItem extends BlockItem {
    private final LightBlock light;

    public LightItem(final LightBlock light, final Properties properties) {
        super(light, properties);
        this.light = light;
    }

    @Override
    public LightBlock getBlock() {
        return this.light;
    }

    @Override
    public void appendHoverText(
            final ItemStack stack,
            final TooltipContext context,
            final TooltipDisplay display,
            final Consumer<Component> tooltip,
            final TooltipFlag type) {
        super.appendHoverText(stack, context, display, tooltip, type);

        if (Objects.requireNonNullElse(stack.get(TWINKLE), false)) {
            tooltip.accept(Component.translatable("item.fairyLights.twinkle").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }

        if (stack.has(COLORS)) {
            for (int color : Objects.requireNonNull(stack.get(COLORS))) {
                tooltip.accept(DyeableItem.getColorName(color).copy().withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
