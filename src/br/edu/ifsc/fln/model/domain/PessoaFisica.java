/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author piter
 */
public class PessoaFisica extends Cliente {
    private String cpf;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append("**** PESSOA FÍSICA ****").append("\n");
        sb.append("=============================================").append("\n");
        sb.append("Nome: " ).append(PessoaFisica.super.getNome()).append("\n");
        sb.append("Celular: " ).append(PessoaFisica.super.getCelular()).append("\n");
        sb.append("E-mail: " ).append(PessoaFisica.super.getEmail()).append("\n");
        sb.append("Data de Cadastro: " ).append(PessoaFisica.super.getDataCadastro()).append("\n");
        sb.append("CPF: " ).append(PessoaFisica.this.getCpf()).append("\n");
        sb.append("Data de Nascimento: " ).append(PessoaFisica.this.getDataNascimento()).append("\n");
        sb.append("=============================================").append("\n");
        return sb.toString();
    }

    @Override
    public String getDados(String observacoes) {
        StringBuilder sb = new StringBuilder();
        sb.append("**** PESSOA FÍSICA ****").append("\n");
        sb.append("=============================================").append("\n");
        sb.append("Nome: " ).append(PessoaFisica.super.getNome()).append("\n");
        sb.append("Celular: " ).append(PessoaFisica.super.getCelular()).append("\n");
        sb.append("E-mail: " ).append(PessoaFisica.super.getEmail()).append("\n");
        sb.append("Data de Cadastro: " ).append(PessoaFisica.super.getDataCadastro()).append("\n");
        sb.append("CPF: " ).append(PessoaFisica.this.getCpf()).append("\n");
        sb.append("Data de Nascimento: " ).append(PessoaFisica.this.getDataNascimento()).append("\n");
        sb.append("=============================================").append("\n");
        sb.append("Observações: ").append(observacoes).append("\n");
        return sb.toString();
    }  
}
