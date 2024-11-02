package com.mrcrayfish.furniture.refurbished.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.furniture.refurbished.mail.DeliveryResult;
import com.mrcrayfish.furniture.refurbished.network.play.ClientPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * Author: MrCrayfish
 */
public record MessageShowDeliveryResult(DeliveryResult result)
{
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageShowDeliveryResult> STREAM_CODEC = StreamCodec.composite(
        DeliveryResult.STREAM_CODEC,
        MessageShowDeliveryResult::result,
        MessageShowDeliveryResult::new
    );

    public static void handle(MessageShowDeliveryResult message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleMessageShowDeliveryResult(message));
        context.setHandled(true);
    }
}
