/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Tomáš
 */
public class LoginManager {

    private final Properties properties = new Properties();
    private final String propertiesName = "properties.properties";
    private String user;
    private String password;
    private String server;
    private boolean expeditions;
    private boolean dungeons;
    private List<String> serverList;  
    private String LANG;
    private String URL;
    

    public LoginManager() {
        init();
    }

    private void init() {
        try {
            InputStream in = getClass().getResourceAsStream(propertiesName);
            properties.load(in);
            readData();
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadServersFromURL();
    }

    private void readData() {
        user = properties.getProperty("user");
        password = properties.getProperty("password");
        server = properties.getProperty("server");
        expeditions = Boolean.parseBoolean(properties.getProperty("expeditions"));
        dungeons = Boolean.parseBoolean(properties.getProperty("dungeons"));
        LANG = properties.getProperty("lang");
        URL = properties.getProperty("url");
    }

    private void saveData(String user, String password, String server, boolean expeditions, boolean dungeons) {
        setDungeons(dungeons);
        setExpeditions(expeditions);
        setPassword(password);
        setServer(server);
        setUser(user);
    }
    
    private void loadServersFromURL() {
        try {
            URL address = new URL("https://"+LANG+URL);
            HttpsURLConnection con = (HttpsURLConnection) address.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            String html = builder.toString();
            Document doc = Jsoup.parse(html);
            Element element = doc.getElementById("login_server");
            List<Element> lis = element.getAllElements();
            serverList = new ArrayList();
            for (int i = 0; i < lis.size(); i++) {
                if (lis.get(i).childNodeSize() == 1) {
                    serverList.add(lis.get(i).text());
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public List<String> getServerList() {
        return serverList;
    }

    public void setServerList() {
        loadServersFromURL();
    }

    public String getLANG() {
        return LANG;
    }

    public void setLANG(String LANG) {
        this.LANG = LANG;
    } 
}
