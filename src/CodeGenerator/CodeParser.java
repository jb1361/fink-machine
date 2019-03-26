package CodeGenerator;

import java.util.ArrayList;
import java.util.List;

public class CodeParser {

    private List<String> FsmSectionList;
    private List<String> CodeSectionList;

    public CodeParser(String[] args) {
        ReadFile fReader = new ReadFile(args[0]);
        System.out.println("Parsing FSM");
        FsmSectionList = fReader.FsmSectionList;
        CodeSectionList = fReader.FsmSectionList;
        WriteCode.Write(args[1], FsmSectionList.toString());
    }
}
