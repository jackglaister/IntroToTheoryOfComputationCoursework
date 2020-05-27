import java.util.*;

public class States{
    public Map<String, String> transitions;
    public String stateName;
    public boolean AcceptState;
    public States(String StateName){
        stateName = StateName;
        transitions = new HashMap<String, String>();
    }
    public void addTransition(String input, String dest){
        transitions.put(input,dest);
    }
}