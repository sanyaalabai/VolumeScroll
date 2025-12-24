package ru.dimaskama.volumescroll;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.common.PlayerState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VolumeScroll {

    private static final Map<UUID, VolumeMessage> VOLUME_MESSAGES = new HashMap<>();

    public static boolean handleScroll(Vector2i vector) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.mouse.wasRightButtonClicked() && client.targetedEntity instanceof AbstractClientPlayerEntity target) {
            PlayerState state = ClientManager.getPlayerStateManager().getState(target.getUuid());
            if (state != null) {
                double prevVolume = VoicechatClient.PLAYER_VOLUME_CONFIG.getVolume(state.getUuid());
                double newVolume = MathHelper.clamp(prevVolume + (prevVolume >= 1.0 ? 0.1 : 0.05) * (vector.y == 0 ? -vector.x : vector.y), 0.0, 4.0);
                VoicechatClient.PLAYER_VOLUME_CONFIG.setVolume(state.getUuid(), newVolume);
                VoicechatClient.PLAYER_VOLUME_CONFIG.save();
                int percent = (int) Math.round(100.0 * (newVolume - 1.0));
                VOLUME_MESSAGES.put(state.getUuid(), new VolumeMessage(Text.literal((percent >= 0 ? "+" + percent : String.valueOf(percent)) + "%")));
                return true;
            }
        }
        return false;
    }

    public static Text modifyNamePlateText(int id, Text backup) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world != null) {
            Entity entity = world.getEntityById(id);
            if (entity != null) {
                VolumeMessage volumeMessage = VOLUME_MESSAGES.get(entity.getUuid());
                if (volumeMessage != null && (System.currentTimeMillis() - volumeMessage.timestamp) < 3000L) {
                    return volumeMessage.text;
                }
            }
        }
        return backup;
    }

    private record VolumeMessage(Text text, long timestamp) {

        public VolumeMessage(Text text) {
            this(text, System.currentTimeMillis());
        }

    }

}