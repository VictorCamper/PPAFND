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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import appAFND.controller.StateController;
import appAFND.controller.TransitionController;
import appAFND.model.Alphabet;
import appAFND.model.Automaton;
import appAFND.model.DFA;
import appAFND.model.Dijkstra;
import appAFND.model.NFA;
import appAFND.model.State;
import appAFND.model.Transition;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.UnaryOperator;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import org.controlsfx.control.PropertySheet;
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
    ArrayList<StateController> statesList;
    ArrayList<TransitionController> transitionsList;
    ArrayList<StateController> statesRedList = new ArrayList<>();
    ArrayList<TransitionController> transitionsRedList = new ArrayList<>();
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
    private ToggleButton buttonState;
    @FXML
    private ToggleButton buttonTransition;
    @FXML
    private Button buttonUndo;
    @FXML
    private Button buttonRedo;
    @FXML
    private SplitPane splitPane;
    
    private SpreadsheetView spreadSheet;
    
    private Pane automatonPane = new Pane();
    private Group groupStates = new Group();
    private Group groupTransitions = new Group();
    private Rectangle canvas = new Rectangle(0, 0, 0, 0);
    private String buttonPressed = new String();
    private int canvasWidth;
    private int canvasHeight;
    
    private int transitionClickCounter;
    private StateController transitionStateFrom;
    private StateController transitionStateTo;
    
    /*private FastFeatureDetector ffd;
    private KeyPointVector keyPoints;*/
    
    @FXML
    private TabPane tabPane;
    
    private Automaton automaton;
    @FXML
    private TextField wordField;

    int stateCounter;
    @FXML
    private Slider sliderZoom;
    
    private Shape intersectionsShape = new Rectangle(0, 0, 0, 0);   
    
    private Color colorStateFinal = Color.web("#006485");
    private Color colorState = Color.DEEPSKYBLUE;
    private Color colorStateIntersect = Color.CRIMSON;
    @FXML
    private Button buttonRead;
    @FXML
    private Button buttonStep;
    private int stepCounter=0;
    private String stepWord = new String();
    @FXML
    private VBox vBox;
    
    private boolean isNewAlphaEmpty = true;
    private boolean isNewWidthEmpty = true;
    private boolean isNewHeightEmpty = true;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<String> start = this.dialogNew();
        
        
        String alpha = start.get(0);
        
        Alphabet alph = new Alphabet();
        AlphabetView alphView = new AlphabetView();
        AlphabetController alphController = new AlphabetController(alph, alphView);
        
        for(String str : alpha.split(", ")){            
            alphController.addCharacter(str.charAt(0));
        }
        
        String type = start.get(1);
        if(type.equals("DFA"))
            this.automaton = new DFA(new ArrayList<>(), alphController, new ArrayList<>(), null, this);
        else
            this.automaton = new NFA(new ArrayList<>(), alphController, new ArrayList<>(), null, this);
        
        this.canvasWidth = Integer.parseInt(start.get(2));
        this.canvasHeight = Integer.parseInt(start.get(3));
        
        
        this.stateCounter = 0;        
        this.transitionClickCounter = 0;
        
        //this.ffd = FastFeatureDetector.create(10/* threshold for detection */, true /* non-max suppression */, FastFeatureDetector.TYPE_9_16); 
        //this.keyPoints = new KeyPointVector(); 
                
        this.canvas.setHeight(this.canvasHeight);
        this.canvas.setWidth(this.canvasWidth);
        this.canvas.setFill(Color.WHITE);
        
        //this.intersectionsShape = Shape.union(new Rectangle(0, 0, 0, 0), this.intersectionsShape);
        
        this.automatonPane.getChildren().add(this.canvas);
        this.automatonPane.getChildren().add(this.groupStates);
        this.automatonPane.getChildren().add(this.groupTransitions);
        
        Group group = new Group(automatonPane);

        // stackpane for centering the content, in case the ScrollPane viewport
        // is larger than zoomTarget
        StackPane content = new StackPane(group);
        group.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            // keep it at least as large as the content
            content.setMinWidth(newBounds.getWidth());
            content.setMinHeight(newBounds.getHeight());
        });
        
        this.scrollPane.setContent(content);
        
        scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
            // use vieport size, if not too small for zoomTarget
            content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());
        });
        
        sliderZoom.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                final double zoomFactor = newValue.doubleValue()/oldValue.doubleValue();
                
                Bounds groupBounds = group.getLayoutBounds();
                final Bounds viewportBounds = scrollPane.getViewportBounds();

                // calculate pixel offsets from [0, 1] range
                double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

                // convert content coordinates to zoomTarget coordinates
                double hRel = scrollPane.getHvalue() / scrollPane.getHmax();
                double vRel = scrollPane.getVvalue() / scrollPane.getVmax();
                Point2D posInZoomTarget = automatonPane.parentToLocal(group.parentToLocal(new Point2D(((groupBounds.getWidth() - viewportBounds.getWidth()) * hRel) + viewportBounds.getWidth() / 2, ((groupBounds.getHeight() - viewportBounds.getHeight()) * vRel) + viewportBounds.getHeight() / 2)));

                // calculate adjustment of scroll position (pixels)
                Point2D adjustment = automatonPane.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

                // do the resizing
                automatonPane.setScaleX(zoomFactor * automatonPane.getScaleX());
                automatonPane.setScaleY(zoomFactor * automatonPane.getScaleY());

                // refresh ScrollPane scroll positions & content bounds
                scrollPane.layout();

                // convert back to [0, 1] range
                // (too large/small values are automatically corrected by ScrollPane)
                groupBounds = group.getLayoutBounds();
                scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
            }
        });       
        
                
        //this.automaton = new NFA();
        
        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                scrollPane.requestFocus();
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
        
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        this.buttonState.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                scrollPane.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }

    @FXML
    private void selectState(ActionEvent event) {
        buttonPressed = "State";
        buttonState.setSelected(true);
        buttonTransition.setSelected(false);
        event.consume();
    }
    
    @FXML
    private void selectTransition(ActionEvent event) {
        buttonPressed = "Transition";
        this.transitionClickCounter = 0;
        buttonState.setSelected(false);
        buttonTransition.setSelected(true);
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
            case "State":
                //String name = dialogState();              // Modified by Victor
                String name = "Q" + this.stateCounter;
                //If the user write a name for the state, create the state, add it to the list of states,
                //draw it in the canvas, add it to the automaton and update table.
                if (!name.isEmpty()){        
                    //Create state
                    State state = new State();
                    StateView stateView = new StateView(this, x, y, this.radius, name);
                    StateController stateController = new StateController(state, stateView);
                    stateView.setController(stateController);
                    
                    boolean overlapped = false;
                    
                    
                    /*for (StateController s : this.statesList){
                        //Verify intersection
                        if(s.compareTo(stateController)==0){                        
                            overlapped = true;                            
                        }
                    }*/
                    
                    if(!overlapped){
                        if (intersectionAddState(stateController))
                            overlapped = true;
                    }
                    
                    if(overlapped){
                        Alert overlaped = new Alert(Alert.AlertType.ERROR);
                        overlaped.setTitle("State error");
                        overlaped.setHeaderText("You can't make a state here");
                        overlaped.setContentText("Is not possible to make a state over a existing state or over a existing transition");
                        overlaped.showAndWait();
                    }
                    
                    //Draw state
                    else {
                        //Create dialog to set the name of the state

                        //Draw state
                        stateController.getStateView().drawState(groupStates);
                        
                        if (this.statesList.size() == 0) {                            
                            automaton.setInitialState(stateController);
                            Polygon arrow = new Polygon(new double[]{0, 0, 10, 10, -10, 10});
                            Circle circleNode = stateController.getStateView().getCircle();
                            arrow.setTranslateX(x - (circleNode.getRadius()+(circleNode.getStrokeWidth()/2)));
                            arrow.setTranslateY(y);
                            arrow.getTransforms().add(new Rotate(90, 0, 0));
                            arrow.setFill(Color.YELLOWGREEN);
                            this.groupStates.getChildren().add(arrow);
                            stateController.getStateView().setArrow(arrow);
                        }
                        //Add state to the list of states
                        this.statesList.add(stateController);
                        //Add state to the automaton
                        this.automaton.addState(stateController);                        
                        //Update table
                        this.stateCounter++;
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
            SpreadsheetCell stateCell = SpreadsheetCellType.STRING.createCell(i+1, 0, 1, 1, state.getStateView().getText().getText());
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
                            label += to.get(k).getStateView().getText().getText();
                        }
                        else
                        {
                            label += to.get(k).getStateView().getText().getText();
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
    
    private SpreadsheetView copyTable(){
        SpreadsheetView sv = new SpreadsheetView(this.spreadSheet.getGrid());
        sv.setShowColumnHeader(false);
        sv.setShowRowHeader(false);
        return sv;
    }
    
    private double getWidthTable(){
        double width = 0;
        for (int i = 0; i < this.spreadSheet.getColumns().size(); i++) {
            width += this.spreadSheet.getColumns().get(i).getWidth();
        }
        return width;
    }
    
    private double getHeightTable(){
        double height = 0;
        for(int i=0;i<this.spreadSheet.getGrid().getRowCount(); i++){
            height += this.spreadSheet.getRowHeight(i);
        }
        return height;
    }
    
//    private SpreadsheetView getTableCopy()
//    {
//        try
//        {
//            FileOutputStream file = new FileOutputStream("tableView.dat");
//            BufferedOutputStream buffer = new BufferedOutputStream(file);
//            ObjectOutputStream output = new ObjectOutputStream(buffer);
//            output.writeObject(this.spreadSheet);
//            output.close();
//            buffer.close();
//            file.close();
//        }
//        catch(Exception ex)
//        {
//            System.out.println("A problem was ocurred with the exportation.");
//        }
//        
//        
//        SpreadsheetView table;
//        try
//        {
//            File archivo = new File("tableView.dat");
//            if (archivo.exists()) {
//                FileInputStream file = new FileInputStream(archivo);
//                BufferedInputStream buffer = new BufferedInputStream(file);
//                ObjectInputStream input = new ObjectInputStream(buffer);
//
//                Object object = input.readObject();
//                if( object instanceof SpreadsheetView )
//                {
//                    table = (SpreadsheetView) object;
//                    ObservableList<SpreadsheetColumn> columns = table.getColumns();
//                    if(columns.get(0).isColumnFixable())
//                        table.getFixedColumns().remove(columns.get(0));
//                    if(this.spreadSheet.isRowFixable(0))
//                        this.spreadSheet.getFixedRows().remove(0);
//                    return table;
//                }
//            }
//            
//            
//        }
//        catch(Exception ex)
//        {
//            System.out.println("Table can't be loaded");
//        } 
//        
//        return null;
//    }
    
    
    
//    private void dialogInitial()
//    {
//        TextInputDialog dialog = new TextInputDialog("");
//        dialog.setTitle("Input alphabet");
//        dialog.setHeaderText("Please, insert symbols for alphabet (separated by comma ,)");
//        dialog.setContentText("Symbols:");
//        
//        Optional<String> result = dialog.showAndWait();
//        
//        if (result.isPresent())
//        {            
//            String[] alphabet = result.get().replaceAll("\\s","").split(",");
//            char[] chars = new char[alphabet.length];
//            Alphabet alph = new Alphabet();
//            AlphabetView alphView = new AlphabetView();
//            AlphabetController alphController = new AlphabetController(alph, alphView);
//            try
//            {
//                for(String str : alphabet)
//                {
//                    if(!alphController.alphabetContains(str.charAt(0))) 
//                        alphController.addCharacter(str.charAt(0));
//                }
//                this.automaton = new NFA(new ArrayList<>(), alphController, new ArrayList<>(), null, this);
//            }
//            catch(Exception ex)
//            {
//                System.exit(0);
//            }
//        }
//        else
//        {
//            System.exit(0);
//        }
//    }
    
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
                this.transitionStateFrom = statecontroller;
            }
            else if (this.transitionClickCounter == 2){
                //State to
                this.transitionStateTo = statecontroller;
                //Request label through a window
                boolean doubleTransition = false;
                for(TransitionController t : transitionsList){
                    if(t.getTransitionFrom().equals(transitionStateFrom) && 
                            t.getTransitionTo().equals(transitionStateTo)){                       
                        doubleTransition=true;
                    }
                }
                for(TransitionController t : transitionsRedList){
                    if(t.getTransitionFrom().equals(transitionStateFrom) && 
                            t.getTransitionTo().equals(transitionStateTo)){                       
                        doubleTransition=true;
                    }
                }
                
                if(!doubleTransition){
                    
                    String[] chars = null;
                    if(this.automaton instanceof NFA){
                        chars = dialogTransitionNFA("Create");
                    }                        
                    else{
                        chars = dialogTransitionDFA("Create", transitionStateFrom);
                    }
                    
                    buttonTransition.fire();
                    if(chars.length>0){
                        if (!chars[0].isEmpty()){  
                            //Verify if any character exist in the alphabet
                            boolean existCharValid = false;
                            for (String c : chars){
                                Character c2 = c.charAt(0);
                                if(automaton.getAlphabet().alphabetContains(c2)){
                                    existCharValid = true;                            
                                }
                                if(existCharValid)
                                    break;
                            }
                            if(existCharValid){                    
                                String label = new String();
                                for (String c : chars){
                                    Character c2 = c.charAt(0);
                                    String c2s = c2.toString();
                                    if (automaton.getAlphabet().alphabetContains(c2)){
                                        if(label.isEmpty()){                                
                                            label = label.concat(c2s);
                                            /*if(this.automaton instanceof NFA){
                                                ((NFA)this.automaton).addTransition(transitionStateFrom, transitionStateTo, c2s);
                                            }*/
                                            this.automaton.addTransition(transitionStateFrom, transitionStateTo, c2s);
                                        }
                                        else
                                            if (!label.contains(c2s))
                                                label = label.concat(", ").concat(c2s);
                                                /*if(this.automaton instanceof NFA){
                                                    ((NFA)this.automaton).addTransition(transitionStateFrom, transitionStateTo, c2s);
                                                }*/
                                                this.automaton.addTransition(transitionStateFrom, transitionStateTo, c2s);
                                    }
                                }

                                Transition transitionmodel = new Transition(this.transitionStateFrom, this.transitionStateTo);                    
                                TransitionView transitionview = new TransitionView(this.transitionStateFrom, this.transitionStateTo, label, this.canvasHeight, this.canvasWidth, this);
                                TransitionController transitionController = new TransitionController(transitionmodel, transitionview);
                                transitionview.setTransitionController(transitionController);

                                transitionStateFrom.fromStateAdd(transitionController);
                                transitionStateTo.toStateAdd(transitionController);

                                //Intersection->Transition red
                                if(intersectionAddTransition(transitionController)){
                                    transitionController.getTransitionView().setRed();
                                    //Add the red transition to a list apart
                                    transitionsRedList.add(transitionController);
                                }
                                else{
                                    transitionsList.add(transitionController);
                                }

                                groupTransitions.getChildren().add(transitionview.getTransition());


                                this.updateTable();
                            }
                        }  
                    }
                    
                }
                else{
                    Alert doubleT = new Alert(Alert.AlertType.ERROR);
                    doubleT.setTitle("Double transition error");
                    doubleT.setHeaderText("You can't make the same transition again");
                    doubleT.setContentText("Try to edit the existing transition");
                    doubleT.showAndWait();
                    buttonTransition.fire();
                }
            }
        }
    }
    String dialogState(String s) {
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New state");
        dialog.setHeaderText("Introduce the name of the new state");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType(s, ButtonData.OK_DONE);
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
    
    private ArrayList<String> dialogNew(){
        Dialog<ArrayList<String>> dialog = new Dialog<>();
        dialog.setTitle("New automaton");
        
        ButtonType loginButtonType = new ButtonType("Create", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancel);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        
        TextField alpha = new TextField();
        alpha.setPromptText("Alphabet");
        ChoiceBox<String> automatonType = new ChoiceBox();
        automatonType.getItems().addAll("DFA (Deterministic Finite Automaton)", "NFA (Nondeterministic Finite Automaton)");
        automatonType.getSelectionModel().selectFirst();
        TextField width = new TextField();
        width.setPromptText("Width");
        TextField height = new TextField();
        height.setPromptText("Height");
        
        width.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("0"))
                     width.setText("");
                if (!newValue.matches("\\d*")) {
                    width.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        height.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("0"))
                     height.setText("");
                if (!newValue.matches("\\d*")) {
                    height.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {

            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {

                if(t.isAdded()){
                    if(!t.getText().isEmpty()){                        
                        
                        if(t.getControlNewText().length()>1){
                            if(!t.getControlText().contains(t.getText())){
                                t.setText(", ".concat(t.getText()));
                                t.selectRange(t.getCaretPosition()+2, t.getCaretPosition()+2);
                            }
                            else{
                                t.setText("");
                            }
                                
                        }
                        
                    }                    
                }

                return t;
            }
        };
        
        alpha.setTextFormatter(new TextFormatter<>(filter));
        
        alpha.caretPositionProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Platform.runLater(new Runnable() {
                @Override public void run() {
                  alpha.deselect();
                  alpha.positionCaret(alpha.getText().length());
                }
              });
            }
        });
        
        alpha.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    // block cursor control keys.
                    case BACK_SPACE:
                        alpha.deletePreviousChar();
                        break;
                    case LEFT:
                    case RIGHT:
                    case UP:
                    case DOWN:
                    case PAGE_UP:
                    case PAGE_DOWN:
                    case HOME:
                    case END:
                        keyEvent.consume();
                        break;
                    default:
                        
                        break;
                        
                }
            }
        });
        
        grid.add(new Label("Alphabet:"), 0, 0);
        grid.add(alpha, 1, 0);
        grid.add(new Label("Type of automaton:"), 0, 1);
        grid.add(automatonType, 1, 1);
        grid.add(new Label("Width:"),0,2);
        grid.add(width, 1, 2);
        grid.add(new Label("Height:"),0,3);
        grid.add(height, 1, 3);

        // Enable/Disable login button depending on whether a username was entered.
        javafx.scene.Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        
        
        // Do some validation (using the Java 8 lambda syntax).
        alpha.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(oldValue.concat(",")) || newValue.equals(oldValue.concat(" "))){
                alpha.setText(oldValue);
            }
            
            if(newValue.trim().isEmpty())
                isNewAlphaEmpty = true;
            else
                isNewAlphaEmpty = false;
            
            loginButton.setDisable(isNewAlphaEmpty || isNewHeightEmpty || isNewWidthEmpty);   
            
        });
        
        width.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.trim().isEmpty())
                isNewWidthEmpty = true;
            else
                isNewWidthEmpty = false;
            
            loginButton.setDisable(isNewAlphaEmpty || isNewHeightEmpty || isNewWidthEmpty);
            
        });
        
        height.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.trim().isEmpty())
                isNewHeightEmpty = true;
            else
                isNewHeightEmpty = false;
            
            loginButton.setDisable(isNewAlphaEmpty || isNewHeightEmpty || isNewWidthEmpty);            
        });
                
        loginButton.setDisable(isNewAlphaEmpty || isNewWidthEmpty || isNewHeightEmpty);
        

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> alpha.requestFocus());


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                ArrayList<String> ret = new ArrayList<>();
                ret.add(alpha.getText());
                ret.add(automatonType.getValue().substring(0, 3));
                ret.add(width.getText());
                ret.add(height.getText());
                return ret;
            }
            return null;
        });
        
        Optional<ArrayList<String>> result = dialog.showAndWait();
        
        if(!result.isPresent())
            System.exit(0);            
                
        return result.get();
        
    }

    String[] dialogTransitionNFA(String buttonLabel){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New transition");
        dialog.setHeaderText("Introduce the characters for the transition.\nOnly are accepted the characters cointained in the alphabet of the automaton.");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType(buttonLabel, ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancel);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField characters = new TextField();
        characters.setPromptText("Characters");
        CheckBox voidChar = new CheckBox();
        
        UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {

            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {

                if(t.isAdded()){
                    if(!t.getText().isEmpty()){
                        
                        if(!automaton.getAlphabet().getCharacters().contains(t.getText().charAt(0))){
                            t.setText("");
                        }

                        else{
                            if(t.getControlNewText().length()>1){
                                if(!t.getControlText().contains(t.getText())){
                                    t.setText(", ".concat(t.getText()));
                                    t.selectRange(t.getCaretPosition()+2, t.getCaretPosition()+2);
                                }
                                else
                                    t.setText("");
                                
                            }
                        }
                        
                    }                    
                }

                return t;
            }
        };
        
        characters.setTextFormatter(new TextFormatter<>(filter));
        
        characters.caretPositionProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Platform.runLater(new Runnable() {
                @Override public void run() {
                  characters.deselect();
                  characters.positionCaret(characters.getText().length());
                }
              });
            }
        });
        
        characters.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    // block cursor control keys.
                    case BACK_SPACE:
                        characters.deletePreviousChar();
                        break;
                    case LEFT:
                    case RIGHT:
                    case UP:
                    case DOWN:
                    case PAGE_UP:
                    case PAGE_DOWN:
                    case HOME:
                    case END:
                        keyEvent.consume();

                }
            }
        });
        
        
        
        
        
        /*characters.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, String newValue) {
                String result = "";
                String[] letters = newValue.replace(",", "").replace(" ", "").split("");   
                if(!letters[0].isEmpty()){
                    if(automaton.getAlphabet().getCharacters().contains(letters[0].charAt(0))){
                        result = letters[0];
                    }
                }
                    
                
                for(int i=1; i<letters.length; i++){
                    if(automaton.getAlphabet().getCharacters().contains(letters[i].charAt(0))){
                        boolean repeated = false;
                        for(int j=0; j<i; j++){
                            if(letters[i].equals(letters[j]))
                                repeated = true;
                        }
                            if(!repeated)
                                result = result+", "+(letters[i]);  
                    }                              
                }
                
                characters.setText(result);
                
            }
        });*/

        grid.add(new Label("Characters:"), 0, 0);
        grid.add(characters, 1, 0);
        grid.add(new Label("Void transition:"), 0, 1);
        grid.add(voidChar, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        javafx.scene.Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        boolean isTextEmpty = true;
        boolean isVoidUnselected = true;
        
        // Do some validation (using the Java 8 lambda syntax).
        characters.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
            voidChar.selectedProperty().addListener((observable2, oldValue2, newValue2) -> {
                loginButton.setDisable(!newValue2 && newValue.trim().isEmpty());          
            });
        });
        
        voidChar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!newValue);
            characters.textProperty().addListener((observable2, oldValue2, newValue2) -> {
                loginButton.setDisable(newValue2.trim().isEmpty() && !newValue);
            });
        });
        
        isTextEmpty = characters.getCharacters().toString().trim().isEmpty();
        
        isVoidUnselected = !(voidChar.isSelected());
        
        loginButton.setDisable(isTextEmpty && isVoidUnselected);
        

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> characters.requestFocus());


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if (voidChar.isSelected())
                    return new Pair<>(characters.getText(), "true");
                else
                    return new Pair<>(characters.getText(), "false");
            }
            return null;
        });
        
        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        if(!result.isPresent())
            return new String[0];
        
        if(voidChar.isSelected()){
            String[] chars = ("\u03BB,".concat(characters.getText())).replaceAll("\\s","").split(",");
            return chars;
        }
        else{
            String[] chars = characters.getText().replaceAll("\\s","").split(",");   
            return chars;
        }
    }

    String[] dialogTransitionDFA(String buttonLabel, StateController from){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New transition");
        dialog.setHeaderText("Introduce the characters for the transition.\nOnly are accepted the characters cointained in the alphabet of the automaton.");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType(buttonLabel, ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancel);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField characters = new TextField();
        characters.setPromptText("Characters");
        CheckBox remChars = new CheckBox();
        
        UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {

            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {

                if(t.isAdded()){
                    if(!t.getText().isEmpty()){
                        
                        /*ArrayList<TransitionController> transitions = from.getStateModel().getFromState();
                        if(!transitions.isEmpty()){
                            for(TransitionController tran : transitions){
                                if(tran.getTransitionView().getText().getText().contains(t.getText())){                                    
                                    t.setText("");
                                    return t;
                                }
                            }                       
                        }*/
                        
                        if(!automaton.getAlphabet().getCharacters().contains(t.getText().charAt(0))){
                            t.setText("");
                        }
                        
                        else{                            
                            if(t.getControlNewText().length()>1 ){
                                if(!t.getControlText().contains(t.getText())){
                                    t.setText(", ".concat(t.getText()));
                                    t.selectRange(t.getCaretPosition()+2, t.getCaretPosition()+2);
                                }
                                else
                                    t.setText("");
                                
                            }
                        }
                        
                    }                    
                }

                return t;
            }
        };
        
        characters.setTextFormatter(new TextFormatter<>(filter));
        
        characters.caretPositionProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Platform.runLater(new Runnable() {
                @Override public void run() {
                  characters.deselect();
                  characters.positionCaret(characters.getText().length());
                }
              });
            }
        });
        
        characters.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    // block cursor control keys.
                    case BACK_SPACE:
                        characters.deletePreviousChar();
                        break;
                    case LEFT:
                    case RIGHT:
                    case UP:
                    case DOWN:
                    case PAGE_UP:
                    case PAGE_DOWN:
                    case HOME:
                    case END:
                        keyEvent.consume();

                }
            }
        });
        

        grid.add(new Label("Characters:"), 0, 0);
        grid.add(characters, 1, 0);
        grid.add(new Label("Remaining characters:"), 0, 1);
        grid.add(remChars, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        javafx.scene.Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        boolean isTextEmpty = true;
        boolean isVoidUnselected = true;
        
        
        // Do some validation (using the Java 8 lambda syntax).
        characters.textProperty().addListener((observable, oldValue, newValue) -> {
                       
            loginButton.setDisable(newValue.trim().isEmpty());
            remChars.selectedProperty().addListener((observable2, oldValue2, newValue2) -> {
                loginButton.setDisable(!newValue2 && newValue.trim().isEmpty());          
            });
        });
        
        remChars.selectedProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(!newValue);
            characters.setDisable(newValue);
        });
        
        isTextEmpty = characters.getCharacters().toString().trim().isEmpty();
        
        isVoidUnselected = !(remChars.isSelected());
        
        loginButton.setDisable(isTextEmpty && isVoidUnselected);
        

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> characters.requestFocus());


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                if (remChars.isSelected())
                    return new Pair<>(characters.getText(), "true");
                else
                    return new Pair<>(characters.getText(), "false");
            }
            return null;
        });
        
        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        if(!result.isPresent())
            return new String[0];
        
        if(remChars.isSelected()){ 
            String sRem = new String();
            ArrayList<TransitionController> transitions = from.getStateModel().getFromState();
            ArrayList<Character> alpha = automaton.getAlphabet().getCharacters();
            
            for(Character c : alpha){
                sRem = sRem.concat(c.toString());
            }
            
            for(TransitionController tran : transitions){
                for(String st : tran.getTransitionView().getText().getText().split(", ")){
                    sRem = sRem.replace(st, "");
                }
            } 
            
            String[] chars = sRem.split("");
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
        
        
        /*for(StateController s : statesList){            
            if(s.compareTo(automaton.getInitialState())!=0 && s.getStateModel().toStateEmpty()){
                Alert result = new Alert(Alert.AlertType.ERROR);
                result.setTitle("State error");
                result.setHeaderText("Unreacheable state:");
                result.setContentText(s.getStateView().getText().getText());
                result.showAndWait();
                return false;
            }
                
        }*/
        
        /*if(automaton.getFinalStates().isEmpty()){
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Final state error");
            result.setHeaderText("There are no final states");
            result.showAndWait();
            return false;
        }
        
        if(!transitionsRedList.isEmpty()){
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Invalid transition error");
            result.setHeaderText("Invalid transition detected");
            result.setContentText("Please, move or delete the transitions showed in red");
            result.showAndWait();
            return false;
        }
        
        if(!statesRedList.isEmpty()){
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Invalid state error");
            result.setHeaderText("Invalid state detected");
            result.setContentText("Please, move or delete the states showed in red");
            result.showAndWait();
            return false;
        }
        
        String sp = null;
        Dijkstra d = new Dijkstra(automaton);
               
        d.sp();
        sp = d.getShortestWord();
        
        if(automaton.getInitialState()==null){
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Initial state error");
            result.setHeaderText("There is no initial state");
            result.showAndWait();
            return false;
        }
        
        else if(!d.isExistPath())
        {
            Alert shortest = new Alert(Alert.AlertType.ERROR);
            shortest.setTitle("Viable path error");
            shortest.setHeaderText("There is no viable path");
            shortest.showAndWait();
            return false;
        }*/   
        
        /*else if(sp.isEmpty() && wordField.getText().isEmpty())
        {
            Alert shortest = new Alert(Alert.AlertType.INFORMATION);
            shortest.setTitle("Word accepted");
            shortest.setHeaderText("The word was accepted!");
            shortest.showAndWait();
            return true;
        
        }*/
        if(this.automaton instanceof NFA && !isValidNFA()){
            return false;
        }
        
        else if(this.automaton instanceof DFA && !isValidDFA()){
            return false;
        }
        
        else{
            return this.automaton.readWord(wordField.getText());
        }
        /*else
        {
            Alert shortest = new Alert(Alert.AlertType.INFORMATION);
            shortest.setTitle("Word accepted");
            shortest.setHeaderText("The word was accepted!");
            shortest.setContentText("This automaton accepts any word");
            shortest.showAndWait();
            return true;
        }*/
        
    }

    @FXML
    private void shortestWord(ActionEvent event) {
               
        
        /*for(StateController s : statesList){            
            if(s.compareTo(automaton.getInitialState())!=0 && s.getStateModel().toStateEmpty()){
                Alert result = new Alert(Alert.AlertType.ERROR);
                result.setTitle("State error");
                result.setHeaderText("Unreacheable state:");
                result.setContentText(s.getStateView().getText().getText());
                result.showAndWait();
                return;
            }
                
        }*/
        /*
        if(automaton.getFinalStates().isEmpty()){
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Final state error");
            result.setHeaderText("There are no final states");
            result.showAndWait();
            return;
        }
        
        if(!transitionsRedList.isEmpty()){
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Invalid transition error");
            result.setHeaderText("Invalid transition detected");
            result.setContentText("Please, move or delete the transitions showed in red");
            result.showAndWait();
            return;
        }
        
        if(!statesRedList.isEmpty()){
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Invalid state error");
            result.setHeaderText("Invalid state detected");
            result.setContentText("Please, move or delete the states showed in red");
            result.showAndWait();
            return;
        }*/
        if(this.automaton instanceof NFA && !isValidNFA()){
            return;
        }
        
        String sp;
        Dijkstra d = new Dijkstra(automaton);

        d.sp();
        sp = d.getShortestWord();
        /*
        if(automaton.getInitialState()==null){
            Alert shortest = new Alert(Alert.AlertType.ERROR);
            shortest.setTitle("Initial state error");
            shortest.setHeaderText("There is no initial state");
            shortest.showAndWait();
        }
        
        else if(!d.isExistPath())
        {
            Alert shortest = new Alert(Alert.AlertType.ERROR);
            shortest.setTitle("Viable path error");
            shortest.setHeaderText("There is no viable path");
            shortest.showAndWait();
        }   */
        if (sp == null){
            Alert shortest = new Alert(Alert.AlertType.INFORMATION);
            shortest.setTitle("Shotest path");
            shortest.setHeaderText("The shortest doesn't exist");
            shortest.showAndWait();
        }
        
        else if (sp.length() == 0)
        {
            Alert shortest = new Alert(Alert.AlertType.INFORMATION);
            shortest.setTitle("Shotest path");
            shortest.setHeaderText("The shortest path is:");
            shortest.setContentText("");
            shortest.showAndWait();
        }
        
        else
        {
            Alert shortest = new Alert(Alert.AlertType.INFORMATION);
            shortest.setTitle("Shotest path");
            shortest.setHeaderText("The shortest path is:");
            shortest.setContentText(sp);
            shortest.showAndWait();
        }
        //System.out.println(d.getShortestWord());
    }

    /*private boolean statesInstersection(StateController s, StateController stateController) {
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
    }*/
    
    boolean intersectionAddState(StateController s){
        Circle circleState = s.getStateView().getCircle();
        Color color = Color.BLACK;
        
        Circle stateCopy = new Circle(circleState.getCenterX(), circleState.getCenterY(), circleState.getRadius(), color);
        stateCopy.setStroke(color);
        stateCopy.setStrokeWidth(circleState.getStrokeWidth());
        
        intersectionsShape.setStroke(color);
        intersectionsShape.setStrokeWidth(0);
        Shape intersection = Shape.intersect(stateCopy, intersectionsShape);
        
        if(!intersection.getBoundsInLocal().isEmpty()){
            return true;
        }
        
        intersectionsShape.setStroke(color);
        intersectionsShape.setStrokeWidth(0);
        intersectionsShape = Shape.union(stateCopy, intersectionsShape);
        /*
        intersectionsStatesShape.setStroke(color);
        intersectionsStatesShape.setStrokeWidth(0);
        intersectionsStatesShape = Shape.union(stateCopy, intersectionsStatesShape);
        //getFeatures(intersectionsShape);
        */
        return false;
    }
    
    boolean intersectionAddTransition(TransitionController t){
        QuadCurve transitionCurve = t.getTransitionView().getCurve();  
        
        Color color = Color.BLACK;     
        
        //Get only the line of the transition
        QuadCurve transitionCopy = new QuadCurve();
        transitionCopy.setStartX(transitionCurve.getStartX());
        transitionCopy.setStartY(transitionCurve.getStartY());
        transitionCopy.setEndX(transitionCurve.getEndX());
        transitionCopy.setEndY(transitionCurve.getEndY());
        transitionCopy.setControlX(transitionCurve.getControlX());
        transitionCopy.setControlY(transitionCurve.getControlY());
        transitionCopy.setFill(color);
        transitionCopy.setStroke(color);
        transitionCopy.setStrokeWidth(transitionCurve.getStrokeWidth());
        
        QuadCurve transitionSub = new QuadCurve();
        transitionSub.setStartX(transitionCurve.getStartX());
        transitionSub.setStartY(transitionCurve.getStartY());
        transitionSub.setEndX(transitionCurve.getEndX());
        transitionSub.setEndY(transitionCurve.getEndY());
        transitionSub.setControlX(transitionCurve.getControlX());
        transitionSub.setControlY(transitionCurve.getControlY());
        transitionSub.setFill(color);
        transitionSub.setStroke(color);
        transitionSub.setStrokeWidth(0);
        //Line of the transition
        Shape transitionLine = Shape.subtract(transitionCopy, transitionSub);
        
        //Get the shape formed by the union of the asociated states
        Circle circleFrom = t.getTransitionFrom().getStateView().getCircle();
        Circle circleTo = t.getTransitionTo().getStateView().getCircle();
        
        Circle cFrom = new Circle(circleFrom.getCenterX(), circleFrom.getCenterY(), circleFrom.getRadius(), color);
        cFrom.setStroke(color);
        cFrom.setStrokeWidth(circleFrom.getStrokeWidth());
        Circle cTo = new Circle(circleTo.getCenterX(), circleTo.getCenterY(), circleTo.getRadius(), color);
        cTo.setStroke(color);
        cTo.setStrokeWidth(circleTo.getStrokeWidth());
        //Shape formed by the asociated states
        Shape circles = Shape.union(cFrom, cTo);
        
        //Intersection with the asociated states        
        Shape interFrom = Shape.intersect(transitionLine, cFrom);
        Shape interTo = Shape.intersect(transitionLine, cTo);
        
        StateController from = t.getTransitionFrom();
        StateController to = t.getTransitionTo();
        
        if(from==to){
            if(interFrom.getBoundsInLocal().getHeight()>20){
                if(!statesRedList.contains(from))
                    statesRedList.add(from);
                from.getStateView().getCircle().setFill(colorStateIntersect);
                from.getStateView().getCircle().setStroke(colorStateIntersect);
                return true;
            }
            else{
                if(statesRedList.contains(from))
                    statesRedList.remove(from);
                from.getStateView().getCircle().setFill(colorState);
                if(automaton.getFinalStates().contains(from))
                    from.getStateView().getCircle().setStroke(colorStateFinal);
                else
                    from.getStateView().getCircle().setStroke(colorState);
            }

            if(interTo.getBoundsInLocal().getHeight()>20){
                if(!statesRedList.contains(to))
                    statesRedList.add(to);
                to.getStateView().getCircle().setFill(colorStateIntersect);
                to.getStateView().getCircle().setStroke(colorStateIntersect);
                return true;
            }
            else{
                if(statesRedList.contains(to))
                    statesRedList.remove(to);
                to.getStateView().getCircle().setFill(colorState);
                if(automaton.getFinalStates().contains(to))
                    to.getStateView().getCircle().setStroke(colorStateFinal);
                else
                    to.getStateView().getCircle().setStroke(colorState);
            }
        }
        
        else{
            if(interFrom.getBoundsInLocal().getWidth()>3){
                if(!statesRedList.contains(from))
                    statesRedList.add(from);
                from.getStateView().getCircle().setFill(colorStateIntersect);
                from.getStateView().getCircle().setStroke(colorStateIntersect);
                return true;
            }
            else{
                if(statesRedList.contains(from))
                    statesRedList.remove(from);
                from.getStateView().getCircle().setFill(colorState);
                if(automaton.getFinalStates().contains(from))
                    from.getStateView().getCircle().setStroke(colorStateFinal);
                else
                    from.getStateView().getCircle().setStroke(colorState);
            }

            if(interTo.getBoundsInLocal().getWidth()>3){
                if(!statesRedList.contains(to))
                    statesRedList.add(to);
                to.getStateView().getCircle().setFill(colorStateIntersect);
                to.getStateView().getCircle().setStroke(colorStateIntersect);
                return true;
            }
            else{
                if(statesRedList.contains(to))
                    statesRedList.remove(to);
                to.getStateView().getCircle().setFill(colorState);
                if(automaton.getFinalStates().contains(to))
                    to.getStateView().getCircle().setStroke(colorStateFinal);
                else
                    to.getStateView().getCircle().setStroke(colorState);
            }
        }
        
        
        //Subtract the intersection of the transition with the asociated states
        transitionLine = Shape.subtract(transitionLine, circles);
        
        Shape intersection = Shape.intersect(intersectionsShape, transitionLine);
        
        /*if(getFeatures(intersection)>0){
            return true;
        }*/
        //automatonPane.getChildren().add(transitionLine);
        
        
        WritableImage image = intersection.snapshot(new SnapshotParameters(), null);
        for(int i=0; i<image.getWidth(); i++){
            for(int j=0; j<image.getHeight(); j++){
                if(image.getPixelReader().getArgb(i, j)!=-1 && image.getPixelReader().getArgb(i, j)!=-263173){
                    //System.out.println(image.getPixelReader().getArgb(i, j));
                    return true;
                }
            }
        }
        
        /*if(getFeatures(intersection)>0)
            return true;*/
        
        intersectionsShape = Shape.union(transitionLine, intersectionsShape);
        
               
        return false;
    }
    
    public void intersectionDeleteTransition(TransitionController t) {
        QuadCurve transitionCurve = t.getTransitionView().getCurve();  
        
        Color color = Color.BLACK;     
        
        //Get only the line of the transition
        QuadCurve transitionCopy = new QuadCurve();
        transitionCopy.setStartX(transitionCurve.getStartX());
        transitionCopy.setStartY(transitionCurve.getStartY());
        transitionCopy.setEndX(transitionCurve.getEndX());
        transitionCopy.setEndY(transitionCurve.getEndY());
        transitionCopy.setControlX(transitionCurve.getControlX());
        transitionCopy.setControlY(transitionCurve.getControlY());
        transitionCopy.setFill(color);
        transitionCopy.setStroke(color);
        transitionCopy.setStrokeWidth(transitionCurve.getStrokeWidth());
        
        QuadCurve transitionSub = new QuadCurve();
        transitionSub.setStartX(transitionCurve.getStartX());
        transitionSub.setStartY(transitionCurve.getStartY());
        transitionSub.setEndX(transitionCurve.getEndX());
        transitionSub.setEndY(transitionCurve.getEndY());
        transitionSub.setControlX(transitionCurve.getControlX());
        transitionSub.setControlY(transitionCurve.getControlY());
        transitionSub.setFill(color);
        transitionSub.setStroke(color);
        transitionSub.setStrokeWidth(0);
        //Line of the transition
        Shape transitionLine = Shape.subtract(transitionCopy, transitionSub);
        
        //Get the shape formed by the union of the asociated states
        Circle circleFrom = t.getTransitionFrom().getStateView().getCircle();
        Circle circleTo = t.getTransitionTo().getStateView().getCircle();
        
        Circle cFrom = new Circle(circleFrom.getCenterX(), circleFrom.getCenterY(), circleFrom.getRadius(), color);
        cFrom.setStroke(color);
        cFrom.setStrokeWidth(circleFrom.getStrokeWidth());
        Circle cTo = new Circle(circleTo.getCenterX(), circleTo.getCenterY(), circleTo.getRadius(), color);
        cTo.setStroke(color);
        cTo.setStrokeWidth(circleTo.getStrokeWidth());
        //Shape formed by the asociated states
        Shape circles = Shape.union(cFrom, cTo);
        
        //Subtract the intersection of the transition with the asociated states
        transitionLine = Shape.subtract(transitionLine, circles);
        
        if(transitionsRedList.contains(t))
            transitionsRedList.remove(t);
        
        intersectionsShape = Shape.subtract(intersectionsShape, transitionLine);
             
    }
    
    void intersectionDeleteState(StateController s){
        Circle circleState = s.getStateView().getCircle();
        Color color = Color.BLACK;
        
        Circle stateCopy = new Circle(circleState.getCenterX(), circleState.getCenterY(), circleState.getRadius(), color);
        stateCopy.setStroke(color);
        stateCopy.setStrokeWidth(circleState.getStrokeWidth());
        
        intersectionsShape.setStroke(color);
        intersectionsShape.setStrokeWidth(0);
        intersectionsShape = Shape.subtract(intersectionsShape, stateCopy);

    }
    
    /*boolean intersectionMoveTransition(TransitionView tview){
        //QuadCurve tview = t.getTransitionView().getCurve();
        //MoveTo move = new MoveTo(tview.getStartX(), tview.getStartY());
        QuadCurve curveTransition = new QuadCurve();        
        curveTransition.setControlX(tview.getCurve().getControlX());
        curveTransition.setControlY(tview.getCurve().getControlY());
        curveTransition.setStartX(tview.getCurve().getStartX());
        curveTransition.setStartY(tview.getCurve().getStartY());
        curveTransition.setEndX(tview.getCurve().getEndX());
        curveTransition.setEndY(tview.getCurve().getEndY());
        curveTransition.setStroke(Color.ORANGE);
        curveTransition.setStrokeWidth(1);
        curveTransition.setFill(null);
        
        StateController from = tview.getFrom();
        StateController to = tview.getTo();
        
        Circle circleFrom = new Circle();
        circleFrom.setRadius(from.getStateView().getCircle().getRadius());
        circleFrom.setCenterX(from.getStateView().getCircle().getCenterX());
        circleFrom.setCenterY(from.getStateView().getCircle().getCenterY());
        circleFrom.setStroke(from.getStateView().getCircle().getStroke());
        circleFrom.setStrokeWidth(from.getStateView().getCircle().getStrokeWidth());
        circleFrom.setFill(from.getStateView().getCircle().getFill());
        
        Text textFrom = new Text(from.getStateView().getText().getText());
        textFrom.setX(from.getStateView().getText().getX());
        textFrom.setY(from.getStateView().getText().getY());
        
        Circle circleTo = new Circle();
        circleTo.setRadius(to.getStateView().getCircle().getRadius());
        circleTo.setCenterX(to.getStateView().getCircle().getCenterX());
        circleTo.setCenterY(to.getStateView().getCircle().getCenterY());
        circleTo.setStroke(to.getStateView().getCircle().getStroke());
        circleTo.setStrokeWidth(to.getStateView().getCircle().getStrokeWidth());
        circleTo.setFill(to.getStateView().getCircle().getFill());
        
        Text textTo = new Text(to.getStateView().getText().getText());
        textTo.setX(to.getStateView().getText().getX());
        textTo.setY(to.getStateView().getText().getY());
        
        
        Rectangle r = new Rectangle(canvasWidth, canvasHeight, Color.WHITE);
        
        //Intersection between the transition and the asociated states
//        Group g1 = new Group(r,circleFrom,textFrom,curveTransition);
//        long featuresFrom = getFeatures(g1);
//        Group g2 = new Group(r,circleTo,textTo, curveTransition);      
//        
//        long featuresTo = getFeatures(g2);
//        
//        Group g3 = new Group(r,curveTransition);
//        long featuresCurve = getFeatures(g3);
//        
//        Group gt = new Group(r,circleFrom,circleTo,textFrom,textTo,curveTransition);
//        long featuresT = getFeatures(gt);
//        
//        System.out.println("Total: "+featuresT);
//        System.out.println("Result: "+(featuresFrom+featuresTo-featuresCurve));
//        
//        if(featuresT < featuresFrom+featuresTo-featuresCurve){
//            return true;
//        }

        Shape interFrom = Shape.intersect(circleFrom, curveTransition);
        Shape interTo = Shape.intersect(circleTo, curveTransition);
        
        if(interFrom.getBoundsInLocal().getWidth()>3){
            if(!statesRedList.contains(from))
                statesRedList.add(from);
            from.getStateView().getCircle().setFill(Color.CRIMSON);
            from.getStateView().getCircle().setStroke(Color.CRIMSON);
            return true;
        }
        else{
            if(statesRedList.contains(from))
                statesRedList.remove(from);
            from.getStateView().getCircle().setFill(Color.DEEPSKYBLUE);
            if(automaton.getFinalStates().contains(from))
                from.getStateView().getCircle().setStroke(Color.web("#006485"));
            else
                from.getStateView().getCircle().setStroke(Color.DEEPSKYBLUE);
        }
        
        if(interTo.getBoundsInLocal().getWidth()>3){
            if(!statesRedList.contains(to))
                statesRedList.add(to);
            to.getStateView().getCircle().setFill(Color.CRIMSON);
            to.getStateView().getCircle().setStroke(Color.CRIMSON);
            return true;
        }
        else{
            if(statesRedList.contains(to))
                statesRedList.remove(to);
            to.getStateView().getCircle().setFill(Color.DEEPSKYBLUE);
            if(automaton.getFinalStates().contains(to))
                to.getStateView().getCircle().setStroke(Color.web("#006485"));
            else
                to.getStateView().getCircle().setStroke(Color.DEEPSKYBLUE);
        }
        
        Group g4 = new Group(r,circleFrom,circleTo,textFrom,textTo,curveTransition);*/
        
        //todo menos transicion
        /*automatonPane.getChildren().remove(tview.getTransition());
        long featuresCanvas = getFeatures(automatonPane);
        
        //transicion con circulos - circulos
        long featuresTransition = getFeatures(g4);
        g4.getChildren().remove(curveTransition);
        featuresTransition -= getFeatures(g4);
        
        //todo con curva transicion
        automatonPane.getChildren().add(curveTransition);
        long featuresTotal = getFeatures(automatonPane);
        automatonPane.getChildren().remove(curveTransition);
        automatonPane.getChildren().add(tview.getTransition());
        
        
        if(featuresTotal != (featuresTransition)+featuresCanvas){
            //System.out.println("inter new state");
            System.out.println("Total: "+ featuresTotal);
            System.out.println("Transition: " + (featuresTransition-featuresStates));
            System.out.println("Canvas: " + featuresCanvas);
            if(transitionsList.contains(tview.getTransitionController()))
                transitionsList.remove(tview.getTransitionController());
            if(!transitionsRedList.contains(tview.getTransitionController()))
                transitionsRedList.add(tview.getTransitionController());
            return true;
        }
        //System.out.println("no inter");
        if(!transitionsList.contains(tview.getTransitionController()))
            transitionsList.add(tview.getTransitionController());
        if(transitionsRedList.contains(tview.getTransitionController()))
            transitionsRedList.remove(tview.getTransitionController());*/
        /*return false;
    }*/
    
    /*private boolean stateTransitionInstersection(StateController s, TransitionController t) {
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
        group.getChildren().add(path);
        
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
    }*/

    /*private long getFeatures(Node node) {
        WritableImage image = node.snapshot(new SnapshotParameters(), null);
        
        File file = new File("features.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
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
    }*/

    public Pane getAutomatonPane()
    {
        return automatonPane;
    }

    public void setAutomatonPane(Pane group)
    {
        this.automatonPane = group;
    }

    public Group getGroupStates()
    {
        return groupStates;
    }

    public void setGroupStates(Group groupStates)
    {
        this.groupStates = groupStates;
    }

    public Group getGroupTransitions()
    {
        return groupTransitions;
    }

    public void setGroupTransitions(Group groupTransitions)
    {
        this.groupTransitions = groupTransitions;
    }
    
    public void removeStateAuxiliar(StateController state, ArrayList<TransitionController> transitions)
    {
        this.statesList.remove(state);
        this.statesRedList.remove(state);
        this.transitionsList.removeAll(transitions);
        this.transitionsRedList.removeAll(transitions);
    }

    @FXML
    private void readWordStep(ActionEvent event) {
        if(stepCounter==0){
            stepWord = wordField.getText();
            
            if (isValidNFA()){
                if (!isEmptyAccepted()){                    
                    stepDisableButtons();
                    if(stepWord.length()>0){
                        this.automaton.readChar(stepWord.substring(0, 1));
                        stepWord = stepWord.substring(1);
                    }
                    stepCounter++;
                }                
            }           
            
        }
    }
    
    private BufferedImage screenshotCanvas(){
        double scaleX = automatonPane.getScaleX();
        double scaleY = automatonPane.getScaleY();
        
        automatonPane.setScaleX(1);
        automatonPane.setScaleY(1);
        
        WritableImage image = automatonPane.snapshot(new SnapshotParameters(), null);
        
        automatonPane.setScaleX(scaleX);
        automatonPane.setScaleY(scaleY);
        return SwingFXUtils.fromFXImage(image,null);
    }
    
    private BufferedImage screenshotTable(){
        SpreadsheetView sv = copyTable();
        Scene s = new Scene(sv, getWidthTable()+2, getHeightTable()+2);
        
        return SwingFXUtils.fromFXImage(sv.snapshot(new SnapshotParameters(), null),null);
    }
    
    private BufferedImage combineImages(BufferedImage i1, BufferedImage i2){               
        int w = i1.getWidth()+ i2.getWidth();
        int h = Math.max(i1.getHeight(), i2.getHeight());
        
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = combined.getGraphics();
        g.drawImage(i1, 0, 0, null);
        g.drawImage(i2, i1.getWidth(), 0, null);
        
        return combined;
    }
    
    private void writeImage(BufferedImage image){
        Stage secondStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fileChooser.setTitle("Save Image");

        File file = fileChooser.showSaveDialog(secondStage);
        if (file != null) {
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @FXML
    private void exportBoth(ActionEvent event) {       
        writeImage(combineImages(screenshotCanvas(), screenshotTable())); 
    }
    
    @FXML
    private void exportCanvas(){     
        writeImage(screenshotCanvas());
    }
    
    @FXML
    private void exportTable(){
        writeImage(screenshotTable());
    }
    
    private void stepDisableButtons(){
        menuFile.setDisable(true);
        menuEdit.setDisable(true);
        menuHelp.setDisable(true);
        menuOptions.setDisable(true);
        buttonRead.setDisable(true);
        buttonRedo.setDisable(true);
        buttonState.setDisable(true);
        buttonState.setSelected(false);
        buttonPressed = "";
        buttonUndo.setDisable(true);
        buttonTransition.setSelected(false);
        buttonTransition.setDisable(true);
        wordField.setDisable(true);
    }
    
    private void stepEnableButtons(){
        menuFile.setDisable(false);
        menuEdit.setDisable(false);
        menuHelp.setDisable(false);
        menuOptions.setDisable(false);
        buttonRead.setDisable(false);
        buttonRedo.setDisable(false);
        buttonState.setDisable(false);
        buttonUndo.setDisable(false);
        buttonTransition.setDisable(false);
        wordField.setDisable(false);
    }
    
    public void acceptAutomaton(){
        for(StateController s : statesList){
            s.getStateView().playAcceptAnimation();
        }
    }
    
    public void rejectAutomaton(){
        for(StateController s : statesList){
            s.getStateView().playRejectAnimation();
        }
    }
    
    private boolean isValidNFA(){
        if(automaton.getFinalStates().isEmpty()){
            rejectAutomaton();
            
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Final state error");
            result.setHeaderText("There are no final states");
            result.showAndWait();
            return false;
        }
        
        if(!transitionsRedList.isEmpty()){
            rejectAutomaton();
            
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Invalid transition error");
            result.setHeaderText("Invalid transition detected");
            result.setContentText("Please, move or delete the transitions showed in red");
            result.showAndWait();
            return false;
        }
        
        if(!statesRedList.isEmpty()){
            rejectAutomaton();
            
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Invalid state error");
            result.setHeaderText("Invalid state detected");
            result.setContentText("Please, move or delete the states showed in red");
            result.showAndWait();
            return false;
        }
        
        if(automaton.getInitialState()==null){
            rejectAutomaton();
            
            Alert result = new Alert(Alert.AlertType.ERROR);
            result.setTitle("Initial state error");
            result.setHeaderText("There is no initial state");
            result.showAndWait();
            return false;
        }
        /*  
        String sp = null;
        Dijkstra d = new Dijkstra(automaton);
             
        d.sp();
        sp = d.getShortestWord();
        
        if(!d.isExistPath()){
            rejectAutomaton();
            
            Alert shortest = new Alert(Alert.AlertType.ERROR);
            shortest.setTitle("Viable path error");
            shortest.setHeaderText("There is no viable path");
            shortest.showAndWait();
            return false;
        } */
        
        return true;
    }
    
    private boolean isEmptyAccepted(){
        String sp;
        Dijkstra d = new Dijkstra(automaton);
               
        d.sp();
        sp = d.getShortestWord();
        
        if(sp.isEmpty() && wordField.getText().isEmpty())
        {
            acceptAutomaton();
            
            Alert shortest = new Alert(Alert.AlertType.INFORMATION);
            shortest.setTitle("Word accepted");
            shortest.setHeaderText("The word was accepted!");
            shortest.showAndWait();
            return true;
        }
        return false;
    }

    private boolean isValidDFA() {
        if(isValidNFA()){
            for(StateController state : automaton.getStates()){
                ArrayList<Character> alpha = automaton.getAlphabet().getCharacters();
                String transitionText = new String();
                for(TransitionController transition : state.getStateModel().getFromState()){
                    transitionText = transitionText.concat(transition.getTransitionView().getText().getText());
                
                }
                for(Character c : alpha){
                        //System.out.println(t);
                        if (!transitionText.contains(c.toString())){
                            rejectAutomaton();
                            return false;
                        }
                    }
            }
            //acceptAutomaton();
            return true;
            
        }
        rejectAutomaton();
        return false;
    }
    
    public ArrayList<TransitionController> getTrasitionsList(){
        return this.transitionsList;
    }
    
    public ArrayList<TransitionController> getTrasitionsRedList(){
        return this.transitionsRedList;
    }
    
}