/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krkoska.tomas
 */
public class UserConfiguration {

    private final Properties properties = new Properties();
    private final String userPropertiesName = "userProp.properties";
    private String user;
    private String password;
    private String server;
    private boolean expeditions;
    private boolean dungeons;
    private InputStream in;
    private FileOutputStream out;

    public UserConfiguration() {
        init();
    }

    private void init() {
        try {
            setStream(true, userPropertiesName);
            setUserConfig();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setUserConfig() {
        setUser(properties.getProperty("user"));
        setPassword(properties.getProperty("password"));
        setServer(properties.getProperty("server"));
        setExpeditions(Boolean.parseBoolean(properties.getProperty("expeditions")));
        setDungeons(Boolean.parseBoolean(properties.getProperty("dungeons")));
    }

    public void setUserConfig(String user, String password, String server, boolean expeditions, boolean dungeons) throws IOException {
        setStream(false, userPropertiesName);
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

    private void setStream(boolean input, String propFile) throws FileNotFoundException {
        if (input) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(UserConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            in = new FileInputStream(propFile);
        } else {
            try {
                if (in != null) {
                    in.close();
                }
                out = new FileOutputStream(propFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
