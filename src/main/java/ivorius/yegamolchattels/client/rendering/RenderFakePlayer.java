package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.entities.EntityFakePlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFakePlayer extends RenderBiped<EntityFakePlayer>
{
    public RenderFakePlayer(RenderManager renderManager)
    {
        super(renderManager, new ModelBiped(0.0f), 0.5f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFakePlayer entity)
    {
        return entity.getLocationSkin();
    }
}
