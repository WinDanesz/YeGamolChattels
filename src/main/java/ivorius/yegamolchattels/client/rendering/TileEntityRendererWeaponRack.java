package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.TileEntityWeaponRack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityRendererWeaponRack extends TileEntitySpecialRenderer<TileEntityWeaponRack>
{
    private static final ResourceLocation TEXTURE_FLOOR = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/weapon_rack_texture.png");
    private static final ResourceLocation TEXTURE_FLOOR_CARVED = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_carved.png");
    private static final ResourceLocation TEXTURE_FLOOR_DAMAGED = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_damaged.png");
    private static final ResourceLocation TEXTURE_WALL = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_wall.png");
    private static final ResourceLocation TEXTURE_WALL_CARVED = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_carved_wall.png");
    private static final ResourceLocation TEXTURE_WALL_DAMAGED = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_damaged_wall.png");

    private final ModelWeaponRack modelFloor = new ModelWeaponRack();
    private final ModelWeaponRackWall modelWall = new ModelWeaponRackWall();
    private final ModelBase[] detailModelsFloor = {
            new ModelWeaponRackDetailMushrooms(),
            new ModelWeaponRackDetailBonemeal(),
            new ModelWeaponRackDetailLeather(),
            new ModelWeaponRackDetailCobweb(),
            null,
            null
    };
    private final ModelBase[] detailModelsWall = {
            new ModelWeaponRackDetailMushroomsWall(),
            new ModelWeaponRackDetailBonemealWall(),
            new ModelWeaponRackDetailLeatherWall(),
            new ModelWeaponRackDetailCobwebWall(),
            null,
            null
    };
    private final ResourceLocation[] detailTexturesFloor = {
            new ResourceLocation(YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_detail_mushrooms.png"),
            new ResourceLocation(YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_detail_bonemeal.png"),
            new ResourceLocation(YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_detail_leather.png"),
            new ResourceLocation(YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_detail_cobweb.png"),
            null,
            null
    };
    private final ResourceLocation[] detailTexturesWall = {
            new ResourceLocation(YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_detail_mushrooms_wall.png"),
            new ResourceLocation(YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_detail_bonemeal_wall.png"),
            new ResourceLocation(YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_detail_leather_wall.png"),
            new ResourceLocation(YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_detail_cobweb_wall.png"),
            null,
            null
    };
    private final float[] weaponSlotPositionsFloor = {-0.28f, -0.12f, 0.13f, 0.29f};
    private final float[] weaponSlotPositionsWall = {-0.6f, -1.5f};

    @Override
    public void render(TileEntityWeaponRack te, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha)
    {
        int type = te.getWeaponRackType();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GlStateManager.rotate(-90.0f * te.direction + 180.0f, 0.0f, 1.0f, 0.0f);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        bindTexture(baseTexture(type, te.effectsApplied[5] ? 2 : te.effectsApplied[4] ? 1 : 0));

        if (type == TileEntityWeaponRack.weaponRackTypeWall)
            modelWall.render(null, 0, 0, -0.1f, 0, 0, 0.0625f);
        else
            modelFloor.render(null, 0, 0, -0.1f, 0, 0, 0.0625f);

        for (int i = 0; i < te.effectsApplied.length; i++)
        {
            ModelBase detailModel = (type == TileEntityWeaponRack.weaponRackTypeWall ? detailModelsWall : detailModelsFloor)[i];
            ResourceLocation detailTexture = (type == TileEntityWeaponRack.weaponRackTypeWall ? detailTexturesWall : detailTexturesFloor)[i];
            if (te.effectsApplied[i] && detailModel != null && detailTexture != null)
            {
                bindTexture(detailTexture);
                detailModel.render(null, 0, 0, -0.1f, 0, 0, 0.0625f);
            }
        }
        GlStateManager.popMatrix();

        for (int i = 0; i < te.getStoredWeaponSlots(); i++)
            renderStoredWeapon(te, partialTicks, type, i);

        GlStateManager.popMatrix();
    }

    private ResourceLocation baseTexture(int type, int variant)
    {
        if (type == TileEntityWeaponRack.weaponRackTypeWall)
            return variant == 2 ? TEXTURE_WALL_DAMAGED : variant == 1 ? TEXTURE_WALL_CARVED : TEXTURE_WALL;

        return variant == 2 ? TEXTURE_FLOOR_DAMAGED : variant == 1 ? TEXTURE_FLOOR_CARVED : TEXTURE_FLOOR;
    }

    private void renderStoredWeapon(TileEntityWeaponRack te, float partialTicks, int type, int slot)
    {
        ItemStack stack = te.storedWeapons[slot];
        if (stack == null || stack.isEmpty())
            return;

        float swing = te.storedWeaponsSwinging[slot] < 1.0f
                ? MathHelper.sin(te.storedWeaponsSwinging[slot] * 5.0f) * (1.0f - te.storedWeaponsSwinging[slot])
                : 0.0f;

        GlStateManager.pushMatrix();
        if (type == TileEntityWeaponRack.weaponRackTypeFloor)
        {
            GlStateManager.translate(weaponSlotPositionsFloor[slot], -0.4f, 0.3f);
            GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-120.0f + swing * 10.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate(0.1f, 0.0f, 0.0f);
        }
        else
        {
            GlStateManager.translate(0.0f, weaponSlotPositionsWall[slot], -0.3f);
            GlStateManager.rotate(135.0f + (slot == 1 ? 180.0f : 0.0f), 0.0f, 0.0f, 1.0f);
        }

        GlStateManager.scale(1.1f, 1.1f, 1.1f);
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
