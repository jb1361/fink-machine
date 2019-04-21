package gui;

import gui.Models.Anchor;
import gui.Models.Link;
import gui.Models.LinkStore;
import gui.Models.NodeStore;
import gui.Models.Settings;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

/**
 * TODO
 * ** Allow drag of links (Change anchors)
 * ** Allow link arc
 * ** Context Menu functions
 * ** Redraw links after node movement
 */

/***
 * Reference: StackPane children
 *** 0 : Root ellipse
 *** 1 : Text
 *** 2 : Optional AcceptState ellipse
 */

public class CanvasPane extends Pane {

    final Canvas canvas;
    final GraphicsContext gc;
    final Settings settings;
    NodeStore nodeStore;
    LinkStore linkStore;
    FSMControls rootControls;
    MenuController menu;
    //gui.Models.Node activeNode;
    //ArrayList<Threads> threads;


    CanvasPane(NodeStore nodeStore,
               LinkStore linkStore,
               Settings settings) {
        // Gather shared data
        this.nodeStore = nodeStore;
        this.linkStore = linkStore;
        this.settings = settings;

        this.canvas = new Canvas();
        this.gc = canvas.getGraphicsContext2D();

        // Redraw canvas when container is resized
        //canvas.widthProperty().addListener(e -> draw(canvas));
        //canvas.heightProperty().addListener(e -> draw(canvas));
    }

    public void initCanvas() {
        canvas.setId("canvas");
        this.getChildren().add(canvas);
        // Set initial dimensions
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        menu.createCanvasContextMenu(this.canvas);
    }

    public void setMenu(MenuController menu) {
        this.menu = menu;
    }

    public GraphicsContext getGC() {
        return this.canvas.getGraphicsContext2D();
    }

    public void drawState(StackPane sp) {
        this.getChildren().add(sp);
    }

    public Anchor getDefaultStateCoordinate(gui.Models.Node node) {
        double r = settings.getNodeRadius();
        double x = (canvas.getWidth() / 2) - r;
        double y = (canvas.getHeight() / 2) - r;
        return new Anchor(x, y);
    }

    public Anchor getDefaultStateCoordinate(StackPane sp, boolean accept) {
        double r = settings.getNodeRadius();
        double x = (canvas.getWidth() / 2) - r;
        double y = (canvas.getHeight() / 2) - r;
        return new Anchor(x, y);
    }

    public StackPane generateNewNodePane(gui.Models.Node node) {

        StackPane sp = node.getPane();
        Anchor anchor = node.getAnchor();
        boolean isAcceptState = node.getAccept();

        Text text = new Text(node.getText());
        Circle state = createCircle(anchor.getX(), anchor.getY(), this.settings.getNodeRadius(), false);
        sp.getChildren().addAll(state, text);
        // Verify accept state
        if (isAcceptState) {
            sp.getChildren().add(createCircle(anchor.getX(), anchor.getY(), this.settings.getNodeRadius(), isAcceptState));
            // Adjust canvas positioning for extra marker
            anchor.setX(anchor.getX() - 5);
            anchor.setY(anchor.getY() - 5);
        }
        // Set state's position on canvas
        sp.setLayoutX(anchor.getX());
        sp.setLayoutY(anchor.getY());
        // Set a few handlers before the pane is drawn
        setStandardEvents(sp);
        // Generate a context menu for each unique node
        menu.createContextMenu(sp);

        return sp;
    }

    public void drawDot(double x, double y) {
        gc.fillOval(x, y, 3, 3);
    }

    public Path buildLinkPath(Anchor startPoint, Anchor endPoint, String text) {
        // Calculate angles
        double endAngle = Math.atan2(endPoint.getY() - startPoint.getY(), endPoint.getX() - startPoint.getX());
        double dx = Math.cos(endAngle);
        double dy = Math.sin(endAngle);

        Path path = new Path();
        ObservableList e = path.getElements();
        // Draw line
        e.add(new MoveTo(startPoint.getX(), startPoint.getY()));
        e.add(new LineTo(endPoint.getX(), endPoint.getY()));
        e.add(new MoveTo(endPoint.getX(), endPoint.getY()));
        // Draw arrow tip
        e.add(new LineTo(endPoint.getX() - 8 * dx + 5 * dy,
                endPoint.getY() - 8 * dy - 5 * dx));
        e.add(new MoveTo(endPoint.getX(), endPoint.getY()));
        e.add(new LineTo(endPoint.getX() - 8 * dx - 5 * dy,
                endPoint.getY() - 8 * dy + 5 * dx));
        return path;
    }

    // TODO Draw links with arcs
    /*
    public void drawLink(Anchor arcStart, Anchor arcEnd, Circle circle, double scale, double startAngle, double endAngle, String text) {
        
        // Create arrow head
        //drawArrowHead(arcEnd, endAngle - scale * (Math.PI / 2));
        
        // Prepare link text
        if (endAngle < startAngle)
            endAngle += Math.PI * 2;
        double textAngle = ((startAngle + endAngle) / 2) + (scale * Math.PI);
        Anchor textAnchor = new Anchor(circle.getCenterX() + circle.getRadius() * Math.cos(textAngle),
                                 circle.getCenterY() + circle.getRadius() * Math.sin(textAngle));
        // Draw text
        //drawLinkText(textAnchor, text, textAngle);
        
    }
    */

    // TODO Draw link text near line
    /*
    public void drawLinkText(Anchor textAnchor, String text, double textAngle) {
        Text measure = new Text(text);
        double width = measure.getLayoutBounds().getWidth();
        // Centers text in link
        textAnchor.x -= width / 2;
        
        Anchor angles = new Anchor(Math.cos(textAngle), Math.sin(textAngle));
        Anchor corners = new Anchor( ( width / 2 + 5 ) * ( angles.x > 0 ? 1 : -1 ), 
                                     ( 15 * (angles.y > 0 ? 1 : -1 ) ) );
        double adjust = angles.y * Math.pow( Math.abs( angles.y ), 40 ) * corners.x - angles.x * Math.pow( Math.abs( angles.x ), 10 ) * corners.y;
        
        // Adjust text anchor
        textAnchor.x = Math.round( textAnchor.x + corners.x - angles.y * adjust );
        textAnchor.y = Math.round( textAnchor.y + corners.y - angles.x * adjust );
        // Draw text
        gc.fillText(text, textAnchor.x, textAnchor.y + 5);
        
    } */

    private void handleNodeDrag(StackPane sp, double endX, double endY) {
        // Find which node object was moved
        String activeNodeText = ((Text) sp.getChildren().get(1)).getText();
        // Convert event movement into local coordinates
        Point2D localCoordinates = canvas.screenToLocal(endX, endY);
        double newX = localCoordinates.getX() - (sp.getWidth() / 2);
        double newY = localCoordinates.getY() - (sp.getHeight() / 4);
        // Save the new coordinates
        gui.Models.Node activeNode = nodeStore.findNodeFromText(activeNodeText);
        // Move state node
        sp.setLayoutX(newX);
        sp.setLayoutY(newY);
        // Find all attached links
        ArrayList<Link> links = linkStore.trackNode(activeNode);
        // Hide links until node is settled
        links.forEach(l -> {
            this.getChildren().remove(this.getChildren().get(l.getIndex()));
        });
    }

    /**
     *
     * @param x : node center X
     * @param y : node center Y
     * @param r : node radius
     * @param a : isAcceptState
     * @return new circle object
     */
    private Circle createCircle(double x, double y, double r, boolean a) {
        // Increase size to wrap state node
        if (a) r += 5;
        Circle newState = new Circle(x, y, r);
        newState.setStroke(this.settings.getStateColor());
        newState.setFill(Color.rgb(0, 0, 0, 0));
        return newState;
    }

    private void setStandardEvents(StackPane sp) {
        sp.setOnDragDetected((MouseEvent m) -> {
            sp.startFullDrag();
            // Prepare links for node movement
            /// Find which node object was moved
            String activeNodeText = ((Text) sp.getChildren().get(1)).getText();
            /// Find all attached links
            ArrayList<Link> links = linkStore.trackNode(this.nodeStore.findNodeFromText(activeNodeText));
            /// Hide links until node is settled
            //****** THIS NEEDS RE-THOUGHT *******/
            links.forEach(l -> {
                this.getChildren().remove(this.getChildren().get(l.getIndex()));
            });
        });

        sp.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                handleNodeDrag(sp, event.getSceneX(), event.getSceneY());
                event.consume();
            }
        });

        sp.setOnMouseReleased((MouseEvent e) -> {
            // Find which node object was moved
            String activeText = ((Text) sp.getChildren().get(1)).getText();
            // Save the new coordinates
            Anchor anchor = new Anchor(sp.getLayoutX(), sp.getLayoutY());
            gui.Models.Node activeNode = nodeStore.findNodeFromText(activeText);
            activeNode.setAnchor(anchor);
            // Find all attached links
            ArrayList<Link> links = linkStore.trackNode(activeNode);
            // Update each with new anchors
            links.forEach(l -> {
                this.rootControls.resetLink(l);
            });
        });
    }

    public void setRootControls(FSMControls rootControls) {
        this.rootControls = rootControls;
    }

}