/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.io.Serializable;

/**
 *
 * @author krkoska.tomas
 */
public abstract class Configuration implements Serializable {

    protected final String PATH = "src/resources/";
    protected final String FILE;
    protected final String FULL_PATH;

    public Configuration(boolean in, String file) {
        this.FILE = file;
        this.FULL_PATH = PATH + FILE;
    }

    public String getFILE() {
        return FILE;
    }

    public String getFULL_PATH() {
        return FULL_PATH;
    }
}
