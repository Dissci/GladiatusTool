/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.configuration.UserConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author Tomáš
 */
public class LoginManager extends Manager {

    private UserConfiguration userConfiguration;
    private int indexServer;

    public LoginManager(WebDriver driver, UserConfiguration userConfiguration, int indexServer) {
        super(driver, 1000L);
        this.userConfiguration = userConfiguration;
        this.indexServer = indexServer;
    }

    @Override
    public void execute() {
        WebElement user = driver.findElement(By.id("login_username"));
        WebElement password = driver.findElement(By.id("login_password"));
        user.sendKeys(userConfiguration.getUser());
        password.sendKeys(userConfiguration.getPassword());
        Select selectBox = new Select(driver.findElement(By.id("login_server")));
        selectBox.selectByIndex(indexServer);
        WebElement submit = driver.findElement(By.id("loginsubmit"));
        submit.click();
    }

    @Override
    public Message getPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
