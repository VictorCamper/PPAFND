package appAFND.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Victor
 */
public class NodeView 
{
    private GraphicsContext gc;

    public NodeView(GraphicsContext gc)
    {
        this.gc = gc;
    }
    
    public void showNode(double x, double y, double radius,int label)
    {
        this.gc.setFill(Color.DODGERBLUE);
        this.gc.setStroke(Color.DODGERBLUE);
        this.gc.strokeOval(x, y, radius*2, radius*2);
        this.gc.fillText("Q" + label, x + (radius/4)*3, y + radius);
    }
}
