package ru.dimaskama.volumescroll.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.dimaskama.volumescroll.VolumeScroll;

@SuppressWarnings("unused")
@Mixin(PlayerEntityRenderer.class)
abstract class PlayerEntityRendererMixin {

    AbstractClientPlayerEntity client;

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"),order=1001)
    private void modifyNamePlateText(AbstractClientPlayerEntity client, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, float f, CallbackInfo ci) {
        this.client=client;
    }

    @ModifyVariable(
            method="renderLabelIfPresent",
            at=@At("HEAD"),
            index=2,
            argsOnly=true,
            order=1002
    )
    private Text modifyNamePlateText(Text text) {
        return VolumeScroll.modifyNamePlateText(client.getId(),text);
    }

}
