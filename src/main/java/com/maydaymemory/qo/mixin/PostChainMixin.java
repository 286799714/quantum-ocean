package com.maydaymemory.qo.mixin;

import com.maydaymemory.qo.api.client.renderer.PostChainInjected;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

@Mixin(PostChain.class)
public class PostChainMixin implements PostChainInjected {
    @Final
    @Shadow
    private List<PostPass> passes;

    @Override
    public void quantum_ocean$setUniform(String name, float... val) {
        Iterator<PostPass> iter = this.passes.iterator();
        if (val.length == 1) {
            while(iter.hasNext()) {
                PostPass postpass = iter.next();
                postpass.getShader().safeGetUniform(name).set(val[0]);
            }
        } else if (val.length == 2) {
            while(iter.hasNext()) {
                PostPass postpass = iter.next();
                postpass.getShader().safeGetUniform(name).set(val[0], val[1]);
            }
        } else if (val.length == 3) {
            while(iter.hasNext()) {
                PostPass postpass = iter.next();
                postpass.getShader().safeGetUniform(name).set(val[0], val[1], val[2]);
            }
        } else if (val.length == 4) {
            while(iter.hasNext()) {
                PostPass postpass = iter.next();
                postpass.getShader().safeGetUniform(name).set(val[0], val[1], val[2], val[4]);
            }
        } else {
            while(iter.hasNext()) {
                PostPass postpass = iter.next();
                postpass.getShader().safeGetUniform(name).set(val);
            }
        }
    }

    @Override
    public void quantum_ocean$setUniform(String name, Matrix4f mat4) {
        for (PostPass postpass : this.passes) {
            postpass.getShader().safeGetUniform(name).set(mat4);
        }
    }
}
