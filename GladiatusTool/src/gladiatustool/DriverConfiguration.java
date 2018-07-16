/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool;

import gladiatustool.manager.LoginManager;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
 * @author krkoska.tomas
 */
public class DriverConfiguration {

    private final Properties properties = new Properties();
    private final String configPropertiesName = "configProp.properties";
    private final String mozilla = "webdriver.firefox.driver";
    private final String chrome = "webdriver.chrome.driver";
    private final String gecko = "lib/geckodriver.exe";
    private InputStream in;
    private FileOutputStream out;
    private String webDriver;
    private boolean isChrome;
    private String LANG;
    private String URL;

    public DriverConfiguration(String webDriver, boolean isChrome) {
        this.isChrome = isChrome;
        this.webDriver = webDriver;
        initSystemProperty();
        init();
    }

    public DriverConfiguration() {
        init();
        setDriverConfig();
    }

    public void init() {
        try {
            setStream(true, configPropertiesName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initSystemProperties(String webDriver, boolean isChrome) {
        setWebDriver(webDriver);
        setIsChrome(isChrome);

        if (isChrome) {
            System.setProperty(chrome, webDriver);
        } else {
            System.setProperty(mozilla, webDriver);
        }

        System.setProperty("webdriver.gecko.driver", gecko);
    }

    private void setDriverConfig() {
        setIsChrome(Boolean.parseBoolean(properties.getProperty("chrome")));
        setWebDriver(properties.getProperty("webDriver"));
        setLANG(properties.getProperty("lang"));
        setURL(properties.getProperty("url"));
    }

    private void initSystemProperty() {
        if (isChrome) {
            System.setProperty(chrome, webDriver);
        } else {
            System.setProperty(mozilla, webDriver);
        }

        System.setProperty("webdriver.gecko.driver", gecko);
    }

    public List<String> loadServersFromURL() {
        List<String> list = new ArrayList();
        try {
            URL address = new URL("https://" + LANG + URL);
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

            for (int i = 0; i < lis.size(); i++) {
                if (lis.get(i).childNodeSize() == 1) {
                    list.add(lis.get(i).text());
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public String getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(String webDriver) {
        this.webDriver = webDriver;
    }

    public boolean isIsChrome() {
        return isChrome;
    }

    public void setIsChrome(boolean isChrome) {
        this.isChrome = isChrome;
    }

    public String getLANG() {
        return LANG;
    }

    public void setLANG(String LANG) {
        this.LANG = LANG;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    
     private void setStream(boolean input, String propFile) throws FileNotFoundException {
        if (input) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
