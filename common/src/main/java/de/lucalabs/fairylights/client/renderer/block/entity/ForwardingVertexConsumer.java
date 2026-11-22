package de.lucalabs.fairylights.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

public abstract class ForwardingVertexConsumer implements VertexConsumer {
    protected abstract VertexConsumer delegate();

    @Override
    public @NotNull VertexConsumer addVertex(float x, float y, float z) {
        this.delegate().addVertex(x, y, z);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setColor(int r, int g, int b, int a) {
        this.delegate().setColor(r, g, b, a);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setColor(int color) {
        this.delegate().setColor(color);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float u, float v) {
        this.delegate().setUv(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv1(int u, int v) {
        this.delegate().setUv1(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv2(int u, int v) {
        this.delegate().setUv2(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setNormal(float x, float y, float z) {
        this.delegate().setNormal(x, y, z);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setLineWidth(float width) {
        this.delegate().setLineWidth(width);
        return this;
    }
}
