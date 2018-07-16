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
        WebElement expedition = Core.DRIVER.findElement(By.id("cooldown_bar_expedition"));
        click(expedition);
        WebElement attack = Core.DRIVER.findElement(By.id("expedition_list"));
        List<WebElement> listt = attack.findElements(By.className("expedition_box"));
        click(listt.get(indexOfExpedition).findElement(By.className("expedition_button")));
    }

    @Override
    public Message getPlan() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExpeditionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        WebElement cooldown_bar = Core.DRIVER.findElement(By.id("cooldown_bar_text_expedition"));
        String time = cooldown_bar.getText();
        Long cooldown = calculateNextExecute(time);
        System.out.println(new Date(cooldown).toString());
        return new Message(cooldown, this);
    }
}
