import java.util.*;
import java.io.*;
public class MainScript{
    public static void main(String[] Args){
        try {
            Scanner dfaFile = new Scanner(new File(Args[0]));
            DFA dfa = TranslateFile(dfaFile);
            dfaFile.close();
            dfaFile = new Scanner(new File(Args[1]));
            DFA dfa1 = TranslateFile(dfaFile);
//            dfa1.PrintDFAToScreen();
            dfaFile.close();
            DFA indfa = findIntersection(dfa,dfa1);
            indfa.PrintDFAToScreen();
        }
        catch (Exception e) {
            System.out.println("Could not load file!");
        }
    }


    public static DFA TranslateFile(Scanner DFA){
        DFA dfa = new DFA();
        int numberOfStates = Integer.parseInt(DFA.nextLine());
        String[] namesOfStates = DFA.nextLine().split(" ");
        if (numberOfStates != namesOfStates.length){
            System.out.println("Warning: irregular number of states detected");
            numberOfStates = namesOfStates.length;
        }
        for (int i = 0; i < numberOfStates; i++){
            States newState = new States(namesOfStates[i]);
            dfa.states.add(newState);
        }
        int alphabetSize = Integer.parseInt(DFA.nextLine());
        String[] alphabet = DFA.nextLine().split(" ");
        if (alphabet.length != alphabetSize){
            System.out.println("Warning: irconsistent size of alphabet detected");
        }
        for (int i = 0; i < numberOfStates; i++){
            String[] transitions = DFA.nextLine().split(" ");
            for (int j = 0; j < alphabetSize; j++){
                dfa.states.get(i).addTransition(alphabet[j],dfa.getState(transitions[j]).stateName);
            }
        }
        dfa.startState = dfa.getState(DFA.nextLine());
        int numberOfResting = Integer.parseInt(DFA.nextLine());
        String[] resting = DFA.nextLine().split(" ");
        for (int i = 0; i < numberOfResting; i++){
            dfa.getState(resting[i]).AcceptState = true;
        }
        while (DFA.hasNextLine()) {
            System.out.print("Warning, this line was ignored:");
            System.out.println(DFA.nextLine());
        }
        return dfa;
    }

    public static DFA findIntersection(DFA dfa1, DFA dfa2){
        // grammar used to return each (s,c) of each sigma where S is the states and c is the inputs
        // this is needed to merge the two DFAs into one for finding the intersection 
        ArrayList<String> transitions1 = getGrammar(dfa1);
        ArrayList<String> transitions2 = getGrammar(dfa2);
        DFA udfa = new DFA();
        for (States state : dfa1.states){
            for (States state1 : dfa2.states){
                //every state produces a new state from a combination of each state's names
                // e.g the start states A and x become Ax
                udfa.states.add(new States(state.stateName+state1.stateName));
                States curState = udfa.getState(state.stateName+state1.stateName);
                curState.AcceptState = state.AcceptState&&state1.AcceptState;
                // for every state, F and F' = new F
            }
        }
        udfa.startState = udfa.getState(dfa1.startState.stateName+dfa2.startState.stateName);
        for (String transit : transitions1){
            for (String transit1 : transitions2){
                //fill transitions into the previously generated states for each transition
                String stateName = String.valueOf(transit.charAt(0))+String.valueOf(transit1.charAt(0));
                States curState = udfa.getState(stateName);
                // ^ load each state for adding transitions
                String curInput = String.valueOf(transit.charAt(1));
                String curInput1 = String.valueOf(transit1.charAt(1));
                if (curInput.equals(curInput1)) {
                    // where both transitions reference the same input is what we want to edit
                    String curOutState = String.valueOf(transit.charAt(2)) + String.valueOf(transit1.charAt(2));
                    curState.addTransition(curInput, curOutState);
                    // for each transition where r belongs to sigma and s belongs to sigma' then the new transition is ((s, r) of same c) where c is the input
                    // (s,c)*(r,c) = (c,(r,s))
                    // sigma is state transitions of the first dfa, sigma' is state transitions of the second dfa
                }
            }
        }
        return udfa;
    }

    private static boolean CheckHas(States state, String input){
        /* testing purposes only */
        return state.transitions.containsKey(input);
    }

    public static ArrayList<String> getGrammar(DFA dfa){
        ArrayList<String> transitions = new ArrayList<String>();
        /*while this could be done with an array of length 2*number of states, arraylists are much easier to manipulate
        */
        for (States state : dfa.states){
            /*
            // for every state in states
            // loop through each transition and add them tot the list of transitions in the form [origin state+input+end state)
            // to build up grammar for machine
            */
            for (String key : state.transitions.keySet()){
                transitions.add(state.stateName+key+state.transitions.get(key));
            }
        }
        return transitions;
    }

    /*
    UNNECCESSARY additionals, just used as a draft, decided to leave it in - don't mark!
    public static DFA findUnion(DFA dfa1, DFA dfa2){
        DFA udfa = new DFA();
        udfa.startState = new States(dfa1.startState.stateName+dfa2.startState.stateName);
        if (dfa1.states.size() <= dfa2.states.size()){
            DFA tempDFA = dfa2;
            dfa2 = dfa1;
            dfa1 = tempDFA;
        }
        States cur1 = dfa1.startState;
        States cur2 = dfa2.startState;
        for (States state : dfa1.states){
            String Statename = cur1.stateName;
            Statename += cur2.stateName;
            cur1 = dfa1.getState(cur1.getNext("a"));
            cur2 = dfa2.getState(cur2.getNext("a"));
            udfa.states.add(new States(Statename));
            States curMaster = udfa.getState(Statename);
            curMaster.addTransition("a",cur1.stateName+cur2.stateName);
            curMaster.AcceptState = (cur1.AcceptState || cur2.AcceptState);
        }
        for (States state : dfa1.states){
            String Statename = cur1.stateName;
            Statename += cur2.stateName;
            cur1 = dfa1.getState(cur1.getNext("b"));
            cur2 = dfa2.getState(cur2.getNext("b"));
            udfa.states.add(new States(Statename));
            States curMaster = udfa.getState(Statename);
            curMaster.addTransition("b",cur1.stateName+cur2.stateName);
            curMaster.AcceptState = (cur1.AcceptState || cur2.AcceptState);
        }
        return udfa;
    }

    /*
    public static DFA findUnion(DFA dfa1, DFA dfa2){
        DFA udfa = new DFA();
        udfa.startState = new States(dfa1.startState.stateName+dfa2.startState.stateName);
        ArrayList<String> transitions = new ArrayList<String>();
        for (States state : dfa1.states) {
            for (String key : state.transitions.keySet()) {
                transitions.add(state.stateName+key+state.transitions.get(key));
            }
        }
        for (States state : dfa2.states) {
            for (String key : state.transitions.keySet()) {
                transitions.add(state.stateName+key+state.transitions.get(key));
            }
        }
        ArrayList<String> focus = new ArrayList<String>();
        focus.add(udfa.startState.stateName);
        ArrayList<String> newFocus;
        DFA biggestDFA;
        if (dfa1.states.size() >= dfa2.states.size()){
            biggestDFA = dfa1;
        }
        else{
            biggestDFA = dfa2;
        }
        for (States state : biggestDFA.states){
            newFocus = new ArrayList<String>();
            for (String stateNames : focus) {
                for (String transition : transitions) {
                    System.out.println(transition);
                    if (stateNames.equals(transition)) {
                        try {
                            udfa.getState(stateNames).transitions.get(String.valueOf(transition.charAt(1))); // see if alphabet chararacter has already been added
                            // udfa.getState(udfa.getState(stateNames).transitions.put(String.valueOf(transition.charAt(1)), udfa.getState(udfa.getState(stateNames).transitions.get(String.valueOf(transition.charAt(1)))).stateName + transitions[2]);
                            String key = String.valueOf(transition.charAt(1));
                            String stateFrom = stateNames;
                            String stateTo = String.valueOf(transition.charAt(2));
                            String stateCur = udfa.getState(stateNames).transitions.get(key);
                            String newState = stateCur+stateTo;
                            udfa.getState(stateNames).transitions.put(key,newState);
                            newFocus.add(newState);
                        }catch (Exception e) {
                            udfa.states.add(new States(String.valueOf(transition.charAt(2))));
                            udfa.getState(stateNames).transitions.put(String.valueOf(transition.charAt(1)),String.valueOf(transition.charAt(2)));
                        }
                    }
                }
            }
            for (String item : newFocus){
                udfa.states.add(new States(item));
            }
            focus = newFocus;
        }
        return udfa;
    }
     */
}