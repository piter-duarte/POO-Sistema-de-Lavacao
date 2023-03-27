/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author Aluno
 */
public enum EStatus {
    ABERTA(1, "ABERTA"), FECHADA(2,"FECHADA"), CANCELADA(3,"CANCELADA");
    
    private int id;
    private String descricao;

    private EStatus(int id, String descricao) {
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
