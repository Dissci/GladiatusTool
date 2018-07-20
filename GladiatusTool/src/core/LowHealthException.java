/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author krkoska.tomas
 */
public class LowHealthException extends Exception {
  public LowHealthException() { super(); }
  public LowHealthException(String message) { super(message); }
  public LowHealthException(String message, Throwable cause) { super(message, cause); }
  public LowHealthException(Throwable cause) { super(cause); }
}
