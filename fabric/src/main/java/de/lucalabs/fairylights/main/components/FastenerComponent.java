package de.lucalabs.fairylights.main.components;

import de.lucalabs.fairylights.main.fastener.Fastener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public final class FastenerComponent extends GenericComponent<Fastener<?>> implements AutoSyncedComponent {

    private static final String FASTENER_KEY = "fastener";

    @Override
    public void readData(final ValueInput readView) {
        if (this.delegate != null) {
            readView.read(FASTENER_KEY, CompoundTag.CODEC).ifPresent(this.delegate::readFromNbt);
        }
    }

    @Override
    public void writeData(final ValueOutput writeView) {
        if (this.delegate != null) {
            final CompoundTag tag = new CompoundTag();
            this.delegate.writeToNbt(tag);
            writeView.store(FASTENER_KEY, CompoundTag.CODEC, tag);
        }
    }

    public FastenerComponent setFastener(final Fastener<?> fastener) {
        return (FastenerComponent) super.set(fastener);
    }
}
