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
import java.util.HashMap;
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
    private String webDriver;
    private boolean isChrome;
    private String LANG;
    private String URL;
    private Document doc;

    public DriverConfiguration(String webDriver) {
        super(true, "configProp.properties");
        this.webDriver = webDriver;
        isWebDriverChrome();
        initSystemProperties(webDriver);

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

    public void initSystemProperties(String webDriver) {
        setWebDriver(webDriver);
        isIsChrome();

        if (isChrome) {
            System.setProperty(chrome, webDriver);
        } else {
            System.setProperty(mozilla, webDriver);
        }
//linkLoginBonus
        System.setProperty("webdriver.gecko.driver", gecko);
    }

    private void setDriverConfig() {
//        try {
        setIsChrome(Boolean.parseBoolean(properties.getProperty("chrome")));
        setWebDriver(properties.getProperty("webdriver"));
        setLANG(properties.getProperty("lang"));
        setURL(properties.getProperty("url"));
//        } catch (IOException ex) {
//            Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
//        }
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

    private Document getHTMLdoc() throws MalformedURLException, IOException {
        URL address = new URL("https://" + LANG + URL);
        HttpsURLConnection con = (HttpsURLConnection) address.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder builder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }
        String html = builder.toString();
        return doc = Jsoup.parse(html);
    }

    public List<String> loadListOfServers(String id) {
        List<String> list = new ArrayList();
        try {
            getHTMLdoc();
            Element element = doc.getElementById(id);
            List<Element> lis = element.getAllElements();

            for (Element li : lis) {
                if (li.childNodeSize() == 1) {
                    list.add(li.text());
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

    public String[][] loadListOfLanguages(String id) {
        Element element = doc.getElementById(id);
        List<Element> lis = element.getElementsByTag("a");
        String[][] list = new String[lis.size()][2];

        for (int i = 0; i < lis.size(); i++) {
            list[i][1] = lis.get(i).text();
            list[i][0] = substLangFromURL(lis.get(i));
        }
        return list;
    }

    private String substLangFromURL(Element element) {
        StringBuilder builder = new StringBuilder(element.attr("href"));
        return builder.substring(2, builder.indexOf("."));
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
