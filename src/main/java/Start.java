import CodeTranspiler.FSMParser;
import gui.Loader;

public class Start {

    public static void main(String[] args) {
        if (args.length > 0) {
            FSMParser fsm = new FSMParser(args);
        } else {
            // We do not create and object here as JavaFX launch method creates the objects.
            Loader.main(args);
        }
    }
}
