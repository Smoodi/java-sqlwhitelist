package de.smoodi.chipbot.sql;

import de.smoodi.chipbot.Main;

import java.sql.*;
import java.util.UUID;

public class SQLBridge {

    private Connection con;
    private String host;
    private String port;
    private String dbname;
    private String username;
    private String pw;

    public SQLBridge(String host, String port, String dbname, String username, String pw) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.username = username;
        this.pw = pw;
        establishConnection();
    }

    /**
     * Returns a full user profile of the a discord link. Can be null if not existing.
     * @param uuid
     * @return
     * @throws SQLException
     */
    public UserProfile getUserProfile(UUID uuid) throws SQLException {
        fixClosedTimeout();

        PreparedStatement stm = con.prepareStatement("SELECT * FROM whitelisted_players WHERE UUID=\""+uuid.toString()+"\"");
        if(stm.execute()) {
            ResultSet r = stm.getResultSet();
            if(!r.next()) return null;
            return new UserProfile(uuid, r.getLong("DiscordID"), r.getBoolean("IsWhitelisted"), r.getString("Nickname"), r.getTimestamp("TimeAdded"), r.getBoolean("Banned"));
        };

        return null;
    }

    /**
     * Internal method for fixing closed connection or timeouts.
     * @throws SQLException
     */
    private void fixClosedTimeout() throws SQLException {
        if(!con.isClosed()) {
            if(con.isValid(2)) {}//nothing
            else {
                con.close();
                establishConnection();
            }
        } else { con.close(); establishConnection(); }
    }

    /**
     * Establishes a new connection with the DB.
     */
    private void establishConnection() {

        //We start a conneciton
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname, username, pw);
            con.setAutoCommit(false);

            Main.main.log("Successfully (re-)connected to the DB...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}