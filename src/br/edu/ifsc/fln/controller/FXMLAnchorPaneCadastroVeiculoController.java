/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {

    @FXML
    private TableView<Veiculo> tableView;

    @FXML
    private TableColumn<Veiculo, String> tableColumnPlaca;

    
    @FXML
    private Label lbVeiculoId;
    
    @FXML
    private Label lbVeiculoPlaca;
    
    @FXML
    private Label lbVeiculoObservacoes;
    
    @FXML
    private Label lbVeiculoModelo;
    
    @FXML
    private Label lbVeiculoCor;
    
    @FXML
    private Label lbVeiculoCliente;


    @FXML
    private Button btInserir;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btRemover;

    private List<Veiculo> listaVeiculos;
    private ObservableList<Veiculo> observableListVeiculos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);

        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));

    }

    public void carregarTableView() {
        tableColumnPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        
        try {
            listaVeiculos = veiculoDAO.listar();
        } catch (DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        
        observableListVeiculos = FXCollections.observableArrayList(listaVeiculos);
        tableView.setItems(observableListVeiculos);
    }
    
    public void selecionarItemTableView(Veiculo veiculo) {
        if (veiculo != null) {
            lbVeiculoId.setText(Integer.toString(veiculo.getId()));
            lbVeiculoPlaca.setText(veiculo.getPlaca());
            lbVeiculoObservacoes.setText(veiculo.getObservacoes());
            lbVeiculoModelo.setText(veiculo.getModelo().getDescricao());
            lbVeiculoCor.setText(veiculo.getCor().getNome());
            lbVeiculoCliente.setText(veiculo.getCliente().getNome());
        } else {
            lbVeiculoId.setText("");
            lbVeiculoPlaca.setText("");
            lbVeiculoObservacoes.setText("");
            lbVeiculoModelo.setText("");
            lbVeiculoCor.setText("");
            lbVeiculoCliente.setText("");
        }
    }
    

    @FXML
    public void handleBtInserir() throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosVeiculosDialog(veiculo);
        if (buttonConfirmarClicked) {
            try {
                veiculoDAO.inserir(veiculo);
            } catch (DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableView();
        }
    }
    
    @FXML
    public void handleBtAlterar() throws IOException {
        Veiculo veiculo = tableView.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosVeiculosDialog(veiculo);
            if (buttonConfirmarClicked) {
                try {
                    veiculoDAO.alterar(veiculo);
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um veiculo na Tabela.");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtRemover() throws IOException {
        Veiculo veiculo = tableView.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Excluir Veículo");
            alert.setContentText("Deseja realmente excluir esse veículo ?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                try {
                    veiculoDAO.remover(veiculo);
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um veiculo na Tabela.");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosVeiculosDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoDialogController.class.getResource( 
            "../view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de veiculos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o produto ao controller
        FXMLAnchorPaneCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }


}
