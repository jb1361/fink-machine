package gui;

import gui.Models.Anchor;
import gui.Models.Link;
import gui.Models.LinkStore;
import gui.Models.Node;
import gui.Models.NodePair;
import gui.Models.NodeStore;
import gui.Models.Settings;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class FSMControls {
    NodeStore nodeStore; // Stores ("Text", Node Object)
    LinkStore linkStore; // Stores (NodePair Object, Link Object)
    CanvasPane cp;
    Settings settings;
    Stage rootStage;

    public FSMControls(Stage rootStage,
                       NodeStore nodeStore,
                       LinkStore linkStore,
                       Settings settings) {
        this.nodeStore = nodeStore;
        this.linkStore = linkStore;
        this.settings = settings;
        this.rootStage = rootStage;
    }

    public void setCanvasPane(CanvasPane cp) {
        this.cp = cp;
    }

    // ---------- STATES ------------ //
    // Create new state, add to list, and display in default location
    public void createState(String text, boolean isAcceptState) {
        Node newNode = new Node(text, isAcceptState);
        newNode.setAnchor(cp.getDefaultStateCoordinate(newNode));
        newNode.setPane(cp.generateNewNodePane(newNode));
        // Add to NodeStore
        nodeStore.addNode(newNode);
        // Draw to canvas
        cp.drawState(newNode.getPane());
    }


    // ---------- LINKS ------------ //
    public void resetLink(Link link) {
        // Remove link record
        this.linkStore.removeLink(link);
        // Create new link
        linkGenerator(link.getStart(), link.getEnd(), link.getText());
    }

    public void linkGenerator(Node startNode, Node endNode, String text) {
        Anchor centerA = generateLinkMidPoint(startNode);
        Anchor centerB = generateLinkMidPoint(endNode);
        Link link = generateLink(startNode, endNode, text, centerA, centerB);
        Path linkPath = generateLinkPath(link);
        // Draw link to canvas
        cp.getChildren().add(linkPath);
        // Record index for reference
        int nodeIndex = cp.getChildren().size() - 1;
        link.setIndex(nodeIndex);
        // Add to LinkStore
        linkStore.addLink(new NodePair(startNode.getText(), endNode.getText()), link);
    }

    public Anchor generateLinkMidPoint(Node node) {
        return new Anchor((node.getPane().getWidth() / 2) + node.getAnchor().getX(),
                (node.getPane().getHeight() / 2) + node.getAnchor().getY());
    }

    public void generateLink(Node a, Node b) {
        generateLink(a, b, "");
    }

    public void generateLink(Node a, Node b, String s) {
    }

    public Link generateLink(Node a, Node b, String text, Anchor start, Anchor end) {
        // Create new pre-populated link
        Link tempLink = new Link(a, b, text);
        // Create new anchor for link
        Anchor tempAnchor = new Anchor();
        // Find actual node midpoints
        tempAnchor.setX((start.getX() + end.getX()) / 2);
        tempAnchor.setY((start.getY() + end.getY()) / 2);
        // Find closest points on Nodes
        Anchor linkStart = a.findClosestAnchor(tempAnchor, start, a.getAccept());
        Anchor linkEnd = b.findClosestAnchor(tempAnchor, end, b.getAccept());
        // Save connecting points
        tempLink.setStartAnchor(linkStart);
        tempLink.setEndAnchor(linkEnd);

        return tempLink;
    }

    public Path generateLinkPath(Link sourceLink) {
        Path linkPath = this.cp.buildLinkPath(sourceLink.getStartAnchor(), sourceLink.getEndAnchor(), sourceLink.getText());
        sourceLink.setLine(linkPath);
        return linkPath;
    }

    public void out(String text) {
        System.out.println(text);
    }

}
