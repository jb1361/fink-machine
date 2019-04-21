package CodeTranspiler;

import java.util.Arrays;
import java.util.List;

public class FSMParser {

    private List<String> FsmSectionList;
    private List<String> CodeSectionList;

    public FSMParser(String[] args) {
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
                output.append("print('Running Module: ").append(header[1]).append("')\n");
            }
            else if (element.contains("accept")) {
                String[] accept_states = element.split(" ");
                accept_states = Arrays.copyOfRange(accept_states, 1, accept_states.length);
                String states = String.join("','", accept_states);
                output.append("accept_states = ['").append(states).append("']\n");
            }
            else if (!element.equals("TRANS")) {
                String[] node = element.split(" ");
                output.append(CreateTransitionCode(node, transitionNode));
                transitionNode++;
            }
        }
        output.append("\telse:\n");
        output.append("\t\tprint('Not a valid transition from state: ' + state)\n");
        output.append("\t\tprint('We can have the FSM stop running here if needed with import sys and sys.exit()')\n");
        return output.toString();
    }
    private String CreateFsmCodeSetup(String startNode) {
        return "state_history = []\n" +
                "state ='" + startNode + "'\n" +
                "ch = ''\n" +
                "def ValidateState():\n" +
                "\tglobal state\n" + "\tglobal ch\n" +
                "\tif(state not in accept_states):\n" +
                "\t\tprint(state + ' is not an accepted state. Reverting back to previous state (We can have the FSM stop running here if needed)')\n" +
                "\t\tstate = state_history[-1][0]\n" +
                "\t\tch = state_history[-1][1]\n" +
                "\t\treturn False\n" +
                "\telse:\n" +
                "\t\treturn True\n" +
                "def UpdateState():\n" +
                "\tglobal state\n" + "\tglobal ch\n";
    }
    private String CreateTransitionCode(String[] node, int count) {
        StringBuilder output = new StringBuilder();
        if (count == 0) {
            output.append(CreateFsmCodeSetup(node[0]));
            output.append("\tif(state == '").append(node[0]).append("' and ch == ").append(node[2]).append("):\n");
        } else {
            output.append("\telif(state == '").append(node[0]).append("' and ch == ").append(node[2]).append("):\n");
        }
            output.append("\t\tstate = '").append(node[1]).append("'\n");
            output.append("\t\tprint('Transitioning to state ").append(node[1]).append(" from state ").append(node[0]).append(" with ch = ' + ch)\n");
            output.append("\t\tif(not ValidateState()):\n");
            output.append("\t\t\treturn\n");
            output.append("\t\t").append(node[3]).append("()\n");

        return output.toString();
    }
    private String CreateStandardInputSection() {
        return "print('Waiting for input...')\n" +
                "try:\n" +
                "\tch = input()\n" +
                "except:\n" +
                "\tprint('Input File is empty')\n" +
                "while(ch):\n" +
                "\tUpdateState()\n" +
                "\tstate_history.append([state , ch])\n" +
                "\ttry:\n" +
                "\t\tch = input()\n" +
                "\texcept:\n" +
                "\t\tbreak\n" +
                "print('Ending State: ' + state)\n" +
                "print('')\n" +
                "print('State History')\n" +
                "print(state_history)\n" +
                "print('FSM finished, exiting...')\n";
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
