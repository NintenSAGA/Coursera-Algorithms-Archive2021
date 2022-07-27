package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int moves;
    private Stack<Board> solution;
    private Stack<BoardMP> solutionMP;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        moves = 0;
        solution = null;
        MinPQ<BoardMP> initPQ = new MinPQ<>();
        MinPQ<BoardMP> twinPQ = new MinPQ<>();
        initPQ.insert(new BoardMP(initial, moves, null));
        twinPQ.insert(new BoardMP(initial.twin(), moves, null));

        while (!initPQ.isEmpty() && !twinPQ.isEmpty()) {
            if (search(initPQ)) {
                break;
            }
            if (search(twinPQ)) {
                moves = -1;
                solution = null;
                solutionMP = null;
                break;
            }
        }
    }

    private boolean search (MinPQ<BoardMP> PQ) {
        BoardMP peek = PQ.delMin();
        if (peek.board.isGoal()) {
            moves = peek.moves;
            solution = new Stack<>();
            solutionMP = new Stack<>();
            while (peek != null) {
                solutionMP.push(peek);
                solution.push(peek.board);
                peek = peek.previous;
            }
            return true;
        }
        for (Board neighbor: peek.board.neighbors()) {
            if (peek.previous == null) {
                PQ.insert(new BoardMP(neighbor, peek.moves+1, peek));
            } else {
                if (!neighbor.equals(peek.previous.board)) {
                    PQ.insert(new BoardMP(neighbor, peek.moves+1, peek));
                }
            }
        }
        return false;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private Iterable<BoardMP> solutionMP() {
        return solutionMP;
    }

    private static class BoardMP implements Comparable<BoardMP> {
        private final Board board;
        private final int moves;
        private final BoardMP previous;
        private final int manhattan;

        private BoardMP(Board board, int moves, BoardMP previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.manhattan = board.manhattan();
        }

        private int mp() {
            return this.manhattan + this.moves;
        }

        @Override
        public int compareTo(BoardMP o) {
            return this.mp() != o.mp() ? Integer.compare(this.mp(), o.mp())
                    : Integer.compare(this.manhattan, o.manhattan);
        }

        @Override
        public String toString() {
            return "MP: " + this.mp() + " Manhattan: " + this.manhattan
                    + " Moves: " + this.moves;
        }
    }
}