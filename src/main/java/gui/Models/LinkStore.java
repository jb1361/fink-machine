package gui.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class LinkStore {

    HashMap<NodePair, Link> linkStore;

    public LinkStore() {
        this.linkStore = new HashMap<>();
    }

    public Link findNodeFromSet(NodePair set) {
        return this.linkStore.get(set);
    }

    public boolean linkExists(NodePair set) {
        return (this.linkStore.containsKey(set));
    }

    public boolean linkExists(String a, String b) {
        NodePair set = new NodePair(a, b);
        return (!linkExists(set));
    }

    public ArrayList<Link> trackNode(Node node) {
        ArrayList<Link> tempLinks = new ArrayList<>();
        linkStore.forEach((nodePair, link) -> {
            if (link.getStart() == node || link.getEnd() == node)
                tempLinks.add(link);
        });
        return tempLinks;
    }


    public boolean addLink(NodePair set, Link newLink) {
        boolean success = false;
        if (!this.linkStore.containsKey(set) && !this.linkStore.containsValue(newLink)) {
            this.linkStore.put(set, newLink);
            success = true;
        }
        return success;
    }

    public Link removeLink(NodePair set) {
        Link tempLink = null;
        if (this.linkStore.containsKey(set))
            tempLink = this.linkStore.remove(set);
        return tempLink;
    }

    public void removeLink(Link link) {
        if (this.linkStore.containsValue(link))
            this.linkStore.remove(new NodePair(link.getStart().getText(), link.getEnd().getText()));
    }

    public void printStore() {
        System.out.println(linkStore.toString());
    }

    public HashMap<NodePair, Link> getList() {
        return this.linkStore;
    }

    public ArrayList<String> printNodes() {
        ArrayList<String> linkNodes = new ArrayList<>();
        String sp = " ";
        String newLine = System.getProperty("line.separator");
        String t = "\t";

        linkStore.forEach((p, link) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(t)
                    .append(link.getStart().getText())
                    .append(sp);
            sb.append(link.getEnd().getText())
                    .append(sp);
            sb.append("'")
                    .append(link.getText())
                    .append("'")
                    .append(sp);
            //sb.append(link.getFunction());
            //if (link.getFunction() == null)
            sb.append("null");
            //else
            //    sb.append(link.getFunction());
            sb.append(newLine);
            linkNodes.add(sb.toString());
        });

        return linkNodes;
    }
}
