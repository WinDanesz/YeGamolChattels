/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.gui;

import ivorius.yegamolchattels.network.PacketGuiAction;
import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.Statue;
import ivorius.yegamolchattels.blocks.StatueHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by lukas on 27.07.14.
 */
public class GuiScreenCarveStatue extends GuiContainer implements GuiControlListener<GuiSlider>
{
    private static ResourceLocation guiTexture = new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "gui_carving.png");

    private GuiSlider sliderSwing;
    private GuiSlider sliderStance;

    private GuiSlider sliderYawHead;
    private GuiSlider sliderPitchHead;

    private GuiButton confirmButton;

    private Entity lastCraftedEntity;
    private int x;
    private int y;
    private int z;

    public GuiScreenCarveStatue(EntityPlayer player, int x, int y, int z)
    {
        super(new ContainerCarveStatue(player.inventory, player, x, y, z));

        this.x = x;
        this.y = y;
        this.z = z;
        ySize = 166 + 5;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(confirmButton = new GuiButton(0, width / 2 - 60, height / 2 - 20, 65, 20, I18n.format("gui.carve.confirm")));
        confirmButton.enabled = false;
        buttonList.add(sliderSwing = new GuiSlider(50, width / 2 - 80, height / 2 - 51, 85, 14, I18n.format("gui.carve.swing")));
        sliderSwing.addListener(this);
        sliderSwing.setMinValue(0.0f);
        sliderSwing.setMaxValue(2.0f);
        buttonList.add(sliderStance = new GuiSlider(50, width / 2 - 80, height / 2 - 36, 85, 14, I18n.format("gui.carve.stance")));
        sliderStance.addListener(this);
        sliderStance.setMinValue(0.0f);
        sliderStance.setMaxValue(5.0f);
        sliderStance.setValue(2.4f);
        buttonList.add(sliderYawHead = new GuiSlider(50, width / 2 - 80, height / 2 - 66, 85, 14, I18n.format("gui.carve.head.yaw")));
        sliderYawHead.addListener(this);
        sliderYawHead.setMinValue(-60.0f);
        sliderYawHead.setMaxValue(60.0f);
        buttonList.add(sliderPitchHead = new GuiSlider(50, width / 2 - 80, height / 2 - 81, 85, 14, I18n.format("gui.carve.head.pitch")));
        sliderPitchHead.addListener(this);
        sliderPitchHead.setMinValue(-60.0f);
        sliderPitchHead.setMaxValue(60.0f);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        Entity newCraftedEntity = ((ContainerCarveStatue) inventorySlots).getCurrentCraftedEntity();
        if (lastCraftedEntity != newCraftedEntity)
        {
            lastCraftedEntity = newCraftedEntity;
            confirmButton.enabled = newCraftedEntity != null && StatueHelper.canCarveStatue(newCraftedEntity, mc.world, x, y, z);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            int chiselItem = mc.player.inventory.currentItem;
            YeGamolChattels.network.sendToServer(PacketGuiAction.packetGuiAction("carveStatue", chiselItem, sliderYawHead.getValue(), sliderPitchHead.getValue(), sliderSwing.getValue(), sliderStance.getValue()));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        this.mc.getTextureManager().bindTexture(guiTexture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        Entity entity = lastCraftedEntity;

        if (entity != null)
        {
            if (entity instanceof EntityLivingBase)
                Statue.setRotations((EntityLivingBase) entity, sliderYawHead.getValue(), sliderPitchHead.getValue(), sliderSwing.getValue(), sliderStance.getValue());

            if (entity instanceof EntitySquid)
            {
                renderEntity(width / 2 + 45, height / 2 - 44, 45.0f / (entity.height * 2.0f), entity);
            }
            else
            {
                float scale = 45f / entity.height;
                renderEntity(width / 2 + 45, height / 2 - 14, scale, entity);
            }
        }
    }

    public static void renderEntity(int x, int y, float scale, Entity entity)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, 50.0f);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);

        RenderHelper.enableStandardItemLighting();
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setPlayerViewY(180.0f);
        renderManager.setRenderShadow(false);
        renderManager.renderEntity(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        renderManager.setRenderShadow(true);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    public void valueChanged(GuiSlider gui)
    {

    }
}
