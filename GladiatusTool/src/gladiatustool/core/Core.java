/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.core;

import gladiatustool.configuration.UserConfiguration;
import gladiatustool.manager.DungeonManager;
import gladiatustool.manager.ExpeditionManager;
import gladiatustool.manager.LoginManager;
import gladiatustool.manager.Message;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author Tomáš
 */
public class Core {

    private final PriorityQueue<Message> queue;
    public static WebDriver DRIVER;
    private final long sleepTime = 1000;
    private final LoginManager login;
    private final DungeonManager dungeonManager;
    private final ExpeditionManager expeditionManager;

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
    }

    private void initDriver(String url, boolean chrome) {
        if (chrome) {
            DRIVER = new ChromeDriver();
        } else {
            DRIVER = new FirefoxDriver();
        }
        DRIVER.manage().window().maximize();
        DRIVER.get("https:" + url);
    }

    private void initBeforeStart() {
        login.execute();

        Message msg = expeditionManager.getPlan();
        queue.add(msg);
        Message msg1 = dungeonManager.getPlan();
        queue.add(msg1);
    }

    public void start() {
        initBeforeStart();
        while (true) {

            executeMessage();

            sleepCore();
        }
    }

    private void executeMessage() {
        Date date = new Date(queue.peek().getExecuteTime());
        System.out.println("Execute at: " + date.toString());

        Date date1 = new Date(System.currentTimeMillis());
        System.out.println("Current: " + date1.toString());

        if (queue.size() > 0 && System.currentTimeMillis() >= queue.peek().getExecuteTime()) {
            Message msg = queue.poll();
            msg.execute();
            Message newMSG = msg.getPlan();
            if (newMSG != null) {
                queue.add(newMSG);
            }
        }
    }

    private void sleepCore() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
