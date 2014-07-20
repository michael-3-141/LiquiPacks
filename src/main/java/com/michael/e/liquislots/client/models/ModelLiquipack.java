package com.michael.e.liquislots.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLiquipack extends ModelBiped
{
    //fields
    ModelRenderer Base;
    ModelRenderer Right_Tank;
    ModelRenderer Left_Tank;
    ModelRenderer Connector;

    public ModelLiquipack()
    {
        super(1, 0, 64, 64);
        textureWidth = 64;
        textureHeight = 64;

        Base = new ModelRenderer(this, 0, 32);
        Base.addBox(-4F, 1F, 2F, 8, 10, 2);
        Base.setRotationPoint(0F, 0F, 0F);
        Base.setTextureSize(64, 64);
        Base.mirror = true;
        bipedBody.addChild(Base);

        Right_Tank = new ModelRenderer(this, 20, 32);
        Right_Tank.addBox(1F, 1F, 5F, 3, 10, 3);
        Right_Tank.setRotationPoint(0F, 0F, 0F);
        Right_Tank.setTextureSize(64, 64);
        Right_Tank.mirror = true;
        bipedBody.addChild(Right_Tank);

        Left_Tank = new ModelRenderer(this, 20, 32);
        Left_Tank.addBox(-4F, 1F, 5F, 3, 10, 3);
        Left_Tank.setRotationPoint(0F, 0F, 0F);
        Left_Tank.setTextureSize(64, 64);
        Left_Tank.mirror = true;
        bipedBody.addChild(Left_Tank);

        Connector = new ModelRenderer(this, 0, 44);
        Connector.addBox(-4F, 5F, 4F, 8, 2, 1);
        Connector.setRotationPoint(0F, 0F, 0F);
        Connector.setTextureSize(64, 64);
        Connector.mirror = true;
        bipedBody.addChild(Connector);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
