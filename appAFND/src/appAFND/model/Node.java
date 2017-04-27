package appAFND.model;

/**
 *
 * @author Victor
 */
public class Node implements Comparable<Node>
{
    private double x;
    private double y;
    private double radius;
    private int label;

    public Node(double x, double y, double radius, int label) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.label = label;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getLabel()
    {
        return label;
    }

    public void setLabel(int label)
    {
        this.label = label;
    }

    @Override
    public int compareTo(Node o) {

        double distance = Math.sqrt(Math.pow(this.x - o.getX(),2) + Math.pow(this.y - o.getY(), 2));

        if(distance <= (radius*2)+6) // Radius from outer class
            return 0;
        return 1;
    }
}
