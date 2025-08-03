package com.magicscience.magicsciencemod.entities;

import com.magicscience.magicsciencemod.MagicScienceMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class MagicEntityRenderer extends EntityRenderer<MagicEntity> {
    private static final ResourceLocation PARTICLE_SPRITE = new ResourceLocation(
        MagicScienceMod.MOD_ID, "textures/particle/magic_1.png"
    );

    public MagicEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0f;
    }

    @Override
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull MagicEntity entity) {
        return PARTICLE_SPRITE;
    }


//    @Override
//    public void render(@NotNull MagicEntity entity, float entityYaw, float partialTicks,
//                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
//
//        poseStack.pushPose();
//
//        // Позиционирование
//        poseStack.translate(0.0D, 0.1D, 0.0D);
//
//        // Сущность всегда "смотрит" на камеру
//        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
//        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
//
//        // Размер "частицы"
//        float scale = 0.5F;
//        poseStack.scale(scale, scale, scale);
//
//        // Рендер квадрата как партикла
//        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(PARTICLE_SPRITE));
//
//        // Координаты текстуры
//        float minU = 0.0F;
//        float maxU = 1.0F;
//        float minV = 0.0F;
//        float maxV = 1.0F;
//
//        // Добавляем вершины
//        vertexConsumer.vertex(poseStack.last().pose(), -0.5F, -0.5F, 0.0F).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),  0.5F, -0.5F, 0.0F).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(),  0.5F,  0.5F, 0.0F).color(255, 255, 255, 255).uv(maxU, minV).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
//        vertexConsumer.vertex(poseStack.last().pose(), -0.5F,  0.5F, 0.0F).color(255, 255, 255, 255).uv(minU, minV).overlayCoords(0).uv2(packedLight).normal(0, 1, 0).endVertex();
//
//        poseStack.popPose();
//
//        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
//    }


}