package appAFND.view;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Victor
 */
public class NodeView 
{
    private Circle circle;
    private Text text;

    public NodeView(double x, double y, double radius,int index)
    {
        this.circle = new Circle(x, y, radius, Color.DODGERBLUE);
        this.circle.setStroke(Color.BLACK);
        String label = "Q"+index;        
        this.text = new Text(label);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        Font font = this.text.getFont();
        float labelWidth = fontLoader.computeStringWidth(label, font);
        
        this.text.setX(x-(labelWidth/2));
        this.text.setY(y+4);
        this.text.setWrappingWidth(radius*2);
    }
    
    public void drawNode(Group g)
    {
        g.getChildren().add(this.circle);
        g.getChildren().add(this.text);        
    }
}
