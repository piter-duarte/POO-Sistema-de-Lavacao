/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import br.edu.ifsc.fln.exception.ExceptionLavacao;

/**
 *
 * @author Aluno
 */
public class Pontuacao {
    private int quantidade;
    private Cliente cliente;

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    
    
    public int adicionar(int quantidade){
        return this.quantidade = this.quantidade + quantidade;
    }
    
    public int subtrair(int quantidade) throws ExceptionLavacao{  
        if(quantidade <= this.quantidade)
        {
            return this.quantidade = this.quantidade - quantidade;   
        }
        else
        {   
            throw new ExceptionLavacao("Erro ao remover pontuação!", new ExceptionLavacao("Não há pontuação suficiente para remoção."));
        }
    }
    
    public int saldo(){
        return quantidade;
    }
}
