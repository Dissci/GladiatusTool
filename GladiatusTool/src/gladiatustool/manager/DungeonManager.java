/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import gladiatustool.core.Core;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Tomáš
 */
public class DungeonManager extends Manager {

    private final int dungeonMode;

    public DungeonManager(Long lag, int dungeonMode) {
        super(lag);
        this.dungeonMode = dungeonMode;
    }

    private void openDungeon() {
        String button = "dif" + dungeonMode;
        WebElement dungeonOption = Core.DRIVER.findElement(By.name(button));
        click(dungeonOption);
    }

    @Override
    public void execute() {
        WebElement dungeon = Core.DRIVER.findElement(By.id("cooldown_bar_dungeon"));
        click(dungeon);
        WebElement enemy;
        try {
            enemy = Core.DRIVER.findElement(By.className("map_label"));
        } catch (Throwable e) {
            openDungeon();
            enemy = Core.DRIVER.findElement(By.className("map_label"));
        }
        click(enemy);
    }

    @Override
    public Message getPlan() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(DungeonManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        WebElement cooldown_bar = Core.DRIVER.findElement(By.id("cooldown_bar_text_dungeon"));
        String time = cooldown_bar.getText();
        Long cooldown = calculateNextExecute(time);
        return new Message(cooldown, this);
    }
}
