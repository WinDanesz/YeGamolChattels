package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.entities.EntityFlag;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderFlag extends Render<EntityFlag>
{
    private static final ResourceLocation POLE_TEXTURE = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "flag_pole.png");
    private static final ResourceLocation CLOTH_TEXTURE = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "flag_cloth.png");

    public RenderFlag(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(EntityFlag entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (entity.world == null)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.enableRescaleNormal();
        renderFlag(entity, partialTicks);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    private void renderFlag(EntityFlag entity, float partialTicks)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float ticks = entity.ticksExisted + partialTicks;
        float wind = EntityFlag.getInterpolatedWind(entity.wind, entity.simWind, partialTicks) * 0.96f + 0.04f;
        float sizeY = entity.getFlagHeight() / 16.0F;
        float poleWidth = 0.0625f;

        bindTexture(POLE_TEXTURE);
        for (int segmentY = 0; segmentY < sizeY; segmentY++)
        {
            setLight(entity, MathHelper.floor(entity.posX), MathHelper.floor(entity.posY) + segmentY, MathHelper.floor(entity.posZ));

            float texY0 = 1.0f - segmentY / sizeY;
            float texY1 = 1.0f - (segmentY + 1) / sizeY;

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

            addQuad(buffer, -poleWidth, segmentY + 1, -0.5D, -poleWidth, segmentY, -0.5D, -poleWidth, segmentY, 0.5D, -poleWidth, segmentY + 1, 0.5D, 0.0, texY1, 0.0, texY0, 1.0, texY0, 1.0, texY1, -1.0F, 0.0F, 0.0F);
            addQuad(buffer, poleWidth, segmentY + 1, 0.5D, poleWidth, segmentY, 0.5D, poleWidth, segmentY, -0.5D, poleWidth, segmentY + 1, -0.5D, 0.0, texY1, 0.0, texY0, 1.0, texY0, 1.0, texY1, 1.0F, 0.0F, 0.0F);
            addQuad(buffer, -0.5D, segmentY + 1, poleWidth, -0.5D, segmentY, poleWidth, 0.5D, segmentY, poleWidth, 0.5D, segmentY + 1, poleWidth, 0.0, texY1, 0.0, texY0, 1.0, texY0, 1.0, texY1, 0.0F, 0.0F, 1.0F);
            addQuad(buffer, 0.5D, segmentY + 1, -poleWidth, 0.5D, segmentY, -poleWidth, -0.5D, segmentY, -poleWidth, -0.5D, segmentY + 1, -poleWidth, 0.0, texY1, 0.0, texY0, 1.0, texY0, 1.0, texY1, 0.0F, 0.0F, -1.0F);

            tessellator.draw();
        }

        float thickness = renderManager.options.fancyGraphics ? (0.015f + (sizeY - 1) * 0.004f) : 0.0125f;
        float clothLength = sizeY * 0.9f;
        int segments = renderManager.options.fancyGraphics ? 32 : 8;

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180.0f, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(poleWidth, sizeY * 0.45f, 0.0f);
        GlStateManager.scale(0.7f, 0.7f, 1.0f);

        double texX0 = ((entity.getColor() % 4) + 0.001) / 4.0;
        double texX1 = ((entity.getColor() % 4) + 0.999) / 4.0;
        double texY0 = ((entity.getColor() >> 2) + 0.001) / 4.0;
        double texY1 = ((entity.getColor() >> 2) + 0.999) / 4.0;
        double folding = Math.max(0.0, 1.0 - wind * 4.0);

        bindTexture(CLOTH_TEXTURE);
        setLight(entity, MathHelper.floor(entity.posX), MathHelper.floor(entity.posY + sizeY), MathHelper.floor(entity.posZ));
        GlStateManager.disableCull();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        for (int xSegment = 0; xSegment < segments; xSegment++)
        {
            double ratio = (xSegment + 0.001) / (double) segments;
            double ratio1 = (xSegment + 0.999) / (double) segments;
            double x0 = clothLength * ratio;
            double x1 = clothLength * ratio1;
            double z0 = MathHelper.sin((float) ((ratio * 2.5 - ticks * 0.06) * Math.PI)) * sizeY * 0.08 * wind;
            z0 += MathHelper.sin((float) (ratio * 25.5)) * sizeY * ratio * 0.05 * folding;
            double z1 = MathHelper.sin((float) ((ratio1 * 2.5 - ticks * 0.06) * Math.PI)) * sizeY * 0.08 * wind;
            z1 += MathHelper.sin((float) (ratio1 * 25.5)) * sizeY * ratio1 * 0.05 * folding;
            double segmentTexX0 = texX0 + (texX1 - texX0) * ratio;
            double segmentTexX1 = texX0 + (texX1 - texX0) * ratio1;

            for (int ySegment = 0; ySegment < segments; ySegment++)
            {
                double ratioY = ySegment / (double) segments;
                double ratioY1 = (ySegment + 1.0) / segments;
                double sag0 = ratio * ratio * sizeY * (0.12 + wind * 0.08);
                double sag1 = ratio1 * ratio1 * sizeY * (0.12 + wind * 0.08);
                double y0 = ratioY * sizeY - sag0;
                double y1 = ratioY * sizeY - sag1;
                double segmentTexY0 = texY0 + (texY1 - texY0) * ratioY;
                double segmentTexY1 = texY0 + (texY1 - texY0) * ratioY1;

                renderSegment(buffer, x0, y0, y1, z0, z1, clothLength / segments, sizeY / segments, thickness, segmentTexX0, segmentTexY0, segmentTexX1, segmentTexY1);
            }
        }
        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    private void setLight(EntityFlag entity, int x, int y, int z)
    {
        int brightness = entity.world.getCombinedLight(new net.minecraft.util.math.BlockPos(x, y, z), 0);
        int sky = brightness % 65536;
        int block = brightness / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, sky, block);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    private static void renderSegment(BufferBuilder buffer, double x, double y, double y1, double z, double z1, double sizeX, double sizeY, double sizeZ, double texX, double texY, double texX1, double texY1)
    {
        double xMin = x;
        double xMax = x + sizeX;

        addQuad(buffer, xMax, y1, z1 - sizeZ, xMin, y, z - sizeZ, xMin, y + sizeY, z - sizeZ, xMax, y1 + sizeY, z1 - sizeZ, texX1, texY, texX, texY, texX, texY1, texX1, texY1, 0.0F, 0.0F, -1.0F);
        addQuad(buffer, xMin, y + sizeY, z + sizeZ, xMin, y, z + sizeZ, xMax, y1, z1 + sizeZ, xMax, y1 + sizeY, z1 + sizeZ, texX, texY1, texX, texY, texX1, texY, texX1, texY1, 0.0F, 0.0F, 1.0F);

        if (sizeZ > 0.0f)
        {
            addQuad(buffer, xMax, y1 + sizeY, z1 - sizeZ, xMin, y + sizeY, z - sizeZ, xMin, y + sizeY, z + sizeZ, xMax, y1 + sizeY, z1 + sizeZ, texX1, texY, texX, texY, texX, texY1, texX1, texY1, 0.0F, 1.0F, 0.0F);
            addQuad(buffer, xMin, y, z + sizeZ, xMin, y, z - sizeZ, xMax, y1, z1 - sizeZ, xMax, y1, z1 + sizeZ, texX, texY1, texX, texY, texX1, texY, texX1, texY1, 0.0F, -1.0F, 0.0F);
            addQuad(buffer, xMin, y + sizeY, z + sizeZ, xMin, y + sizeY, z - sizeZ, xMin, y, z - sizeZ, xMin, y, z + sizeZ, texX1, texY1, texX, texY1, texX, texY, texX1, texY, -1.0F, 0.0F, 0.0F);
            addQuad(buffer, xMax, y1, z1 - sizeZ, xMax, y1 + sizeY, z1 - sizeZ, xMax, y1 + sizeY, z1 + sizeZ, xMax, y1, z1 + sizeZ, texX, texY, texX, texY1, texX1, texY1, texX1, texY, 1.0F, 0.0F, 0.0F);
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
    protected ResourceLocation getEntityTexture(EntityFlag entity)
    {
        return POLE_TEXTURE;
    }
}
