package de.lucalabs.fairylights.main.util;

import java.util.function.Function;
import net.minecraft.util.ARGB;

public final class ColorUtils {
    private ColorUtils() {}

    public static int WHITE = ARGB.color(255, 255, 255, 255);

    public static int transformArgb(
            int color,
            Function<Float, Float> fA,
            Function<Float, Float> fR,
            Function<Float, Float> fG,
            Function<Float, Float> fB) {
        float a = ARGB.alpha(color) / 255F;
        float r = ARGB.red(color) / 255F;
        float g = ARGB.green(color) / 255F;
        float b = ARGB.blue(color) / 255F;
        return ARGB.colorFromFloat(fA.apply(a), fR.apply(r), fG.apply(g), fB.apply(b));
    }
}
