package de.lucalabs.fairylights.platform;

import de.lucalabs.fairylights.client.net.ClientNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class NeoForgeClientNetworking implements ClientNetworking {

    @Override
    public void send(final CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }
}
