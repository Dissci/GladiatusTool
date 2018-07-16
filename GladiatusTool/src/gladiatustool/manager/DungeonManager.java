/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.core.Core;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Tomáš
 */
public class DungeonManager extends Manager {

    private int dungeonMode;

    public DungeonManager(Long lag, int dungeonMode) {
        super(lag);
        this.dungeonMode = dungeonMode;
    }

    @Override
    public void execute() {
        WebElement dungeon = Core.DRIVER.findElement(By.id("cooldown_bar_dungeon"));
        dungeon.click();
        Core.DRIVER.findElement(By.className("map_label")).click();
        /**
         * Tu este pridat situaciu ked skonci zalar a treba otvorit novy !!!
         */

    }

    @Override
    public Message getPlan() {
        WebElement cooldown_bar = Core.DRIVER.findElement(By.id("cooldown_bar_text_dungeon"));
        String time = cooldown_bar.getText();
        Long cooldown = calculateNextExecute(time);
        return new Message(cooldown, this);
    }
}
