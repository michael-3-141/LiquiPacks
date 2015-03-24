package com.michael.e.liquislots.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLiquipack extends ModelBiped {
    public ModelRenderer tank11;
    public ModelRenderer tank12;
    public ModelRenderer tank21;
    public ModelRenderer tank22;
    public ModelRenderer tank31;
    public ModelRenderer tank32;
    public ModelRenderer tank41;
    public ModelRenderer tank42;
    public ModelRenderer strap1;
    public ModelRenderer strap2;
    public ModelRenderer strap3;
    public ModelRenderer pipeleft1;
    public ModelRenderer pipeleft2;
    public ModelRenderer pipeleft3;
    public ModelRenderer piperight1;
    public ModelRenderer piperight2;
    public ModelRenderer piperight3;

    public ModelLiquipack() {
        super(1, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.pipeleft3 = new ModelRenderer(this, 17, 56);
        this.pipeleft3.addBox(5.0F, 14.0F, 4.0F, 1, 1, 1, 0F);
        this.tank41 = new ModelRenderer(this, 41, 58);
        this.tank41.addBox(-4.0F, 12.5F, 3.5F, 9, 4, 2, 0F);
        this.strap1 = new ModelRenderer(this, 0, 60);
        this.strap1.addBox(-4.5F, 1.5F, 2.0F, 10, 3, 1, 0F);
        this.tank11 = new ModelRenderer(this, 41, 58);
        this.tank11.addBox(-4.0F, 0.5F, 4.0F, 9, 4, 2, 0F);
        this.tank32 = new ModelRenderer(this, 49, 38);
        this.tank32.addBox(1.5F, 4.5F, 2.5F, 3, 8, 4, 0F);
        this.pipeleft2 = new ModelRenderer(this, 24, 45);
        this.pipeleft2.addBox(6.0F, 3.0F, 4.0F, 1, 12, 1, 0F);
        this.piperight1 = new ModelRenderer(this, 17, 56);
        this.piperight1.addBox(-5.0F, 3.0F, 4.0F, 1, 1, 1, 0F);
        this.tank21 = new ModelRenderer(this, 37, 40);
        this.tank21.addBox(-4.0F, 4.5F, 3.5F, 4, 8, 2, 0F);
        this.strap3 = new ModelRenderer(this, 0, 45);
        this.strap3.addBox(-4.5F, 4.5F, 2.0F, 2, 12, 1, 0F);
        this.tank22 = new ModelRenderer(this, 49, 38);
        this.tank22.addBox(-3.5F, 4.5F, 2.5F, 3, 8, 4, 0F);
        this.tank12 = new ModelRenderer(this, 37, 51);
        this.tank12.addBox(-4.0F, 1.0F, 3.0F, 9, 3, 4, 0F);
        this.tank42 = new ModelRenderer(this, 37, 51);
        this.tank42.addBox(-4.0F, 13.0F, 3.0F, 9, 3, 4, 0F);
        this.piperight3 = new ModelRenderer(this, 17, 56);
        this.piperight3.addBox(-5.0F, 14.0F, 4.0F, 1, 1, 1, 0F);
        this.tank31 = new ModelRenderer(this, 37, 40);
        this.tank31.addBox(1.0F, 4.5F, 3.5F, 4, 8, 2, 0F);
        this.strap2 = new ModelRenderer(this, 6, 45);
        this.strap2.addBox(3.5F, 4.5F, 2.0F, 2, 12, 1, 0F);
        this.pipeleft1 = new ModelRenderer(this, 17, 56);
        this.pipeleft1.addBox(5.0F, 3.0F, 4.0F, 1, 1, 1, 0F);
        this.piperight2 = new ModelRenderer(this, 24, 45);
        this.piperight2.addBox(-6.0F, 3.0F, 4.0F, 1, 12, 1, 0F);

        this.bipedBody.addChild(tank11);
        this.bipedBody.addChild(tank12);
        this.bipedBody.addChild(tank21);
        this.bipedBody.addChild(tank22);
        this.bipedBody.addChild(tank31);
        this.bipedBody.addChild(tank32);
        this.bipedBody.addChild(tank41);
        this.bipedBody.addChild(tank42);
        this.bipedBody.addChild(strap1);
        this.bipedBody.addChild(strap2);
        this.bipedBody.addChild(strap3);
        this.bipedBody.addChild(pipeleft1);
        this.bipedBody.addChild(pipeleft2);
        this.bipedBody.addChild(pipeleft3);
        this.bipedBody.addChild(piperight1);
        this.bipedBody.addChild(piperight2);
        this.bipedBody.addChild(piperight3);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if(entity.isSneaking()){
            tank11.rotateAngleX = 0.5F;
            tank12.rotateAngleX = 0.5F;
            tank21.rotateAngleX = 0.5F;
            tank22.rotateAngleX = 0.5F;
            tank31.rotateAngleX = 0.5F;
            tank32.rotateAngleX = 0.5F;
            tank41.rotateAngleX = 0.5F;
            tank42.rotateAngleX = 0.5F;
            strap1.rotateAngleX = 0.5F;
            strap2.rotateAngleX = 0.5F;
            strap3.rotateAngleX = 0.5F;
            pipeleft1.rotateAngleX = 0.5F;
            pipeleft2.rotateAngleX = 0.5F;
            pipeleft3.rotateAngleX = 0.5F;
            piperight1.rotateAngleX = 0.5F;
            piperight2.rotateAngleX = 0.5F;
            piperight3.rotateAngleX = 0.5F;
        }
        else{
            tank11.rotateAngleX = 0F;
            tank12.rotateAngleX = 0F;
            tank21.rotateAngleX = 0F;
            tank22.rotateAngleX = 0F;
            tank31.rotateAngleX = 0F;
            tank32.rotateAngleX = 0F;
            tank41.rotateAngleX = 0F;
            tank42.rotateAngleX = 0F;
            strap1.rotateAngleX = 0F;
            strap2.rotateAngleX = 0F;
            strap3.rotateAngleX = 0F;
            pipeleft1.rotateAngleX = 0F;
            pipeleft2.rotateAngleX = 0F;
            pipeleft3.rotateAngleX = 0F;
            piperight1.rotateAngleX = 0F;
            piperight2.rotateAngleX = 0F;
            piperight3.rotateAngleX = 0F;
        }
        super.render(entity, f, f1, f2, f3, f4, 0.0525F);
    }
}
