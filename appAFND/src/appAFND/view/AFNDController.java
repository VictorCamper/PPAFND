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
import appAFND.controller.TransitionController;
import appAFND.model.Alphabet;
import appAFND.model.Automaton;
import appAFND.model.Dijkstra;
import appAFND.model.NFA;
import appAFND.model.State;
import appAFND.model.Transition;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;
import org.bytedeco.javacpp.opencv_features2d.FastFeatureDetector;
import static org.bytedeco.javacpp.opencv_features2d.drawKeypoints;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
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
    private ArrayList<StateController> statesList;
    private ArrayList<TransitionController> transitionsList;
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
    private Group groupStates = new Group();
    private Group groupTransitions = new Group();
    private Rectangle canvas = new Rectangle(0, 0, 0, 0);
    private String buttonPressed;
    private int canvasWidth;
    private int canvasHeight;
    
    private int transitionClickCounter;
    private StateController transitionS1;
    private StateController transitionS2;
    
    private FastFeatureDetector ffd;
    private KeyPointVector keyPoints;
    
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
        
        this.ffd = FastFeatureDetector.create(25/* threshold for detection */, true /* non-max suppression */, FastFeatureDetector.TYPE_7_12);
        this.keyPoints = new KeyPointVector();
        
        this.transitionClickCounter = 0;
        
        this.canvasWidth = 1200;
        this.canvasHeight = 800;
        
        this.canvas.setHeight(canvasHeight);
        this.canvas.setWidth(this.canvasWidth);
        this.canvas.setFill(Color.WHITE);
        
        this.group.getChildren().add(this.canvas);
        this.group.getChildren().add(this.groupStates);
        this.group.getChildren().add(this.groupTransitions);
        
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
        this.statesList = new ArrayList<>();
        this.transitionsList = new ArrayList<>();
        
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
        //Handle clicks in borders of the canvas
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
                //String name = dialogState();              // Modified by Victor
                String name = "Q" + this.automaton.getStates().size();
                //If the user write a name for the state, create the state, add it to the list of states,
                //draw it in the canvas, add it to the automaton and update table.
                if (!name.isEmpty()){        
                    //Create state
                    State state = new State(false, name, false);
                    StateView stateView = new StateView(this, x, y, this.radius, name);
                    StateController stateController = new StateController(state, stateView);
                    stateView.setController(stateController);
                    
                    boolean overlapped = false;
                    
                    
                    for (StateController s : this.statesList){
                        //Verify intersection
                        if(s.compareTo(stateController)==0){                        
                            overlapped = true;
                        }
                    }
                    //Draw state
                    if (!overlapped) {
                        //Create dialog to set the name of the state

                        //Draw node
                        stateController.getStateView().drawNode(groupStates);
                        
                        if (this.statesList.size() == 0) {                            
                            automaton.setInitialState(stateController);
                            Polygon arrow = new Polygon(new double[]{0, 0, 10, 10, -10, 10});
                            Circle circleNode = stateController.getStateView().getCircle();
                            arrow.setTranslateX(x - (circleNode.getRadius()+(circleNode.getStrokeWidth()/2)));
                            arrow.setTranslateY(y);
                            arrow.getTransforms().add(new Rotate(90, 0, 0));
                            arrow.setFill(Color.YELLOWGREEN);
                            this.groupStates.getChildren().add(arrow);
                        }
                        //Add state to the list of states
                        this.statesList.add(stateController);
                        //Add state to the automaton
                        this.automaton.addState(stateController);                        
                        //Update table
                        this.updateTable();                        
                    }
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
    
    public Automaton getAutomaton(){
        return this.automaton;
    }
    
    public void updateTable()
    {
        double divider  = 0;
        this.splitPane.setDividerPosition(0, 0.85f);
        if(this.splitPane.getDividerPositions().length != 0)
            divider = this.splitPane.getDividerPositions()[0];
        else 
            divider = 0.85f;
        this.splitPane.getItems().remove(this.spreadSheet);
        //this.splitPane.setDividerPositions(0.85f);
        
        int rowCount = this.automaton.getStates().size()+1;
        int colCount = this.automaton.getAlphabet().alphabetSize()+1;
        
        GridBase grid = new GridBase(rowCount, colCount);
        ArrayList<ObservableList<SpreadsheetCell>> rows = new ArrayList<>(grid.getRowCount());
        
        HashMap<StateController, HashMap<String, ArrayList<StateController>>> transitions = this.automaton.getF();
        
        ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();
        SpreadsheetCell cell00 = SpreadsheetCellType.STRING.createCell(0, 0, 1, 1, "Q/\u03A3");
        cell00.setStyle("-fx-background-color: #00BFFF; -fx-alignment: center;");
        list.add(cell00);
        
        for(int j = 0; j < this.automaton.getAlphabet().alphabetSize(); j++)
        {
            SpreadsheetCell alphCell = SpreadsheetCellType.STRING.createCell(0, j + 1, 1, 1, this.automaton.getAlphabet().getCharacter(j).toString());
            alphCell.setEditable(false);
            alphCell.setStyle("-fx-background-color: #8ADFFC; -fx-alignment: center;");
            
            list.add(alphCell);
        }
        
        rows.add(list);
        
        
        for (int i = 0; i < this.automaton.getStates().size(); ++i)
        {
            ObservableList<SpreadsheetCell> list2 = FXCollections.observableArrayList();

            StateController state = this.automaton.getStates().get(i);
            SpreadsheetCell stateCell = SpreadsheetCellType.STRING.createCell(i+1, 0, 1, 1, state.getStateLabel());
            stateCell.setEditable(false);
            if(state.equals(automaton.getInitialState()))
                stateCell.setStyle("-fx-background-color: #9ACD32");
            else if(automaton.getFinalStates().contains(state))
                stateCell.setStyle("-fx-background-color: #E67E22");
            else 
                stateCell.setStyle("-fx-background-color: #8ADFFC");
            list2.add(stateCell);
            HashMap<String, ArrayList<StateController>> stateTransitions = transitions.get(state);
            for (int j = 0; j < this.automaton.getAlphabet().alphabetSize(); ++j)
            {
                ArrayList<StateController> to = stateTransitions.get(this.automaton.getAlphabet().getCharacter(j).toString());
                String label = "";
                if(to != null)
                {
                    for (int k = 0; k < to.size(); k++)
                    {
                        if(k != 0)
                        {
                            label += ", ";
                            label += to.get(k).getStateLabel();
                        }
                        else
                        {
                            label += to.get(k).getStateLabel();
                        }
                    }
                }
                else { 
                    label += " ";
                }
                SpreadsheetCell transCell = SpreadsheetCellType.STRING.createCell(i+1, j+1, 1, 1, label);
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
    
    //Used for the creation of transitions
    void stateClicked(StateController statecontroller) {
        //Obtain the states (from and to), obtain the label for the transition,
        //add states to the transition model, create the transition view, 
        //create the transition controller,
        //draw the transition view, add the transition to the automaton
        //update table.
        if (this.buttonPressed.equals("Transition")){
            this.transitionClickCounter++;            
            if (this.transitionClickCounter == 1){
                //State from
                this.transitionS1 = statecontroller;
            }
            else if (this.transitionClickCounter == 2){
                //State to
                this.transitionS2 = statecontroller;
                //Request label through a window
                String[] chars = dialogTransition();
                if (!chars[0].isEmpty()){                   
                    
                    String label = new String();
                    for (String c : chars){
                        Character c2 = c.charAt(0);
                        String c2s = c2.toString();
                        if (automaton.getAlphabet().alphabetContains(c2)){
                            if(label.isEmpty()){                                
                                label = label.concat(c2s);
                                if(this.automaton instanceof NFA){
                                    ((NFA)this.automaton).addTransition(transitionS1, transitionS2, c2s);
                                }
                            }
                            else
                                if (!label.contains(c2s))
                                    label = label.concat(", ").concat(c2s);
                                    if(this.automaton instanceof NFA){
                                        ((NFA)this.automaton).addTransition(transitionS1, transitionS2, c2s);
                                    }
                        }
                    }
                    
                    Transition transitionmodel = new Transition(this.transitionS1, this.transitionS2);                    
                    TransitionView transitionview = new TransitionView(this.transitionS1, this.transitionS2, label, this.canvasHeight, this.canvasWidth);
                    TransitionController transitionController = new TransitionController(transitionmodel, transitionview);
                    
                    for(StateController s : statesList){
                        for(TransitionController t : transitionsList)
                            if(stateTransitionInstersection(s, t))
                                System.out.println("hola");
                    }
                    
                    groupTransitions.getChildren().add(transitionview.getTransition());
                    transitionsList.add(transitionController);
                    
                    this.updateTable();
                }                
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

    @FXML
    private boolean readWord(ActionEvent event)
    {
        String sp = null;
        try{
            Dijkstra d = new Dijkstra(automaton);
            d.sp();
            sp = d.getShortestWord();
        }
        catch(Exception e){
        }
        if(automaton.getInitialState()==null){
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Initial state error");
            result.setHeaderText("There is no initial state");
            result.showAndWait();
            return false;
        }
        
        else if(automaton.getFinalStates().contains(automaton.getInitialState())){
            Alert result = new Alert(Alert.AlertType.INFORMATION);
            result.setTitle("Word accepted");
            result.setHeaderText("The word was accepted!");
            result.showAndWait();
            return true;
        }
        else if(!(sp.isEmpty()))
        {
            return this.automaton.readWord(wordField.getText());
        }
        else
        {
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Viable path error");
            result.setHeaderText("There is no viable path");
            result.showAndWait();
            return false;
        }
        
    }

    @FXML
    private void shortestWord(ActionEvent event) {
        String sp = null;
        try{
            Dijkstra d = new Dijkstra(automaton);
            d.sp();
            sp = d.getShortestWord();
        }
        catch(Exception e){
        }
        if(automaton.getInitialState()==null){
            Alert shortest = new Alert(Alert.AlertType.ERROR);
            shortest.setTitle("Initial state error");
            shortest.setHeaderText("There is no initial state");
            shortest.showAndWait();
        }
        else if(automaton.getFinalStates().contains(automaton.getInitialState())){
            Alert shortest = new Alert(Alert.AlertType.INFORMATION);
            shortest.setTitle("Shotest path");
            shortest.setHeaderText("This automaton accepts any word");
            shortest.setContentText("The shortest path is: \"\" ");
            shortest.showAndWait();
        }
        else if(!(sp.isEmpty()))
        {
            Alert shortest = new Alert(Alert.AlertType.INFORMATION);
            shortest.setTitle("Shotest path");
            shortest.setHeaderText("The shortest path is:");
            shortest.setContentText(sp);
            shortest.showAndWait();
        }
        else
        {
            Alert shortest = new Alert(Alert.AlertType.ERROR);
            shortest.setTitle("Viable path error");
            shortest.setHeaderText("There is no viable path");
            shortest.showAndWait();
        }       
            
        //System.out.println(d.getShortestWord());
    }

    private boolean statesInstersection(StateController s, StateController stateController) {
        Circle c1 = new Circle();
        c1.setRadius(s.getStateView().getCircle().getRadius());
        c1.setStroke(Color.ALICEBLUE);
        c1.setStrokeWidth(s.getStateView().getCircle().getStrokeWidth());
        c1.setCenterX(s.getStateView().getCircle().getCenterX());
        c1.setCenterY(s.getStateView().getCircle().getCenterY());

        Circle c2 = new Circle();
        c2.setRadius(stateController.getStateView().getCircle().getRadius());
        c1.setStroke(Color.ALICEBLUE);
        c2.setStrokeWidth(stateController.getStateView().getCircle().getStrokeWidth());
        c2.setCenterX(stateController.getStateView().getCircle().getCenterX());
        c2.setCenterY(stateController.getStateView().getCircle().getCenterY());

        Path path = (Path) Shape.intersect(c1, c2);
        return path.getElements().size()>0;
    }
    
    private boolean stateTransitionInstersection(StateController s, TransitionController t) {
        Circle c1 = new Circle();
        c1.setRadius(s.getStateView().getCircle().getRadius()+(s.getStateView().getCircle().getStrokeWidth()/2));
        c1.setCenterX(s.getStateView().getCircle().getCenterX());
        c1.setCenterY(s.getStateView().getCircle().getCenterY());
        c1.setStroke(Color.BLACK);
        c1.setStrokeWidth(2);
        c1.setFill(null);

        QuadCurve tview = t.getTransitionView().getCurve();
        //MoveTo move = new MoveTo(tview.getStartX(), tview.getStartY());
        QuadCurve c2 = new QuadCurve();        
        c2.setControlX(tview.getControlX());
        c2.setControlY(tview.getControlY());
        c2.setStartX(tview.getStartX());
        c2.setStartY(tview.getStartY());
        c2.setEndX(tview.getEndX());
        c2.setEndY(tview.getEndY());
        c2.setStroke(Color.BLACK);
        c2.setStrokeWidth(2);
        c2.setFill(null);
        
        /*
        QuadCurve c3 = new QuadCurve();
        c3.setControlX(tview.getControlX());
        c3.setControlY(tview.getControlY());
        c3.setStartX(tview.getStartX());
        c3.setStartY(tview.getStartY());
        c3.setEndX(tview.getEndX());
        c3.setEndY(tview.getEndY());
        c3.setStroke(Color.ALICEBLUE);
        c3.setStrokeWidth(tview.getStrokeWidth()+1);
        
        Path line = new Path();
        MoveTo move = new MoveTo();
        move.setX(tview.getStartX());
        move.setY(tview.getStartY());
        QuadCurveTo c4 = new QuadCurveTo();
        c4.setControlX(tview.getControlX());
        c4.setControlY(tview.getControlY());
        c4.setX(tview.getEndX());
        c4.setY(tview.getEndY());
        line.getElements().addAll(move,c4);
        line.setFill(Color.ALICEBLUE);
        line.setStroke(Color.BLACK);
        
        Path line2 = (Path) Shape.subtract(c3,c2);        
        
        
        //System.out.println(line.getElements().size());
        
        //union function which combines any two shapes
        Path path = (Path) Shape.intersect(line2, c1);
        path.setFill(Color.RED);
        group.getChildren().add(path);*/
        
        //return path.getBoundsInLocal().getHeight()>5 || path.getBoundsInLocal().getWidth()>5;
        
        //Interseccion transicion/estado
        //Sacar foto a transicion y estado, calcular vertices
        
        Circle circle = s.getStateView().getCircle();
        QuadCurve curve = t.getTransitionView().getCurve();
        
        long pointsT = getFeatures(c2);
        System.out.println("T:"+pointsT);
        
        long pointsS = getFeatures(c1);
        System.out.println("S:"+pointsS);
        
        Group g = new Group(c1, c2);
        long pointsG = getFeatures(g);
        System.out.println("G:"+pointsG); 
        
        
        
        
        if (pointsT+pointsS < pointsG)
            System.out.println("inter");
        
        //Agregar a un group
        //Sacar foto y. alcular. vertices fel group
        //Si la cantidad de vertices de group es mayor x 2 o mas, hay interseccion
        
        //interseccion transicion/transicion
        //calcular vertices x separado
        //Group
        //calcular vertices group
        //si la cantidad de vertices de las curvas y group iguales -> interse cion
        //si la cantidad de vertices cambia -> interseccion
        
        return false;
    }

    private long getFeatures(Node node) {
       WritableImage image = node.snapshot(new SnapshotParameters(), null);
        // TODO: probably use a file chooser here
        File file = new File("features.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            // TODO: handle exception here 
        } 
            Mat image2 = imread(file.getAbsolutePath(), IMREAD_COLOR);
            
            file.delete();
            
            ffd.detect(image2, keyPoints);

            //Muestra una imagen en una ventana nueva con los vertices detectados 
            Mat c = new Mat(); drawKeypoints(image2, keyPoints, c, new Scalar(0, 0, 255, 0), DrawMatchesFlags.DEFAULT); 
            OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat(); 
            CanvasFrame canvasFrame = new CanvasFrame("hola", 1); canvasFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
            canvasFrame.showImage(converter.convert(c));

            //Cantidad de vertices detectados 
             return keyPoints.size();
    }
}