package com.goby56.blazinglyfastboats.render.entity.model

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity


// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


class MotorBoatEntityModel<T : Entity?>(root: ModelPart) : EntityModel<T>() {
    private val hull: ModelPart
    private val engine: ModelPart

    init {
        hull = root.getChild("hull")
        engine = root.getChild("engine")
    }

    fun setupAnim(
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
    }

    fun renderToBuffer(
        poseStack: PoseStack?,
        vertexConsumer: VertexConsumer?,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        hull.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        engine.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation = ModelLayerLocation(ResourceLocation("modid", "custom_model"), "main")
        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition: PartDefinition = meshdefinition.getRoot()
            val hull: PartDefinition = partdefinition.addOrReplaceChild(
                "hull",
                CubeListBuilder.create().texOffs(0, 27)
                    .addBox(-8.0f, -7.0f, -11.0f, 16.0f, 6.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 22.0f, 5.5f, 0.0f, 3.1416f, 0.0f)
            )
            val back: PartDefinition = hull.addOrReplaceChild(
                "back",
                CubeListBuilder.create().texOffs(0, 19)
                    .addBox(6.0f, -9.0f, 23.5f, 3.0f, 6.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(0, 23).addBox(-6.0f, -5.0f, 23.5f, 12.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(13, 19).addBox(-9.0f, -9.0f, 23.5f, 3.0f, 6.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 2.0f, -5.5f)
            )
            val sides: PartDefinition = hull.addOrReplaceChild(
                "sides",
                CubeListBuilder.create().texOffs(0, 35)
                    .addBox(0.5f, -31.0f, -7.0f, 28.0f, 6.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(0, 43).addBox(1.5f, -31.0f, -25.0f, 28.0f, 6.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-15.0f, 24.0f, -10.5f, 0.0f, -1.5708f, 0.0f)
            )
            val floor: PartDefinition = hull.addOrReplaceChild(
                "floor",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-13.5f, -8.0f, -2.0f, 28.0f, 16.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 4.5f, 1.5708f, 1.5708f, 0.0f)
            )
            val engine: PartDefinition = partdefinition.addOrReplaceChild(
                "engine",
                CubeListBuilder.create().texOffs(62, 0)
                    .addBox(-5.0f, -15.0f, -20.0f, 10.0f, 10.0f, 10.0f, CubeDeformation(0.0f))
                    .texOffs(62, 20).addBox(-2.0f, -5.0f, -19.0f, 4.0f, 2.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(78, 20).addBox(-2.0f, -3.0f, -18.0f, 4.0f, 4.0f, 3.0f, CubeDeformation(0.0f))
                    .texOffs(92, 20).addBox(-1.0f, -2.0f, -20.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(100, 20).addBox(-2.0f, -3.0f, -21.0f, 4.0f, 4.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 24.0f, 0.0f)
            )
            return LayerDefinition.create(meshdefinition, 128, 64)
        }
    }

    override fun render(
        matrices: MatrixStack?,
        vertices: VertexConsumer?,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        TODO("Not yet implemented")
    }

    override fun setAngles(
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        TODO("Not yet implemented")
    }
}