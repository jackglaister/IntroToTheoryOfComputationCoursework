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
}
