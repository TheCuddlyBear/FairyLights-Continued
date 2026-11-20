package de.lucalabs.fairylights.main.string;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.lucalabs.fairylights.main.registries.FairyLightRegistries;
import java.util.Objects;
import net.minecraft.resources.Identifier;

public record StringType(int color) {
    public static final Codec<StringType> CODEC = FairyLightRegistries.STRING_TYPES.byNameCodec()
            .fieldOf("color")
            .codec();
}