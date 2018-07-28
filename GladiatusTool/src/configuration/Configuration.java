/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krkoska.tomas
 */
public abstract class Configuration implements Serializable {

    protected final Properties properties = new Properties();
    protected final String PATH = "src/resources/";
    protected final String FILE;
    protected final String FULL_PATH;
    protected InputStream in;
    protected FileOutputStream out;
    private final String format = ".ser";

    public Configuration(boolean in, String file) {
        this.FILE = file;
        this.FULL_PATH = PATH + FILE;
        init(in);
    }

    public void init(boolean in) {
        try {
            setStream(in, FULL_PATH);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
    protected void setStream(boolean input, String propFile) throws FileNotFoundException {
        if (input) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            in = new FileInputStream(propFile);
            try {
                properties.load(in);
            } catch (IOException ex) {
                Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                if (in != null) {
                    in.close();
                }
                out = new FileOutputStream(propFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DriverConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
  
}
