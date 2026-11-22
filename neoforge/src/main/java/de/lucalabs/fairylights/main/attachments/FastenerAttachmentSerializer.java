package de.lucalabs.fairylights.main.attachments;

import de.lucalabs.fairylights.main.fastener.Fastener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

/**
 * (De)serializes a {@link Fastener} attachment. Deserialization is holder-aware: it recreates the
 * correct Fastener subtype for the holder (block entity / fence / player) before reading the NBT,
 * mirroring the per-holder factories the Fabric build registers through Cardinal Components.
 */
public class FastenerAttachmentSerializer implements IAttachmentSerializer<Fastener<?>> {

    private static final String FASTENER_KEY = "fastener";

    @Override
    public @Nullable Fastener<?> read(final IAttachmentHolder holder, final ValueInput input) {
        // Reuse the existing fastener if one is already attached (NeoForge has not overwritten it yet
        // at this point). AbstractFastener#readFromNbt updates connections that already exist in place,
        // preserving their computed catenary; allocating a fresh fastener on every sync instead drops
        // that state for a frame and makes an actively-dragged connection flicker. This mirrors how
        // Cardinal Components reads into the existing component on Fabric.
        Fastener<?> fastener = holder.getExistingData(FairyLightsAttachments.FASTENER.get()).orElse(null);
        if (fastener == null) {
            fastener = FairyLightsAttachments.createFor(holder);
        }
        if (fastener != null) {
            final Fastener<?> target = fastener;
            input.read(FASTENER_KEY, CompoundTag.CODEC).ifPresent(target::readFromNbt);
        }
        return fastener;
    }

    @Override
    public boolean write(final Fastener<?> attachment, final ValueOutput output) {
        final CompoundTag tag = new CompoundTag();
        attachment.writeToNbt(tag);
        output.store(FASTENER_KEY, CompoundTag.CODEC, tag);
        return true;
    }
}
