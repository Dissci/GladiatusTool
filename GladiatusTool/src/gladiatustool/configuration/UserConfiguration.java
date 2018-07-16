/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.configuration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krkoska.tomas
 */
public class UserConfiguration extends Configuration {

    private String user;
    private String password;
    private String server;
    private boolean expeditions;
    private boolean dungeons;

    public UserConfiguration() {
        super(true, "userProp.properties");
        init();
    }

    private void init() {
        try {
            readUserConfig();
        } catch (IOException ex) {
            Logger.getLogger(UserConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readUserConfig() throws IOException {
        setUser(properties.getProperty("user"));
        setPassword(properties.getProperty("password"));
        setServer(properties.getProperty("server"));
        setExpeditions(Boolean.parseBoolean(properties.getProperty("expeditions")));
        setDungeons(Boolean.parseBoolean(properties.getProperty("dungeons")));
    }

    public void setUserConfig(String user, String password, String server, boolean expeditions, boolean dungeons) throws IOException {
        setStream(false, FULL_PATH);
        setUser(user);
        setPassword(password);
        setServer(server);
        setExpeditions(expeditions);
        setDungeons(dungeons);

        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("server", server);
        properties.setProperty("expeditions", Boolean.toString(expeditions));
        properties.setProperty("dungeons", Boolean.toString(dungeons));

        properties.store(out, null);
        out.close();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isExpeditions() {
        return expeditions;
    }

    public void setExpeditions(boolean expeditions) {
        this.expeditions = expeditions;
    }

    public boolean isDungeons() {
        return dungeons;
    }

    public void setDungeons(boolean dungeons) {
        this.dungeons = dungeons;
    }
}
