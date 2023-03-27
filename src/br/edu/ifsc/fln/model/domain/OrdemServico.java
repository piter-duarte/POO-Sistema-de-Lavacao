/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aluno
 */
public class OrdemServico {
    private int numero;
    private BigDecimal total;
    private LocalDate agenda;
    private double desconto;
   
    
    private EStatus status;
   
    
    private List<ItemOS> itensOS;
    private Veiculo veiculo;

    public OrdemServico() {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public BigDecimal getTotal() {
        calcularServico();
        return total;
    }

    
    /* o método setTotal foi retirado para evitar inconsistência, logo, para obter o total é necessário
       executar o método calcularServico que é chamado pelo método getTotal*/
    //public void setTotal(BigDecimal total) {
    //    this.total =;
    //}

    public LocalDate getAgenda() {
        return agenda;
    }

    public void setAgenda(LocalDate agenda) {
        this.agenda = agenda;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    } 

    public List<ItemOS> getItensOS() {
        return itensOS;
    }

    public void setItensOS(List<ItemOS> itensOS) {
        this.itensOS = itensOS;
    }
    
    public void add(ItemOS itemOS){
        if(itensOS == null){
            itensOS = new ArrayList<>();
        }
        itensOS.add(itemOS);
        itemOS.setOrdemServico(this);
    }
    
    public void remove(ItemOS itemOs){
        itensOS.remove(itemOs);
    }
    
    private void calcularServico() {
        total = new BigDecimal(0.0);
        for (ItemOS item: this.getItensOS()) {
            total = total.add(item.getValorServico());
        }
        if (desconto >= 0) {
            BigDecimal descontoOS = new BigDecimal(total.doubleValue() * this.desconto / 100.0);
            total = total.subtract(descontoOS);    
        }
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
    
    
    
}