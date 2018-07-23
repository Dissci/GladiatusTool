/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

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
    private boolean arena;
    private boolean turma;
    private int lag;
    private int criticalHealthLevel;
    private int dungeonMode;
    private int expeditionFocus;
    private int serverIndex;

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
        try {
            setUser(properties.getProperty("user"));
            setPassword(properties.getProperty("password"));
            setServer(properties.getProperty("server"));
            setExpeditions(Boolean.parseBoolean(properties.getProperty("expeditions")));
            setDungeons(Boolean.parseBoolean(properties.getProperty("dungeons")));
            setArena(Boolean.parseBoolean(properties.getProperty("arena")));
            setTurma(Boolean.parseBoolean(properties.getProperty("turma")));
            setCriticalHealthLevel(Integer.parseInt(
                    properties.getProperty("criticalHealthLevel")));
            setLag(Integer.parseInt(properties.getProperty("lag")));
            setDungeonMode(Integer.parseInt(properties.getProperty("dungeonMode")));
            setExpeditionFocus(Integer.parseInt(properties.getProperty("expeditionFocus")));
        } catch (NumberFormatException e) {
            
        }
    }

    public void setUserConfig(String user, String password, String server,
            boolean expeditions, boolean dungeons, boolean arena, boolean turma,
            int criticalHealthLevel, int lag, int dungeonMode, int expeditionFocus,
            int serverIndex) throws IOException {

        setStream(false, FULL_PATH);
        setUser(user);
        setPassword(password);
        setServer(server);
        setExpeditions(expeditions);
        setDungeons(dungeons);
        setArena(arena);
        setTurma(turma);
        setCriticalHealthLevel(criticalHealthLevel);
        setLag(lag);
        setDungeonMode(dungeonMode);
        setExpeditionFocus(expeditionFocus);
        setServerIndex(serverIndex);

        properties.setProperty("user", user);
        properties.setProperty("password", password);
        properties.setProperty("server", server);
        properties.setProperty("expeditions", Boolean.toString(expeditions));
        properties.setProperty("dungeons", Boolean.toString(dungeons));
        properties.setProperty("arena", Boolean.toString(arena));
        properties.setProperty("turma", Boolean.toString(turma));
        properties.setProperty("criticalHealthLevel",
                Integer.toString(criticalHealthLevel));
        properties.setProperty("lag", Integer.toString(lag));
        properties.setProperty("dungeonMode", Integer.toString(dungeonMode));
        properties.setProperty("expeditionFocus", Integer.toString(expeditionFocus));

        properties.store(out, null);
        out.close();
    }

//    public void checkIfnull() {
//        if(user == null || user.isEmpty())
//    }
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

    public boolean isArena() {
        return arena;
    }

    public void setArena(boolean arena) {
        this.arena = arena;
    }

    public boolean isTurma() {
        return turma;
    }

    public void setTurma(boolean turma) {
        this.turma = turma;
    }

    public int getLag() {
        return lag;
    }

    public void setLag(int lag) {
        this.lag = lag;
    }

    public int getCriticalHealthLevel() {
        return criticalHealthLevel;
    }

    public void setCriticalHealthLevel(int criticalHealthLevel) {
        this.criticalHealthLevel = criticalHealthLevel;
    }

    public int getExpeditionFocus() {
        return expeditionFocus;
    }

    public void setExpeditionFocus(int expeditionFocus) {
        this.expeditionFocus = expeditionFocus;
    }

    public int getDungeonMode() {
        return dungeonMode;
    }

    public void setDungeonMode(int dungeonMode) {
        this.dungeonMode = dungeonMode;
    }

    public int getServerIndex() {
        return serverIndex;
    }

    public void setServerIndex(int serverIndex) {
        this.serverIndex = serverIndex;
    }
}
