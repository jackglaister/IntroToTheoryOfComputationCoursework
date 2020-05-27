import java.io.*;
import java.util.Scanner;

public class MainScript{
    public static void main(String[] Args){
        try {
            // opens the file listed in the first argument and prints it's complement to the screen
            // if the file can't be opened a laod file error is printed to the console
            Scanner dfaFile = new Scanner(new File(Args[0]));
            DFA dfa = TranslateFile(dfaFile);
            dfa.makeCompliment();
            dfa.PrintDFAToScreen();
            dfaFile.close();
        }
        catch (Exception e) {
            System.out.println("Could not load file!");
        }
    }
    public static DFA TranslateFile(Scanner DFA) {
        // follows the spec line by line to read the dfa from the file 
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