package solver;

import exceptions.ParseProblemException;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Utils {
    @Getter
    static List<Integer> resultBoard;

    static boolean checkIfSolvable(Board initial, boolean isClassical){
        boolean isSolvable;
        int a, b, aZero, bZero;
        a = initial.getNumberWrongPairs();
        aZero = initial.getZero().x;
        if (isClassical)
            isSolvable = (initial.dimension() % 2 == 0) ?((a + aZero) % 2 == 1) : a % 2 == 0;
        else {
            b = getNumberWrongPairs();
            bZero = initial.dimension() / 2;
            isSolvable = (initial.dimension() % 2 == 0) ?
                    (a + aZero) % 2 == (b + bZero) % 2
                    : a % 2 == b % 2;
        }
        return isSolvable;
    }

    private static int getNumberWrongPairs(){
        int num;
        int count = 0;

        for (int i = 0; i < resultBoard.size(); i++){
            num = resultBoard.get(i);
            for (int j = 0; j < i; j++) {
                if (num == 0 || resultBoard.get(j) == 0)
                    continue;
                if (num < resultBoard.get(j))
                    count++;
            }
        }
        return count;
    }

    static int[] parsePuzzleFile(String fileName) throws FileNotFoundException {
        int[] blocks;

        Scanner scanner = new Scanner(new File(fileName));
        String line = scanner.nextLine();
        while (line.contains("#") && scanner.hasNextLine()){
            line = scanner.nextLine();
        }
        if (!scanner.hasNextLine())
            throw new ParseProblemException("There is no size of puzzle in the file");
        int size = Integer.parseInt(line);
        if (size == 0)
            throw new ParseProblemException("There is no size of puzzle in the file");
        blocks = parseBlocks(scanner, size);

        return blocks;
    }

    private static int[] parseBlocks(Scanner scanner, int size){
        int[] blocks = new int[size * size];
        boolean[] checkElems = new boolean[size * size];
        String[] elems;
        int count = 0;
        int i;
        boolean hasZero = false;
        String line;

        while (scanner.hasNextLine()){
            if (count > size)
                throw new ParseProblemException("Wrong number of strings in puzzle");
            line = scanner.nextLine();
            if (line.contains("#")) {
                elems = line.trim().split("#");
                if (elems[0].isEmpty())
                    continue;
                else
                    line = elems[0];
            }
            elems = line.split("\\s+");
            if (elems.length != size)
                throw new ParseProblemException("Wrong number of elements in String");
            i = -1;
            while (++i < size) {
                blocks[count * size + i] = Integer.parseInt(elems[i]);
                if (blocks[count* size + i] >= size * size)
                    throw new ParseProblemException("Wrong element in puzzle");
                if (blocks[count* size + i] == 0){
                    if (hasZero)
                        throw new ParseProblemException("More than one Zero");
                    hasZero = true;
                }
                if (checkElems[blocks[count * size + i]])
                    throw new ParseProblemException("This element is already exist");
                checkElems[blocks[count * size + i]] = true;
            }
            count++;
        }
        if (!hasZero)
            throw new ParseProblemException("There is no zero place in the puzzle");
        if (count != size)
            throw new ParseProblemException("Wrong number of strings in puzzle");
        return blocks;
    }

    static List<Flag> parseFlags(String[] params){
        List<Flag> flags = new ArrayList<>();
        if (params.length == 1) {
            flags.add(Flag.CLASSICALSOLUTION);
            flags.add(Flag.AALGORITM);
            flags.add(Flag.MANHATTAN);
            flags.add(Flag.PRINTCOMPLEXITYINFO);
            flags.add(Flag.PRINTSTEPS);
        }
        for (int i = 1; i < params.length; i++) {
            flags.add(Flag.returnFlag(params[i]));
        }
        return flags;
    }

    static void generateBoard(int size, Flag type){
        int[] board = new int[size * size];
        int value = 1;
        if (type == Flag.CLASSICALSOLUTION) {
            for (int i = 0; i < size * size - 1; i++)
                board[i] = value++;
            board[size * size - 1] = 0;
        } else {
           for (int step = 0; step < size - 1; step++) {
               for (int i = step; i < size - step; i++)
                   board[step * size + i] = value++;
               for (int j = step + 1; j < size - step; j++)
                   board[j * size + size - (1 + step)] = value++;
               for (int i = size - step - 2; i >= step; i--)
                   board[size * (size - 1 - step) + i] = value++;
               for (int j = size - step - 2; j > step; j--)
                   board[j * size + step] = value++;
           }
           if (size % 2 == 1)
               board[size/2 * size + size/2] = 0;
           else
               board[size/2 * size + size/2 - 1] = 0;
        }
       resultBoard = Arrays.stream(board).boxed().collect(Collectors.toList());
    }

}
