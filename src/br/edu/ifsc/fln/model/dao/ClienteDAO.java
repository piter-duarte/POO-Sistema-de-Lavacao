/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mpisc
 */
public class ClienteDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Cliente cliente) throws DAOException {
        String sql = "INSERT INTO cliente(nome, celular, email, endereco, dataCadastro, dataNascimento) VALUES(?, ?, ?, ?, ?, ?)";
        String sqlPF = "INSERT INTO pessoa_fisica(id_cliente, cpf) VALUES((SELECT max(id) FROM cliente), ?)";
        String sqlPJ = "INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricaoEstadual) VALUES((SELECT max(id) FROM cliente), ?, ?)";
        try {
            //armazena os dados da superclasse
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEndereco());
            stmt.setDate(5, java.sql.Date.valueOf(cliente.getDataCadastro()));
            stmt.setDate(6, java.sql.Date.valueOf(cliente.getDataNascimento()));
            stmt.execute();
            //armazena os dados da subclasse
            if (cliente instanceof PessoaFisica) {
                stmt = connection.prepareStatement(sqlPF);
                stmt.setString(1, ((PessoaFisica)cliente).getCpf());
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlPJ);
                stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
                stmt.setString(2, ((PessoaJuridica)cliente).getInscricaoEstadual());
                stmt.execute();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível salvar o registro do cliente no banco de dados!", ex);
        }
    }

    public void alterar(Cliente cliente) throws DAOException {
        String sql = "UPDATE cliente SET nome=?, celular=?, email=?, endereco=?, dataCadastro=?, dataNascimento=? WHERE id=?";
        String sqlPF = "UPDATE pessoa_fisica SET cpf=? WHERE id_cliente = ?";
        String sqlPJ = "UPDATE pessoa_juridica SET cnpj=?, inscricaoEstadual=? WHERE id_cliente = ?";  
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCelular());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEndereco());
            stmt.setDate(5, java.sql.Date.valueOf(cliente.getDataCadastro()));
            stmt.setDate(6, java.sql.Date.valueOf(cliente.getDataNascimento()));
            stmt.setInt(7, cliente.getId());
            stmt.execute();
            if (cliente instanceof PessoaFisica) {
                stmt = connection.prepareStatement(sqlPF);
                stmt.setString(1, ((PessoaFisica)cliente).getCpf());
                stmt.setInt(2, cliente.getId());
                stmt.execute();
            } else {
                stmt = connection.prepareStatement(sqlPJ);
                stmt.setString(1, ((PessoaJuridica)cliente).getCnpj());
                stmt.setString(2, ((PessoaJuridica)cliente).getInscricaoEstadual());
                stmt.setInt(3, cliente.getId());
                stmt.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o registro do cliente no banco de dados!", ex);
        }
    }

    public boolean remover(Cliente cliente)throws DAOException {
        String sql = "DELETE FROM cliente WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi excluir o registro do cliente no banco de dados!", ex);
        }
    }

    public List<Cliente> listar() throws DAOException {
        String sql = "SELECT * FROM cliente c "
                        + "LEFT JOIN pessoa_fisica pf on pf.id_cliente = c.id "
                        + "LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id;";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = populateVO(resultado);
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem dos clientes do banco de dados!", ex);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) throws DAOException {
        String sql = "SELECT * FROM cliente c "
                        + "LEFT JOIN pessoa_fisica pf on pf.id_cliente = c.id "
                        + "LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id WHERE id=?";
        Cliente retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a pesquisa do cliente no banco de dados!", ex);
        }
        return retorno;
    }
    
    private Cliente populateVO(ResultSet rs) throws SQLException {
        Cliente cliente;      
        if (rs.getString("cnpj") == null || rs.getString("cnpj").length() <= 0) 
        {
            //é um cliente PessoaFisica
            cliente =  new PessoaFisica();
            ((PessoaFisica)cliente).setCpf(rs.getString("cpf"));
        }
        else
        {
           //é um cliente PessoaJuridica
           cliente =  new PessoaJuridica();
           ((PessoaJuridica)cliente).setCnpj(rs.getString("cnpj"));
           ((PessoaJuridica)cliente).setInscricaoEstadual(rs.getString("inscricaoEstadual"));
        }
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCelular(rs.getString("celular"));
        cliente.setEndereco(rs.getString("endereco"));
        cliente.setEmail(rs.getString("email"));
        cliente.setDataCadastro(rs.getDate("dataCadastro").toLocalDate());
        cliente.setDataNascimento(rs.getDate("dataNascimento").toLocalDate());
        return cliente;
    }
    
    public int getClienteAutoID(Cliente cliente) throws DAOException{
        
        String sql1= "SELECT max(id) as id FROM cliente";
        int id = 0;
        try {         
            PreparedStatement stmt = connection.prepareStatement(sql1);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
            id = resultado.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível recuperar o id do cliente no banco de dados!", ex);
        }
       
        return id;
    }
    
}