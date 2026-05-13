package ivorius.yegamolchattels.network;

import io.netty.buffer.ByteBuf;

public interface PartialUpdateHandler
{
    void writeUpdateData(ByteBuf buffer, String context, Object... params);

    void readUpdateData(ByteBuf buffer, String context);
}
