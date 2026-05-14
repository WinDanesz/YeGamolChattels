package ivorius.yegamolchattels.client;

import ivorius.yegamolchattels.YGCProxy;
import ivorius.yegamolchattels.blocks.*;
import ivorius.yegamolchattels.client.rendering.*;
import ivorius.yegamolchattels.entities.EntityFlag;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements YGCProxy
{
    @Override
    public void loadConfig(String categoryID)
    {
    }

    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityFlag.class, RenderFlag::new);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStatue.class,          new TileEntityRendererStatue());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrandfatherClock.class, new TileEntityRendererGrandfatherClock());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWeaponRack.class,      new TileEntityRendererWeaponRack());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrindstone.class,      new TileEntityRendererGrindstone());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGong.class,            new TileEntityRendererGong());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemShelf.class,       new TileEntityRendererItemShelf());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySnowGlobe.class,       new TileEntityRendererSnowGlobe());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySawBench.class,        new TileEntityRendererSawBench());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTablePress.class,      new TileEntityRendererTablePress());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLootChest.class,       new TileEntityRendererLootChest());

        TileEntityRendererPedestal pedestalRenderer = new TileEntityRendererPedestal();
        pedestalRenderer.registerModel(EnumPedestalEntry.stonePedestal,   new ModelPedestalStoneBlock());
        pedestalRenderer.registerModel(EnumPedestalEntry.ironPedestal,    new ModelPedestalIron());
        pedestalRenderer.registerModel(EnumPedestalEntry.goldPedestal,    new ModelPedestalGold());
        pedestalRenderer.registerModel(EnumPedestalEntry.diamondPedestal, new ModelPedestalDiamond());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, pedestalRenderer);
    }

    @Override
    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().player;
    }
}
