package com.progamer110521.thegreatend;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "thegreatend");

    public static final ResourceLocation endpoem_ru = ResourceLocation.fromNamespaceAndPath("thegreatend", "vpoem_ru_ru");

    public static final Holder<SoundEvent> ENDPOEM_RU = SOUND_EVENTS.register("vpoem_ru_ru", SoundEvent::createVariableRangeEvent);

    public static ResourceLocation getVoiceoverLocalization(String languageCode) {

        if (languageCode.equals("ru_ru")) {
            return endpoem_ru;
        }
        return null;
    }
}
