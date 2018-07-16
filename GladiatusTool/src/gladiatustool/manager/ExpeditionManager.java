/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

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

    public ExpeditionManager(WebDriver driver, Long lag, int indexOfExpedition) {
        super(driver, lag);
        this.indexOfExpedition = indexOfExpedition;
    }

    @Override
    public void execute() {
        WebElement expedition = driver.findElement(By.id("cooldown_bar_expedition"));
        expedition.click();
        WebElement attack = driver.findElement(By.id("expedition_list"));
        List<WebElement> listt = attack.findElements(By.className("expedition_box"));
        listt.get(indexOfExpedition).findElement(By.className("expedition_button")).click();       
    }

    @Override
    public Message getPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
