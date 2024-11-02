package com.mrcrayfish.furniture.refurbished.mail;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public record DeliveryResult(boolean success, Optional<String> message)
{
    public static final StreamCodec<FriendlyByteBuf, DeliveryResult> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL,
        DeliveryResult::success,
        ByteBufCodecs.optional(ByteBufCodecs.stringUtf8(256)),
        DeliveryResult::message,
        DeliveryResult::new
    );

    public static DeliveryResult createSuccess(String translationKey)
    {
        return new DeliveryResult(true, Optional.of(translationKey));
    }

    public static DeliveryResult createFail(String translationKey)
    {
        return new DeliveryResult(false, Optional.of(translationKey));
    }
}