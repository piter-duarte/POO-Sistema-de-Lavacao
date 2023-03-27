package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Motor;
import br.edu.ifsc.fln.model.domain.Pontuacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PontuacaoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Pontuacao pontuacao) throws DAOException {
        
       String sql = "INSERT INTO pontuacao(id_cliente) VALUES(?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pontuacao.getCliente().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PontuacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível salvar o registro da pontuação do cliente no banco de dados!", ex);
        }
    }

    public void alterar(Pontuacao pontuacao) throws DAOException {
        String sql = "UPDATE pontuacao SET quantidade=? WHERE id_cliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pontuacao.getQuantidade());
            stmt.setInt(2, pontuacao.getCliente().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PontuacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o registro da pontuação do cliente no banco de dados!", ex);
        }
    }

    public void remover(Pontuacao pontuacao) throws DAOException {
        String sql = "DELETE FROM pontuacao WHERE id_cliente=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pontuacao.getCliente().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PontuacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi excluir o registro da pontuação do cliente no banco de dados!", ex);
        }
    }
    
    public List<Pontuacao> listar() throws DAOException {
        String sql = "SELECT * FROM pontuacao";
        List<Pontuacao> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Pontuacao pontuacao = new Pontuacao();
                pontuacao.getCliente().setId(resultado.getInt("id_cliente"));
                pontuacao.setQuantidade(resultado.getInt("quantidade"));
                retorno.add(pontuacao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarcaDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem das pontuações dos clientes do banco de dados!", ex);
        }
        return retorno;
    }
    
    public int buscarQuantidade(Pontuacao pontuacao) throws DAOException {
        String sql = "SELECT quantidade FROM pontuacao WHERE id_cliente = ?";
        Pontuacao retorno = new Pontuacao();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pontuacao.getCliente().getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.setQuantidade(resultado.getInt("quantidade"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a pesquisa da cor no banco de dados!", ex);
        }
        return retorno.getQuantidade();
    }
    
}
