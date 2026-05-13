package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.entities.EntityBanner;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBanner extends Render<EntityBanner>
{
    private final ResourceLocation textureBannerSmall = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "bannersSmall.png");
    private final ResourceLocation textureBannerLarge = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "bannersLarge.png");

    public RenderBanner(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(EntityBanner entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBanner entity)
    {
        return entity.getSize() == 2 ? textureBannerLarge : textureBannerSmall;
    }
}
