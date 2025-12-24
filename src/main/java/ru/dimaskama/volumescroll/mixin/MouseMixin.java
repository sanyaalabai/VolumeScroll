package ru.dimaskama.volumescroll.mixin;

import net.minecraft.client.Mouse;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.dimaskama.volumescroll.VolumeScroll;

@SuppressWarnings("unused")
@Mixin(Mouse.class)
abstract class MouseMixin {

    @Inject(
            method = "onMouseScroll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z"
            ),
            cancellable = true
    )
    private void handleMouseScroll(long window, double h, double v, CallbackInfo ci) {
        Vector2i vector=new Vector2i((int)h, (int)v);
        if (VolumeScroll.handleScroll(vector)) {
            ci.cancel();
        }
    }

}
