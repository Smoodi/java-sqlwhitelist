package de.smoodi.chipbot;

import de.smoodi.chipbot.listeners.WhitelistingManager;
import de.smoodi.chipbot.sql.SQLBridge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    /**
     * Basic settings
     */
    private final String PREFIX = ChatColor.BOLD + "" + ChatColor.RED + "[SQLWhitelist]: " + ChatColor.RESET;
    public static Main main;

    public static SQLBridge sql;

    /**
     * On Plugin init.
     */
    @Override
    public void onEnable() {
        log("Starting SQLWhitelist...");
        main = this;

        File config = new File(getDataFolder(), "config.yml");
        if(!config.exists()) {
            log("Creating default plugin config. Please set up.");
            this.saveDefaultConfig();
            this.getServer().shutdown();
        }
        else {
            FileConfiguration c = this.getConfig();

            String host = c.getString("sql-host");
            String dbname = c.getString("sql-db");
            String port = String.valueOf(c.getInt("sql-port"));
            String user = c.getString("sql-user");
            String pw = c.getString("sql-pw");

            log("Connecting to " + host + ", " + dbname + ":" + port + " as " + user + "...");

            sql = new SQLBridge(host,port,dbname,user,pw);
            this.getServer().getPluginManager().registerEvents(new WhitelistingManager(), this);
        }
    }

    @Override
    public void onDisable() {
        log("Disabling SQLWhitelist...");
    }

    public void log(String msg){
        Bukkit.getConsoleSender().sendMessage(PREFIX + msg);
    }
}
