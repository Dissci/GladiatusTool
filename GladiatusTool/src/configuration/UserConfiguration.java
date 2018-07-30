/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.io.IOException;

/**
 *
 * @author krkoska.tomas
 */
public class UserConfiguration extends Configuration {

    private String user = "";
    private String password = "";
    private String server = "";
    private boolean expeditions = true;
    private boolean dungeons = true;
    private boolean arena = true;
    private boolean turma = true;
    private int lag = 10;
    private int criticalHealthLevel = 0;
    private int dungeonMode = 0;
    private int expeditionFocus = 0;
    private int serverIndex = 0;

    public UserConfiguration() {
        super(true, "user");
    }

    public void setUserConfig(String user, String password, String server,
            boolean expeditions, boolean dungeons, boolean arena, boolean turma,
            int criticalHealthLevel, int lag, int dungeonMode, int expeditionFocus,
            int serverIndex) throws IOException {

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
