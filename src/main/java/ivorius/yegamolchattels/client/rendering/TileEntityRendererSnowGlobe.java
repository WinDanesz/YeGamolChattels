package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.blocks.TileEntitySnowGlobe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

public class TileEntityRendererSnowGlobe extends TileEntitySpecialRenderer<TileEntitySnowGlobe>
{
    private static final ItemStack BASE_STACK = new ItemStack(Blocks.PLANKS);
    private static final ItemStack SNOW_STACK = new ItemStack(Blocks.SNOW);
    private static final ItemStack HOUSE_STACK = new ItemStack(Blocks.PLANKS);
    private static final ItemStack REALITY_STACK = new ItemStack(Blocks.ENDER_CHEST);
    private static final ItemStack GLOBE_STACK = new ItemStack(Blocks.GLASS);

    @Override
    public void render(TileEntitySnowGlobe te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        int renderPass = MinecraftForgeClient.getRenderPass();
        float realityRatio = te.realityGlobeRatio;
        float time = te.getWorld() != null ? te.getWorld().getTotalWorldTime() + partialTicks : partialTicks;
        float rumble = te.getRumbling();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5f, y + 0.35f, z + 0.5f);

        if (renderPass == 0)
        {
            renderStack(BASE_STACK, 0.0f, -0.42f, 0.0f, 0.9f, 0.18f, 0.9f, 0.0f, 1.0f);
            renderStack(SNOW_STACK, 0.0f, -0.22f, 0.0f, 0.62f, 0.12f, 0.62f, 0.0f, 1.0f);

            float spin = time * 2.0f;
            float rumbleX = rumble > 0.0f ? (float) Math.sin(time * 0.37f) * rumble : 0.0f;
            float rumbleZ = rumble > 0.0f ? (float) Math.cos(time * 0.29f) * rumble : 0.0f;

            if (realityRatio < 1.0f)
                renderStack(HOUSE_STACK, rumbleX, -0.05f, rumbleZ, 0.3f, 0.3f, 0.3f, spin, 1.0f - realityRatio);
            if (realityRatio > 0.0f)
                renderStack(REALITY_STACK, -rumbleX, -0.02f, -rumbleZ, 0.28f, 0.28f, 0.28f, -spin, realityRatio);
        }
        else if (renderPass == 1)
        {
            renderStack(GLOBE_STACK, 0.0f, 0.02f, 0.0f, 0.82f, 0.82f, 0.82f, 0.0f, 0.7f);
        }

        GlStateManager.popMatrix();
    }

    private void renderStack(ItemStack stack, float x, float y, float z, float scaleX, float scaleY, float scaleZ, float rotationY, float alpha)
    {
        if (stack.isEmpty() || alpha <= 0.0f)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(rotationY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(scaleX, scaleY, scaleZ);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
