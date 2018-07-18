/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.core.Core;
import java.util.ArrayList;
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

    private final String foodType = "64";

    public HealthManager(Long lag) {
        super(lag);
    }

    private void goToOverview() throws Throwable {
        Core.DRIVER.get(Core.OVERVIEW_URL);
    }

    public void findFood() {
        WebElement inventoryBox = Core.DRIVER.findElement(By.className("inventoryBox"));
        List<WebElement> tabs = inventoryBox.findElements(By.className("awesome-tabs"));
        for (WebElement tab : tabs) {
            tab.click();
            WebElement inv = Core.DRIVER.findElement(By.id("inv"));
            List<WebElement> items = inv.findElements(By.tagName("div"));
            for (WebElement item : items) {
                try {
                    if (foodType.equals(item.getAttribute("data-content-type"))) {
                        String n = item.getAttribute("data-tooltip");
                        System.out.println(n);
                    }

                    dragAndDrop(item, Core.DRIVER.findElement(By.className("ui-droppable")));
                } catch (Throwable e) {

                }
            }
        }
    }

    private void dragAndDrop(WebElement dragged, WebElement dropped) {
        Actions builder = new Actions(Core.DRIVER);
        Action dragAndDrop = builder.clickAndHold(dragged)
                .moveToElement(dropped)
                .release(dropped)
                .build();

        dragAndDrop.perform();
    }

    private void checkHealth() {
        //inventoryBox 
        //data-vitality -> zivot
        //char_leben -> HP
        // id = inv
        //class = awesome-tabs
    }

    @Override
    public void execute() {
        try {
            goToOverview();

        } catch (Throwable ex) {
            Logger.getLogger(HealthManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Message getPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
