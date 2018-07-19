/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.core;

import gladiatustool.configuration.DriverConfiguration;
import gladiatustool.configuration.UserConfiguration;
import gladiatustool.manager.DungeonManager;
import gladiatustool.manager.ExpeditionManager;
import gladiatustool.manager.HealthManager;
import gladiatustool.manager.LoginManager;
import gladiatustool.manager.Message;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author Tomáš
 */
public class Core implements Runnable {

    private PriorityQueue<Message> queue;
    public static WebDriver DRIVER;
    public static String OVERVIEW_URL;
    public static String LOGON_URL;
    private final long sleepTime = 1000;
    private boolean chrome;
    private String url;
    private LoginManager login;
    private DungeonManager dungeonManager;
    private ExpeditionManager expeditionManager;
    private HealthManager healthManager;
    private boolean expeditionPermition;
    private boolean dungeonPermition;
    private boolean arenaPermition;
    private boolean turmaPermition;
    private int criticalHealthLevel;

    public Core(UserConfiguration userConfiguration,
            DriverConfiguration driverConfiguration) {

        initVariables(userConfiguration, driverConfiguration);
        initDriver(driverConfiguration.getLANG() + driverConfiguration.getURL(), driverConfiguration.isIsChrome());
        initQueue();
        initManagers(userConfiguration, userConfiguration.getServerIndex(), userConfiguration.getExpeditionFocus(),
                userConfiguration.getDungeonMode(), userConfiguration.getLag(), criticalHealthLevel);

    }

    private void initVariables(UserConfiguration userConfiguration,
            DriverConfiguration driverConfiguration) {
        this.chrome = driverConfiguration.isIsChrome();
        this.url = driverConfiguration.getLANG() + driverConfiguration.getURL();
        this.expeditionPermition = userConfiguration.isExpeditions();
        this.dungeonPermition = userConfiguration.isDungeons();
        this.arenaPermition = userConfiguration.isArena();
        this.turmaPermition = userConfiguration.isTurma();
        this.criticalHealthLevel = userConfiguration.getCriticalHealthLevel();
    }

    private void initQueue() {
        queue = new PriorityQueue<>(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                if (o1.getExecuteTime() < o2.getExecuteTime()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    private void initManagers(UserConfiguration userConfiguration,
            int serverIndex, int expeditionEnemy, int dungeonMode, long lag,
            int criticalHealthLevel) {

        healthManager = new HealthManager(sleepTime, criticalHealthLevel);
        login = new LoginManager(userConfiguration, serverIndex);
        dungeonManager = dungeonPermition ? new DungeonManager(lag, dungeonMode) : null;
        expeditionManager = expeditionPermition ? new ExpeditionManager(lag, expeditionEnemy) : null;
    }

    private void initDriver(String url, boolean chrome) {
        if (chrome) {
            DRIVER = new ChromeDriver();
        } else {
            DRIVER = new FirefoxDriver();
        }
        DRIVER.manage().window().maximize();
        LOGON_URL = "https:" + url;
        DRIVER.get(LOGON_URL); //tu pridat exception
    }

    private void initBeforeStart() {
        login.execute();
        if (expeditionPermition) {
            Message msg = expeditionManager.getPlan();
            queue.add(msg);
        }
        if (dungeonPermition) {
            Message msg1 = dungeonManager.getPlan();
            queue.add(msg1);
        }
    }

    private void checkNotification() {
        try {
            WebElement element = DRIVER.findElement(By.id("blackoutDialognotification"));
            element.findElement(By.className("awesome-button")).click();
        } catch (Throwable e) {

        }
    }

    private void manageLowHealth() {

    }

    private void start() {
        initBeforeStart();
        while (true) {

            checkNotification();

            try {
                healthManager.checkHealth();
                executeMessage();
            } catch (LowHealthException ex) {
                DRIVER.close();
                System.exit(0);
            }
            sleepCore();
        }
    }

    private void executeMessage() {
        if (queue.size() > 0 && System.currentTimeMillis() >= queue.peek().getExecuteTime()) {
            Message msg = queue.poll();
            try {
                msg.execute();
                Message newMSG = msg.getPlan();
                if (newMSG != null) {
                    queue.add(newMSG);
                }
            } catch (Throwable e) {
                DRIVER.close();
                initDriver(url, chrome);
                initBeforeStart();
            }
        }
    }
//linkLoginBonus   
//awesome-button big toto je ako class pre novy lvl a rovnako ako aj linkloginbonus
//linknotification
    //linkcancelsoulboundConfirm

    private void sleepCore() {
        Thread th = new Thread();
        try {
            th.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        start();
    }
}
