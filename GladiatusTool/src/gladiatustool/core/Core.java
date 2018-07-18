/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.core;

import gladiatustool.configuration.UserConfiguration;
import gladiatustool.manager.DungeonManager;
import gladiatustool.manager.ExpeditionManager;
import gladiatustool.manager.HealthManager;
import gladiatustool.manager.LoginManager;
import gladiatustool.manager.Message;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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

    private final PriorityQueue<Message> queue;
    public static WebDriver DRIVER;
    public static String OVERVIEW_URL;
    public static String LOGON_URL;
    private final long sleepTime = 1000;
    private final LoginManager login;
    private final DungeonManager dungeonManager;
    private final ExpeditionManager expeditionManager;
    private final boolean chrome;
    private final String url;

    public Core(String url, boolean chrome, UserConfiguration userConfiguration, int serverIndex, int expeditionEnemy, int dungeonMode, long lag) {
        initDriver(url, chrome);
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
        login = new LoginManager(userConfiguration, serverIndex);
        dungeonManager = new DungeonManager(lag, dungeonMode);
        expeditionManager = new ExpeditionManager(lag, expeditionEnemy);
        this.chrome = chrome;
        this.url = url;
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
        Message msg = expeditionManager.getPlan();
        queue.add(msg);
        Message msg1 = dungeonManager.getPlan();
        queue.add(msg1);
    }

    private void checkNotification() {
        try {
            WebElement element = DRIVER.findElement(By.id("blackoutDialognotification"));
            element.findElement(By.className("awesome-button")).click();
        } catch (Throwable e) {

        }
    }
    HealthManager healthManager = new HealthManager(sleepTime);

    private void start() {
        initBeforeStart();
        while (true) {

            checkNotification();
            healthManager.findFood();            
            try {
                healthManager.checkHealth();
                executeMessage();
            } catch (LowHealthException ex) {
                DRIVER.close();
            } catch (Throwable e) {
                DRIVER.close();
                initDriver(url, chrome);
                initBeforeStart();
            }
            sleepCore();
        }
    }

    private void executeMessage() {
        if (queue.size() > 0 && System.currentTimeMillis() >= queue.peek().getExecuteTime()) {
            Message msg = queue.poll();
            msg.execute();
            Message newMSG = msg.getPlan();
            if (newMSG != null) {
                queue.add(newMSG);
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
