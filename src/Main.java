import CodeGenerator.CodeParser;
import GUI.StartGUI;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            CodeParser fsm = new CodeParser(args);
        } else {
            StartGUI gui = new StartGUI(args);
        }
    }
}
