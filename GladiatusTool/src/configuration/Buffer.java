/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;
import manager.LoginManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author krkoska.tomas
 */
public class Buffer {

    private final String format = ".ser";

    public void serializableObject(Object object, String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(new File(fileName + format)));

            outputStream.writeObject(object);
            outputStream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException ex) {
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Object deserializableObject(String fileName) {
        Object object = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileName + format);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
            object = objectInputStream.readObject();
            objectInputStream.close();

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
            }
            return object;
        }
    }

    public Document getHTMLdoc(String URL, String LANG) {
        Document doc = null;
        try {
            URL address = new URL("https://" + LANG + URL);
            HttpsURLConnection con = (HttpsURLConnection) address.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            String html = builder.toString();
            doc = Jsoup.parse(html);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Internet connetction problem !", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return doc;
    }

    public List<String> loadListOfServers(String id, Document doc) {
        List<String> list = new ArrayList();
        Element element = doc.getElementById(id);
        List<Element> lis = element.getAllElements();

        for (Element li : lis) {
            if (li.childNodeSize() == 1) {
                list.add(li.text());
            }
        }
        return list;
    }

    public String[][] loadListOfLanguages(String id, Document doc) {
        Element element = doc.getElementById(id);
        List<Element> lis = element.getElementsByTag("a");
        String[][] list = new String[lis.size()][2];

        for (int i = 0; i < lis.size(); i++) {
            list[i][1] = lis.get(i).text();
            list[i][0] = substLangFromURL(lis.get(i));
        }
        return list;
    }

    private String substLangFromURL(Element element) {
        StringBuilder builder = new StringBuilder(element.attr("href"));
        return builder.substring(2, builder.indexOf("."));
    }
}
