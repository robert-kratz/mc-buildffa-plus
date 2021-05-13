package us.rjks.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 17:18
 *
 **************************************************************************/

public class TitleManager {

    public static void sendActionBar(Player player, String message) {
        try {
            CraftPlayer p = (CraftPlayer)player;
            IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
            p.getHandle().playerConnection.sendPacket(ppoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTitle(Player player, String text, String sub) {
        player.sendTitle(text, sub);
    }

}
