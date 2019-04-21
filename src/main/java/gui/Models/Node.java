package gui.Models;

import gui.CanvasPane;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javacalculus.core.CALC;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;

public class Node {
    
    class Attempt {
        Anchor point;
        double cost;
        public Attempt(Anchor point, double cost) {
            this.point = point;
            this.cost = cost;
        }
        public Anchor getPt() { return this.point; }
        public double getCost() { return this.cost; }
        public void setPt(Anchor a) { this.point = a; }
        public void setCost(double d) { this.cost = d; }
    }
    
    Anchor anchor;
    boolean isAcceptState;
    String text;
    ArrayList<Function> functions;
    StackPane pane;
    
    public Node() {
        isAcceptState = false;
        text = "";
        anchor = new Anchor();
        pane = new StackPane();
        functions = new ArrayList<Function>();
    }
    
    public Node(String text, boolean isAcceptState) {
        this.text = text;
        this.anchor = new Anchor();
        this.pane = new StackPane();
        this.isAcceptState = isAcceptState;
    }
    
    public Node(String text, boolean isAcceptState, StackPane pane) {
        this.text = text;
        this.pane = pane;
        this.anchor = new Anchor();
        this.isAcceptState = isAcceptState;
    }
    
    public Node copyNode(Node src, Anchor a) {
        Node temp = new Node();
        temp.setAnchor(a);
        temp.setAccept(src.getAccept());
        temp.setText(src.getText());
        //temp.setFunction();
        temp.setPane(src.getPane());
        return temp;
    }
    
    // Should include mouse offsets
    public void setAnchors(double x, double y) {
        this.anchor.x = x;
        this.anchor.y = y;
    }
    
    // Requires a radius be provided from calling function
    public boolean containsPoint(double x, double y, double r) {
        double tempX = x - this.anchor.x;
        double tempY = y - this.anchor.y;
        
        return Math.pow(tempX,2) + Math.pow(tempY,2) < (r * r);
    }
    
    // Requires a radius be provided from calling function
    /**
     * 
     * @param x - Reference point 
     * @param y - Reference point
     * @param nA - isAcceptState
     * @param ref - Reference midpoint
     * @return new Anchor point
     */
    public Anchor findClosestAnchor(Anchor mid, Anchor center, boolean isAcceptState) {
        CanvasPane cp = (CanvasPane) pane.getParent();
        Anchor tempAnchor = new Anchor();
        // Grab circle from selected state
        Circle state = ((Circle) this.pane.getChildren().get(0));
        double r = state.getRadius();
        // Adjust for extra circle
        if (isAcceptState) r += 5;
        // Calculate vector
        double vX = mid.x - center.x;
        double vY = mid.y - center.y;
        // Calculate magnitude
        double vM = Math.sqrt(Math.pow(vX, 2) + Math.pow(vY, 2));
        // Find closest point
        double pX = center.x + (vX / vM) * r;
        double pY = center.y + (vY / vM) * r;
        tempAnchor.x = pX;
        tempAnchor.y = pY;
        
        return tempAnchor;
    }
    
    public String getText() { return this.text; }
    public void setText(String text) { this.text = text; }
    public boolean getAccept() { return this.isAcceptState; }
    public void setAccept(boolean b) { this.isAcceptState = b; }
    public Anchor getAnchor() { return this.anchor; }
    public void setAnchor(Anchor a) { this.anchor = a; }
    public StackPane getPane() { return this.pane; }
    public void setPane(StackPane sp) { this.pane = sp; }
}
