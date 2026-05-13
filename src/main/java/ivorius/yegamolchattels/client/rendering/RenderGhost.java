package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.entities.EntityGhost;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGhost extends RenderLiving<EntityGhost>
{
    private final ResourceLocation texture = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "ghostTexture.png");

    public RenderGhost(RenderManager renderManager)
    {
        super(renderManager, new ModelGhost(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGhost entity)
    {
        return texture;
    }
}
