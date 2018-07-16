/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import java.util.Random;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author krkoska.tomas
 */
public abstract class Manager {

    protected Long lag;
    protected WebDriver driver;
    protected Random rndLag;    

    public Manager(WebDriver driver, Long lag) {
        this.driver = driver;
        this.lag = lag;
        this.rndLag = new Random(lag);
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

    protected long getRandomLag() {
        return rndLag.nextLong();
    }
}
