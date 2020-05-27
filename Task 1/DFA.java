import java.util.*;

public class DFA{
    public ArrayList<States> states;
    public States startState;
    public DFA(){
        // constructor
        states = new ArrayList<States>();
    }
    public States getState(String stateName){
        for (States state : states){
            if (state.stateName.equals(stateName)){
                return state;
            }
        }
        // if the state can't be found this is printed to the console and state 0 is returned
        System.out.println("Can't find state!");
        return states.get(0);
    }
    public void makeCompliment(){
        // changes all accept states to non accept states
        // if all accept states are non accept states then all accepted strings of the original dfa will not be in the new dfa's language
        for (States state : states){
            state.AcceptState = !state.AcceptState;
        }
    }
    public void PrintDFAToScreen() {
        System.out.println(states.size());
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
        System.out.println(namesOfStates);
        System.out.println("2");
        System.out.println("A B");
        for (States state : states) {
            for (String key : state.transitions.keySet()) {
                System.out.println(state.stateName + "-->" + state.transitions.get(key) + ":" + key);
            }
        }
        System.out.println(startState.stateName);
        System.out.println(numberOfAccept);
        System.out.println(acceptanceStates);
    }
}