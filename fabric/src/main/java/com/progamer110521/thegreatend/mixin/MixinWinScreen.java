package com.progamer110521.thegreatend.mixin;

import com.progamer110521.thegreatend.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WinScreen.class)
public abstract class MixinWinScreen {

    @Unique
    private boolean hasStartedPlaying = false;

    @Unique
    private static String minecraftLanguage = "temp";

    @Unique
    private SoundInstance poemSoundInstance = null;

    @Unique
    private void getUserLanguage() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraftLanguage = minecraft.options.languageCode;
    }

    @ModifyArg(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/WinScreen;wrapCreditsIO(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/gui/screens/WinScreen$CreditsReader;)V"),
            index = 0)
    private ResourceLocation modifyEndTextPath(ResourceLocation originalPath) {
        getUserLanguage();

        if (originalPath.equals(ResourceLocation.withDefaultNamespace("texts/end.txt"))) {
            if (minecraftLanguage.equals("ru_ru")) {
                return ResourceLocation.fromNamespaceAndPath("thegreatend", "texts/poem_ru_ru.txt");
            }
        }
        return originalPath;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        getUserLanguage();

        if (!hasStartedPlaying) {
            playEndPoemSound();
            hasStartedPlaying = true;
        }
    }

    @Inject(method = "onClose", at = @At("HEAD"))
    private void onClose(CallbackInfo ci) {
        stopEndPoemSound();
    }

    @Unique
    private void playEndPoemSound() {
        Minecraft minecraft = Minecraft.getInstance();
        ResourceLocation voiceoverLocalization = ModSounds.getVoiceoverLocalization(minecraftLanguage);

        if (voiceoverLocalization != null) {

            SoundInstance poemSoundInstanceLoc = new SimpleSoundInstance(
                    voiceoverLocalization,
                    SoundSource.VOICE,
                    1.0F,
                    1.0F,
                    minecraft.level.getRandom(),
                    false,
                    0,
                    SoundInstance.Attenuation.NONE,
                    0.0, 0.0, 0.0,
                    true
            );

            poemSoundInstance = poemSoundInstanceLoc;
            minecraft.getSoundManager().play(poemSoundInstanceLoc);
        }
    }

    @Unique
    private void stopEndPoemSound() {
        Minecraft minecraft = Minecraft.getInstance();

        if (poemSoundInstance != null) {
            minecraft.getSoundManager().stop(poemSoundInstance);
        }
    }
}
