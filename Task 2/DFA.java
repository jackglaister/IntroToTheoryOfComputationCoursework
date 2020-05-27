import java.util.*;

public class DFA{
    public ArrayList<States> states;
    public States startState;
    public DFA(){
        states = new ArrayList<States>();
    }
    public States getState(String stateName){
        for (States state : states){
            if (state.stateName.equals(stateName)){
                return state;
            }
        }
        System.out.println("Can't find state!");
        return states.get(0);
    }
    public void makeCompliment() {
        for (States state : states) {
            state.AcceptState = !state.AcceptState;
        }
    }



/*    public void insertionAfter(DFA secondDFA) {
        for (States state : secondDFA.states) {
            if (state.AcceptState) {
                States newState = new States(secondDFA.startState.stateName);
                newState.transitions = state.transitions;
                for (String key : secondDFA.startState.transitions.keySet()) {
                    newState.addTransition(key, secondDFA.startState.transitions.get(key));
                }
                String oldState = state.stateName;
                state = newState;
                for (States state3 : states) {
                    for (String key : state3.transitions.keySet()) {
                        if (state3.transitions.get(key).equals(oldState)) {
                            state3.addTransition(key, newState.stateName);
                        }
                    }
                }
            }
        }
        for (States state : secondDFA.states){
            if (!state.AcceptState){
                states.add(state);
            }
        }
    }
*/
    public void PrintDFAToScreen() {
        System.out.println("Number of states: "+states.size());
        String namesOfStates = "";
        int numberOfAccept = 0;
        String acceptanceStates = "";
        for (int i = 0; i < states.size(); i++) {
            namesOfStates += states.get(i).stateName;
            if (states.get(i).AcceptState) {
                acceptanceStates += states.get(i).stateName + " ";
                numberOfAccept++;
            }
        }
        System.out.println("States: "+namesOfStates);
        System.out.println("Alphabet length: 2");
        System.out.println("Alphabet: A B");
        for (States state : states) {
            for (String key : state.transitions.keySet()) {
                System.out.println(state.stateName + "-->" + state.transitions.get(key) + ":" + key);
            }
        }
        System.out.println("Start state: "+startState.stateName);
        System.out.println("Number of acceptance states: "+numberOfAccept);
        System.out.println("Acceptance states: "+acceptanceStates);
    }
}
