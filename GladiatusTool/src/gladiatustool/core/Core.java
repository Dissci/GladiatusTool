/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.core;

import gladiatustool.manager.Message;
import java.util.Comparator;
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

    private long time = System.currentTimeMillis();
    private PriorityQueue<Message> queue;
    private WebDriver driver;
    private final long sleepTime = 1000;

    public Core(String url, boolean chrome) {
        driver = initDriver(url, chrome);
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

    public WebDriver initDriver(String url, boolean chrome) {       
        if (chrome) {
            driver = new ChromeDriver();
        } else {
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
        driver.get(url);
        return driver;
    }

    public void start() {

        while (true) {

            executeMessage();

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

    private void sleepCore() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
