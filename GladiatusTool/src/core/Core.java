/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import configuration.DriverConfiguration;
import configuration.UserConfiguration;
import java.util.ArrayList;
import manager.DungeonManager;
import manager.ExpeditionManager;
import manager.HealthManager;
import manager.LoginManager;
import manager.Message;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import manager.ArenaManager;
import manager.CircuTurmaManager;
import manager.FightManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
    private CircuTurmaManager circuTurmaManager;
    private ArenaManager arenaManager;

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
        circuTurmaManager = turmaPermition ? new CircuTurmaManager(lag, "cooldown_bar_text_ct", "cooldown_bar_ct", 3, "own3") : null;

        arenaManager = arenaPermition ? new ArenaManager(lag, "cooldown_bar_text_arena", "cooldown_bar_arena", 1, "own2") : null;
    }

    private void initDriver(String url, boolean chrome) {
        if (chrome) {
            DRIVER = new ChromeDriver();
        } else {
            DRIVER = new FirefoxDriver();
        }
        DRIVER.manage().window().maximize();
        LOGON_URL = "https:" + url;
        DRIVER.get(LOGON_URL);
    }

    private void initExpedition() {
        if (expeditionPermition) {
            Message msg = expeditionManager.getPlan();
            if (msg != null) {
                queue.add(msg);
            } else {
                throw new NullPointerException();
            }
        }
    }

    private void initDungeon() {
        if (dungeonPermition) {
            Message msg1 = dungeonManager.getPlan();
            if (msg1 != null) {
                queue.add(msg1);
            } else {
                throw new NullPointerException();
            }
        }
    }

    private void initArena() {
        if (arenaPermition) {
            Message msg1 = arenaManager.getPlan();
            if (msg1 != null) {
                queue.add(msg1);
            } else {
                throw new NullPointerException();
            }
        }
    }

    private void initCircu() {
        if (turmaPermition) {
            Message msg1 = circuTurmaManager.getPlan();
            if (msg1 != null) {
                queue.add(msg1);
            } else {
                throw new NullPointerException();
            }
        }
    }

    private void initBeforeStart() {
        login.execute();
        initExpedition();
        initDungeon();
        initCircu();
        initArena();
    }

    private void checkNotification() {
        try {
            WebElement element = DRIVER.findElement(By.id("blackoutDialognotification"));
            element.findElement(By.className("awesome-button")).click();
        } catch (Throwable e) {

        }
    }

    private void reload() {
        DRIVER.close();
        initQueue();
        initDriver(url, chrome);
        initBeforeStart();
    }

    private void beforeStart() {
        try {
            initBeforeStart();
        } catch (NullPointerException e) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            }
            reload();
        }
    }

    private void start() {

        beforeStart();

        while (true) {
            try {
                checkNotification();

                executeMessage();
            } catch (NoSuchElementException e) {
                reload();
            } catch (NullPointerException ex) {
                reload();
            }
            sleepCore();
        }
    }

    private boolean manageHeal() {
        healthManager.checkHealth();

        return healthManager.isStoppedPlan();
    }
    private boolean wasDeleted = false;

    private void executeMessage() {
        Message msg = null;
        boolean lowHealth = manageHeal();
        if (lowHealth) {
            List<Message> list = new ArrayList();
            for (Message message : queue) {
                if ((message.getManager() instanceof ExpeditionManager)
                        || (message.getManager() instanceof ArenaManager)) {
                    list.add(message);
                }
            }
            for (Message message : list) {
                queue.remove(message);
            }
            wasDeleted = true;
        } else {
            if (wasDeleted) {
                initExpedition();
                initArena();
                wasDeleted = false;
            }
        }
        if (queue.size() > 0 && System.currentTimeMillis() >= queue.peek().getExecuteTime()) {

            msg = queue.poll();

            try {
                msg.execute();
                Message newMSG = msg.getPlan();
                if (newMSG != null) {
                    queue.add(newMSG);
                }
            } catch (Throwable e) {
                reload();
            }
        }
    }

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
