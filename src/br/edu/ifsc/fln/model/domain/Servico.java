/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author Aluno
 */
public class Servico {
    private int id;
    private String descricao;
    private double valor;
    static private int pontos = 10;
    private ECategoriaS categoria;

    public Servico() {
    }

    public Servico(int id, String descricao, double valor, ECategoriaS categoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
    } 
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getPontos() {
        return pontos;
    }

    public ECategoriaS getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoriaS categoria) {
        this.categoria = categoria;
    }
    
    @Override
    public String toString() {
        return this.descricao;
    }
    
}
