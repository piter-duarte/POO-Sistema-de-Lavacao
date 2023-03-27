/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author piter
 */
public enum ETipoCombustivel {
    GASOLINA(1, "GASOLINA"), ETANOL(2,"ETANOL"), FLEX(3,"FLEX"), GNV(4,"GNV"), OUTRO(5,"OUTRO"); 
    private int id;
    private String descricao;

    private ETipoCombustivel(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }
    
    public String getDescricao() {
        return descricao;
    }    
}
