/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import appAFND.model.Node;
import appAFND.controller.NodeController;

/**
 * FXML Controller class
 *
 * @author kirit
 */
public class AFNDController implements Initializable
{

    @FXML
    private Button button_initial;
    @FXML
    private Button button_state;
    @FXML
    private Button button_final;
    @FXML
    private Button button_transition;
    @FXML
    private ComboBox<?> combo_zoomLevel;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Canvas canvas;

    private int radius;
    private ArrayList<NodeController> nodes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.radius = 25;
        this.nodes = new ArrayList<>();
    }

    @FXML
    private void drawInitial(ActionEvent event)
    {
    }

    @FXML
    private void drawState(MouseEvent event)
    {
        GraphicsContext g = this.canvas.getGraphicsContext2D();

        Node node = new Node(event.getX() - this.radius, event.getY() - this.radius, this.radius, this.nodes.size());
        NodeView nodeView = new NodeView(this.canvas.getGraphicsContext2D());
        NodeController nodeController = new NodeController(node, nodeView);

        boolean overlapped = false;

        for (NodeController item : this.nodes)
        {
            if (item.compareTo(nodeController) == 0)
            {
                overlapped = true;
            }
        }

        if (!overlapped)
        {
            nodeController.showNode();

            /*
            if(this.nodes.size() == 0)
            {
            // ADD INITIAL NODE
            }
            ADD NODES TO NFA
             */
            this.nodes.add(nodeController);
        }
    }

    @FXML
    private void drawFinal(ActionEvent event)
    {
    }

    @FXML
    private void drawTransition(ActionEvent event)
    {
    }

    @FXML
    private void zoom(ActionEvent event)
    {
    }

    @FXML
    private void drawState(ActionEvent event)
    {
    }
}
