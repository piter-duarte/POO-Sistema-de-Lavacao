/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author piter
 */
public class Motor {
    private int potencia;
   
    private Modelo modelo;
    private ETipoCombustivel tipoCombustivel;

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public ETipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(ETipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }    
    
}
