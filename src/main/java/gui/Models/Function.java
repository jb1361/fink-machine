package gui.Models;

import java.util.ArrayList;

class Parameter {
    String type;
    String name;

    public Parameter(String t, String n) {
        this.type = t;
        this.name = n;
    }
}

public class Function {
    String name;
    ArrayList<Parameter> parameters;
    String contents;
    // Link or State to assist in look up
    String targetType;
    // Link.text or State.text for looking up later
    String targetName;

    public Function() {
        parameters = new ArrayList<Parameter>();
    }

    public void addParam(String type, String name) {
        Parameter param = new Parameter(type, name);
        this.parameters.add(param);
    }

}
