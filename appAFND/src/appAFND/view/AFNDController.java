/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

import appAFND.controller.AlphabetController;
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
import appAFND.controller.StateController;
import appAFND.model.Alphabet;
import appAFND.model.Automaton;
import appAFND.model.NFA;
import appAFND.model.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
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
    private SplitPane splitPane;
    
    private SpreadsheetView spreadSheet;
    
    private Group group = new Group();
    private Rectangle canvas = new Rectangle(0, 0, 0, 0);
    private String buttonPressed;
    private int canvasWidth;
    private int canvasHeight;
    @FXML
    private TabPane tabPane;
    
    private Automaton f;
    @FXML
    private TextField wordField;
    @FXML
    private Button buttonExecute;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.askForAlphabet();
        this.canvasWidth = 1200;
        this.canvasHeight = 800;
        
        this.canvas.setHeight(canvasHeight);
        this.canvas.setWidth(this.canvasWidth);
        this.canvas.setFill(Color.WHITE);
        this.group.getChildren().add(this.canvas);
        this.scrollPane.setContent(this.group);
        
        //this.f = new NFA();
        
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
        
        this.updateTable();
        
        
        this.splitPane.getItems().addAll(this.spreadSheet);
        this.splitPane.setDividerPositions(0.99);
        
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

    public void setF(Automaton f)
    {
        this.f = f;
    }
    
    private void updateTable()
    {
        
        this.f.addState(new StateController(new State(true, "Q1", true), new StateView()));

        int rowCount = this.f.getStates().size() + 1;
        int colCount = this.f.getAlphabet().alphabetSize() + 1;
        
        GridBase grid = new GridBase(rowCount, colCount);
        
        ArrayList<ObservableList<SpreadsheetCell>> rows = new ArrayList<>(grid.getRowCount());
        
        HashMap<StateController, HashMap<String, ArrayList<StateController>>> transitions = this.f.getF();
        
        ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
        SpreadsheetCell cell00 = SpreadsheetCellType.STRING.createCell(0, 0, 0, 0, "Q/\u03A3");
        cell00.setStyle("-fx-background-color: #8FE6F3");
        list.add(cell00);
        
        for(int j = 0; j < this.f.getAlphabet().alphabetSize(); j++)
        {
            SpreadsheetCell alphCell = SpreadsheetCellType.STRING.createCell(0, j + 1, 0, 0, this.f.getAlphabet().getCharacter(j).toString());
            list.add(alphCell);
        }
        
        rows.add(list);
        
        list = FXCollections.observableArrayList();
        
        for (int i = 0; i < this.f.getStates().size(); i++)
        {
            StateController state = this.f.getStates().get(i);
            SpreadsheetCell stateCell = SpreadsheetCellType.STRING.createCell(i + 1, 0, 0, 0, state.getStateLabel());
            list.add(stateCell);
            HashMap<String, ArrayList<StateController>> stateTransitions = transitions.get(state);
            for (int j = 0; j < this.f.getAlphabet().alphabetSize(); j++)
            {
                ArrayList<StateController> to = stateTransitions.get(this.f.getAlphabet().getCharacter(j).toString());
                String label = "";
                if(to != null)
                {
                    for (int k = 0; k < to.size(); k++)
                    {
                        label += to.get(k).getStateLabel();
                        if(!(k < to.size() - 1))
                            label += ", ";
                    }
                }
                else {
                    label += " ";
                }
                SpreadsheetCell transCell = SpreadsheetCellType.STRING.createCell(i+ 1, j + 1, 0, 0, label);
                list.add(transCell);
            }
        }
        rows.add(list);
        
        grid.setRows(rows);
        this.spreadSheet = new SpreadsheetView(grid);
        this.spreadSheet.setEditable(false);
        this.spreadSheet.setShowColumnHeader(false);
        this.spreadSheet.setShowRowHeader(false);
        
    }
    
    private void askForAlphabet()
    {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Input alphabet");
        dialog.setHeaderText("Please, insert symbols for alphabet (separated by semicolon ;)");
        dialog.setContentText("Symbols:");
        
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent())
        {
            String[] alphabet = result.get().split(";");
            char[] chars = new char[alphabet.length];
            Alphabet alph = new Alphabet();
            AlphabetView alphView = new AlphabetView();
            AlphabetController alphController = new AlphabetController(alph, alphView);
            try
            {
                for(String str : alphabet)
                {
                    alphController.addCharacter(str.charAt(0));
                }
                this.f = new NFA(new ArrayList<>(), alphController, new ArrayList<>(), null);
            }
            catch(Exception ex)
            {
                System.exit(0);
            }
        }
        else
        {
            System.exit(0);
        }
    }
}
