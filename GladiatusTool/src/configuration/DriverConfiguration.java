/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import manager.LoginManager;
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
    private final String gecko = "driver/geckodriver.exe";
    private final String chromeDriver = "driver/chromedriver.exe";
    private String webDriver;
    private boolean isChrome;
    private String LANG = "en";
    private String URL = ".gladiatus.gameforge.com/game/";

    public DriverConfiguration(String webDriver) {
        super(true, "config");
        this.webDriver = webDriver;
        isWebDriverChrome();
        initSystemProperties(webDriver);

    }

    private void isWebDriverChrome() {
        if (webDriver != null) {
            if (webDriver.contains("chrome")) {
                isChrome = true;
            } else {
                isChrome = false;
            }
        }
    }

    public void initSystemProperties(String webDriver) {
        setWebDriver(webDriver);
        isIsChrome();

        initSystemProperty();
    }


    public void setDriverConfig(String webDriver, String lang) {
//     
        setWebDriver(webDriver);
        setLANG(lang);
        isWebDriverChrome();
//       
    }

    private void initSystemProperty() {
        if (isChrome) {
            System.setProperty(chrome, webDriver);
        } else {
            System.setProperty(mozilla, webDriver);
        }
        System.setProperty("webdriver.gecko.driver", gecko);

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
