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
import org.openqa.selenium.WebElement;

/**
 *
 * @author krkoska.tomas
 */
public class QuestsManager extends Manager {

    private final boolean dungeon;
    private final boolean expedition;
    private final boolean arena;
    private final boolean circuTurma;
    private final String accept = "accept";
    private final String inactive = "contentboard_slot_inactive";
    private final String dungeonQ = "dungeon";
    private final String circuTurmeQ = "grouparena";
    private final String arenaQ = "_arena";
    private final String combat = "combat";
    private final String tabBar = "menuitem";
    private final String questTable = "contentboard_start";
    private final String acceptButton = "quest_slot_button_accept";
    private final String restartButton = "quest_slot_button_restart";
    private final String finishButton = "quest_slot_button_finish";
    private String expeditionEnemyName = "@SpecialCharacter";
    private String dungeonName = "@@SpecialCharacter";

    public QuestsManager(boolean dungeon, boolean expedition, boolean arena, boolean circuTurma, Long lag) {
        super(lag);
        this.dungeon = dungeon;
        this.expedition = expedition;
        this.arena = arena;
        this.circuTurma = circuTurma;
    }

    @Override
    public void inExecute() {
        Core.DRIVER.findElements(By.className(tabBar)).get(1).click();
        WebElement table = Core.DRIVER.findElement(By.className(questTable));
        restartQuests(table);
        finishQuest(table);
        if (!isFullStackOfQuests()) {
            chooseQuest(table);
        }
    }

    private void chooseQuest(WebElement questTable) {
        List<WebElement> list = questTable.findElements(By.className(inactive));
        boolean accepted = false;
        for (WebElement webElement : list) {
            String urlIcon = webElement.findElement(By.className("quest_slot_icon")).getAttribute("style");
            String questText = webElement.findElement(By.className("quest_slot_title")).getText();
            if ((dungeon && (questText.contains(dungeonName) || urlIcon.contains(combat)))
                    || (expedition && (questText.contains(expeditionEnemyName) || urlIcon.contains(combat)))
                    || (circuTurma && urlIcon.contains(circuTurmeQ))
                    || (arena && (urlIcon.contains(arenaQ) || urlIcon.contains(combat)))) {
                webElement.findElement(By.className(acceptButton)).click();
                accepted = true;
                break;
            }
        }
        if (!accepted) {
            resetTimer();
        }
    }

    private void resetTimer() {
        Core.DRIVER.findElement(By.id("quest_footer_reroll")).findElement(By.className("awesome-button")).click();
    }

    private void finishQuest(WebElement questTable) {
        List<WebElement> list = questTable.findElements(By.className(finishButton));
        for (WebElement webElement : list) {
            webElement.click();
        }
    }

    private void restartQuests(WebElement questTable) {
        List<WebElement> list = questTable.findElements(By.className(restartButton));
        for (WebElement webElement : list) {
            webElement.click();
        }
    }

    private boolean isFullStackOfQuests() {
        String accepted = Core.DRIVER.findElement(By.id("quest_header_accepted")).getText();
        return accepted.substring(accepted.indexOf(": ") + 1, accepted.indexOf(" /")).contains("5");
    }

    @Override
    public Message getPlan() {
        Thread th = new Thread();
        try {
            th.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DungeonManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        Core.DRIVER.findElements(By.className(tabBar)).get(1).click();
        String time = "";
        try {
            WebElement cooldown_bar = Core.DRIVER.findElement(By.id("quest_header_cooldown")).findElement(By.className("ticker"));
            time = cooldown_bar.getText();
        } catch (NoSuchElementException e) {

        }
        Long cooldown = calculateNextExecute(time);
        goOnOverview();
        return new Message(cooldown, this);
    }

    public void setEnemyNames(String expeditionName, String dungeonName) {
        this.expeditionEnemyName = expeditionName;
        this.dungeonName = dungeonName;
    }

    @Override
    protected void beforeExecute() {
    }

    @Override
    protected void afterExecute() {
    }
}
