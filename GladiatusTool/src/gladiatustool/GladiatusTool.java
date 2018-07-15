/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool;

import com.sun.security.ntlm.Client;
import java.util.List;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;


/**
 *
 * @author Tomáš
 */
public class GladiatusTool {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
      System.setProperty("webdriver.firefox.driver","‪C:/Program Files/Mozilla Firefox/firefox.exe");
      System.setProperty("webdriver.gecko.driver", "C:/Users/Tomáš/Downloads/geckodriver-v0.21.0-win64/geckodriver.exe");
      WebDriver driver  = new FirefoxDriver();
      driver.manage().window().maximize();
      driver.get("https://s18-sk.gladiatus.gameforge.com/game/index.php?mod=overview&login=1&cnt=0&sh=6c4149ed03037902036f8ccb9f33524c");
      Thread.sleep(1000);
      WebElement user = driver.findElement(By.id("login_username"));
      WebElement password = driver.findElement(By.id("login_password"));
      user.sendKeys("Papa");
      password.sendKeys("56c9a3s3");
 
     Select selectBox = new Select(driver.findElement(By.id("login_server")));
     selectBox.selectByIndex(9);
        System.out.println("Login");
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
