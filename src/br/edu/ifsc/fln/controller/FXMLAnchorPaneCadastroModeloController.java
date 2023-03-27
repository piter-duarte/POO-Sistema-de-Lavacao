/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.MotorDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
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
public class FXMLAnchorPaneCadastroModeloController implements Initializable {

    @FXML
    private TableView<Modelo> tableView;

    @FXML
    private TableColumn<Modelo, String> tableColumnNome;

    @FXML
    private Label lbModeloId;

    @FXML
    private Label lbModeloDescricao;

    @FXML
    private Label lbModeloMarca;
    
    @FXML
    private Label lbModeloCategoria;
    
    @FXML
    private Label lbModeloMotorPotencia;
    
    @FXML
    private Label lbModeloMotorCombustivel;

    @FXML
    private Button btInserir;

    @FXML
    private Button btAlterar;

    @FXML
    private Button btRemover;

    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final MotorDAO motorDAO = new MotorDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        motorDAO.setConnection(connection);

        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableView() {
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        try {
            listaModelos = modeloDAO.listar();
        } catch (DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        
        observableListModelos = FXCollections.observableArrayList(listaModelos);
        tableView.setItems(observableListModelos);
    }
    
    public void selecionarItemTableView(Modelo modelo) {
        if (modelo != null) {
            lbModeloId.setText(Integer.toString(modelo.getId()));
            lbModeloDescricao.setText(modelo.getDescricao());
            lbModeloMarca.setText(modelo.getMarca().getNome());
            lbModeloCategoria.setText(modelo.getCategoria().name());
            lbModeloMotorPotencia.setText(Integer.toString(modelo.getMotor().getPotencia()));
            lbModeloMotorCombustivel.setText(modelo.getMotor().getTipoCombustivel().name());
        } else {
            lbModeloId.setText("");
            lbModeloDescricao.setText("");
            lbModeloMarca.setText("");
            lbModeloCategoria.setText("");
            lbModeloMotorPotencia.setText("");
            lbModeloMotorCombustivel.setText("");
        }
    }
    

    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo();
        Motor motor = new Motor();
        modelo.setMotor(motor);
        motor.setModelo(modelo);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosModelosDialog(modelo);
        if (buttonConfirmarClicked) {
            try {
                modeloDAO.inserir(modelo);
            } catch (DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            try {
                modelo.getMotor().getModelo().setId(modeloDAO.getModeloAutoID(modelo));
            } catch (DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            try {
                motorDAO.inserir(modelo.getMotor());
            } catch (DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableView();
        }
    }
    
    @FXML
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableView.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            modelo.getMotor().setModelo(modelo);
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosModelosDialog(modelo);
            if (buttonConfirmarClicked) {
                try {
                    modeloDAO.alterar(modelo);
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                try {
                    motorDAO.alterar(modelo.getMotor());
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um modelo na Tabela.");
            alert.show();
        }
    }
    
    @FXML
    public void handleBtRemover() throws IOException {
        Modelo modelo = tableView.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Excluir Modelo");
            alert.setContentText("Deseja realmente excluir esse modelo ?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK)
            {
                try {
                    modeloDAO.remover(modelo);
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                try {
                    motorDAO.remover(modelo.getMotor());
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um modelo na Tabela.");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosModelosDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloDialogController.class.getResource( 
            "../view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de modelos");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //Setando o produto ao controller
        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmarClicked();
    }


}
