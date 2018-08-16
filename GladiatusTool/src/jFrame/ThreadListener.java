/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jFrame;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomáš
 */
public class ThreadListener implements Runnable {

    private Thread th;
    private LoginFrame frame;

    public ThreadListener(Thread th, LoginFrame frame) {
        this.th = th;
        this.frame = frame;
    }

    private void checkThreadLife() {
        while (th.isAlive()) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        frame.driverIsClosed();
    }

    @Override
    public void run() {
        checkThreadLife();
    }
}
