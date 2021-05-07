package solver;

import exceptions.ParseProblemException;
import exceptions.SolverException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        List<Flag> flags;
        int[] blocks;

        try {
            if (args.length == 0) {
                PrintInfo.printHelp();
                String[] params = sc.nextLine().split(" ");
                blocks = Utils.parsePuzzleFile(params[0]);
                flags = Utils.parseFlags(params);
            } else {
                blocks = Utils.parsePuzzleFile(args[0]);
                flags = Utils.parseFlags(args);
            }
            List<Board> initial = initBoard(flags, blocks);
            if (initial.isEmpty())
                throw new ParseProblemException("Wrong number of Flags, check if you initialize heuristic and algo type");
            if (!Utils.checkIfSolvable(initial.get(0), flags.contains(Flag.CLASSICALSOLUTION)))
                throw new SolverException("Puzzle couldn't be solved");
            for (Board boardInitail : initial) {
                Instant start = Instant.now();
                Solver solver = new Solver(boardInitail);
                Instant end = Instant.now();
                if (flags.contains(Flag.PRINTCOMPLEXITYINFO))
                    PrintInfo.printComplexity(solver.moves(), String.valueOf(Duration.between(start, end)),boardInitail.getAlgorithmType(), boardInitail.getHeuristic(), solver.getMaximumNumberOfStates(), solver.getEverInOpenSet());
                if (flags.contains(Flag.PRINTCOLOREDSTEPS))
                    PrintInfo.printColoredSteps(solver.solution());
                else if (flags.contains(Flag.PRINTSTEPS))
                    PrintInfo.printSteps(solver.solution());
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private static List<Board> initBoard(List<Flag> flags, int[] blocks){
        if (!flags.contains(Flag.SNAKESOLUTION) && !flags.contains(Flag.CLASSICALSOLUTION) ||
                flags.contains(Flag.SNAKESOLUTION) && flags.contains(Flag.CLASSICALSOLUTION))
            throw new ParseProblemException("Choose Classical or Snake mode");
        if (flags.contains(Flag.SNAKESOLUTION))
            Utils.generateBoard((int)Math.sqrt(blocks.length), Flag.SNAKESOLUTION);
        else
            Utils.generateBoard((int)Math.sqrt(blocks.length), Flag.CLASSICALSOLUTION);
        return initBoardAlgoritm(flags, blocks);
    }

    private static List<Board> initBoardAlgoritm(List<Flag> flags, int[] blocks){
        List<Board> boardList = new ArrayList<>();
        if (flags.contains(Flag.UNIFORMCOST))
            boardList.add(new Board(blocks, Flag.UNIFORMCOST, Flag.HEMMING, 0));
        if (flags.contains(Flag.AALGORITM))
            boardList.addAll(initBoardHeuristics(flags, blocks, Flag.AALGORITM));
        if (flags.contains(Flag.GREEDYSEARCH))
            boardList.addAll(initBoardHeuristics(flags, blocks, Flag.GREEDYSEARCH));
        return boardList;
    }

    private static List<Board> initBoardHeuristics(List<Flag> flags, int[] blocks, Flag algorithm) {
        List<Board> boardList = new ArrayList<>();
        if (flags.contains(Flag.HEMMING) || flags.contains(Flag.SA))
            boardList.add(new Board(blocks, algorithm, Flag.HEMMING, 0));
        if (flags.contains(Flag.CHEBYSHEV) || flags.contains(Flag.SA))
            boardList.add(new Board(blocks, algorithm, Flag.CHEBYSHEV, 0));
        if (flags.contains(Flag.EUCLIDEAN) || flags.contains(Flag.SA))
            boardList.add(new Board(blocks, algorithm, Flag.EUCLIDEAN, 0));
        if (flags.contains(Flag.LINEARCONFLICT) || flags.contains(Flag.SA))
            boardList.add(new Board(blocks, algorithm, Flag.LINEARCONFLICT, 0));
        if (flags.contains(Flag.MANHATTAN) || flags.contains(Flag.SA))
            boardList.add(new Board(blocks, algorithm, Flag.MANHATTAN, 0));
        if (boardList.isEmpty())
            throw new ParseProblemException("Choose Flag with heuristic");
        return boardList;
    }
}
