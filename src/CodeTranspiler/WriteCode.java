package CodeTranspiler;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class WriteCode {
    public static void Write(String filename, String code) {
        System.out.println("Writing to File");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false));
            writer.append(code);
            writer.close();
        }
        catch (Exception e) {
        e.printStackTrace();
        }
    }
}
