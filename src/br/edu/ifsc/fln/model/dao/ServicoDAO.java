package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ECategoriaS;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Servico servico) throws DAOException {
        String sql = "INSERT INTO servico(descricao, valor, pontos, categoria) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValor());
            stmt.setInt(3, servico.getPontos());
            stmt.setString(4, servico.getCategoria().name());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível salvar o registro do serviço no banco de dados!", ex);
        }
    }

    public void alterar(Servico servico) throws DAOException {
        String sql = "UPDATE servico SET descricao=?, valor=?, pontos=?, categoria=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getValor());
            stmt.setInt(3, servico.getPontos());
            stmt.setString(4, servico.getCategoria().name());
            stmt.setInt(5, servico.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o registro do serviço no banco de dados!", ex);
        }
    }

    public void remover(Servico servico) throws DAOException {
        String sql = "DELETE FROM servico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi excluir o registro do serviço no banco de dados!", ex);
        }
    }

    public List<Servico> listar() throws DAOException {
        String sql = "SELECT * FROM servico";
        List<Servico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Servico servico = new Servico();
                servico.setId(resultado.getInt("id"));
                servico.setDescricao(resultado.getString("descricao"));
                servico.setValor(resultado.getDouble("valor"));
                servico.setCategoria(Enum.valueOf(ECategoriaS.class, resultado.getString("categoria")));
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem dos serviços do banco de dados!", ex);
        }
        return retorno;
    }

    public Servico buscar(Servico servico) throws DAOException {
        String sql = "SELECT * FROM servico WHERE id=?";
        Servico retorno = new Servico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                servico.setId(resultado.getInt("id"));
                servico.setDescricao(resultado.getString("descricao"));
                servico.setValor(resultado.getDouble("valor"));
                servico.setCategoria(Enum.valueOf(ECategoriaS.class, resultado.getString("categoria")));
                retorno = servico;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a pesquisa do serviço no banco de dados!", ex);
        }
        return retorno;
    }
    
     public List<Servico> listarPorCategoria(Veiculo veiculo) throws DAOException {
        String sql = "SELECT * FROM servico WHERE categoria = ?";
        List<Servico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getModelo().getCategoria().name());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Servico servico = new Servico();
                servico.setId(resultado.getInt("id"));
                servico.setDescricao(resultado.getString("descricao"));
                servico.setValor(resultado.getDouble("valor"));
                servico.setCategoria(Enum.valueOf(ECategoriaS.class, resultado.getString("categoria")));
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível realizar a listagem dos serviços do banco de dados!", ex);
        }
        return retorno;
    }
}
