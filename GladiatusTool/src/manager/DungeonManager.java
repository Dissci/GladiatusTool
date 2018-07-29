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
public class DungeonManager extends Manager {

    private final int dungeonMode;
    private String dungeonName;

    public DungeonManager(Long lag, int dungeonMode) {
        super(lag);
        this.dungeonMode = dungeonMode;
    }

    private void openDungeon() {
        try {
            String button = "dif" + dungeonMode;
            WebElement dungeonOption = Core.DRIVER.findElement(By.name(button));
            click(dungeonOption);
            attackOnEnemy();
        } catch (Throwable e) {

        }
    }

    private void attackOnEnemy() throws Throwable {
        List<WebElement> allImages = Core.DRIVER.findElements(By.tagName("img"));
        for (WebElement allImage : allImages) {
            if (allImage.getAttribute("src").contains("combatloc.gif")) {
                attack(allImage);
                return;
            }
        }
        throw new Throwable();
    }

    private void setDungeonName() {
        if (dungeonName == null) {
            dungeonName = Core.DRIVER.findElement(By.className("dungeon_header_open")).getText();
        }
    }

    public String getDungeonName() {
        return dungeonName;
    }

    @Override
    public void inExecute() {
        WebElement dungeon = Core.DRIVER.findElement(By.id("cooldown_bar_dungeon"));
        click(dungeon);
        try {
            attackOnEnemy();
        } catch (Throwable e) {
            openDungeon();
        }
        setDungeonName();
    }

    @Override
    public Message getPlan() {
        Thread th = new Thread();
        try {
            th.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DungeonManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        WebElement cooldown_bar = Core.DRIVER.findElement(By.id("cooldown_bar_text_dungeon"));
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
