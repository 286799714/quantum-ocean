package com.maydaymemory.qo.client.event;

import com.maydaymemory.qo.ModQuantumOcean;
import com.maydaymemory.qo.api.client.renderer.PostChainInjected;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = ModQuantumOcean.MODID, value = Dist.CLIENT)
public class ScannerRenderEvent {
    private static float fov = 70;
    private static long scanStartTimestamp = 0;

    @SubscribeEvent
    public static void processPostChain(RenderLevelStageEvent event) {
        float timeFromStart = computeTimeFromStartScan();
        if (timeFromStart > 5f) {
            return;
        }
        RenderLevelStageEvent.Stage stage = event.getStage();
        if (stage == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            ResourceLocation postChainLocation = ResourceLocation.fromNamespaceAndPath("quantum_ocean", "scanner");
            PostChain postChain = Minecraft.getInstance().getShaderManager().getPostChain(postChainLocation, LevelTargetBundle.MAIN_TARGETS);
            if (postChain != null) {
                Matrix4f projectionMatrix = Minecraft.getInstance().gameRenderer.getProjectionMatrix(fov);
                ((PostChainInjected)postChain).quantum_ocean$setUniform("ProjMatIn", projectionMatrix);
                ((PostChainInjected)postChain).quantum_ocean$setUniform("TimeFromStart", timeFromStart);
                postChain.process(Minecraft.getInstance().getMainRenderTarget(), Minecraft.getInstance().gameRenderer.resourcePool);
            }
        }
    }

    @SubscribeEvent
    public static void captureFov(ViewportEvent.ComputeFov event) {
        if (event.usedConfiguredFov()) {
            fov = event.getFOV();
        }
    }

    @SubscribeEvent
    public static void onDoScan(LivingEntityUseItemEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof LocalPlayer player) {
            ItemStack itemStack = event.getItem();
            if (itemStack.getItem() == ModQuantumOcean.EXAMPLE_ITEM.get()) {
                player.displayClientMessage(MutableComponent.create(PlainTextContents.create("test")), true);
                scanStartTimestamp = System.currentTimeMillis();
            }
        }
    }

    private static float computeTimeFromStartScan() {
        return (System.currentTimeMillis() - scanStartTimestamp) / 1000.0f;
    }
}
