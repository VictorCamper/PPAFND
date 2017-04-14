/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

import appAFND.controller.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;

/**
 * FXML Controller class
 *
 * @author kirit
 */
public class AFNDController implements Initializable {

    @FXML
    private Button button_initial;
    @FXML
    private Button button_state;
    @FXML
    private Button button_final;
    @FXML
    private Button button_transition;
    @FXML
    private Button button_undo;
    @FXML
    private Button button_redo;
    @FXML
    private ComboBox<?> combo_zoomLevel;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Canvas canvas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void drawInitial(ActionEvent event) {
    }

    @FXML
    private void drawState(ActionEvent event) {
    }

    @FXML
    private void drawFinal(ActionEvent event) {
    }

    @FXML
    private void drawTransition(ActionEvent event) {
    }

    @FXML
    private void undo(ActionEvent event) {
    }

    @FXML
    private void redo(ActionEvent event) {
    }

    @FXML
    private void zoom(ActionEvent event) {
    }
    
}
