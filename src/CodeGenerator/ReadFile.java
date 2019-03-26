package CodeGenerator;

import java.io.*;

public class ReadFile {
    public ReadFile(String fileName) {
        File file = new File(fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
                System.out.println(line);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}

