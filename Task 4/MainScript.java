import java.util.*;
import java.io.*;
public class MainScript{
    public static void main(String[] Args){
        try {
            Scanner dfaFile = new Scanner(new File(Args[0]));
            DFA dfa = TranslateFile(dfaFile);
            dfaFile.close();
            ArrayList<String> visited = new ArrayList<String>();
            States state = dfa.startState;
            String contains = CheckContainsString(dfa,state,visited,"");
            System.out.println(contains);
        }
        catch (Exception e) {
            System.out.println("Could not load file!");
        }
    }

    public static String CheckContainsString(DFA dfa, States state, ArrayList<String> visited, String contains){ ;
        /*
        This method uses a bredth first search, checking intially if each state is in an accept state, if it is the inputs that got it there are printed and it is classified as not empty
        If it isn't an accept state, we check each transition's state to see if it's visited, in the current traversal we built up, if it hasn't we then visit it. 
        This is repeated unit we have visited all states that are linked by a transition and either: found a path or haven't
        If we've found a path, we return the path and that we've found one and the DFA contains a non empty set
        The empty string isn't part of the empty set and so if we start at the start state and are already in the accept state, then the dfa is still non empty
        if the dfa only contains the empty set then we return that it's empty. 
        */

        visited.add(state.stateName);
        if (state.AcceptState){
            return " language not empty "+contains;
        }
        for (String input : state.transitions.keySet()){
            States nextState = dfa.getState(state.transitions.get(input));
            boolean isVisited = false;
            for (String visit : visited) {
                if (nextState.stateName.equals(visit)) {
                    isVisited = true;
                }
            }
            if (!isVisited){
                contains += input;
                return CheckContainsString(dfa,nextState,visited,contains);
            }
        }
        if (dfa.states.size() == visited.size()){
            return "language empty";
        }
        return "language empty";
    }

    public static DFA TranslateFile(Scanner DFA) {
        DFA dfa = new DFA();
        int numberOfStates = Integer.parseInt(DFA.nextLine());
        String[] namesOfStates = DFA.nextLine().split(" ");
        if (numberOfStates != namesOfStates.length) {
            System.out.println("Warning: irregular number of states detected");
            numberOfStates = namesOfStates.length;
        }
        for (int i = 0; i < numberOfStates; i++) {
            States newState = new States(namesOfStates[i]);
            dfa.states.add(newState);
        }
        int alphabetSize = Integer.parseInt(DFA.nextLine());
        String[] alphabet = DFA.nextLine().split(" ");
        if (alphabet.length != alphabetSize) {
            System.out.println("Warning: irconsistent size of alphabet detected");
        }
        for (int i = 0; i < numberOfStates; i++) {
            String[] transitions = DFA.nextLine().split(" ");
            for (int j = 0; j < alphabetSize; j++) {
                dfa.states.get(i).addTransition(alphabet[j], dfa.getState(transitions[j]).stateName);
            }
        }
        dfa.startState = dfa.getState(DFA.nextLine());
        int numberOfResting = Integer.parseInt(DFA.nextLine());
        String[] resting = DFA.nextLine().split(" ");
        for (int i = 0; i < numberOfResting; i++) {
            dfa.getState(resting[i]).AcceptState = true;
        }
        while (DFA.hasNextLine()) {
            System.out.print("Warning, this line was ignored:");
            System.out.println(DFA.nextLine());
        }
        return dfa;
    }
}