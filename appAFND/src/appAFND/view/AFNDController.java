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
    private ComboBox<?> combo_zoomLevel;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Canvas canvas;
    
    
    private int radius;
    private ArrayList<Node> nodes;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.radius = 25;
        this.nodes = new ArrayList<>();
    }    

    @FXML
    private void drawInitial(ActionEvent event) {
    }

    @FXML
    private void drawState(MouseEvent event) {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        Node node = new Node(event.getX() - this.radius, event.getY() - this.radius);
        
        boolean overlapped = false;
        
        for(Node item : this.nodes)
            if (item.compareTo(node) == 0)
                overlapped = true;
            
        if(!overlapped)
        {
            g.strokeOval(event.getX() - this.radius, event.getY() - this.radius, this.radius*2, this.radius*2);
            g.fillText("Q" + this.nodes.size(), event.getX(), event.getY());
            /*
            if(this.nodes.size() == 0)
            {
            // ADD INITIAL NODE
            }
            ADD NODES TO NFA
            */
            this.nodes.add(node);
        }
    }

    @FXML
    private void drawFinal(ActionEvent event) {
    }

    @FXML
    private void drawTransition(ActionEvent event) {
    }

    @FXML
    private void zoom(ActionEvent event) {
    }
    
    class Node implements Comparable<Node>
    {
        private double x;
        private double y;

        public Node(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
        
        @Override
        public int compareTo(Node o) {
            
            double distance = Math.sqrt(Math.pow(this.x - o.getX(),2) + Math.pow(this.y - o.getY(), 2));
                        
            if(distance <= (radius*2)) // Radius from outer class
                return 0;
            return 1;
        }
    }
    
}
