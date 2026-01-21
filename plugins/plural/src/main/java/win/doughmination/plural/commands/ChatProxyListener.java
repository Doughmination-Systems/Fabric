package win.doughmination.plural.commands;

import win.doughmination.plural.DoughPluralMain;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import java.util.UUID;

public class ChatProxyListener {
    public static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            if (sender instanceof ServerPlayerEntity player) {
                UUID playerUUID = player.getUuid();

                if (!DoughPluralMain.systemDataMap.containsKey(playerUUID)) {
                    return true; // Allow message if no system is present
                }

                DoughPluralMain.SystemData systemData = DoughPluralMain.systemDataMap.get(playerUUID);
                if (systemData.activeFront == null || systemData.activeFront.isEmpty()) {
                    return true; // Allow message if player isn't fronting
                }

                String formattedMessage = "<" + systemData.activeFront + " (" + systemData.systemName + ")> " + message.getContent().getString();
                Text newMessage = Text.literal(formattedMessage);

                sender.getServer().getPlayerManager().broadcast(newMessage, false);
                return false; // Prevent the original message from being sent
            }
            return true;
        });
    }
}
