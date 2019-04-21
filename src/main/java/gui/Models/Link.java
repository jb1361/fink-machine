package gui.Models;

import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;

public class Link {
    Node start;
    Node end;
    String text;
    int canvasIndex;
    double textPadding;
    double parallelAngle;
    double perpendicularAngle;
    double arcAngle;
    Anchor startAnchor;
    Anchor endAnchor;
    Anchor arcAnchor;
    javafx.scene.Node linePath;
    Arc arcPath;

    public Link() {
        initLink();
    }

    public Link(Node a, Node b) {
        initLink();
        this.start = a;
        this.end = b;
    }

    public Link(Node a, Node b, String text) {
        initLink();
        this.start = a;
        this.end = b;
        this.text = text;
    }

    public Link(Node a, Node b, String text, Anchor startAnchor, Anchor endAnchor) {
        initLink();
        this.start = a;
        this.end = b;
        this.text = text;
        this.startAnchor = startAnchor;
        this.endAnchor = endAnchor;
    }

    public void initLink() {
        this.start = new Node();
        this.end = new Node();
        this.text = "";
        // gui-only data
        /// Adjust text during line placement
        this.textPadding = 0;
        this.parallelAngle = 0;
        this.perpendicularAngle = 0;
        this.arcAngle = 0;
        /// Canvas attachment
        this.startAnchor = new Anchor();
        this.endAnchor = new Anchor();
        this.arcAnchor = new Anchor();
        /// Canvas children objects
        linePath = new Path();
        arcPath = new Arc();
    }

    public boolean hasArc() {
        return (this.arcAngle != 0);
    }

    public Circle getCircle(Anchor start, Anchor end, Anchor rad) {

        // Get distance
        Anchor startD = new Anchor(end.x - start.x, end.y - start.y);
        Anchor endD = new Anchor(rad.x - end.x, rad.y - end.y);

        double startSlope = -1;
        double endSlope = -1;

        // This needs to throw an error
        if (startD.x != 0 && endD.x != 0) {
            startSlope = startD.y / startD.x;
            endSlope = endD.y / endD.x;
        }

        double centerX = ((startSlope * endSlope * (start.y - rad.y)) + (endSlope * (start.x + end.x)) - (startSlope * (end.x + rad.x))) / (2 * (endSlope - startSlope));
        double centerY = (-1 * (centerX - (start.x + end.x) / 2)) / (startSlope + (start.y + end.y) / 2);
        double radius = Math.sqrt(Math.pow(end.x - centerX, 2) + Math.pow(end.y - centerY, 2));

        Circle circle = new Circle(centerX, centerY, radius);
        return circle;
    }


    public Node getStart() {
        return this.start;
    }

    public Node getEnd() {
        return this.end;
    }

    public String getText() {
        return this.text;
    }

    public double getTextPadding() {
        return this.textPadding;
    }

    public double getParallelAngle() {
        return this.parallelAngle;
    }

    public double getPerpendicularAngle() {
        return this.perpendicularAngle;
    }

    public Anchor getArcAnchor() {
        return this.arcAnchor;
    }

    public Anchor getStartAnchor() {
        return this.startAnchor;
    }

    public Anchor getEndAnchor() {
        return this.endAnchor;
    }

    public int getIndex() {
        return this.canvasIndex;
    }

    public javafx.scene.Node getLine() {
        return this.linePath;
    }

    public Arc getArc() {
        return this.arcPath;
    }

    public double getArcAngle() {
        return this.arcAngle;
    }

    public void setStart(Node n) {
        this.start = n;
    }

    public void setEnd(Node n) {
        this.end = n;
    }

    public void setText(String s) {
        this.text = s;
    }

    public void setTextPadding(double d) {
        this.textPadding = d;
    }

    public void setParallelAngle(double d) {
        this.parallelAngle = d;
    }

    public void setPerpendicularAngle(double d) {
        this.perpendicularAngle = d;
    }

    public void setArcAnchor(Anchor a) {
        this.arcAnchor = a;
    }

    public void setStartAnchor(Anchor a) {
        this.startAnchor = a;
    }

    public void setEndAnchor(Anchor a) {
        this.endAnchor = a;
    }

    public void setLine(javafx.scene.Node p) {
        this.linePath = p;
    }

    public void setArc(Arc a) {
        this.arcPath = a;
    }

    public void setIndex(int i) {
        this.canvasIndex = i;
    }

    public void setArcAngle(double a) {
        this.arcAngle = a;
    }

}
