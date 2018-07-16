/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.configuration;

import gladiatustool.manager.LoginManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author krkoska.tomas
 */
public class DriverConfiguration extends Configuration {

    private final String mozilla = "webdriver.firefox.driver";
    private final String chrome = "webdriver.chrome.driver";
    private final String gecko = "lib/geckodriver.exe";
    private String webDriver;
    private boolean isChrome;
    private String LANG;
    private String URL;

    public DriverConfiguration(String webDriver) {
        super(true, "configProp.properties");
        this.webDriver = webDriver;
        isWebDriverChrome();
        initSystemProperties(webDriver, isChrome);

    }

    public DriverConfiguration() {
        super(true, "configProp.properties");
        setDriverConfig();
        initSystemProperty();
    }

    private void isWebDriverChrome() {
        if (webDriver.contains("chrome")) {
            isChrome = true;
        } else {
            isChrome = false;
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
        setWebDriver(properties.getProperty("webdriver"));
        setLANG(properties.getProperty("lang"));
        setURL(properties.getProperty("url"));
    }

    public void setDriverConfig(String webDriver, String lang) {
        try {
            setStream(false, FULL_PATH);
            setWebDriver(webDriver);
            setLANG(lang);
            isWebDriverChrome();
            properties.setProperty("chrome", Boolean.toString(isChrome));
            properties.setProperty("webdriver", webDriver);
            properties.setProperty("lang", lang);

            properties.store(out, null);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            JOptionPane.showMessageDialog(null, "Internet connetction problem !", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return list;
    }

    public String getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(String webDriver) {
        this.webDriver = webDriver;
        isWebDriverChrome();
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
}
