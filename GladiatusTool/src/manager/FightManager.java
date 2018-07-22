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
 * @author krkoska.tomas
 */
public class FightManager extends Manager {

    private final String cooldownBarText;
    private final int numberTab;
    private final String cooldownBar;
    private final String own;

    public FightManager(Long lag, String cooldownBarText, String cooldownBar, int numberTab, String own) {
        super(lag);
        this.cooldownBarText = cooldownBarText;
        this.numberTab = numberTab;
        this.cooldownBar = cooldownBar;
        this.own = own;
    }

    @Override
    public void execute() {
        click(Core.DRIVER.findElement(By.id(cooldownBar)));
        WebElement mainnav = Core.DRIVER.findElement(By.id("mainnav"));
        List<WebElement> tabs = mainnav.findElements(By.className("awesome-tabs"));
        click(tabs.get(numberTab));
        WebElement table = Core.DRIVER.findElement(By.id(own));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        click(chooseEnemy(rows));
    }

    private WebElement chooseEnemy(List<WebElement> rows) {
        int i = 1;
        int level = Integer.MAX_VALUE;
        WebElement attackButton = null;
        int higherServer = 0;
        while (i < rows.size()) {
            List<WebElement> columns = rows.get(i).findElements(By.tagName("td"));
            int currentServer = Integer.parseInt(columns.get(2).getText());
            if (higherServer == currentServer) {
                int currentLevel = Integer.parseInt(columns.get(1).getText());
                if (level > currentLevel) {
                    level = currentLevel;
                    attackButton = columns.get(columns.size() - 1);
                }
            } else if (higherServer < currentServer) {
                higherServer = currentServer;
                level = Integer.parseInt(columns.get(1).getText());
                attackButton = columns.get(columns.size() - 1);
            }
            i++;
        }
        return attackButton.findElement(By.className("attack"));
    }

    @Override
    public Message getPlan() {
        Thread th = new Thread();
        try {
            th.sleep(1000);
            WebElement cooldown_bar = Core.DRIVER.findElement(By.id(cooldownBarText));
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
