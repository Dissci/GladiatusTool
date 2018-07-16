/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.main;

import gladiatustool.jFrame.LoginFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author Tomáš
 */
public class GladiatusTool {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);

//        WebDriver driver = new FirefoxDriver();
//        driver.manage().window().maximize();
//        driver.get("https://sk.gladiatus.gameforge.com/game/");
//        Thread.sleep(1000);
//        WebElement user = driver.findElement(By.id("login_username"));
//        WebElement password = driver.findElement(By.id("login_password"));
//        user.sendKeys("Papa");
//        password.sendKeys("56c9a3s3");
// 
//     Select selectBox = new Select(driver.findElement(By.id("login_server")));
//     List<WebElement> list = selectBox.getOptions();
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(i+". "+list.get(i).getText());
//        }
//        Scanner sc = new Scanner(System.in);
//        
//        selectBox.selectByIndex(sc.nextInt());
//        System.out.println("Login");
//     WebElement submit  = driver.findElement(By.id("loginsubmit"));
//     submit.click();
//     WebElement expedition = driver.findElement(By.id("cooldown_bar_expedition"));
//     expedition.click();
//     WebElement attack = driver.findElement(By.id("expedition_list"));
//     List<WebElement> listt = attack.findElements(By.className("expedition_box"));
//     listt.get(1).findElement(By.className("expedition_button")).click();
//      WebElement dungeon = driver.findElement(By.id("cooldown_bar_dungeon"));
//     dungeon.click();
//            WebElement cl = driver.findElement(By.className("map_label"));
//            cl.click();
    }
}
