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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author krkoska.tomas
 */
public class HealthManager extends Manager {

    private final int criticalHealthLevel;
    private boolean stoppedPlan = false;

    public HealthManager(Long lag, int criticalHealthLevel) {
        super(lag);
        this.criticalHealthLevel = criticalHealthLevel;
    }

    public WebElement getFood() {
        WebElement inventoryBox = Core.DRIVER.findElement(By.className("inventoryBox"));
        List<WebElement> tabs = inventoryBox.findElements(By.className("awesome-tabs"));
        WebElement food = null;
        for (WebElement tab : tabs) {
            click(tab);
            WebElement inv = Core.DRIVER.findElement(By.id("inv"));
            List<WebElement> items = inv.findElements(By.tagName("div"));

            for (WebElement item : items) {
                if (item.getAttribute("class").contains("item-i-7")) {
                    return item;
                }
            }
        }
        return food;
    }

    private void dragAndDrop(WebElement dragged, WebElement dropped) {
        Actions builder = new Actions(Core.DRIVER);
        Action dragAndDrop = builder.clickAndHold(dragged)
                .moveToElement(dropped)
                .release(dropped)
                .build();

        dragAndDrop.perform();
    }

    private int getCurrentHealth() throws NoSuchElementException, NullPointerException, SessionNotCreatedException {
        List<WebElement> list = Core.DRIVER.findElements(By.id("header_values_hp_percent"));
        if (list.isEmpty() || list == null) {
            goOnOverview();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HealthManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            list = Core.DRIVER.findElements(By.id("header_values_hp_percent"));
            if (list.isEmpty() || list == null) {
                throw new NoSuchElementException("Cant find element");
            }
        }

        String healthText = list.get(0).getText();
        return Integer.parseInt(healthText.substring(0, healthText.indexOf("%")));
    }

    public void checkHealth() throws NoSuchElementException {
        int health = criticalHealthLevel;
        try {
            health = getCurrentHealth();
        } catch (SessionNotCreatedException e) {
            goOnOverview();
            health = getCurrentHealth();
        }

        if (health <= criticalHealthLevel) {
            if (!stoppedPlan) {
                goOnOverview();
                click(Core.DRIVER.findElement(By.className("doll1")));
                WebElement food = getFood();

                if (food == null) {
                    stoppedPlan = true;
                } else {
                    dragAndDrop(food, Core.DRIVER.findElement(By.className("ui-droppable")));
                }
            }
        } else {
            stoppedPlan = false;
        }
    }

    public boolean isStoppedPlan() {
        return stoppedPlan;
    }

    public void setStoppedPlan(boolean stoppedPlan) {
        this.stoppedPlan = stoppedPlan;
    }

    @Override
    public Message getPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void beforeExecute() {
    }

    @Override
    protected void inExecute() {
    }

    @Override
    protected void afterExecute() {
    }
}
