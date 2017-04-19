package appAFND.controller;

import appAFND.model.Node;
import appAFND.view.NodeView;

/**
 *
 * @author Victor
 */
public class NodeController implements Comparable<NodeController>
{

    private Node model;
    private NodeView view;

    public NodeController(Node model, NodeView view)
    {
        this.model = model;
        this.view = view;
    }
    
    private Node getNode() // PARCHE
    {
        return this.model;
    }
    
    public void setNodeX(double x)
    {
        this.model.setX(x);
    }

    public void setNodeY(double y)
    {
        this.model.setY(y);
    }
    
    public void setNodeRadius(double radius)
    {
        this.model.setRadius(radius);
    }
    
    public void setNodeLabel(int label)
    {
        this.model.setLabel(label);
    }
    
    public double getNodeX()
    {
        return this.model.getX();
    }
    
    public double getNodeY()
    {
        return this.model.getY();
    }
    
    public double getNodeRadius()
    {
        return this.model.getRadius();
    }
    
    public int getNodeLabel()
    {
        return this.model.getLabel();
    }
    
    public void showNode()
    {
        this.view.showNode(this.getNodeX(), this.getNodeY(),this.getNodeRadius(), this.getNodeLabel());
    }

    @Override
    public int compareTo(NodeController o)
    {
        return this.model.compareTo(o.getNode());
    }

}
