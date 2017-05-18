/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

import appAFND.controller.AlphabetController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import appAFND.controller.StateController;
import appAFND.model.Alphabet;
import appAFND.model.Automaton;
import appAFND.model.NFA;
import appAFND.model.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import jdk.nashorn.internal.ir.CallNode;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.controlsfx.control.spreadsheet.SpreadsheetColumn;


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
    private ArrayList<StateController> states;
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
    
    private int transitionClickCounter=0;
    private Circle transitionC1;
    private Circle transitionC2;
    
    @FXML
    private TabPane tabPane;
    
    private Automaton automaton;
    @FXML
    private TextField wordField;
    @FXML
    private Button buttonExecute;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dialogInitial();
        
        this.transitionClickCounter = 0;
        
        this.canvasWidth = 1200;
        this.canvasHeight = 800;
        
        this.canvas.setHeight(canvasHeight);
        this.canvas.setWidth(this.canvasWidth);
        this.canvas.setFill(Color.WHITE);
        this.group.getChildren().add(this.canvas);
        this.scrollPane.setContent(this.group);
        
        //this.automaton = new NFA();
        
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
        this.states = new ArrayList<>();
        this.transitions = new ArrayList<>();
        
        this.updateTable();
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
        this.transitionClickCounter = 0;
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
        if(x < this.radius){
            x = this.radius;
        }
        else if (x > this.canvasWidth-this.radius-2){
            x = this.canvasWidth-this.radius-2;
        }

        double y = event.getY();
        if(y < this.radius){
            y = this.radius;
        }
        else if (y > this.canvasHeight-this.radius-2){
            y = this.canvasHeight-this.radius-2;
        }
        
        switch(buttonPressed){
            case "Edit":
                //Right click -> Context menu (change label, set final)
                //Drag node with transitions
                break;
                
            case "State":
                String name = dialogState();
                State state = new State(false, name, false);
                StateView stateView = new StateView(this, x, y, this.radius, name);
                StateController nodeController = new StateController(state, stateView);

                boolean overlapped = false;

                for (StateController item : this.states)
                {
                    if (item.compareTo(nodeController) == 0)
                    {
                        overlapped = true;
                    }
                }

                if (!overlapped) {
                    //Create dialog to set the name of the state
                    
                    //Draw node
                    group.getChildren().add(nodeController.getStateView().getCircle());
                    group.getChildren().add(nodeController.getStateView().getText());
                    
                    if (this.states.size() == 0) {
                        state.setInitial();
                        Polygon arrow = new Polygon(new double[]{0, 0, 10, 10, -10, 10});
                        Circle circleNode = nodeController.getStateView().getCircle();
                        arrow.setTranslateX(x - (circleNode.getRadius()+(circleNode.getStrokeWidth()/2)));
                        arrow.setTranslateY(y);
                        arrow.getTransforms().add(new Rotate(90, 0, 0));
                        arrow.setFill(Color.YELLOWGREEN);
                        this.group.getChildren().add(arrow);
                    } //ADD NODES TO NFA 
                    this.states.add(nodeController);
                    /*WritableImage image = group.snapshot(new SnapshotParameters(), null);
                    // TODO: probably use a file chooser here
                    File file = new File("canvas.png");
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    } catch (IOException e) {
                        // TODO: handle exception here 
                    } 
                        Mat image2 = imread(file.getAbsolutePath(), IMREAD_COLOR);*/
                        //FastFeatureDetector ffd = FastFeatureDetector.create(25/* threshold for detection */, true /* non-max suppression */, FastFeatureDetector.TYPE_7_12);
                        /*KeyPointVector keyPoints = new KeyPointVector();
                        ffd.detect(image2, keyPoints);
                        
                        //Muestra una imagen en una ventana nueva con los vertices detectados 
                        Mat c = new Mat(); drawKeypoints(image2, keyPoints, c, new Scalar(0, 0, 255, 0), DrawMatchesFlags.DEFAULT); 
                        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat(); 
                        CanvasFrame canvas = new CanvasFrame("hola", 1); canvas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
                        canvas.showImage(converter.convert(c));
                        
                        //Cantidad de vertices detectados 
                        System.out.println(keyPoints.size());*/
                    }
                break;
            case "Transition":
                break;
            default:
                break;
                
        
        }  
    }

    public void setAutomaton(Automaton automaton)
    {
        this.automaton = automaton;
    }
    
    private void updateTable()
    {
        double divider  = 0;
        this.splitPane.setDividerPosition(0, 0.85f);
        if(this.splitPane.getDividerPositions().length != 0)
            divider = this.splitPane.getDividerPositions()[0];
        else 
            divider = 0.85f;
        this.splitPane.getItems().remove(this.spreadSheet);
        //this.splitPane.setDividerPositions(0.85f);
        
        int rowCount = this.automaton.getStates().size() + 1;
        int colCount = this.automaton.getAlphabet().alphabetSize() + 1;
        
        GridBase grid = new GridBase(rowCount, colCount);
        ArrayList<ObservableList<SpreadsheetCell>> rows = new ArrayList<>(grid.getRowCount());
        
        HashMap<StateController, HashMap<String, ArrayList<StateController>>> transitions = this.automaton.getF();
        
        ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
        SpreadsheetCell cell00 = SpreadsheetCellType.STRING.createCell(0, 0, 1, 1, "Q/\u03A3");
        cell00.setStyle("-fx-background-color: #00BFFF");
        list.add(cell00);
        
        for(int j = 0; j < this.automaton.getAlphabet().alphabetSize(); j++)
        {
            SpreadsheetCell alphCell = SpreadsheetCellType.STRING.createCell(0, j + 1, 1, 1, this.automaton.getAlphabet().getCharacter(j).toString());
            alphCell.setEditable(false);
            alphCell.setStyle("-fx-background-color: #8ADFFC");
            list.add(alphCell);
        }
        
        rows.add(list);
        
        
        for (int i = 0; i < this.automaton.getStates().size(); i++)
        {
            ObservableList<SpreadsheetCell> list2 = FXCollections.observableArrayList();

            StateController state = this.automaton.getStates().get(i);
            SpreadsheetCell stateCell = SpreadsheetCellType.STRING.createCell(i + 1, 0, 1, 1, state.getStateLabel());
            stateCell.setEditable(false);
            if(state.isStateInitial())
                stateCell.setStyle("-fx-background-color: #9ACD32");
            else 
                stateCell.setStyle("-fx-background-color: #8ADFFC");
            list2.add(stateCell);
            HashMap<String, ArrayList<StateController>> stateTransitions = transitions.get(state);
            for (int j = 0; j < this.automaton.getAlphabet().alphabetSize(); j++)
            {
                ArrayList<StateController> to = stateTransitions.get(this.automaton.getAlphabet().getCharacter(j).toString());
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
                transCell.setEditable(false);
                transCell.setStyle("-fx-background-color: #ffffff");
                list2.add(transCell);
            }
            rows.add(list2);

        }
        
        grid.setRows(rows);
        this.spreadSheet = new SpreadsheetView(grid);
        this.spreadSheet.setEditable(false);
        this.spreadSheet.setShowColumnHeader(false);
        this.spreadSheet.setShowRowHeader(false);
        this.spreadSheet.setContextMenu(new ContextMenu());
        
        //this.spreadSheet.setDisable(false);
        ObservableList<SpreadsheetColumn> columns = this.spreadSheet.getColumns();
        if(columns.get(0).isColumnFixable())
            this.spreadSheet.getFixedColumns().addAll(columns.get(0));
        if(this.spreadSheet.isRowFixable(0))
            this.spreadSheet.getFixedRows().add(new Integer(0));
        
        this.splitPane.setDividerPositions(divider);
        this.splitPane.getItems().add(this.spreadSheet);
        //this.splitPane.setDividerPositions(0.95);    
    }
    
    private void dialogInitial()
    {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Input alphabet");
        dialog.setHeaderText("Please, insert symbols for alphabet (separated by comma ,)");
        dialog.setContentText("Symbols:");
        
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent())
        {            
            String[] alphabet = result.get().replaceAll("\\s","").split(",");
            char[] chars = new char[alphabet.length];
            Alphabet alph = new Alphabet();
            AlphabetView alphView = new AlphabetView();
            AlphabetController alphController = new AlphabetController(alph, alphView);
            try
            {
                for(String str : alphabet)
                {
                    if(!alphController.alphabetContains(str.charAt(0)))
                        alphController.addCharacter(str.charAt(0));
                }
                this.automaton = new NFA(new ArrayList<>(), alphController, new ArrayList<>(), null);
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
    
    void stateClicked(StateView view) {
        if (this.buttonPressed.equals("Transition")){
            this.transitionClickCounter++;            
            if (this.transitionClickCounter == 1){
                this.transitionC1 = view.getCircle();
            }
            else if (this.transitionClickCounter == 2){
                this.transitionC2 = view.getCircle();
                this.transitionClickCounter = 0;
                //Request label through a window
                String[] chars = dialogTransition();
                String label = new String();
                for (String c : chars){

                    if (automaton.getAlphabet().alphabetContains(c.charAt(0))){
                        if(label.isEmpty())
                            label = label.concat(((Character)c.charAt(0)).toString());
                        else
                            if (!label.contains(((Character)c.charAt(0)).toString()))
                                label = label.concat(", ").concat(((Character)c.charAt(0)).toString());                            
                    }
                }

                TransitionView transition = new TransitionView(this.transitionC1, this.transitionC2, label, this.canvasHeight, this.canvasWidth);
                group.getChildren().add(transition.getTransition());
                transitions.add(transition);
            }

        }
    }

    private String dialogState() {
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New state");
        dialog.setHeaderText("Introduce the name of the new state");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Create", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField state = new TextField();
        state.setPromptText("Name of the state");
        state.lengthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (state.getText().length() >= 8) {
                        // if it's 11th character then just setText to previous
                        // one
                        state.setText(state.getText().substring(0, 8));
                    }
                }
            }
        });

        grid.add(new Label("Name:"), 0, 0);
        grid.add(state, 1, 0);

        // Enable/Disable login button depending on whether a username was entered.
        javafx.scene.Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        state.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> state.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return state.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        return state.getText();

        /*result.ifPresent(usernamePassword -> {
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });*/
    }

    private String[] dialogTransition() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New transition");
        dialog.setHeaderText("Introduce the characters for the transition (separated by comma ,)");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Create", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField characters = new TextField();
        characters.setPromptText("Characters");
        CheckBox voidChar = new CheckBox();

        grid.add(new Label("Characters:"), 0, 0);
        grid.add(characters, 1, 0);
        grid.add(new Label("Void transition:"), 0, 1);
        grid.add(voidChar, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        javafx.scene.Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        characters.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());            
        });
        
        voidChar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!newValue);          
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> characters.requestFocus());


        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        if(voidChar.isSelected()){
            String[] chars = ("\u03BB,".concat(characters.getText())).replaceAll("\\s","").split(",");
            return chars;
        }
        else{
            String[] chars = characters.getText().replaceAll("\\s","").split(",");   
            return chars;
        }
    }
}
