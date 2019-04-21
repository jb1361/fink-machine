
package gui.Models;
// NodePairs are ordered
// A,B is different from B,A
public class NodePair {
    String startNode;
    String endNode;
    
    public NodePair(String a, String b) {
        this.startNode = a;
        this.endNode = b;
    }

    public String getStartNode() {
        return startNode;
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public String getEndNode() {
        return endNode;
    }

    public void setEndNode(String endNode) {
        this.endNode = endNode;
    }
    
    public String getValue() { return "('" + startNode + "', '" + endNode + "')"; }
    
    
    
    @Override
    public boolean equals(Object args0) {
       if(args0 == this) { return true; }
       if(!(args0 instanceof NodePair)) { return false; }
       boolean pass = false;
       NodePair test = (NodePair) args0;
       if (test.startNode.equals(this.startNode) &&
           test.endNode.equals(this.endNode))
           pass = true;

       return pass;
    }
    
    @Override
    public int hashCode()
    {
        return getValue().hashCode();
    }
    

}
