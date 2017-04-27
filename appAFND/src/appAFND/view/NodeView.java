package appAFND.view;

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
public class NodeView
{

    private Circle circle;
    private Text text;
    private ContextMenu context = new ContextMenu();
    

    public NodeView(double x, double y, double radius, int index)
    {
        MenuItem changeInitial = new MenuItem("Change to initial");
        MenuItem changeFinal = new MenuItem("Change to final");
        context.getItems().addAll(changeInitial, changeFinal);
        
        this.circle = new Circle(x, y, radius, Color.DEEPSKYBLUE);
        this.circle.setStroke(Color.DEEPSKYBLUE);
        this.circle.setStrokeWidth(3);
        
        String label = "Q" + index;
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
                contextMenu(event);
            }
        });

        this.text.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                contextMenu(event);
            }
        });
        
        changeInitial.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event)
            {
                // Para cambiar a nodo inicial.
                isInitialNodo(event);
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
    
    
    private void isFinalNodo()
    {
        circle.setStroke(Color.web("#006485"));
        circle.setStrokeWidth(3);
    }
    
    private void isInitialNodo(ActionEvent event)
    {
        Polygon arrow = new Polygon(new double[] {0,0,10,10,-10,10});
        arrow.setTranslateX(circle.getCenterX()-circle.getRadius());
        arrow.setTranslateY(circle.getCenterY());
        arrow.getTransforms().add(new Rotate(90,0,0));
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

    public void drawNode(Group g)
    {
        g.getChildren().add(this.circle);
        g.getChildren().add(this.text);
    }
}
