package appAFND.view;

import appAFND.controller.StateController;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

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

    public StateView(AFNDController afndcontroller, double x, double y, double radius, String name)
    {        
        MenuItem changeFinal = new MenuItem("Change to final");
        context.getItems().addAll(changeFinal);
        
        this.afndcontroller = afndcontroller;
        
        this.circle = new Circle(x, y, radius, Color.DEEPSKYBLUE);
        this.circle.setStroke(Color.DEEPSKYBLUE);
        this.circle.setStrokeWidth(3);
        
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
              isFinalNodo();
          }
        });
        
    }

    public void setController(StateController controller) {
        this.controller = controller;
    }
    
    private void isFinalNodo()
    {
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
    
    public Text getText(){
        return this.text;
    }
    
    public double getCenterX(){
        return circle.getCenterX();
    }
    
    public double getCenterY(){
        return circle.getCenterY();
    }

    @Override
    public int compareTo(StateView o) {
        double  distance = Math.sqrt(Math.pow(this.getCenterX() - o.getCenterX(),2)+Math.pow(this.getCenterY() - o.getCenterY(),2));
        
        if(distance <= (this.getCircle().getRadius()*2 + this.getCircle().getStrokeWidth()/2))
            return 0;
        return 1;
       
    }
}
