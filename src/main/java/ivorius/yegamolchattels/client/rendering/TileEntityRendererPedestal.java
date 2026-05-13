package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.blocks.EnumPedestalEntry;
import ivorius.yegamolchattels.blocks.TileEntityPedestal;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityRendererPedestal extends TileEntitySpecialRenderer<TileEntityPedestal>
{
    public void registerModel(EnumPedestalEntry entry, ModelBase model)
    {
    }

    @Override
    public void render(TileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
    }
}
