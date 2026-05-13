package com.progamer110521.thegreatend.mixin;

import com.progamer110521.thegreatend.Constants;
import com.progamer110521.thegreatend.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WinScreen.class)
public abstract class MixinWinScreen {

    @Shadow
    private float scroll;

    @Shadow
    private float scrollSpeed;

    @Unique
    private boolean theGreat_End_1_21_1$hasStartedPlaying = false;

    @Unique
    private final Minecraft theGreat_End_1_21_1$minecraft = Minecraft.getInstance();

    @Unique
    private int theGreat_End_1_21_1$maxClientFramerate = 0;

    @Unique
    private boolean theGreat_End_1_21_1$InitiallyTurnedOnVsync;

    @Unique
    private static String theGreat_End_1_21_1$minecraftLanguage = "temp";

    @Unique
    private static final float theGreat_End_1_21_1$FIXED_SCROLL_DELTA = 0.5f;

    @Unique
    private void theGreat_End_1_21_1$getUserLanguage() {
        theGreat_End_1_21_1$minecraftLanguage = theGreat_End_1_21_1$minecraft.options.languageCode;
    }

    @ModifyArg(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/WinScreen;wrapCreditsIO(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/gui/screens/WinScreen$CreditsReader;)V"),
            index = 0)
    private ResourceLocation modifyEndTextPath(ResourceLocation originalPath) {
        theGreat_End_1_21_1$getUserLanguage();

        if (originalPath.equals(ResourceLocation.withDefaultNamespace("texts/end.txt"))) {
            if (theGreat_End_1_21_1$minecraftLanguage.equals("ru_ru")) {
                return ResourceLocation.fromNamespaceAndPath("thegreatend", "texts/poem_ru_ru.txt");
            }
        }
        return originalPath;
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(FF)F"
            )
    )
    private float redirectScrollMax(float a, float b) {
        float fixedScroll = this.scroll + theGreat_End_1_21_1$FIXED_SCROLL_DELTA * this.scrollSpeed;
        return Math.max(a, fixedScroll);
    }

//    @Inject(method = "render", at = @At("TAIL"))
//    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
//        Constants.LOG.info("Current FPS: {}, VSync: {}", theGreat_End_1_21_1$minecraft.getFps(), theGreat_End_1_21_1$InitiallyTurnedOnVsync);
//    }

    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        theGreat_End_1_21_1$maxClientFramerate = theGreat_End_1_21_1$minecraft.options.framerateLimit().get();
        theGreat_End_1_21_1$InitiallyTurnedOnVsync = theGreat_End_1_21_1$minecraft.options.enableVsync().get();

        Constants.LOG.info("theGreat_End_1_21_1$maxClientFramerate = {}; theGreat_End_1_21_1$InitiallyTurnedOnVsync = {}", theGreat_End_1_21_1$maxClientFramerate, theGreat_End_1_21_1$InitiallyTurnedOnVsync);

        if (theGreat_End_1_21_1$InitiallyTurnedOnVsync) {
            theGreat_End_1_21_1$minecraft.options.enableVsync().set(false);
        }

        theGreat_End_1_21_1$minecraft.options.framerateLimit().set(45);

        theGreat_End_1_21_1$getUserLanguage();

        if (!theGreat_End_1_21_1$hasStartedPlaying) {
            theGreat_End_1_21_1$playEndPoemSound();
            theGreat_End_1_21_1$hasStartedPlaying = true;
        }
    }

    @Inject(method = "onClose", at = @At("HEAD"))
    private void onClose(CallbackInfo ci) {
        theGreat_End_1_21_1$stopEndPoemSound();

        if (theGreat_End_1_21_1$InitiallyTurnedOnVsync) {
            theGreat_End_1_21_1$minecraft.options.enableVsync().set(true);
        }

        theGreat_End_1_21_1$minecraft.options.framerateLimit().set(theGreat_End_1_21_1$maxClientFramerate);

        Constants.LOG.info("Set VSync to {} and max framerate to {}", theGreat_End_1_21_1$InitiallyTurnedOnVsync, theGreat_End_1_21_1$maxClientFramerate);

        if (theGreat_End_1_21_1$minecraft.options.framerateLimit().get() == 45 || theGreat_End_1_21_1$minecraft.options.framerateLimit().get() == 40) {
            if (theGreat_End_1_21_1$minecraft.player != null) theGreat_End_1_21_1$minecraft.player.sendSystemMessage(Component.translatable("thegreatend.message.framerate_change_error"));
        }
    }

    @Unique
    private void theGreat_End_1_21_1$playEndPoemSound() {
        ResourceLocation voiceoverLocalization = ModSounds.getVoiceoverLocalization(theGreat_End_1_21_1$minecraftLanguage);

        if (voiceoverLocalization != null) {
            theGreat_End_1_21_1$minecraft.getSoundManager().play(new SimpleSoundInstance(
                            voiceoverLocalization,
                            SoundSource.VOICE,
                            1.0F,
                            1.0F,
                            theGreat_End_1_21_1$minecraft.level.getRandom(),
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
    private void theGreat_End_1_21_1$stopEndPoemSound() {
        theGreat_End_1_21_1$minecraft.getSoundManager().stop(ModSounds.endpoem_ru, SoundSource.MUSIC);
    }
}