/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.core.Core;
import java.util.List;
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
        expedition.click();
        WebElement attack = Core.DRIVER.findElement(By.id("expedition_list"));
        List<WebElement> listt = attack.findElements(By.className("expedition_box"));
        listt.get(indexOfExpedition).findElement(By.className("expedition_button")).click();
    }

    @Override
    public Message getPlan() {
        WebElement cooldown_bar = Core.DRIVER.findElement(By.id("cooldown_bar_text_expedition"));
        String time = cooldown_bar.getText();
        Long cooldown = calculateNextExecute(time);
        return new Message(cooldown, this);
    }
}
