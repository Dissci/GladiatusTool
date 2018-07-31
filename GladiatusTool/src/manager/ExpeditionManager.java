/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import core.Core;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Tomáš
 */
public class ExpeditionManager extends Manager {

    private final int indexOfExpedition;
    private String enemyName;

    public ExpeditionManager(Long lag, int indexOfExpedition) {
        super(lag);
        this.indexOfExpedition = indexOfExpedition;
    }

    private void setExpeditionEnemyName(WebElement element) {
        if (enemyName == null) {
            enemyName = element.findElement(By.className("expedition_name")).getText();
        }
    }

    public String getExpeditionEnemyName() {
        return enemyName;
    }

    @Override
    public void inExecute() {
        WebElement expedition = Core.DRIVER.findElement(By.id("cooldown_bar_expedition"));
        click(expedition);
        WebElement attack = Core.DRIVER.findElement(By.id("expedition_list"));
        List<WebElement> listt = attack.findElements(By.className("expedition_box"));
        if (listt.get(indexOfExpedition).findElements(By.className("expedition_cooldown_reduce")).size() == 0) {
            setExpeditionEnemyName(listt.get(indexOfExpedition));
            listt.get(indexOfExpedition).findElement(By.className("expedition_button")).click();
        }
    }

    @Override
    public Message getPlan() {
        sleepThreadTo1000();
        WebElement cooldown_bar = Core.DRIVER.findElement(By.id("cooldown_bar_text_expedition"));
        String time = cooldown_bar.getText();
        Long cooldown = calculateNextExecute(time);
        return new Message(cooldown, this);
    }

    @Override
    protected void beforeExecute() {
    }

    @Override
    protected void afterExecute() {
    }
}
