/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private TextField tfPlaca;
    
    @FXML
    private TextField tfObservacoes;
    
    @FXML
    private ComboBox<Modelo> cbModelo;
    
    @FXML
    private ComboBox<Cor> cbCor;
    
    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private Button btConfirmar;

    @FXML
    private Button btCancelar;
    
//    private List<Categoria> listaCategorias;
//    private ObservableList<Categoria> observableListCategorias;
        
    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final CorDAO corDAO = new CorDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Veiculo veiculo; 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        corDAO.setConnection(connection);
        clienteDAO.setConnection(connection);
        
        carregarComboBoxModelos();
        carregarComboBoxCores();
        carregarComboBoxClientes();
        
        setFocusLostHandle();
    } 
    
    private void setFocusLostHandle() {
        tfPlaca.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (tfPlaca.getText() == null || tfPlaca.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfPlaca.requestFocus();
                }
            }
        });
    }
    
//This works fine too:    
//root.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//    focusState(newValue);
//});
//
//private void focusState(boolean value) {
//    if (value) {
//        System.out.println("Focus Gained");
//    }
//    else {
//        System.out.println("Focus Lost");
//    }
//} 
    
    private List<Modelo> listaModelos;
    private ObservableList<Modelo> observableListModelos; 
    
    public void carregarComboBoxModelos() {
        try {
            listaModelos = modeloDAO.listar();
        } catch (DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListModelos = 
                FXCollections.observableArrayList(listaModelos);
        cbModelo.setItems(observableListModelos);
    }   
    
    private List<Cor> listaCores;
    private ObservableList<Cor> observableListCores; 
    
    public void carregarComboBoxCores() {
        try {
            listaCores = corDAO.listar();
        } catch (DAOException ex) {
             AlertDialog.exceptionMessage(ex);
        }
        observableListCores = 
                FXCollections.observableArrayList(listaCores);
        cbCor.setItems(observableListCores);
    } 
    
    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes; 
    public void carregarComboBoxClientes() {
        try {
            listaClientes = clienteDAO.listar();
        } catch (DAOException ex) {
             AlertDialog.exceptionMessage(ex);
        }
        observableListClientes = 
                FXCollections.observableArrayList(listaClientes);
        cbCliente.setItems(observableListClientes);
    }
    
    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the produto
     */
    public Veiculo getVeiculo() {
        return veiculo;
    }

    /**
     * @param produto the produto to set
     */
    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        tfPlaca.setText(veiculo.getPlaca());
        tfObservacoes.setText(veiculo.getObservacoes());
        cbModelo.getSelectionModel().select(veiculo.getModelo());
        cbCor.getSelectionModel().select(veiculo.getCor());
        cbCliente.getSelectionModel().select(veiculo.getCliente());
    }    
    
    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacoes(tfObservacoes.getText());
            veiculo.setModelo(cbModelo.getSelectionModel().getSelectedItem());
            veiculo.setCor(cbCor.getSelectionModel().getSelectedItem());
            veiculo.setCliente(cbCliente.getSelectionModel().getSelectedItem());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }
    
        //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (tfPlaca.getText() == null || tfPlaca.getText().isEmpty()) {
            errorMessage += "Placa inválida!\n";
        }
        
        if (tfObservacoes.getText() == null || tfObservacoes.getText().isEmpty()) {
            errorMessage += "Observações inválidas!\n";
        }
        
        if (cbModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um modelo!\n";
        }
        
       if (cbCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma cor!\n";
        }
       
        if (cbCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um cliente!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
   
}
