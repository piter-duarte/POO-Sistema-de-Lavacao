/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.test;

import br.edu.ifsc.fln.exception.ExceptionLavacao;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.model.domain.Pontuacao;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.time.LocalDate;
import java.time.Month;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mpisc
 */
public class TestMain {
    public static void main(String[] args) {

//        Cliente fisico = new PessoaFisica();
//        
//        fisico.setId(158);
//        fisico.setNome("nomeClienteFisico");
//        fisico.setCelular("(48) 9876-1234");
//        fisico.setEmail("fisico@email.com");
//        fisico.setDataCadastro(LocalDate.of(2000, Month.JANUARY, 8));
//        ((PessoaFisica)fisico).setCpf("123-879-471-21");
//        ((PessoaFisica)fisico).setDataNascimento(LocalDate.of(1970, Month.JULY, 18));
//        
//        Cliente juridico = new PessoaJuridica();
//        juridico.setId(345);
//        juridico.setNome("nomeClienteJuridico");
//        juridico.setCelular("(48) 1234-6789");
//        juridico.setEmail("juridico@email.com");
//        juridico.setDataCadastro(LocalDate.of(2019, Month.OCTOBER, 10));
//        ((PessoaJuridica)juridico).setCnpj("78.274.270/0591-58");
//        ((PessoaJuridica)juridico).setInscricaoEstadual("541.455.164.173"); 
//        
//        print(fisico);
//        System.out.println("");
//        print(juridico);

        
//        Pontuacao pontuacao = new Pontuacao();
//        pontuacao.setQuantidade(85);
//        try {
//            pontuacao.subtrair(86);
//        } catch (ExceptionLavacao ex) {
//                    System.out.println(ex.getCause());
//        }
//        Cliente cliente = new PessoaFisica();
//        cliente.setNome("José Freitas");
//        Veiculo teste1 = new Veiculo("Testando1");
//        Veiculo teste2 = new Veiculo("Testando2");
//        Veiculo teste3 = new Veiculo("Testando3");
//        
        //cliente.add(teste1);
        //cliente.add(teste2);
//        try {
//            cliente.remove(teste3);
//        } catch (ExceptionLavacao ex) {
//            System.out.println("" + ex.getMessage() + "" + ex.getCause());
//        }
    }
    
//    public static void print(Cliente cliente) {
//        System.out.printf("**** Dados do Cliente " + cliente.getClass().getSimpleName() + " ****\n");
//        System.out.println("Nome...................: " + cliente.getNome());
//        System.out.println("Celular................: " + cliente.getCelular());
//        System.out.println("Email..................: " + cliente.getEmail());
//        System.out.println("Data_Cadastro..........: " + cliente.getDataCadastro());
//        
//        if (cliente instanceof PessoaFisica) 
//        {
//            System.out.println("CPF....................: " + ((PessoaFisica)cliente).getCpf());
//            System.out.println("Data_Nascimento........: " + ((PessoaFisica)cliente).getDataNascimento());
//        } 
//        else 
//        {
//            System.out.println("CNPJ...................: " + ((PessoaJuridica)cliente).getCnpj());
//            System.out.println("Inscrição_Estadual.....: " + ((PessoaJuridica)cliente).getInscricaoEstadual());
//        }
//    };


}
