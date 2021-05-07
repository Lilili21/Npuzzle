package solver;

import exceptions.SolverException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Solver {
    private Board initial;
    private List<Board> result = new ArrayList<Board>();
    @Getter
    private int everInOpenSet = 0, maximumNumberOfStates = 1;

    private class ITEM{
        private ITEM prevBoard;
        private Board board;

        private ITEM(ITEM prevBoard, Board board) {
            this.prevBoard = prevBoard;
            this.board = board;
        }

        public Board getBoard() {
            return board;
        }
    }

    public Solver(Board initial) {
        this.initial = initial;

        PriorityQueue<ITEM> priorityQueue = new PriorityQueue<ITEM>(10, new Comparator<ITEM>() {
            @Override
            public int compare(ITEM o1, ITEM o2) {
                return new Double(measure(o1)).compareTo(new Double(measure(o2)));
            }
        });
        Map<String, Board> closedSet = new HashMap<>();

        priorityQueue.add(new ITEM(null, initial));
        while (maximumNumberOfStates < 1000000) {
            ITEM board = priorityQueue.poll();
            assert board != null;
            closedSet.put(board.board.getBlocksString(),board.board);
            if (board.board.isGoal()) {
                itemToList(new ITEM(board, board.board));
                return;
            }
            Iterator iterator = board.board.neighbors().iterator();
            everInOpenSet++;
            while (iterator.hasNext()) {
                Board board1 = (Board) iterator.next();
                if (board1 != null && !closedSet.containsKey(board1.getBlocksString())) {
                    priorityQueue.add(new ITEM(board, board1));
                    maximumNumberOfStates++;

                }
            }
        }
        System.out.println(maximumNumberOfStates);
        throw new SolverException("Memory is full but puzzle is still unsolved.");
    }

    private static double measure(ITEM item){

        if (item.board.getAlgorithmType()==Flag.GREEDYSEARCH)
            return item.getBoard().getH();
        if (item.board.getAlgorithmType()==Flag.UNIFORMCOST)
            return item.board.getC();
        return item.getBoard().getH() + item.board.getC();
    }

    private void itemToList(ITEM item){
        ITEM item2 = item;
        while (true){
            item2 = item2.prevBoard;
            if(item2 == null) {
                Collections.reverse(result);
                return;
            }
            result.add(item2.board);
        }
    }

    public int moves() {
        return result.size() - 1;
    }

    public Iterable<Board> solution() {
        return result;
    }

}
