/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.core.Core;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;

/**
 *
 * @author krkoska.tomas
 */
public abstract class Manager {

    protected long lag;
    protected Random rndLag;

    public Manager(Long lag) {
        this.lag = lag;
        this.rndLag = new Random(lag);
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
        //int pomLag = ((int) lag * 1000);
        //long lagger = (long) rndLag.nextInt(pomLag);
        //return lagger;
        return 0L;
    }

    protected long calculateNextExecute(String cooldown) {
        Long time = System.currentTimeMillis();
        if (cooldown.contains(":")) {
            StringBuilder builder = new StringBuilder(cooldown);
            String seconds = builder.substring(builder.lastIndexOf(":") + 1);
            String minutes = builder.substring(builder.indexOf(":") + 1, builder.lastIndexOf(":"));

            time += (Long.parseLong(minutes, 10) * 60000);
            time += (Long.parseLong(seconds, 10) * 1000);
        }
        time += getRandomLag();
        return time;
    }

    protected void click(WebElement element) {
        Thread th = new Thread();
        try {
            th.sleep(1000);
            element.click();
        } catch (InterruptedException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void goOnOverview() {
        Thread th = new Thread();
        try {
            th.sleep(1000);
            Core.DRIVER.get(Core.OVERVIEW_URL);
        } catch (InterruptedException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void attack(WebElement element) {
        click(element);
        goOnOverview();
    }
}
