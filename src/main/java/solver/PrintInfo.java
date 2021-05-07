package solver;

import java.util.ArrayList;

public class PrintInfo {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void printColoredSteps(Iterable<Board> result) {
        ArrayList<Board> arrayListResult = (ArrayList)result;
        System.out.println(arrayListResult.get(0).toStringFormatMatrix());
        for(int i = 0; i <arrayListResult.size() - 1; i++) {
            System.out.println(arrayListResult.get(i+1).toStringFormatMatrixColored(arrayListResult.get(i)));
        }
        System.out.println();
    }

    public static void printSteps(Iterable<Board> result) {
        for(Board board : result) {
            System.out.println(board.toString());
        }
    }

    public static void printHelp() {
        System.out.println("usage: puzzle.txt -\"Flags\"\n"+
                "Flags:"+
                "-Cl -Classical n-puzzle solution\n" +
                "\t  -Sn -Snake n-puzzle solution\n" +
                "\t  -A -A* algorithm search\n" +
                "\t  -G -Greedy search\n" +
                "\t  -U -Uniform cost search\n" +
                "\t  -M -Manhattan distance\n" +
                "\t  -E -Euclidean distance\n" +
                "\t  -CH -ChebyShev distance\n" +
                "\t  -H -Hemming distance\n" +
                "\t  -L -LinearConflict + Manhattan distance\n"+
                "\t  -SA -Use 5 heuristics\n" +
                "\t  -O -Print complexity info\n" +
                "\t  -C -Print colored steps\n" +
                "\t  -P -Print steps");
    }

    public static void printComplexity(int moves, String Otime,Flag algorithm, Flag heuristic, int maximumNumberOfStates, int everInOpenSet){
        StringBuilder parameters = new StringBuilder();
        parameters.append("Parameters: algorithm - ").append(algorithm.toString());
        if (algorithm != Flag.UNIFORMCOST)
            parameters.append("\nHeuristic - ").append(heuristic.toString());
        parameters.append("\nMinimum number of moves: ").append(moves)
                .append("\nMaximum number of states ever represented in memory at the same time (complexity in size): ").append(maximumNumberOfStates)
                .append("\nNumber of states ever in the opened set (complexity in time): ").append(everInOpenSet)
                .append("\nTime lapse: ").append(Otime);
        System.out.println(parameters.toString());
    }
}
