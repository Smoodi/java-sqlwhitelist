package de.smoodi.chipbot.listeners;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import de.smoodi.chipbot.Main;
import de.smoodi.chipbot.sql.UserProfile;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;

public class WhitelistingManager implements Listener {

    /**
     * Fired on player connect attempt.
     * @param ev
     */
    @EventHandler
    public void onWhitelistCheck(ProfileWhitelistVerifyEvent ev) {

        Main.main.log("Checking SQL database for player... (UUID: " + ev.getPlayerProfile().getId().toString() + ")");
        ev.setWhitelisted(false);
        ev.setKickMessage(ChatColor.BOLD + "" + ChatColor.RED +"It seems like the white-list server is taking a while to respond." +
                ChatColor.RESET+"\nPlease try again later.");

        try {
            UserProfile up = Main.sql.getUserProfile(ev.getPlayerProfile().getId());
            if(up != null) {
                if (!up.isBanned() && !up.isWhitelisted()) ev.setKickMessage(ChatColor.BOLD + "" + ChatColor.RED +"It seems like this account has been used before but is currently disabled."+
                        ChatColor.RESET+"\nThis happens if the associated Minecraft account was changed or you are no longer an active patreon or member of the server.");
                if (up.isBanned()) ev.setKickMessage(ChatColor.BOLD + "" + ChatColor.RED +"Your account has been permanently terminated."+ChatColor.RESET+"\n You are banned from joining the Patreon-only Minecraft server.");
                if (!up.isBanned() && up.isWhitelisted()) ev.setWhitelisted(true);
            }
            else {
                ev.setWhitelisted(false);
                ev.setKickMessage(ChatColor.BOLD + "" + ChatColor.RED + "It seems like you are not a registered patreon user or your Minecraft account is not fully linked." + ChatColor.RESET + "\nPlease make sure to join the Chipflake" +
                        " Patreon and link your minecraft account with the according bot.");
            }
            up = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();

            ev.setKickMessage(ChatColor.BOLD + "" + ChatColor.RED + "It seems like there has been an internal server error when communicating with the white-list server."+ChatColor.RESET+"\nPlease contact a server admin about this issue in order for them to resolve it.");
        }

    }

}
