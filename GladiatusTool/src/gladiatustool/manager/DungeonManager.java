/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Tomáš
 */
public class DungeonManager extends Manager {

    public DungeonManager(WebDriver driver, Long lag) {
        super(driver, lag);
    }

    @Override
    public void execute() {
        WebElement dungeon = driver.findElement(By.id("cooldown_bar_dungeon"));
        dungeon.click();
        driver.findElement(By.className("map_label")).click();
        /**
         * Tu este pridat situaciu ked skonci zalar a treba otvorit novy !!!
         */

    }

    @Override
    public Message getPlan() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
