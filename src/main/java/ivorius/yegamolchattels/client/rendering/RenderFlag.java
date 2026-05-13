package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.entities.EntityFlag;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFlag extends Render<EntityFlag>
{
    private final ResourceLocation texture = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "flag_cloth.png");

    public RenderFlag(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(EntityFlag entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFlag entity)
    {
        return texture;
    }
}
