package ivorius.yegamolchattels.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class RenderEngineOverride extends TextureManager
{
    public TextureManager renderEngine;
    public ResourceLocation textureOverride;

    public RenderEngineOverride()
    {
        super(Minecraft.getMinecraft().getResourceManager());
        renderEngine = Minecraft.getMinecraft().getTextureManager();
    }

    @Override
    public void bindTexture(ResourceLocation resourceLocation)
    {
        renderEngine.bindTexture(textureOverride != null ? textureOverride : resourceLocation);
    }
}
