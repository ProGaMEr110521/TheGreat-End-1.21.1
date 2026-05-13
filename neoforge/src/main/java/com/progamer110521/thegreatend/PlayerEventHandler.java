package com.progamer110521.thegreatend;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = "thegreatend")
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (Minecraft.getInstance().options.framerateLimit().get() == 45 || Minecraft.getInstance().options.framerateLimit().get() == 40) {
            Constants.LOG.info("Нехорошо получилось");
            event.getEntity().sendSystemMessage(Component.translatable("thegreatend.message.framerate_change_error"));
        }
    }
}
