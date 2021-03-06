package appAFND.view;

import appAFND.controller.StateController;
import appAFND.controller.TransitionController;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Victor
 */
public class StateView implements Comparable<StateView>
{

    private Circle circle;
    private Text text;
    private ContextMenu context = new ContextMenu();
    private StateController controller;
    private AFNDController afndcontroller;
    private Polygon arrow;
    private StrokeTransition st;
    private FillTransition ft;

    public StateView(AFNDController afndcontroller, double x, double y, double radius, String name)
    {        
        MenuItem changeFinal = new MenuItem("Set/Unset Final");
        MenuItem deleteState = new MenuItem("Delete State");
        MenuItem changeName = new MenuItem("Change Name");
        context.getItems().addAll(changeFinal,changeName);
        
        if(afndcontroller.getAutomaton().getInitialState()!=null)
            if(afndcontroller.getAutomaton().getInitialState()!=controller)
                context.getItems().add(deleteState);
        
        this.afndcontroller = afndcontroller;
        
        this.circle = new Circle(x, y, radius, Color.DEEPSKYBLUE);
        this.circle.setStroke(Color.DEEPSKYBLUE);
        this.circle.setStrokeWidth(3);
        
        st = new StrokeTransition();
        st.setShape(circle);
        st.setDuration(new Duration(500));
        st.setToValue(Color.GOLD);
        st.setCycleCount(Timeline.INDEFINITE);
        st.setAutoReverse(true);
        
        ft = new FillTransition();
        ft.setShape(circle);
        ft.setDuration(new Duration(500));
        ft.setToValue(Color.GOLD);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);        
        
        String label = name;
        this.text = new Text(label);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        Font font = this.text.getFont();
        float labelWidth = fontLoader.computeStringWidth(label, font);

        this.text.setX(x - (labelWidth / 2));
        this.text.setY(y + 4);
        this.text.setWrappingWidth(radius * 2);

        this.circle.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton() == MouseButton.PRIMARY){
                    afndcontroller.stateClicked(controller);
                }
                contextMenu(event);
            }
        });

        this.text.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton() == MouseButton.PRIMARY){                                  
                    
                    afndcontroller.stateClicked(controller);
                }
                contextMenu(event);
            }
        });

        changeFinal.setOnAction(new EventHandler<ActionEvent> (){
          @Override
          public void handle(ActionEvent event)
          {
              // Para cambiar a nodo final
              if(afndcontroller.getAutomaton().getFinalStates().contains(controller))
                  isNotFinalNodo();
              else
                  isFinalNodo();
          }
        });
        
        deleteState.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                //Delete state from the intersections shape
                afndcontroller.intersectionDeleteState(controller);
                
                afndcontroller.statesList.remove(controller);
                afndcontroller.statesRedList.remove(controller);
                
                ArrayList<TransitionController> transitions = new ArrayList<>();
                //transitions.addAll(controller.getStateModel().getFromState());
                transitions.addAll(controller.getStateModel().getToState());

                 for(TransitionController transition : transitions)
                 {
                     StateController from = transition.getTransitionFrom();
                     //System.out.println(from.getStateView().text.getText());
                     HashMap<String, ArrayList<StateController>> aux = afndcontroller.getAutomaton().getF().get(from);
                     for(Character word : afndcontroller.getAutomaton().getAlphabet().getCharacters())
                     {
                        if(aux.get(word.toString()) != null){
                            aux.get(word.toString()).remove(controller);
                        }
                     }
                     afndcontroller.intersectionDeleteTransition(transition);
                 }

                afndcontroller.getAutomaton().getF().remove(controller);
                afndcontroller.getAutomaton().getStates().remove(controller);

                transitions.addAll(controller.getStateModel().getFromState());
                for(TransitionController transition : transitions)
                {
                    afndcontroller.getGroupTransitions().getChildren().remove(transition.getTransitionView().getTransition());
                    afndcontroller.getAutomatonPane().getChildren().remove(transition.getTransitionView().getTransition());
                }

                afndcontroller.getGroupStates().getChildren().remove(circle);
                afndcontroller.getGroupStates().getChildren().remove(text);
                afndcontroller.getGroupStates().getChildren().remove(arrow);

                afndcontroller.updateTable();
                afndcontroller.removeStateAuxiliar(controller, transitions);

                for(StateController state : afndcontroller.getAutomaton().getStates())
                {
                    for (TransitionController transition : transitions)
                    {
                        state.getStateModel().getToState().remove(transition);
                        //state.getStateModel().getFromState().remove(controller);
                    }

                }
                
                    
            }
        });
        
        changeName.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                Text text2;
                String label = afndcontroller.dialogState("Modify");
                Boolean continuee = true;
                if(!label.isEmpty()){
                    for (StateController state : afndcontroller.getAutomaton().getStates())
                    {
                        if(state.getStateView().getText().getText().equals(label))
                        {
                            Alert refuse = new Alert(Alert.AlertType.ERROR);
                            refuse.setTitle("Name error");
                            refuse.setHeaderText("The name is already in use");
                            refuse.showAndWait();
                            continuee = false;
                            break;
                        }
                    }
                    if(continuee)
                    {
                        text2 = new Text(label);
                        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
                        Font font = text2.getFont();
                        float labelWidth = fontLoader.computeStringWidth(label, font);


                        text.setText(text2.getText());
                        text.setX(x - (labelWidth / 2));
                        text.setY(y + 4);
                        afndcontroller.updateTable();
                    }
                }
            }
                
            
        });
        
    }
    
    public void playStepAnimation(){
        ft.play();
        st.play();
    }
    
    public void playAcceptAnimation(){
        ft.setCycleCount(4);
        st.setCycleCount(4);
        
        ft.setToValue(Color.LIMEGREEN);
        if(afndcontroller.getAutomaton().getFinalStates().contains(controller))          
            st.setToValue(Color.GREEN);
        
        else
            st.setToValue(Color.LIMEGREEN);
        
        ft.play();
        st.play();
        
        ft.setCycleCount(Timeline.INDEFINITE);
        st.setCycleCount(Timeline.INDEFINITE);
        
        ft.setToValue(Color.GOLD);
        if(afndcontroller.getAutomaton().getFinalStates().contains(controller)){
            st.setToValue(Color.ORANGE);
        }
        else{
            st.setToValue(Color.GOLD);
        }
        
    }
    
    public void playRejectAnimation(){
        ft.setCycleCount(4);
        st.setCycleCount(4);
        
        ft.setToValue(Color.TOMATO);
        if(afndcontroller.getAutomaton().getFinalStates().contains(controller))          
            st.setToValue(Color.CRIMSON);
        
        else
            st.setToValue(Color.TOMATO);
        
        ft.play();
        st.play();
        
        ft.setCycleCount(Timeline.INDEFINITE);
        st.setCycleCount(Timeline.INDEFINITE);
        
        ft.setToValue(Color.GOLD);
        if(afndcontroller.getAutomaton().getFinalStates().contains(controller)){
            st.setToValue(Color.ORANGE);
        }
        else{
            st.setToValue(Color.GOLD);
        }
        
    }
    
    public void pauseStepAnimation(){
        ft.jumpTo(Duration.ZERO);
        ft.pause();
        st.jumpTo(Duration.ZERO);
        st.pause(); 
    }

    public void setController(StateController controller) {
        this.controller = controller;
    }
    
    private void isNotFinalNodo() {
        st.setToValue(Color.GOLD);
        if(!afndcontroller.statesRedList.contains(controller))  
            circle.setStroke(Color.DEEPSKYBLUE);
        afndcontroller.getAutomaton().getFinalStates().remove(controller);
        afndcontroller.updateTable();
    }
    
    private void isFinalNodo() {
        st.setToValue(Color.ORANGE);
        if(!afndcontroller.statesRedList.contains(controller))            
            circle.setStroke(Color.web("#006485"));  
        afndcontroller.getAutomaton().getFinalStates().add(controller);
        afndcontroller.updateTable();
    }
    
    private void contextMenu(MouseEvent event)
    {
        if (event.getButton() == MouseButton.SECONDARY)
        {
            double x = event.getScreenX();
            double y = event.getScreenY();
            context.show(circle, x, y);
        } else
        {
            context.hide();
        }
    }

    public void drawState(Group g)
    {
        g.getChildren().add(this.circle);
        g.getChildren().add(this.text);
    }
    
    public Circle getCircle(){
        return this.circle;
    }
    
    public void setText(String name)
    {
        this.text.setText(name);
    }
    
    public Text getText(){
        return this.text;
    }
    
    public double getCenterX(){
        return circle.getCenterX();
    }
    
    public double getCenterY(){
        return circle.getCenterY();
    }

    public Polygon getArrow()
    {
        return arrow;
    }

    public void setArrow(Polygon arrow)
    {
        this.arrow = arrow;
    }
    
    @Override
    public int compareTo(StateView o) {
        double  distance = Math.sqrt(Math.pow(this.getCenterX() - o.getCenterX(),2)+Math.pow(this.getCenterY() - o.getCenterY(),2));
        
        if(distance <= (this.getCircle().getRadius()*2 + this.getCircle().getStrokeWidth()/2))
            return 0;
        return 1;
       
    }
}
