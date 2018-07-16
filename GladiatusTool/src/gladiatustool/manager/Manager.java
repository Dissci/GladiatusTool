/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gladiatustool.manager;

import java.util.Random;

/**
 *
 * @author krkoska.tomas
 */
public abstract class Manager {

    protected Long lag;
    protected Random rndLag;

    public Manager(Long lag) {
        this.lag = lag;
        this.rndLag = new Random(lag);
    }

    public abstract void execute();

    public abstract Message getPlan();

    public Long getLag() {
        return lag;
    }

    public void setLag(Long lag) {
        this.lag = lag;
    }

    protected long getRandomLag() {
        return 0L;//rndLag.nextLong();
    }

    protected long calculateNextExecute(String cooldown) {
        Long time = System.currentTimeMillis();
        if (cooldown.contains(":")) {
            StringBuilder builder = new StringBuilder(cooldown);
            String seconds = builder.substring(builder.lastIndexOf(":") + 1);
            String minutes = builder.substring(builder.indexOf(":") + 1, builder.lastIndexOf(":"));
            minutes = cutNul(minutes);
            seconds = cutNul(seconds);
            time += Long.parseLong(minutes, 10) * 60000;
            time += Long.parseLong(seconds, 10) * 1000;
        }
        time += getRandomLag();
        return time;
    }

    private String cutNul(String time) {
        StringBuilder builder = new StringBuilder(time);
        if (builder.charAt(0) == '0') {
            time = builder.substring(1);
        }
        return time;
    }
}
