/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.core.Core;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Tomáš
 */
public class ExpeditionManager extends Manager {

    private int indexOfExpedition;

    public ExpeditionManager(Long lag, int indexOfExpedition) {
        super(lag);
        this.indexOfExpedition = indexOfExpedition;
    }

    @Override
    public void execute() {
        try {
            WebElement expedition = Core.DRIVER.findElement(By.id("cooldown_bar_expedition"));
            click(expedition);
            WebElement attack = Core.DRIVER.findElement(By.id("expedition_list"));
            List<WebElement> listt = attack.findElements(By.className("expedition_box"));
            listt.get(indexOfExpedition).findElement(By.className("expedition_cooldown_reduce"));
            attack(listt.get(indexOfExpedition).findElement(By.className("expedition_button")));
        } catch (Throwable e) {
            System.out.println("Rubin expedition catch");
        }
    }

    @Override
    public Message getPlan() {
        Thread th = new Thread();
        try {
            th.sleep(1000);
            WebElement cooldown_bar = Core.DRIVER.findElement(By.id("cooldown_bar_text_expedition"));
            String time = cooldown_bar.getText();
            Long cooldown = calculateNextExecute(time);
            return new Message(cooldown, this);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExpeditionManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable e) {

        }
        return null;
    }
}
