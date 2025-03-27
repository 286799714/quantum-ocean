package com.maydaymemory.qo.client.event;

import com.maydaymemory.qo.ModQuantumOcean;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = ModQuantumOcean.MODID, value = Dist.CLIENT)
public class SplitRenderEvent {
    @SubscribeEvent
    public static void processPostChain(RenderLevelStageEvent event) {
        RenderLevelStageEvent.Stage stage = event.getStage();
        if (stage == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            ResourceLocation postChainLocation = ResourceLocation.fromNamespaceAndPath("quantum_ocean", "split");
            PostChain postChain = Minecraft.getInstance().getShaderManager().getPostChain(postChainLocation, LevelTargetBundle.MAIN_TARGETS);
            if (postChain != null) {
                postChain.process(Minecraft.getInstance().getMainRenderTarget(), Minecraft.getInstance().gameRenderer.resourcePool);
            }
        }
    }
}
