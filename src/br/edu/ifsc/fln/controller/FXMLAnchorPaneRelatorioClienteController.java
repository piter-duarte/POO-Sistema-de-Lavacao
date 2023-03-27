/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneRelatorioClienteController implements Initializable {

  @FXML
    private Button buttonImprimir;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteCelular;

    @FXML
    private TableColumn<Cliente, LocalDate> tableColumnClienteDataCadastro;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteEmail;

    @FXML
    private TableColumn<Cliente, Integer> tableColumnClienteID;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;

    @FXML
    private TableView<Cliente> tableView;
    
    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        carregarTableView();
    }    
    
    private void carregarTableView() {
        try {
            listaClientes = clienteDAO.listar();
        } catch (DAOException ex) {
            Logger.getLogger(FXMLAnchorPaneRelatorioClienteController.class.getName()).log(Level.SEVERE, null, ex);
            AlertDialog.exceptionMessage(ex);
            return;
        }
        
        tableColumnClienteID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnClienteCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        tableColumnClienteEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnClienteDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));
        observableListClientes = FXCollections.observableArrayList(listaClientes);
        tableView.setItems(observableListClientes);
    }
    
    //@FXML
    public void handleImprimir() throws JRException {
        URL url = getClass().getResource("../report/PrjSistemaLavacaoRelCliente.jasper");
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(url);
        
        //null: caso não existam filtros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
        
        //false: não deixa fechar a aplicação principal
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);  
    }
    
}
