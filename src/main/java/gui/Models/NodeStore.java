/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.Models;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeStore {
    HashMap<String, Node> nodeStore;

    public NodeStore() {
        this.nodeStore = new HashMap<>();
    }

    public Node findNodeFromText(String text) {
        return this.nodeStore.get(text);
    }

    public boolean nodeExists(String text) {
        return (!text.isEmpty() && !this.nodeStore.containsKey(text));
    }

    public boolean addNode(Node newNode) {
        boolean success = false;
        if (!newNode.getText().isEmpty() && !this.nodeStore.containsKey(newNode.getText())) {
            this.nodeStore.put(newNode.getText(), newNode);
            success = true;
        }
        return success;
    }

    public Node removeNode(Node oldNode) {
        Node tempNode = null;
        if (this.nodeStore.containsValue(oldNode))
            tempNode = this.nodeStore.remove(oldNode.getText());
        return tempNode;
    }

    public ArrayList<String> getNodes() {
        return new ArrayList<>(this.nodeStore.keySet());
    }

    public ArrayList<String> printAccept() {
        ArrayList<String> acceptNodes = new ArrayList<>();

        nodeStore.forEach((n, node) -> {
            if (node.isAcceptState)
                acceptNodes.add(node.getText());
        });


        return acceptNodes;
    }

    public HashMap<String, Node> getList() {
        return this.nodeStore;
    }


}
