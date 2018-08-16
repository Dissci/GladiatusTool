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
import manager.QuestsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
    private final long sleepTime = 250;
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
    private QuestsManager questsManager;
    private boolean pause = false;

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
        questsManager = new QuestsManager(dungeonPermition, expeditionPermition, arenaPermition, turmaPermition, lag);
    }

    private void initDriver(String url, boolean chrome) {
        if (chrome) {
            DRIVER = new ChromeDriver();
        } else {
            DRIVER = new FirefoxDriver();
        }

        DRIVER.manage().deleteAllCookies();
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

    private void initQuests() {
        questsManager = new QuestsManager(dungeonPermition, expeditionPermition, arenaPermition, turmaPermition, 0L);
        Message msg1 = questsManager.getPlan();
        if (msg1 != null) {
            queue.add(msg1);
        } else {
            throw new NullPointerException();
        }
    }

    private void initBeforeStart() {
        login.logIn();
        checkNotification();
        initExpedition();
        initDungeon();
        initCircu();
        initArena();
        initQuests();
    }

    private void checkNotification() {
        try {
            WebElement element = DRIVER.findElement(By.id("blackoutDialognotification"));
            element.findElement(By.className("awesome-button")).click();
        } catch (Throwable e) {
            try {
                DRIVER.findElement(By.id("linkLoginBonus")).click();
            } catch (Throwable ee) {

            }
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
            checkNotification();
            try {
                Message msg = executeMessage();
                if (msg != null) {
                    setPlan(msg);
                }
            } catch (StaleElementReferenceException e) {
                reload();
            } catch (NoSuchElementException eex) {
                reload();
            } catch (NullPointerException ex) {
                reload();
            } catch (WebDriverException ee) {
                reload();
            } catch (Throwable exx) {
                reload();
            }
            sleepCore();
        }
    }

    private void chechHealth() {
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
        }
        if (!lowHealth) {
            if (wasDeleted) {
                initExpedition();
                initArena();
                wasDeleted = false;
            }
        }
    }

    private boolean manageHeal() {
        healthManager.checkHealth();

        return healthManager.isStoppedPlan();
    }
    private boolean wasDeleted = false;

    private Message executeMessage() {
        if (queue.size() > 0 && System.currentTimeMillis() >= queue.peek().getExecuteTime()) {
            chechHealth();
            Message msg = queue.poll();
            int attempts = 0;
            while (attempts < 2) {
                try {
                    msg.execute();
                    setNamesForQuestManager(msg);
                    break;
                } catch (StaleElementReferenceException e) {
                } catch (NoSuchElementException eex) {
                } catch (NullPointerException ex) {
                } catch (WebDriverException ee) {
                }
                attempts++;
                if (attempts == 2) {
                    throw new NoSuchElementException("");
                }
            }
            return msg;
        }
        return null;
    }
    private boolean expNameSetted = false;
    private boolean dungeonNameSetted = false;

    private void setPlan(Message msg) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                Message newMSG = msg.getPlan();
                if (newMSG != null) {
                    queue.add(newMSG);
                }
                break;
            } catch (StaleElementReferenceException e) {
            } catch (NoSuchElementException eex) {
            } catch (NullPointerException ex) {
            } catch (WebDriverException ee) {
            }
            attempts++;
            if (attempts == 2) {
                throw new NoSuchElementException("");
            }
        }
    }

    private void setNamesForQuestManager(Message msg) {
        if (msg.getManager() instanceof ExpeditionManager && !expNameSetted) {
            questsManager.setEnemyNames(getExpeditionEnemyName(), getDungeonName());
            expNameSetted = true;
        } else if (msg.getManager() instanceof DungeonManager && !dungeonNameSetted) {
            questsManager.setEnemyNames(getExpeditionEnemyName(), getDungeonName());
            dungeonNameSetted = true;
        }
    }

    private void sleepCore() {
        Thread th = new Thread();
        try {
            th.sleep(sleepTime);
            DRIVER.getTitle();
            while (pause) {
                DRIVER.getTitle();
                th.sleep(sleepTime);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Core.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getExpeditionEnemyName() {
        return expeditionManager == null ? "@SpecialCharacter" : expeditionManager.getExpeditionEnemyName();
    }

    public String getDungeonName() {
        return dungeonManager == null ? "@SpecialCharacter" : dungeonManager.getDungeonName();
    }

    public synchronized void pauseBot() {
        pause = true;
    }

    @Override
    public void run() {
        start();
    }

    public synchronized void unPauseBot() {
        pause = false;
    }
}
