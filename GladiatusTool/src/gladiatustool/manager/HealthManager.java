/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.core.Core;
import gladiatustool.core.LowHealthException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author krkoska.tomas
 */
public class HealthManager extends Manager {

    private final int criticalHealthLevel;

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

    private int getCurrentHealth() {

        String healthText = Core.DRIVER.findElement(By.id("header_values_hp_percent")).getText();
        return Integer.parseInt(healthText.replace("%", ""));
    }

    public void checkHealth() throws LowHealthException {
        int health = getCurrentHealth();
        if (health <= criticalHealthLevel) {
            goOnOverview();
            WebElement food = getFood();

            if (health <= criticalHealthLevel && food == null) {
                throw new LowHealthException();
            } else if (health <= criticalHealthLevel) {
                dragAndDrop(food, Core.DRIVER.findElement(By.className("ui-droppable")));
            }
        }
    }

    @Override
    public void execute() {
    }

    @Override
    public Message getPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
