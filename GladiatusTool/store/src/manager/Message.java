/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

/**
 *
 * @author krkoska.tomas
 */
public class Message {

    private final Manager manager;
    private final Long executeTime;

    public Message(Long executeTime, Manager manager) {
        this.manager = manager;
        this.executeTime = executeTime;
    }

    public Manager getManager() {
        return manager;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void execute() throws Throwable {
        manager.execute();
    }
    
    public Message getPlan() {
        return manager.getPlan();
    }
}
