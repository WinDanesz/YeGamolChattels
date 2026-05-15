package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.entities.EntityBanner;
import ivorius.yegamolchattels.entities.EntityFlag;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityHanging;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBanner extends Render<EntityBanner>
{
    private final ResourceLocation textureBannerSmall = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "banners_small.png");
    private final ResourceLocation textureBannerLarge = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "banners_large.png");

    public RenderBanner(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(EntityBanner entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.enableRescaleNormal();
        bindEntityTexture(entity);
        GlStateManager.scale(0.0625F, 0.0625F, 0.0625F);

        int bannerWidth = entity.getWidthPixels();
        int bannerHeight = entity.getHeightPixels();
        int texShiftX = (entity.getColor() % 4) * bannerWidth;
        int texShiftY = (entity.getColor() >> 2) * bannerHeight;
        renderBanner(entity, bannerWidth, bannerHeight, texShiftX, texShiftY, partialTicks);

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    private void renderBanner(EntityBanner entity, int bannerWidth, int bannerHeight, int texShiftX, int texShiftY, float partialTicks)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float ticks = entity.ticksExisted + partialTicks;
        float wind = EntityFlag.getInterpolatedWind(entity.wind, entity.simWind, partialTicks);

        double xShift = -bannerWidth / 2.0;
        double yShift = -bannerHeight / 2.0;

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        for (int x = 0; x < bannerWidth; x++)
        {
            for (int y = 0; y < bannerHeight; y++)
            {
                double thickness = renderManager.options.fancyGraphics ? 0.7f : 0.0f;
                if ((bannerHeight - y) < 2 + bannerHeight / 16)
                    thickness *= 2.0f;
                double zShift = -thickness * 0.5 + 0.5;

                double x1 = x + 0.999;
                double y1 = y + 0.999;

                double minX = xShift + x;
                double minY = yShift + y;
                double texMinX = ((texShiftX + bannerWidth) - x1) / (bannerWidth * 4.0);
                double texMaxX = ((texShiftX + bannerWidth) - (x + 0.001)) / (bannerWidth * 4.0);
                double texMinY = ((texShiftY + bannerHeight) - y1) / (bannerHeight * 4.0);
                double texMaxY = ((texShiftY + bannerHeight) - (y + 0.001)) / (bannerHeight * 4.0);

                double waveEffect = (bannerHeight - y - 4) * 0.015f * wind;
                double waveEffect1 = (bannerHeight - y1 - 4) * 0.015f * wind;
                double z0 = waveEffect > 0.0 ? MathHelper.sin((float) ((y * 0.04f + ticks * 0.06f) * Math.PI)) * waveEffect : 0.0;
                double z1 = waveEffect1 > 0.0 ? MathHelper.sin((float) (((float) y1 * 0.04f + ticks * 0.06f) * Math.PI)) * waveEffect1 : 0.0;

                if (!renderManager.options.fancyGraphics)
                {
                    z0 = Math.min(z0, 0.3);
                    z1 = Math.min(z1, 0.3);
                }

                setLight(entity, (float) minX + 0.5f, (float) minY + 0.5f);
                renderSegment(buffer, minX, minY, zShift + z0, zShift + z1, 1.0, 1.0, thickness, texMinX, texMinY, texMaxX, texMaxY);
            }
        }
        tessellator.draw();
    }

    private void setLight(EntityHanging entity, float x, float y)
    {
        int lightX = MathHelper.floor(entity.posX);
        int lightY = MathHelper.floor(entity.posY + y / 16.0F);
        int lightZ = MathHelper.floor(entity.posZ);

        if (entity.facingDirection == net.minecraft.util.EnumFacing.NORTH)
            lightX = MathHelper.floor(entity.posX - x / 16.0F);
        else if (entity.facingDirection == net.minecraft.util.EnumFacing.SOUTH)
            lightX = MathHelper.floor(entity.posX + x / 16.0F);
        else if (entity.facingDirection == net.minecraft.util.EnumFacing.WEST)
            lightZ = MathHelper.floor(entity.posZ + x / 16.0F);
        else if (entity.facingDirection == net.minecraft.util.EnumFacing.EAST)
            lightZ = MathHelper.floor(entity.posZ - x / 16.0F);

        int brightness = this.renderManager.world.getCombinedLight(new net.minecraft.util.math.BlockPos(lightX, lightY, lightZ), 0);
        int sky = brightness % 65536;
        int block = brightness / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, sky, block);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    private static void renderSegment(BufferBuilder buffer, double x, double y, double z, double z1, double sizeX, double sizeY, double sizeZ, double texX, double texY, double texX1, double texY1)
    {
        int zShift = -1;
        addQuad(buffer,
                x + sizeX, y, z + zShift * sizeZ,
                x, y, z + zShift * sizeZ,
                x, y + sizeY, z1 + zShift * sizeZ,
                x + sizeX, y + sizeY, z1 + zShift * sizeZ,
                texX1, texY, texX, texY, texX, texY1, texX1, texY1,
                0.0F, 0.0F, zShift);

        zShift = 1;
        addQuad(buffer,
                x, y + sizeY, z1 + zShift * sizeZ,
                x, y, z + zShift * sizeZ,
                x + sizeX, y, z + zShift * sizeZ,
                x + sizeX, y + sizeY, z1 + zShift * sizeZ,
                texX, texY1, texX, texY, texX1, texY, texX1, texY1,
                0.0F, 0.0F, zShift);

        if (sizeZ > 0.0f)
        {
            addQuad(buffer,
                    x + sizeX, y + sizeY, z1 - sizeZ,
                    x, y + sizeY, z1 - sizeZ,
                    x, y + sizeY, z1 + sizeZ,
                    x + sizeX, y + sizeY, z1 + sizeZ,
                    texX1, texY, texX, texY, texX, texY1, texX1, texY1,
                    0.0F, 1.0F, 0.0F);
            addQuad(buffer,
                    x, y, z + sizeZ,
                    x, y, z - sizeZ,
                    x + sizeX, y, z - sizeZ,
                    x + sizeX, y, z + sizeZ,
                    texX, texY1, texX, texY, texX1, texY, texX1, texY1,
                    0.0F, -1.0F, 0.0F);
            addQuad(buffer,
                    x, y + sizeY, z1 + sizeZ,
                    x, y + sizeY, z1 - sizeZ,
                    x, y, z - sizeZ,
                    x, y, z + sizeZ,
                    texX1, texY1, texX, texY1, texX, texY, texX1, texY,
                    -1.0F, 0.0F, 0.0F);
            addQuad(buffer,
                    x + sizeX, y, z - sizeZ,
                    x + sizeX, y + sizeY, z1 - sizeZ,
                    x + sizeX, y + sizeY, z1 + sizeZ,
                    x + sizeX, y, z + sizeZ,
                    texX, texY, texX, texY1, texX1, texY1, texX1, texY,
                    1.0F, 0.0F, 0.0F);
        }
    }

    private static void addQuad(BufferBuilder buffer,
                                double x1, double y1, double z1,
                                double x2, double y2, double z2,
                                double x3, double y3, double z3,
                                double x4, double y4, double z4,
                                double u1, double v1,
                                double u2, double v2,
                                double u3, double v3,
                                double u4, double v4,
                                float nx, float ny, float nz)
    {
        buffer.pos(x1, y1, z1).tex(u1, v1).normal(nx, ny, nz).endVertex();
        buffer.pos(x2, y2, z2).tex(u2, v2).normal(nx, ny, nz).endVertex();
        buffer.pos(x3, y3, z3).tex(u3, v3).normal(nx, ny, nz).endVertex();
        buffer.pos(x4, y4, z4).tex(u4, v4).normal(nx, ny, nz).endVertex();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBanner entity)
    {
        return entity.getSize() == 2 ? textureBannerLarge : textureBannerSmall;
    }
}
