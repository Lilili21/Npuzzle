package solver;

import exceptions.BoardException;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Board {
    private final int[] blocks;
    private final Point zero = new Point();
    private double h;
    @Getter
    private int c;
    private Flag algorithmType;
    private Flag heuristic;

    public Board(int[] blocks, Flag algorithmType, Flag heuristic, int c) {
        this.blocks = deepCopy(blocks);
        this.heuristic = heuristic;
        this.algorithmType = algorithmType;
        this.c = c;
        switch (heuristic) {
            case HEMMING :
                countHnZero();
                break;
            case MANHATTAN:
                countManhattanDistance();
                break;
            case EUCLIDEAN:
                countEuclideanDistance();
                break;
            case LINEARCONFLICT:
                countlinearConflicts();
                break;
            case CHEBYSHEV:
                countChebyshevDistance();
                break;
            default:
                throw new BoardException("Heuristic is wrong");
        }
    }

    private void countHnZero(){
        h = 0;
        for (int i = 0; i < blocks.length; i++) {  //  в этом цикле определяем координаты нуля и вычисляем h(x)
            if (blocks[i] != Utils.getResultBoard().get(i) && blocks[i] != 0) {  // если 0 не на своем месте - не считается
                    h += 1;
            }
            if (blocks[i] == 0) {
                zero.setLocation(i / dimension(), i % dimension());
            }
        }
    }

    private void countManhattanDistance(){
        double dist = 0;
        int num;
        int indexResult;

        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != 0) { //  1
                num = blocks[i];
                indexResult = Utils.getResultBoard().indexOf(num);
                dist += Math.abs(i / dimension() - indexResult / dimension()) + Math.abs(i % dimension() - indexResult % dimension());
            }
            if (blocks[i] == 0) {
                zero.setLocation(i / dimension(), i % dimension());
            }
        }
        this.h = dist;
    }

    private void countChebyshevDistance(){
        double dist = 0;
        int num;
        int indexResult;

        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != 0) { //  1
                num = blocks[i];
                indexResult = Utils.getResultBoard().indexOf(num);
                dist += Math.max(Math.abs(i / dimension() - indexResult / dimension()), Math.abs(i % dimension() - indexResult % dimension()));
            }
            if (blocks[i] == 0) {
                zero.setLocation(i / dimension(), i % dimension());
            }
        }
        this.h = dist;
    }

    private void countEuclideanDistance(){
        double dist = 0;
        int num;
        int indexResult;

        for (int i = 0; i < blocks.length; i++) {  //  в этом цикле определяем координаты нуля и вычисляем h(x)
            if (blocks[i] != 0) { //  1
                num = blocks[i];
                indexResult = Utils.getResultBoard().indexOf(num);
                dist += Math.pow(i / dimension() - indexResult / dimension(), 2) + Math.pow(i % dimension() - indexResult % dimension(), 2);
            }
            if (blocks[i] == 0) {
                zero.setLocation(i / dimension(), i % dimension());
            }
        }
        this.h = dist;
    }

    private int countConflicts(List<Integer> ourBlocks, List<Integer> resultBlocks){
        int conflicts = 0;
        int a, b;

        for (int i = 0; i < resultBlocks.size(); i++){
            a = resultBlocks.get(i);
            if (ourBlocks.contains(a)) {
              for (int j = i; j < resultBlocks.size(); j++) {
                  b = resultBlocks.get(j);
                  if (ourBlocks.contains(b) && ourBlocks.indexOf(b) < ourBlocks.indexOf(a))
                      conflicts++;
              }
            }
        }
        return conflicts;
    }

    public void countlinearConflicts() {
       int conflicts = 0;
       List<Integer> ourBlocksRow;
       List<Integer> resultBlocksRow;
        List<Integer> ourBlocksColumn;
        List<Integer> resultBlocksColumn;

        for (int i = 0; i < dimension(); i++) {//проходим по столбцам и строкам
            ourBlocksColumn = new ArrayList<>();
            ourBlocksRow = new ArrayList<>();
            resultBlocksColumn = new ArrayList<>();
            resultBlocksRow = new ArrayList<>();
            for (int j = 0; j < dimension(); j++) {
                ourBlocksRow.add(blocks[i * dimension() + j]);
                resultBlocksRow.add(Utils.getResultBoard().get(i * dimension() + j));
                ourBlocksColumn.add(blocks[i + j * dimension()]);
                resultBlocksColumn.add(Utils.getResultBoard().get(i + j * dimension()));
            }
            conflicts += countConflicts(ourBlocksColumn, resultBlocksColumn);
            conflicts += countConflicts(ourBlocksRow, resultBlocksRow);
        }
        countManhattanDistance();
        this.h += 2 * conflicts;
    }

    public int dimension() {
        return (int) Math.sqrt(blocks.length);
    }

    public int getNumberWrongPairs(){
        int num;
        int count = 0;

        for (int i = 0; i < blocks.length; i++){
            num = blocks[i];
            for (int j = 0; j < i; j++) {
                if (num == 0 || blocks[j] == 0)
                    continue;
                if (num < blocks[j])
                    count++;
            }
        }
        return count;
    }

    public boolean isGoal() {
        return h == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (board.dimension() != dimension()) return false;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != board.blocks[i]) {
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        Set<Board> boardList = new HashSet<Board>();
        boardList.add(chng(getNewBlock(), zero.x, zero.y, zero.x, zero.y + 1));
        boardList.add(chng(getNewBlock(), zero.x, zero.y, zero.x, zero.y - 1));
        boardList.add(chng(getNewBlock(), zero.x, zero.y, zero.x - 1, zero.y));
        boardList.add(chng(getNewBlock(), zero.x, zero.y, zero.x + 1, zero.y));

        return boardList;
    }

    private int[] getNewBlock() { //  опять же, для неизменяемости
        return deepCopy(blocks);
    }

    private Board chng(int[] blocks2, int x1, int y1, int x2, int y2) {
        if (x2 > -1 && x2 < dimension() && y2 > -1 && y2 < dimension()) {
            int t = blocks[x2 * dimension() + y2];
            blocks2[x2* dimension() + y2] = blocks2[x1 * dimension() + y1];
            blocks2[x1 * dimension() + y1] = t;
            return new Board(blocks2, this.algorithmType, this.heuristic, this.c + 1);
        } else
            return null;

    }

    public String toStringFormatMatrix() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            s.append(String.format("%2d ",blocks[i]));
            if ((i + 1) % dimension() == 0)
                s.append("\n");
        }
        return s.toString();
    }

    public String toStringFormatMatrixColored(Board board2compare) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] == board2compare.blocks[i])
                s.append(String.format("%2d ", blocks[i]));
            else {
                s.append(PrintInfo.ANSI_CYAN);
                s.append(String.format("%2d ", blocks[i]));
                s.append(PrintInfo.ANSI_RESET);
            }
            if ((i + 1) % dimension() == 0)
                s.append("\n");
        }
        return s.toString();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            s.append(String.format("%2d ",blocks[i]));
            if ((i + 1) % dimension() == 0)
                s.append("n");
        }
        return s.toString();
    }

    private static int[] deepCopy(int[] original) {
        if (original == null) {
            return null;
        }
        final int[] result = new int[original.length];
        System.arraycopy(original, 0, result, 0, original.length);
        return result;
    }
}
