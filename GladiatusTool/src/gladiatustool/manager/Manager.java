/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import org.openqa.selenium.WebDriver;

/**
 *
 * @author krkoska.tomas
 */
public abstract class Manager {

    protected Long lag;
    protected WebDriver driver;

    public Manager(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public abstract void execute();

    public abstract Message getPlan();

    public Long getLag() {
        return lag;
    }

    public void setLag(Long lag) {
        this.lag = lag;
    }
}
