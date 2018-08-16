/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import configuration.Buffer;
import configuration.Encryptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Tomáš
 */
public class Authorization {

    private final String URL = "www.timeanddate.com/worldclock/";
    private final String timeID = "ctu";
    private LocalDate currentDate;
    private LocalDate licence;
    private Buffer buffer;
    private String userName;

    public Authorization() {
        buffer = new Buffer();
    }

    private void readLicenceFromFile() {
        try {
            List<String> list = buffer.readBytes("src/resources/wwdc");
            licence = LocalDate.parse(list.get(0));
            userName = list.get(1);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "License file is missing !", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unkown License error !", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public boolean checkLicense() {
        Document doc = buffer.getHTMLdoc(URL, "");
        Element element = doc.getElementById(timeID);
        currentDate = parseStringToDate(element.text());
        readLicenceFromFile();
        int result = checkLicenceDate();
        return result < 0 ? false : true;
    }

    public boolean checkLicenceName(String userName) {
        return this.userName.compareToIgnoreCase(userName) == 0 ? true : false;
    }

    public LocalDate parseStringToDate(String text) {
        StringBuilder builder = new StringBuilder(text);
        String input = builder.substring(builder.indexOf(", ") + 2, builder.indexOf(" at"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);
        return LocalDate.parse(input, formatter);
    }

    private int checkLicenceDate() {
        return licence.compareTo(currentDate);
    }

    public String getURL() {
        return URL;
    }

    public String getTimeID() {
        return timeID;
    }

    public LocalDate getLicence() {
        return licence;
    }

    public void setLicence(LocalDate licence) {
        this.licence = licence;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public String getUserName() {
        return userName;
    }
}
