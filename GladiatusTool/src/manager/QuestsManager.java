/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import core.Core;
import java.util.Iterator;
import java.util.List;
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
    private final String inactive = "contentboard_slot_inactive";
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
    private Long fullStackCooldown = 200000L;
    private boolean firstPlan = true;
    private long globalCooldown = 200000L;
    private boolean fullQuestsStack = false;

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
        restartQuests();
        finishQuest();
        fullQuestsStack = isFullStackOfQuests();
        if (!fullQuestsStack && getCooldownText().contains("-")) {
            chooseQuest();
        }
        globalCooldown = calculateNextExecute(getCooldownText(), false);
    }

    private String getUrlIcon(WebElement element) {
        String urlIcon = "@@";
        int attempts = 0;
        while (attempts < 3) {
            try {
                List<WebElement> list = element.findElements(By.className("quest_slot_icon"));
                if (list != null && !list.isEmpty()) {
                    urlIcon = list.get(0).getAttribute("style");
                    break;
                }
            } catch (NoSuchElementException e) {
                attempts++;
            }
        }
        return urlIcon;
    }

    private String getQuestText(WebElement element) {
        String questText = "@@";
        int attempts = 0;
        while (attempts < 3) {
            try {
                List<WebElement> list = element.findElements(By.className("quest_slot_title"));
                if (list != null && !list.isEmpty()) {
                    questText = list.get(0).getText();
                    break;
                }
            } catch (NoSuchElementException e) {
                attempts++;
            }
        }
        return questText;
    }

    private void chooseQuest() {
        WebElement table = Core.DRIVER.findElement(By.className(questTable));
        sleepThread(150);
        List<WebElement> list = table.findElements(By.className(inactive));

        boolean accepted = false;
        for (WebElement webElement : list) {
            sleepThread(150);
            String urlIcon = getUrlIcon(webElement);
            sleepThread(150);
            String questText = getQuestText(webElement);

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

    private void finishQuest() {
        WebElement table = Core.DRIVER.findElement(By.className(questTable));
        sleepThread(150);
        List<WebElement> list = table.findElements(By.className(finishButton));
        Iterator<WebElement> i = list.iterator();

        while (i.hasNext()) {
            WebElement webElement = i.next();
            webElement.click();
            i.remove();
        }
    }

    private void restartQuests() {
        WebElement table = Core.DRIVER.findElement(By.className(questTable));
        sleepThread(150);
        List<WebElement> list = table.findElements(By.className(restartButton));
        Iterator<WebElement> i = list.iterator();

        while (i.hasNext()) {
            WebElement webElement = i.next();
            webElement.click();
            i.remove();
        }
    }

    private boolean isFullStackOfQuests() {
        String accepted = Core.DRIVER.findElement(By.id("quest_header_accepted")).getText();
        return accepted.substring(accepted.indexOf(": ") + 1, accepted.indexOf(" /")).equals(accepted.substring(accepted.lastIndexOf("/") + 1));
    }

    private String getCooldownText() {
        String time = "-";
        try {
            WebElement cooldown_bar = Core.DRIVER.findElement(By.id("quest_header_cooldown"))
                    .findElement(By.className("ticker"));
            time = cooldown_bar.getText();
        } catch (NoSuchElementException e) {
        }
        return time;
    }

    @Override
    public Message getPlan() {
        sleepThreadTo1000();

        Long cooldown = fullStackCooldown;
        if (!firstPlan) {
            cooldown += System.currentTimeMillis();
            if (cooldown > globalCooldown && !fullQuestsStack) {
                cooldown = globalCooldown;
            }
        } else {
            firstPlan = false;
        }
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

    public void reset() {
        
    }
}
