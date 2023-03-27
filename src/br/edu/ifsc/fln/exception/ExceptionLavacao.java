/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.exception;

/**
 *
 * @author piter
 */
public class ExceptionLavacao extends Exception {
     /**
     * Creates a new instance of <code>ExceptionLavacao</code>
     * without detail message.
     */
    public ExceptionLavacao() {
    }

    /**
     * Constructs an instance of <code>ExceptionLavacao</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExceptionLavacao(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>ExceptionLavacao</code> with the
     * specified cause.
     *
     * @param cause the specified Exception cause.
     */
    public ExceptionLavacao(Exception cause) {
        super(cause);
    }
    
    /**
     * Constructs an instance of <code>ExceptionLavacao</code> with the
     * specified cause and detail message.
     * 
     * @param msg the detail message.
     * @param cause the specified Exception cause.
     */    
    public ExceptionLavacao(String msg, Exception cause) {
        super(msg, cause);
    }
}
