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
    
    public String getExpeditionEnemyName(){
        return enemyName;
    }

    @Override
    public void execute() {
        try {
            WebElement expedition = Core.DRIVER.findElement(By.id("cooldown_bar_expedition"));
            click(expedition);
            WebElement attack = Core.DRIVER.findElement(By.id("expedition_list"));
            List<WebElement> listt = attack.findElements(By.className("expedition_box"));
            if (listt.get(indexOfExpedition).findElements(By.className("expedition_cooldown_reduce")).size() == 0) {
                setExpeditionEnemyName(listt.get(indexOfExpedition));
                attack(listt.get(indexOfExpedition).findElement(By.className("expedition_button")));
            }
        } catch (Throwable e) {

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
