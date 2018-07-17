/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.configuration.UserConfiguration;
import gladiatustool.core.Core;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author Tomáš
 */
public class LoginManager extends Manager {

    private UserConfiguration userConfiguration;
    private int indexServer;

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
    public void execute() {
        fillTheField("login_username", userConfiguration.getUser());
        fillTheField("login_password", userConfiguration.getPassword());
              
        Select selectBox = new Select(Core.DRIVER.findElement(By.id("login_server")));
        selectBox.selectByIndex(indexServer);
        click(Core.DRIVER.findElement(By.id("loginsubmit")));
        Core.OVERVIEW_URL = Core.DRIVER.getCurrentUrl();
    }

    @Override
    public Message getPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
