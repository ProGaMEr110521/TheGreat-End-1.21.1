package com.progamer110521.thegreatend;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class thegreatend {

    public thegreatend(IEventBus eventBus) {
        ModSounds.SOUND_EVENTS.register(eventBus);

        CommonClass.init();
    }
}