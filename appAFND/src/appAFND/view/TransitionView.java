/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Kazuto
 */
public class TransitionView {

    private QuadCurve curve;
    private AnchorCenter center;
    private Anchor start;
    private Anchor end;
    private Arrow arrow;
    private Text text;
    private Group group = new Group();
    private double canvasHeight, canvasWidth;
    
    public TransitionView(Circle c1, Circle c2, String label, double canvasHeight, double canvasWidth){
        this.curve = new QuadCurve();
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        
        //Calculate the start/end points of the transition
        double sx = calcX(c1,c2);
        double sy = calcY(c1,c2);
        double ex = calcX(c2,c1);
        double ey = calcY(c2,c1);
        
        this.curve.setStartX(sx);
        this.curve.setStartY(sy);        
        this.curve.setEndX(ex);
        this.curve.setEndY(ey);
        
        double midx = (sx+ex)/2;
        double midy = (sy+ey)/2;
        this.curve.setControlX(midx);
        this.curve.setControlY(midy);     
        
        this.curve.setStroke(Color.web("#0169CE"));
        this.curve.setStrokeWidth(2);
        this.curve.setStrokeLineCap(StrokeLineCap.BUTT);
        this.curve.setFill(Color.TRANSPARENT);
        
        this.text = new Text(label);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        Font font = this.text.getFont();
        float textWidth = fontLoader.computeStringWidth(label, font);
        this.text.setX(midx-(textWidth/2));
        this.text.setY(midy-12);        
        
        this.center = new AnchorCenter(Color.GOLD, curve, text, textWidth);
        this.start = new Anchor(Color.TRANSPARENT, curve.startXProperty(), curve.startYProperty(), curve, center, text, textWidth, true);
        this.end = new Anchor(Color.TRANSPARENT, curve.endXProperty(), curve.endYProperty(), curve, center, text, textWidth, false);
        double[] arrowShape = new double[]{0, 0, 5, 10, -5, 10};
        this.arrow = new Arrow(curve, 1f, arrowShape);
        
        this.group.getChildren().addAll(this.curve, this.arrow, this.start, this.center, this.end, this.text);
    }

    public Group getTransition() {
        return this.group;
    }

    private double calcX(Circle c1, Circle c2) {
        double Ax = c1.getCenterX();
        double Ay = c1.getCenterY();
        double Bx = c2.getCenterX();
        double By = c2.getCenterY();
        double r = c1.getRadius()+(c1.getStrokeWidth()/2);
        double distance = Math.sqrt(Math.pow(Bx-Ax,2) + Math.pow(By-Ay,2));
        return Ax+(r*(Bx-Ax)/distance);
    }
    
    private double calcY(Circle c1, Circle c2) {
        double Ax = c1.getCenterX();
        double Ay = c1.getCenterY();
        double Bx = c2.getCenterX();
        double By = c2.getCenterY();
        double r = c1.getRadius()+(c1.getStrokeWidth()/2);
        double distance = Math.sqrt(Math.pow(Bx-Ax,2) + Math.pow(By-Ay,2));
        return Ay+(r*(By-Ay)/distance);
    }

    


    public class Arrow extends Polygon {

        public double rotate;
        public float t;
        QuadCurve curve;
        Rotate rz;

        public Arrow(QuadCurve curve, float t) {
            super();
            this.curve = curve;
            this.t = t;
            init();
        }

        public Arrow(QuadCurve curve, float t, double[] arg0) {
            super(arg0);
            this.curve = curve;
            this.t = t;
            init();
        }

        private void init() {
            //Arrow color
            setFill(Color.web("#0169CE"));

            rz = new Rotate();
            {
                rz.setAxis(Rotate.Z_AXIS);
            }
            getTransforms().addAll(rz);

            update();
        }

        public void update() {
            double size = Math.max(curve.getBoundsInLocal().getWidth(), curve.getBoundsInLocal().getHeight());
            double scale = size / 4d;

            Point2D ori = eval(curve, t);
            Point2D tan = evalDt(curve, t).normalize().multiply(scale);

            setTranslateX(ori.getX());
            setTranslateY(ori.getY());

            double angle = Math.atan2(tan.getY(), tan.getX());

            angle = Math.toDegrees(angle);

            // arrow origin is top => apply offset
            double offset = -90;
            if (t > 0.5) {
                offset = +90;
            }

            rz.setAngle(angle + offset);

        }

        /**
         * Evaluate the quad curve at a parameter 0<=t<=1, returns a Point2D
         * @param c
         *
         *
         * the QuadCurve Curve
         * @param t param between 0 and 1
         * @return a Point2D
         */
        private Point2D eval(QuadCurve c, float t) {
            Point2D p = new Point2D(Math.pow(1 - t, 2) * c.getStartX()
                    + 2 * t * (1 - t) * c.getControlX()
                    + t * t * c.getEndX(),
                    Math.pow(1 - t, 2) * c.getStartY()
                    + 2 * t * (1 - t) * c.getControlY()
                    + t * t * c.getEndY());
            return p;
        }

        /**
         * Evaluate the tangent of the quad curve at a parameter 0<=t<=1,
         * returns a Point2D @param c
         *
         * the QuadCurve @param t param between 0 and 1 @return a Point2D
         *
         */
        private Point2D evalDt(QuadCurve c, float t) {
            Point2D p = new Point2D(-2 * (1 - t) * c.getStartX()
                    + 2 * (1 - 2 * t) * c.getControlX()
                    + 2 * t * c.getEndX(),
                    -2 * (1 - t) * c.getStartY()
                    + 2 * (1 - 2 * t) * c.getControlY()
                    + 2 * t * c.getEndY());
            return p;
        }
    }

    class Anchor extends Circle {

        Anchor(Color color, DoubleProperty x, DoubleProperty y, QuadCurve curve, AnchorCenter center, Text text, float labelWidth, boolean isStart) {
            super(x.get(), y.get(), 10);

            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(1);
            setStrokeType(StrokeType.OUTSIDE);

            x.bind(centerXProperty());
            y.bind(centerYProperty());
            //enableDrag(curve, center, text, labelWidth, isStart);
        }

        // make a node movable by dragging it around with the mouse.
        private void enableDrag(QuadCurve curve, AnchorCenter center, Text text, float labelWidth, boolean isStart) {
            final Delta dragDelta = new Delta();
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = getCenterX() - mouseEvent.getX();
                    dragDelta.y = getCenterY() - mouseEvent.getY();
                    getScene().setCursor(Cursor.MOVE);
                }
            });
            setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getScene().setCursor(Cursor.HAND);
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    double newX = mouseEvent.getX() + dragDelta.x;
                    if (newX > getRadius() && newX < canvasWidth-getRadius()) {
                        setCenterX(newX);
                        if (isStart) {
                            center.setX((curve.endXProperty().get() + newX) * 0.25 + 0.5 * curve.controlXProperty().get());
                        }
                        else {
                            center.setX((curve.startXProperty().get() + newX) * 0.25 + 0.5 * curve.controlXProperty().get());
                        }
                        text.setX(center.getX()-(labelWidth/2));

                    }
                    double newY = mouseEvent.getY() + dragDelta.y;
                    if (newY > getRadius() && newY < canvasHeight-getRadius()) {
                        setCenterY(newY);
                        if (isStart) {
                            center.setY((curve.endYProperty().get() + newY) * 0.25 + 0.5 * curve.controlYProperty().get());
                            
                        }
                        else {
                            center.setY((curve.startYProperty().get() + newY) * 0.25 + 0.5 * curve.controlYProperty().get());
                        }
                        text.setY(center.getY()-10);

                    }

                    // update arrow position          
                    arrow.update();

                }
            });
            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown()) {
                        getScene().setCursor(Cursor.HAND);
                    }
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown()) {
                        getScene().setCursor(Cursor.DEFAULT);
                    }
                }
            });
        }

        // records relative x and y co-ordinates.
        private class Delta {
            double x, y;
        }
    }

    class AnchorCenter extends Circle {

        AnchorCenter(Color color, QuadCurve curve, Text text, float labelWidth) {
            DoubleProperty x = curve.controlXProperty();
            DoubleProperty y = curve.controlYProperty();
            DoubleProperty sx = curve.startXProperty();
            DoubleProperty sy = curve.startYProperty();
            DoubleProperty ex = curve.endXProperty();
            DoubleProperty ey = curve.endYProperty();

            this.setCenterX(x.get() / 2 + (sx.get() + ex.get()) / 4);
            this.setCenterY(y.get() / 2 + (sy.get() + ey.get()) / 4);
            this.setRadius(10);
            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(1);
            setStrokeType(StrokeType.OUTSIDE);

            enableDrag(curve, text, labelWidth);
        }

        // make a node movable by dragging it around with the mouse.
        private void enableDrag(QuadCurve curve, Text text, float labelWidth) {
            final Delta dragDelta = new Delta();
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = getCenterX() - mouseEvent.getX();
                    dragDelta.y = getCenterY() - mouseEvent.getY();
                    getScene().setCursor(Cursor.CLOSED_HAND);
                }
            });
            setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getScene().setCursor(Cursor.OPEN_HAND);
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    double newX = mouseEvent.getX() + dragDelta.x;
                    
                    if (newX > getRadius() && newX < canvasWidth-getRadius()) {
                        setCenterX(newX);
                        text.setX(newX-(labelWidth/2));
                        curve.setControlX(2 * newX - ((curve.startXProperty().get() + curve.endXProperty().get()) * (0.5)));
                    }
                    
                    double newY = mouseEvent.getY() + dragDelta.y;
                    if (newY > getRadius()+text.getBoundsInLocal().getHeight() && newY < canvasHeight) {                        
                        setCenterY(newY);
                        text.setY(newY-12);
                        curve.setControlY(2 * newY - ((curve.startYProperty().get() + curve.endYProperty().get()) * (0.5)));
                    }

                    // update arrow positions          
                    arrow.update();

                }
            });
            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown()) {
                        getScene().setCursor(Cursor.OPEN_HAND);
                    }
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown()) {
                        getScene().setCursor(Cursor.DEFAULT);
                    }
                }
            });
        }

        private void setX(double d) {
            setCenterX(d);
        }

        private void setY(double d) {
            setCenterY(d);
        }

        private double getX() {
            return getCenterX();
        }
        
        private double getY() {
            return getCenterY();
        }

        // records relative x and y co-ordinates.
        private class Delta {
            double x, y;
        }
    }
}