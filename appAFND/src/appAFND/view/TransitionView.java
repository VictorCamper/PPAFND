/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

import appAFND.controller.StateController;
import appAFND.controller.TransitionController;
import appAFND.model.Automaton;
import appAFND.model.NFA;
import appAFND.model.Transition;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
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
    private double midx,midy;
    private Group group = new Group();
    private double canvasHeight, canvasWidth;
    private StateController from, to;
    private AFNDController afndController;
    private TransitionController tController;
    private ContextMenu context= new ContextMenu();
    
    public TransitionView(StateController from, StateController to, String label, double canvasHeight, double canvasWidth, AFNDController afndController){
        this.curve = new QuadCurve();
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.from = from;
        this.to = to;
        this.afndController = afndController;
        MenuItem edit = new MenuItem("Edit transition");
        //MenuItem add = new MenuItem("add");
        MenuItem delete = new MenuItem("Delete transition");
        context.getItems().addAll(edit,delete);
        
        Circle c1 = this.from.getStateView().getCircle();
        Circle c2 = this.to.getStateView().getCircle();
        
        this.text = new Text(label);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        Font font = this.text.getFont();
        float textWidth = fontLoader.computeStringWidth(label, font);
        
        if(this.from.equals(to)){
            if(c1.getCenterY()-c1.getRadius()==0)
            {
                this.curve.setStartX(c1.getCenterX()-5);
                this.curve.setStartY(c1.getCenterY()+(c1.getRadius()+(c1.getStrokeWidth()/2))-1.5);        
                this.curve.setEndX(c1.getCenterX()+5);
                this.curve.setEndY(c1.getCenterY()+(c1.getRadius()+(c1.getStrokeWidth()/2))-1.5);     
                this.curve.setControlX(c1.getCenterX());
                this.curve.setControlY(c1.getCenterY()+70);   

                this.text.setX(c1.getCenterX()-(textWidth/2));
                this.text.setY(c1.getCenterY()+35);  
            }
            else{
                this.curve.setStartX(c1.getCenterX()-5);
                this.curve.setStartY(c1.getCenterY()-(c1.getRadius()+(c1.getStrokeWidth()/2))+1.5);        
                this.curve.setEndX(c1.getCenterX()+5);
                this.curve.setEndY(c1.getCenterY()-(c1.getRadius()+(c1.getStrokeWidth()/2))+1.5);     
                this.curve.setControlX(c1.getCenterX());
                this.curve.setControlY(c1.getCenterY()-70);   

                this.text.setX(c1.getCenterX()-(textWidth/2));
                this.text.setY(c1.getCenterY()-59);  
            }
        }
        else{
            //Calculate the start/end points of the transition            
            double sx = calcX(c1.getCenterX(),c1.getCenterY(),c2.getCenterX(),c2.getCenterY(),c1.getRadius(),c1.getStrokeWidth());
            double sy = calcY(c1.getCenterX(),c1.getCenterY(),c2.getCenterX(),c2.getCenterY(),c1.getRadius(),c1.getStrokeWidth());
            double ex = calcX(c2.getCenterX(),c2.getCenterY(),c1.getCenterX(),c1.getCenterY(),c2.getRadius(),c2.getStrokeWidth());
            double ey = calcY(c2.getCenterX(),c2.getCenterY(),c1.getCenterX(),c1.getCenterY(),c2.getRadius(),c2.getStrokeWidth());

            this.curve.setStartX(sx);
            this.curve.setStartY(sy);        
            this.curve.setEndX(ex);
            this.curve.setEndY(ey);

            midx = (sx+ex)/2;
            midy = (sy+ey)/2;
            this.curve.setControlX(midx);
            this.curve.setControlY(midy); 
            
            this.text.setX(midx-(textWidth/2));
            this.text.setY(midy-12);  
        }
        this.curve.setStroke(Color.web("#0169CE"));
        this.curve.setStrokeWidth(2);
        this.curve.setStrokeLineCap(StrokeLineCap.BUTT);
        this.curve.setFill(null);
        
        
              
        
        this.center = new AnchorCenter(Color.GOLD, curve, text, textWidth, this);
        this.start = new Anchor(Color.TRANSPARENT, curve.startXProperty(), curve.startYProperty(), curve, center, text, textWidth, true, from, this);
        this.end = new Anchor(Color.TRANSPARENT, curve.endXProperty(), curve.endYProperty(), curve, center, text, textWidth, false, to, this);
        double[] arrowShape = new double[]{0, 0, 5, 10, -5, 10};
        this.arrow = new Arrow(curve, 1f, arrowShape);
        
        this.group.getChildren().addAll(this.curve, this.arrow, this.start, this.center, this.end, this.text);
        
        this.curve.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.getButton() == MouseButton.SECONDARY)
                    contextMenu(event);
                /*else
                    afndController.transitionClicked(tController);*/
            }
        });
        
        this.curve.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                ((Node) event.getSource()).setCursor(Cursor.HAND);
            }
        });
        
        this.curve.setOnMouseExited(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                ((Node) event.getSource()).setCursor(Cursor.DEFAULT);
            }
        });
        
        edit.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                //boolean continuee = false;
                Automaton automaton = afndController.getAutomaton();
                String[] chars = afndController.dialogTransition("Modify");
                if (!chars[0].isEmpty()){ 
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
                                    if(automaton instanceof NFA){
                                        //((NFA)automaton).addTransition(from, to, c2s);
                                    }
                                }
                                else
                                    if (!label.contains(c2s))
                                        label = label.concat(", ").concat(c2s);
                                        if(automaton instanceof NFA){
                                            //((NFA)automaton).addTransition(from, to, c2s);
                                        }
                            }
                        }
                        //Edit transition
                        HashMap<String, ArrayList<StateController>> aux = afndController.getAutomaton().getF().get(from);
                        
                        for(Character c :afndController.getAutomaton().getAlphabet().getCharacters()){
                            String s = c.toString();
                            aux.get(s).remove(to);
                        }
                        
                        text.setText(label);
                        String[] tran2 = label.split(", ");
                        
                        for(String s2 : tran2){
                            //if (!s.contains(s2))
                                aux.get(s2).add(to);
                        }
                        
                        
                        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
                        Font font = text.getFont();
                        float textWidth = fontLoader.computeStringWidth(label, font);
                        text.setX(midx-(textWidth/2));
                        
                        
                          
                    }
                }
                
                afndController.updateTable();              
                
            }
        });
        
        delete.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                //Delete from the intersections shape
                afndController.intersectionDeleteTransition(tController);
                
                //Delete from the list of transitions of the linked states
                to.getStateModel().getToState().remove(tController);
                from.getStateModel().getFromState().remove(tController);
                
                //Delete transition from the list of transitions
                afndController.transitionsList.remove(tController);
                afndController.transitionsRedList.remove(tController);
                
                //Delete from the logic
                HashMap<String, ArrayList<StateController>> aux = afndController.getAutomaton().getF().get(from);
                String[] tran = text.getText().split(", ");
                for(String s : tran){
                    aux.get(s).remove(to);
                }
                
                afndController.updateTable();
                
                //Delete visually
                afndController.getGroupTransitions().getChildren().remove(tController.getTransitionView().getTransition());
                afndController.getAutomatonPane().getChildren().remove(tController.getTransitionView().getTransition());
            }
        });
    }
    
    private void contextMenu(MouseEvent event)
    {
        if (event.getButton() == MouseButton.SECONDARY)
        {
            double x = event.getScreenX();
            double y = event.getScreenY();
            context.show(curve, x, y);
        } else
        {
            context.hide();
        }
    }
    
    public Group getTransition() {
        return this.group;
    }
    
    public QuadCurve getCurve(){
        return this.curve;
    }
    
    public StateController getFrom(){
        return this.from;
    }
    
    public StateController getTo(){
        return this.to;
    }
    
    public TransitionController getTransitionController(){
        return this.tController;
    }
    public void setTransitionController(TransitionController t){
        this.tController = t;
    }
    
    private double calcX(double Ax, double Ay, double Bx, double By, double radius, double strokeWidth) {
        double r = radius+(strokeWidth/2);
        double distance = Math.sqrt(Math.pow(Bx-Ax,2) + Math.pow(By-Ay,2));
        return Ax+(r*(Bx-Ax)/distance);
    }
    
    private double calcY(double Ax, double Ay, double Bx, double By, double radius, double strokeWidth) {        
        double r = radius+(strokeWidth/2);
        double distance = Math.sqrt(Math.pow(Bx-Ax,2) + Math.pow(By-Ay,2));
        return Ay+(r*(By-Ay)/distance);
    }

    void setRed() {
        this.arrow.setFill(Color.RED);
        this.curve.setStroke(Color.RED);
    }
    
    void setBlue() {
        this.arrow.setFill(Color.web("#0169CE"));
        this.curve.setStroke(Color.web("#0169CE"));
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

        Anchor(Color color, DoubleProperty x, DoubleProperty y, QuadCurve curve, AnchorCenter center, Text text, float labelWidth, boolean isStart, StateController state, TransitionView t) {
            super(x.get(), y.get(), 10);

            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(1);
            setStrokeType(StrokeType.OUTSIDE);

            x.bind(centerXProperty());
            y.bind(centerYProperty());
            enableDrag(curve, center, text, labelWidth, isStart, state, t);
        }

        // make a node movable by dragging it around with the mouse.
        private void enableDrag(QuadCurve curve, AnchorCenter center, Text text, float labelWidth, boolean isStart, StateController state, TransitionView t) {
            final Delta dragDelta = new Delta();
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = getCenterX() - mouseEvent.getX();
                    dragDelta.y = getCenterY() - mouseEvent.getY();
                    getScene().setCursor(Cursor.CLOSED_HAND);
                    afndController.intersectionDeleteTransition(t.getTransitionController());
                }
            });
            setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getScene().setCursor(Cursor.OPEN_HAND);
                    if(afndController.intersectionAddTransition(t.getTransitionController()))
                        t.setRed();                    
                    else
                        t.setBlue();
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    double newX = mouseEvent.getX() + dragDelta.x;
                    if (newX > getRadius() && newX < canvasWidth-getRadius()) {
                        //setCenterX(newX);
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
                        //setCenterY(newY);
                        if (isStart) {
                            center.setY((curve.endYProperty().get() + newY) * 0.25 + 0.5 * curve.controlYProperty().get());
                            
                        }
                        else {
                            center.setY((curve.startYProperty().get() + newY) * 0.25 + 0.5 * curve.controlYProperty().get());
                        }
                        text.setY(center.getY()-10);

                    }
                    if(newX > getRadius() && newX < canvasWidth-getRadius() && newY > getRadius() && newY < canvasHeight-getRadius()){
                        Circle c = state.getStateView().getCircle();
                        setCenterX(calcX(c.getCenterX(), c.getCenterY(), newX, newY, c.getRadius(), c.getStrokeWidth()));
                        setCenterY(calcY(c.getCenterX(), c.getCenterY(), newX, newY, c.getRadius(), c.getStrokeWidth()));
                        
                    }

                    // update arrow position          
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

        // records relative x and y co-ordinates.
        private class Delta {
            double x, y;
        }
    }

    class AnchorCenter extends Circle {

        AnchorCenter(Color color, QuadCurve curve, Text text, float labelWidth, TransitionView t) {
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

            enableDrag(curve, text, labelWidth, t);
        }

        // make a node movable by dragging it around with the mouse.
        private void enableDrag(QuadCurve curve, Text text, float labelWidth, TransitionView t) {
            final Delta dragDelta = new Delta();
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = getCenterX() - mouseEvent.getX();
                    dragDelta.y = getCenterY() - mouseEvent.getY();
                    getScene().setCursor(Cursor.CLOSED_HAND);
                    afndController.intersectionDeleteTransition(t.getTransitionController());
                }
            });
            setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getScene().setCursor(Cursor.OPEN_HAND);
                    if(afndController.intersectionAddTransition(t.getTransitionController()))
                        t.setRed();                    
                    else
                        t.setBlue();
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