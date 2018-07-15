/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomáš
 */
public class Login {

    private final Properties properties = new Properties();
    private final String propertiesName = "properties.properties";
    private String user;
    private String password;
    private String server;
    private boolean expeditions;
    private boolean dungeons;
    private String serverList;  

    public Login() {
        init();
    }

    private void init() {
        try {
            InputStream in = getClass().getResourceAsStream(propertiesName);
            properties.load(in);
            readData();
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void readData() {
        user = properties.getProperty("user");
        password = properties.getProperty("password");
        server = properties.getProperty("server");
        expeditions = Boolean.parseBoolean(properties.getProperty("expeditions"));
        dungeons = Boolean.parseBoolean(properties.getProperty("dungeons"));
    }

    private void saveData(String user, String password, String server, boolean expeditions, boolean dungeons) {
        setDungeons(dungeons);
        setExpeditions(expeditions);
        setPassword(password);
        setServer(server);
        setUser(user);
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
        properties.setProperty("user", user);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        properties.setProperty("password", password);
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
        properties.setProperty("server", server);
    }

    public boolean isExpeditions() {
        return expeditions;
    }

    public void setExpeditions(boolean expeditions) {
        this.expeditions = expeditions;
        properties.setProperty("expeditions", Boolean.toString(expeditions));
    }

    public boolean isDungeons() {
        return dungeons;
    }

    public void setDungeons(boolean dungeons) {
        this.dungeons = dungeons;
        properties.setProperty("dungeons", Boolean.toString(dungeons));
    }

}
