package CodeGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ReadFile {
    List<String> FsmSectionList = new ArrayList<String>();
    List<String> CodeSectionList = new ArrayList<String>();

    ReadFile(String fileName) {
        System.out.println("Reading File");
        File file = new File(fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            line = br.readLine();
            if(line != null) {
                if (line.equals("FSM")) ReadFSMSection(br);
                else if (line.equals("CODE")) ReadCodeSection(br);
                else System.out.println("Invalid FSM file");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void ReadFSMSection(BufferedReader br) {
        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("CODE")) {
                    ReadCodeSection(br);
                    return;
                } else FsmSectionList.add(line);
            }
        }
        catch(Exception e){
                e.printStackTrace();
        }
    }
    private void ReadCodeSection(BufferedReader br) {
        String line;
        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("FSM")) {
                    ReadFSMSection(br);
                    return;
                } else CodeSectionList.add(line);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

