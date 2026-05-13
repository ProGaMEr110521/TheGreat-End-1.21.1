package com.progamer110521.thegreatend.mixin;

import com.progamer110521.thegreatend.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
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
    private boolean theGreat_End_1_20_1$hasStartedPlaying = false;

    @Unique
    private static String theGreat_End_1_20_1$minecraftLanguage = "temp";

    @Unique
    private void theGreat_End_1_20_1$getUserLanguage() {
        Minecraft minecraft = Minecraft.getInstance();
        theGreat_End_1_20_1$minecraftLanguage = minecraft.options.languageCode;
    }

    @ModifyArg(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/WinScreen;wrapCreditsIO(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/gui/screens/WinScreen$CreditsReader;)V"),
            index = 0)
    private ResourceLocation modifyEndTextPath(ResourceLocation originalPath) {
        theGreat_End_1_20_1$getUserLanguage();

        if (originalPath.equals(ResourceLocation.withDefaultNamespace("texts/end.txt"))) {
            if (theGreat_End_1_20_1$minecraftLanguage.equals("ru_ru")) {
                return ResourceLocation.fromNamespaceAndPath("thegreatend", "texts/poem_ru_ru.txt");
            }
        }
        return originalPath;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        theGreat_End_1_20_1$getUserLanguage();

        if (!theGreat_End_1_20_1$hasStartedPlaying) {
            theGreat_End_1_20_1$playEndPoemSound();
            theGreat_End_1_20_1$hasStartedPlaying = true;
        }
    }

    @Inject(method = "onClose", at = @At("HEAD"))
    private void onClose(CallbackInfo ci) {
        theGreat_End_1_20_1$stopEndPoemSound();
    }

    @Unique
    private void theGreat_End_1_20_1$playEndPoemSound() {
        Minecraft minecraft = Minecraft.getInstance();
        ResourceLocation voiceoverLocalization = ModSounds.getVoiceoverLocalization(theGreat_End_1_20_1$minecraftLanguage);

        if (voiceoverLocalization != null) {

            minecraft.getSoundManager().play(new SimpleSoundInstance(
                            voiceoverLocalization,
                            SoundSource.VOICE,
                            1.0F,
                            1.0F,
                            minecraft.level.getRandom(),
                            false,
                            0,
                            SimpleSoundInstance.Attenuation.NONE,
                            0.0, 0.0, 0.0,
                            true
                    )
            );
        }
    }

    @Unique
    private void theGreat_End_1_20_1$stopEndPoemSound() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundManager().stop(ModSounds.ENDPOEM_RU.getId(), SoundSource.MUSIC);
    }
}
