/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import configuration.UserConfiguration;
import core.Core;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author Tomáš
 */
public class LoginManager extends Manager {

    private UserConfiguration userConfiguration;
    private int indexServer;
    private final long restartTime = 3600000;

    public LoginManager(UserConfiguration userConfiguration, int indexServer) {
        super(1000L);
        this.userConfiguration = userConfiguration;
        this.indexServer = indexServer;
    }

    private void fillTheField(String element, String text) {
        WebElement webElement = Core.DRIVER.findElement(By.id(element));
        webElement.sendKeys(text);
    }

    @Override
    public void inExecute() {
        Core.DRIVER.manage().deleteAllCookies();
        Core.DRIVER.close();
    }

    @Override
    public Message getPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void beforeExecute() {
    }

    @Override
    protected void afterExecute() {
    }

    public void logIn() {
        try {
            WebElement element = Core.DRIVER.findElement(By.className("openX_interstitial"));
            element.findElement(By.tagName("a")).click();
        } catch (Throwable e) {

        }

        fillTheField("login_username", userConfiguration.getUser());
        fillTheField("login_password", userConfiguration.getPassword());

        Select selectBox = new Select(Core.DRIVER.findElement(By.id("login_server")));
        selectBox.selectByIndex(indexServer);
        click(Core.DRIVER.findElement(By.id("loginsubmit")));
        Core.OVERVIEW_URL = Core.DRIVER.getCurrentUrl();
    }
}
