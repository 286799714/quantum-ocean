package com.maydaymemory.qo.api.client.renderer;

import org.joml.Matrix4f;

public interface PostChainInjected {
    void quantum_ocean$setUniform(String name, float... val);
    void quantum_ocean$setUniform(String name, Matrix4f mat4);
}
