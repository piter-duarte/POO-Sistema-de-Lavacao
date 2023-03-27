/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;

/**
 *
 * @author piter
 */
public class PessoaJuridica extends Cliente {
    private String cnpj;
    private String inscricaoEstadual;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append("**** PESSOA JURÍDICA ****").append("\n");
        sb.append("=============================================").append("\n");
        sb.append("Nome: " ).append(PessoaJuridica.super.getNome()).append("\n");
        sb.append("Celular: " ).append(PessoaJuridica.super.getCelular()).append("\n");
        sb.append("E-mail: " ).append(PessoaJuridica.super.getEmail()).append("\n");
        sb.append("Data de Cadastro: " ).append(PessoaJuridica.super.getDataCadastro()).append("\n");
        sb.append("CNPJ: " ).append(PessoaJuridica.this.getCnpj()).append("\n");
        sb.append("Data de Nascimento: " ).append(PessoaJuridica.this.getDataNascimento()).append("\n");
        sb.append("Inscrição Estadual: " ).append(PessoaJuridica.this.getInscricaoEstadual()).append("\n");
        sb.append("=============================================").append("\n");
        return sb.toString();
    }

    @Override
    public String getDados(String observacoes) {
        StringBuilder sb = new StringBuilder();
        sb.append("**** PESSOA JURÍDICA ****").append("\n");
        sb.append("=============================================").append("\n");
        sb.append("Nome: " ).append(PessoaJuridica.super.getNome()).append("\n");
        sb.append("Celular: " ).append(PessoaJuridica.super.getCelular()).append("\n");
        sb.append("E-mail: " ).append(PessoaJuridica.super.getEmail()).append("\n");
        sb.append("Data de Cadastro: " ).append(PessoaJuridica.super.getDataCadastro()).append("\n");
        sb.append("CNPJ: " ).append(PessoaJuridica.this.getCnpj()).append("\n");
        sb.append("Data de Nascimento: " ).append(PessoaJuridica.this.getDataNascimento()).append("\n");
        sb.append("Inscrição Estadual: " ).append(PessoaJuridica.this.getInscricaoEstadual()).append("\n");
        sb.append("=============================================").append("\n");
        sb.append("Observações: ").append(observacoes).append("\n");
        return sb.toString();
    } 
    
}
