/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author piter
 */
public enum ECategoriaS {
    PEQUENO(1, "PEQUENO"), MEDIO(2,"MEDIO"), GRANDE(3,"GRANDE"), MOTO(4,"MOTO"), PADRAO(5,"PADRAO"), TODAS(6, "TODAS");
    
    private int id;
    private String descricao;

    private ECategoriaS(int id, String descricao) {
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
