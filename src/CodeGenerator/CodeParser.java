package CodeGenerator;

import com.sun.corba.se.spi.orbutil.fsm.FSM;

import javax.naming.CompositeName;
import java.util.ArrayList;
import java.util.List;

public class CodeParser {

    private List<String> FsmSectionList;
    private List<String> CodeSectionList;

    public CodeParser(String[] args) {
        ReadFile fReader = new ReadFile(args[0]);
        System.out.println("Parsing FSM");
        FsmSectionList = fReader.FsmSectionList;
        CodeSectionList = fReader.CodeSectionList;
        String output = "";
        output += ParseCodeSection();
        output += ParseFsmSection();
        output += CreateStandardInputSection();
        System.out.println("Writing output file");
        WriteCode.Write(args[1], output);
        System.out.println("Done");
        System.out.println();
    }
    private String ParseFsmSection() {
        StringBuilder output = new StringBuilder();
        int transitionNode = 0;
        for (String element : FsmSectionList) {
            if(element.contains("FSM")) {
                String[] header = element.split(" ");
                output.append("print('Running Module: " + header[1] + "')\n");
            }else if (!element.equals("trans")) {
                String[] node = element.split(" ");
                output.append(CreateTransitionCode(node, transitionNode));
                transitionNode++;
            }
        }
        output.append("\telse:\n");
        output.append("\t\tprint('Not a valid transition from state: ' + state)\n");
        return output.toString();
    }
    private String CreateFsmCodeSetup(String startNode) {
        StringBuilder output = new StringBuilder();
        output.append("state_history = []\n");
        output.append("state ='" + startNode + "'\n");
        output.append("ch = ''\n");
        output.append("def UpdateState():\n");
        output.append("\tglobal state\n" + "\tglobal ch\n");
        return output.toString();
    }
    private String CreateTransitionCode(String[] node, int count) {
        StringBuilder output = new StringBuilder();
        if (count == 0) {
            output.append(CreateFsmCodeSetup(node[0]));
            output.append("\tif(state == '" + node[0] +"' and ch == " + node[2] + "):\n");
            output.append("\t\tstate = '" + node[1] + "'\n");
            output.append("\t\tstate_history.append(state + ' : ' + ch)\n");
            output.append("\t\t" + node[3] + "()\n");
        } else {
            output.append("\telif(state == '" + node[0] +"' and ch == " + node[2] + "):\n");
            output.append("\t\tstate = '" + node[1] + "'\n");
            output.append("\t\tstate_history.append(state + ' : ' + ch)\n");
            output.append("\t\t" + node[3] + "()\n");
        }
        return output.toString();
    }
    private String CreateStandardInputSection() {
        StringBuilder output = new StringBuilder();
        output.append("print('Waiting for input...')\n");
        output.append("ch = input()\n");
        //output.append("\tNo input FSM is exiting...\n");
        output.append("while(ch):\n");
        output.append("\tUpdateState()\n");
        output.append("\tch = input()\n");

        output.append("print('Ending State: ' + state)\n");
        output.append("print('')\n");
        output.append("print('State History')\n");
        output.append("print('FSM finished, exiting...')\n");
        return output.toString();
    }
    private String ParseCodeSection() {
        StringBuilder output = new StringBuilder();
        for (String element : CodeSectionList) {
            output.append(element);
            output.append("\n");
        }
        return output.toString();
    }
}
