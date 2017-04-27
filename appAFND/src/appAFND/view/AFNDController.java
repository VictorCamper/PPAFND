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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import appAFND.model.Node;
import appAFND.controller.NodeController;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author kirit
 */
public class AFNDController implements Initializable
{

    @FXML
    private ScrollPane scrollPane;

    private int radius;
    private ArrayList<NodeController> nodes;
    private ArrayList<TransitionView> transitions;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuFile;
    @FXML
    private Menu menuEdit;
    @FXML
    private Menu menuOptions;
    @FXML
    private Menu menuHelp;
    @FXML
    private Button buttonMove;
    @FXML
    private Button buttonState;
    @FXML
    private Button buttonTransition;
    @FXML
    private Button buttonUndo;
    @FXML
    private Button buttonRedo;
    @FXML
    private ComboBox<?> comboZoom;
    @FXML
    private TableView<?> tableView;
    
    private Group group = new Group();
    private Rectangle canvas = new Rectangle(0, 0, 0, 0);
    private String buttonPressed;
    private int canvasWidth;
    private int canvasHeight;
    @FXML
    private TabPane tabPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.canvasWidth = 1000;
        this.canvasHeight = 800;
        
        this.canvas.setHeight(canvasHeight);
        this.canvas.setWidth(this.canvasWidth);
        this.canvas.setFill(Color.WHITE);
        this.group.getChildren().add(this.canvas);
        this.scrollPane.setContent(this.group);
        
        
        
        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                canvasClick(event);
            }
        });
        
        this.canvas.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                canvasDrag(event);
            }
        });
        
        this.radius = 25;
        this.nodes = new ArrayList<>();
        this.transitions = new ArrayList<>();
    }

    @FXML
    private void zoom(ActionEvent event) {
    }

    @FXML
    private void selectEdit(ActionEvent event) {
        buttonMove.setCursor(Cursor.OPEN_HAND);
        buttonPressed = "Edit";
        event.consume();
    }

    @FXML
    private void selectState(ActionEvent event) {
        buttonPressed = "State";
        event.consume();
    }
    
    @FXML
    private void selectTransition(ActionEvent event) {
        buttonPressed = "Transition";
        event.consume();
    }

    @FXML
    private void undo(ActionEvent event) {
    }

    @FXML
    private void redo(ActionEvent event) {
    }

  
    private void canvasDrag(MouseEvent event) {
    }

    private void canvasClick(MouseEvent event) {
        double x = event.getX();                
        if(x < 25){
            x = 25;
        }
        else if (x > this.canvasWidth-25){
            x = this.canvasWidth-25;
        }

        double y = event.getY();
        if(y < 25){
            y = 25;
        }
        else if (y > this.canvasHeight-25){
            y = this.canvasHeight-25;
        }
        
        switch(buttonPressed){
            case "Edit":
                //Right click -> Context menu (change label, set final)
                //Drag node with transitions
                break;
                
            case "State":
                Node node = new Node(x, y, this.radius, this.nodes.size());
                NodeView nodeView = new NodeView(x, y, this.radius, this.nodes.size());
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
                    nodeController.drawNode(this.group);

                    
                    if(this.nodes.size() == 0)
                    {
                    // ADD INITIAL NODE
                    }
                    //ADD NODES TO NFA
                    
                    this.nodes.add(nodeController);
                    
                    WritableImage image = group.snapshot(new SnapshotParameters(), null);

                    // TODO: probably use a file chooser here
                    File file = new File("canvas.png");

                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    } catch (IOException e) {
                        // TODO: handle exception here
                    }
                }
                break;
                
            case "Transition":
                TransitionView transition = new TransitionView(x, y, x, y, this.transitions.size());
                group.getChildren().add(transition.getTransition());
                transitions.add(transition);
                
                              
                    
                
                
                
                break;
            default:
                break;
                
        
        }
        
        
    }
}
