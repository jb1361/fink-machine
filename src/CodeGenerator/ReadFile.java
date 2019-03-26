package CodeGenerator;

import java.io.*;

public class ReadFile {
    public ReadFile(String[] args) {
        File file = new File(args[0]);
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

